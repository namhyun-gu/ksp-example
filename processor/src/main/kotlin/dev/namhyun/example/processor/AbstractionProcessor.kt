package dev.namhyun.example.processor

import com.squareup.kotlinpoet.*
import org.jetbrains.kotlin.ksp.processing.CodeGenerator
import org.jetbrains.kotlin.ksp.processing.KSPLogger
import org.jetbrains.kotlin.ksp.processing.Resolver
import org.jetbrains.kotlin.ksp.processing.SymbolProcessor
import org.jetbrains.kotlin.ksp.symbol.KSClassDeclaration
import org.jetbrains.kotlin.ksp.symbol.KSVisitorVoid

class AbstractionProcessor : SymbolProcessor {
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
        val symbols = resolver.getSymbolsWithAnnotation("dev.namhyun.example.processor.Abstraction")
        symbols
            .filterIsInstance<KSClassDeclaration>()
            .map { it.accept(AbstractionVisitor(), Unit) }
    }

    inner class AbstractionVisitor : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val packageName = classDeclaration.containingFile!!.packageName.asString()
            val className = classDeclaration.simpleName.asString()
            val file = codeGenerator.createNewFile(packageName, className)

            val interfaceType = TypeSpec.interfaceBuilder("I$className")
                .addFunctions(
                    classDeclaration.getAllFunctions()
                        .filter {
                            !arrayOf("equals", "hashCode", "toString")
                                .contains(it.simpleName.asString())
                        }
                        .map {
                            FunSpec.builder(it.simpleName.asString())
                                .addParameters(it.parameters.map {
                                    ParameterSpec.builder(
                                        it.name!!.asString(),
                                        ClassName.bestGuess(it.type?.resolve()?.declaration?.qualifiedName?.asString()!!)
                                    )
                                        .build()
                                })
                                .returns(ClassName.bestGuess(it.returnType?.resolve()?.declaration?.qualifiedName?.asString()!!))
                                .addModifiers(KModifier.ABSTRACT)
                                .build()
                        }.toList()
                )
                .build()

            val implementsType = TypeSpec.classBuilder("${className}Impl")
                .addSuperinterface(ClassName.bestGuess("${packageName}.I$className"))
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter(
                            ParameterSpec.builder(
                                className.toLowerCase(),
                                ClassName.bestGuess("$packageName.$className")
                            )
                                .build()
                        )
                        .build()
                )
                .addProperty(
                    PropertySpec.builder(
                        className.toLowerCase(),
                        ClassName.bestGuess("$packageName.$className")
                    )
                        .initializer(className.toLowerCase())
                        .addModifiers(KModifier.PRIVATE)
                        .build()
                )
                .addFunctions(
                    classDeclaration.getAllFunctions()
                        .filter {
                            !arrayOf("equals", "hashCode", "toString")
                                .contains(it.simpleName.asString())
                        }
                        .map {
                            val builder = FunSpec.builder(it.simpleName.asString())
                                .addParameters(it.parameters.map {
                                    ParameterSpec.builder(
                                        it.name!!.asString(),
                                        ClassName.bestGuess(it.type?.resolve()?.declaration?.qualifiedName?.asString()!!)
                                    )
                                        .build()
                                })
                                .addModifiers(KModifier.OVERRIDE)

                            val invokeParameters =
                                it.parameters.joinToString(", ") { it.name!!.asString() }
                            val returnClassName =
                                ClassName.bestGuess(it.returnType?.resolve()?.declaration?.qualifiedName?.asString()!!)
                            if (returnClassName != Unit::class.asClassName()) {
                                builder.addStatement("return ${className.toLowerCase()}.${it.simpleName.asString()}($invokeParameters)")
                                builder.returns(returnClassName)
                            } else {
                                builder.addStatement("${className.toLowerCase()}.${it.simpleName.asString()}($invokeParameters)")
                            }
                            builder.build()
                        }.toList()
                )
                .build()

            FileSpec.builder(packageName, className)
                .addType(interfaceType)
                .addType(implementsType)
                .build()
                .writeTo(file.toAppendable())
        }
    }
}