package dev.namhyun.example

import dev.namhyun.example.processor.Hello

@Hello
class Test {
}

fun main() {
    Test().hello()
}