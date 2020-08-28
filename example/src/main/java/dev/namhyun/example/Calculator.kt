package dev.namhyun.example

import dev.namhyun.example.processor.Abstraction

@Abstraction
class Calculator {
    fun add(a: Int, b: Int): Int {
        return a + b
    }

    fun minus(a: Int, b: Int): Int {
        return a - b
    }
}

fun main() {
    println(CalculatorImpl(Calculator()).add(1, 2))
}