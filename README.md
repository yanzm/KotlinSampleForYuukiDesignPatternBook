「Java言語で学ぶデザインパターン入門（結城 浩 著）」
のサンプルコードを Kotlin で書き直したサンプルです。

各章には original/ と improved/ があります。
* original : できるだけ元のサンプルコードをそのまま Kotlin 化したもの
* improved : より Kotlin らしいコードに改修したもの

例)
original
```kotlin
for (i in 0 until 20) {
  ...
}
```

improved
```kotlin
repeat(20) {
  ...
}
