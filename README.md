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

