package io.github.jolkert.discordbotlink.extension

import java.lang.NumberFormatException

val InternalCapitalRegex = "(?<!^)[A-Z]".toRegex()
fun String.pascalToSnake() = InternalCapitalRegex.replace(this) { "_${it.value}" }.lowercase()
fun String.pascalToTitle() = InternalCapitalRegex.replace(this) { " ${it.value}" }

fun String.toNullableInt(radix: Int = 10): Int? = try { toInt(radix) } catch (_: NumberFormatException) { null; }