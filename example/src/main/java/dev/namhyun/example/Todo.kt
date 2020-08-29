package dev.namhyun.example

import dev.namhyun.example.processor.PrimaryKey
import dev.namhyun.example.processor.Repository
import dev.namhyun.example.processor.UseCoroutine

@Repository
data class Todo(@PrimaryKey val id: Int, val title: String, val content: String)

@Repository
@UseCoroutine
data class CoroutineTodo(@PrimaryKey val id: Int, val title: String, val content: String)

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

    val coroutineRepository = object : CoroutineTodoRepository {
        override fun create(CoroutineTodo: CoroutineTodo) {
            TODO("Not yet implemented")
        }

        override suspend fun readAll(): List<CoroutineTodo> {
            TODO("Not yet implemented")
        }

        override suspend fun read(id: Int) {
            TODO("Not yet implemented")
        }

        override fun update(CoroutineTodo: CoroutineTodo) {
            TODO("Not yet implemented")
        }

        override fun delete(id: Int) {
            TODO("Not yet implemented")
        }

    }
}