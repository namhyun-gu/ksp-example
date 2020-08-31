package dev.namhyun.example.processor

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import org.jetbrains.kotlin.ksp.processing.CodeGenerator
import org.jetbrains.kotlin.ksp.processing.KSPLogger
import org.jetbrains.kotlin.ksp.processing.Resolver
import org.jetbrains.kotlin.ksp.processing.SymbolProcessor
import org.jetbrains.kotlin.ksp.symbol.KSClassDeclaration
import org.jetbrains.kotlin.ksp.symbol.KSFunctionDeclaration
import org.jetbrains.kotlin.ksp.symbol.KSVisitorVoid

class RepositoryProcessor : SymbolProcessor {
    lateinit var codeGenerator: CodeGenerator

    override fun finish() {
        // No-op
    }

    override fun init(
        options: Map<String, String>,
        kotlinVersion: KotlinVersion,
        codeGenerator: CodeGenerator,
        logger: KSPLogger
    ) {
        this.codeGenerator = codeGenerator
    }

    override fun process(resolver: Resolver) {
        val symbols = resolver.getSymbolsWithAnnotation("dev.namhyun.example.processor.Repository")
        symbols
            .filterIsInstance<KSClassDeclaration>()
            .map { it.accept(RepositoryVisitor(), Unit) }
    }

    inner class RepositoryVisitor : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            classDeclaration.primaryConstructor!!.accept(this, data)
        }

        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
            val parent = function.parentDeclaration as KSClassDeclaration
            val packageName = parent.packageName.asString()
            val className = parent.simpleName.asString()
            val repositoryName = "${className}Repository"
            val file = codeGenerator.createNewFile(packageName, repositoryName)

            var useCoroutine = false
            parent.annotations.forEach { annotation ->
                if (annotation.annotationType.resolve()?.declaration?.qualifiedName!!.asString()
                    == "dev.namhyun.example.processor.UseCoroutine"
                ) {
                    useCoroutine = true
                }
            }

            var primaryKey: ParameterSpec? = null
            function.parameters.forEach {
                val name = it.name!!.asString()
                val typeName =
                    it.type?.resolve()?.declaration?.qualifiedName?.asString() ?: "<ERROR>"
                val typeArgs = it.type!!.element!!.typeArguments
                if (typeArgs.isNotEmpty()) {
                    // Support generic
                }
                it.annotations.forEach { annotation ->
                    if (annotation.annotationType.resolve()?.declaration?.qualifiedName!!.asString()
                        == "dev.namhyun.example.processor.PrimaryKey"
                    ) {
                        primaryKey = ParameterSpec.builder(name, ClassName.bestGuess(typeName))
                            .build()
                    }
                }
            }

            val createFunc = FunSpec.builder("create")
                .addParameter(className, ClassName.bestGuess(parent.qualifiedName!!.asString()))
                .addModifiers(KModifier.ABSTRACT)
                .build()

            val readAllFunc = FunSpec.builder("readAll")
                .addModifiers(
                    if (useCoroutine) listOf(
                        KModifier.SUSPEND,
                        KModifier.ABSTRACT
                    ) else listOf(KModifier.ABSTRACT)
                )
                .returns(LIST.parameterizedBy(listOf(ClassName.bestGuess(parent.qualifiedName!!.asString()))))
                .build()

            val readFunc = FunSpec.builder("read")
                .addParameter(primaryKey!!)
                .addModifiers(
                    if (useCoroutine) listOf(
                        KModifier.SUSPEND,
                        KModifier.ABSTRACT
                    ) else listOf(KModifier.ABSTRACT)
                )
                .returns(ClassName.bestGuess(parent.qualifiedName!!.asString()))
                .build()

            val updateFunc = FunSpec.builder("update")
                .addParameter(className, ClassName.bestGuess(parent.qualifiedName!!.asString()))
                .addModifiers(KModifier.ABSTRACT)
                .build()

            val deleteFunc = FunSpec.builder("delete")
                .addParameter(primaryKey!!)
                .addModifiers(KModifier.ABSTRACT)
                .build()

            val repositoryTypeSpec = TypeSpec.interfaceBuilder(repositoryName)
                .addFunctions(listOf(createFunc, readAllFunc, readFunc, updateFunc, deleteFunc))
                .build()

            FileSpec.builder(packageName, className)
                .addType(repositoryTypeSpec)
                .build()
                .writeTo(file.toAppendable())
        }
    }
}