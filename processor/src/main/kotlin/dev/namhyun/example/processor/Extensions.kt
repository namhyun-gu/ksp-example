package dev.namhyun.example.processor

import java.io.OutputStream

fun OutputStream.appendText(str: String) {
    write(str.toByteArray())
}

fun OutputStream.toAppendable(): OutputStreamAdapter {
    return OutputStreamAdapter(this)
}

class OutputStreamAdapter(private val outputStream: OutputStream) : Appendable {

    override fun append(charSequence: CharSequence?): java.lang.Appendable {
        outputStream.write(charSequence.toString().toByteArray())
        return this
    }

    override fun append(charSequence: CharSequence?, p1: Int, p2: Int): java.lang.Appendable {
        outputStream.write(charSequence.toString().toByteArray())
        return this
    }

    override fun append(char: Char): java.lang.Appendable {
        outputStream.write(char.toInt())
        return this
    }
}