package dev.namhyun.example.processor

import java.io.OutputStream

fun OutputStream.appendText(str: String) {
    write(str.toByteArray())
}