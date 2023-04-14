package io.github.jolkert.discordbotlink.extension

val InternalCapitalRegex = "(?<!^)[A-Z]".toRegex()
fun String.pascalToSnake() = InternalCapitalRegex.replace(this) { "_${it.value}" }.lowercase()
fun String.pascalToTitle() = InternalCapitalRegex.replace(this) { " ${it.value}" }