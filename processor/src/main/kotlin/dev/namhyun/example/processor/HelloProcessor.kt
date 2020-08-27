package dev.namhyun.example.processor

import org.jetbrains.kotlin.ksp.processing.CodeGenerator
import org.jetbrains.kotlin.ksp.processing.KSPLogger
import org.jetbrains.kotlin.ksp.processing.Resolver
import org.jetbrains.kotlin.ksp.processing.SymbolProcessor
import org.jetbrains.kotlin.ksp.symbol.KSClassDeclaration
import org.jetbrains.kotlin.ksp.symbol.KSVisitorVoid

class HelloProcessor : SymbolProcessor {
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
        val symbols = resolver.getSymbolsWithAnnotation("dev.namhyun.example.processor.Hello")
        println(symbols)
        symbols
            .filterIsInstance<KSClassDeclaration>()
            .map { it.accept(HelloVisitor(), Unit) }
    }

    inner class HelloVisitor : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val packageName = classDeclaration.containingFile!!.packageName.asString()
            val targetClassName = classDeclaration.simpleName.asString()
            val className = "${classDeclaration.simpleName.asString()}sHello"
            val file = codeGenerator.createNewFile(packageName, className)

            file.use {
                it.apply {
                    appendText("package $packageName\n\n")
                    appendText("fun $targetClassName.hello() {\n")
                    appendText("    println(\"$targetClassName says hello!\")\n")
                    appendText("}\n")
                }
            }
        }
    }
}