package com.nicolas.nkloteria.utils

fun String.isNumeric(): Boolean {
    return this.all { it.isDigit() }
}