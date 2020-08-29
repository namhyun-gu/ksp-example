# ksp-example

> Example of [Kotlin Symbol Processing API](https://github.com/android/kotlin)

- [HelloProcessor](processor/src/main/kotlin/dev/namhyun/example/processor/HelloProcessor.kt)

  Generate hello extension function to class

  - Source

  ```kotlin
  @Hello
  class Test {
  }
  ```

  - Generated

  ```kotlin
  fun Test.hello() {
    println("Test says hello!")
  }
  ```

- [AbstractionProcessor](processor/src/main/kotlin/dev/namhyun/example/processor/AbstractionProcessor.kt)

  - Source

  ```kotlin
  @Abstraction
  class Calculator {
    fun add(a: Int, b: Int): Int {
        return a + b
    }

    fun minus(a: Int, b: Int): Int {
        return a - b
    }
  }
  ```

  - Generated

  ```kotlin
  interface ICalculator {
    fun add(a: Int, b: Int): Int

    fun minus(a: Int, b: Int): Int
  }

  class CalculatorImpl(
    private val calculator: Calculator
  ) : ICalculator {
    override fun add(a: Int, b: Int): Int = calculator.add(a, b)

    override fun minus(a: Int, b: Int): Int = calculator.minus(a, b)
  }
  ```

- [RepositoryProcessor](processor/src/main/kotlin/dev/namhyun/example/processor/RepositoryProcessor.kt)

  - Source

  ```kotlin
  @Repository
  data class Todo(@PrimaryKey val id: Int, val title: String, val content: String)
  ```

  - Generated

  ```kotlin
  import kotlin.Int
  import kotlin.collections.List

  interface TodoRepository {
    fun create(Todo: Todo)

    fun readAll(): List<Todo>

    fun read(id: Int)

    fun update(Todo: Todo)

    fun delete(id: Int)
  }
  ```
