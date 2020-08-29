package dev.namhyun.example

import dev.namhyun.example.processor.PrimaryKey
import dev.namhyun.example.processor.Repository

@Repository
data class Todo(@PrimaryKey val id: Int, val title: String, val content: String)

fun main() {
    val repository = object : TodoRepository {
        override fun create(Todo: Todo) {
            TODO("Not yet implemented")
        }

        override fun readAll(): List<Todo> {
            TODO("Not yet implemented")
        }

        override fun read(id: Int) {
            TODO("Not yet implemented")
        }

        override fun update(Todo: Todo) {
            TODO("Not yet implemented")
        }

        override fun delete(id: Int) {
            TODO("Not yet implemented")
        }
    }
}