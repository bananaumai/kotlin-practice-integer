package dev.bananaumai.kotlin.practice.integer

import java.lang.AssertionError

fun main() {
//    val n = 0xFF
//    println("$n, ${n.shr(8)}")
//    println("${Integer.toBinaryString(n)}, ${Integer.toBinaryString(n.shr(8))}")
//    println("${Integer.toHexString(n)}, ${Integer.toHexString(n.shr(8))}")
//    println("${"%02x".format(n.toByte())}, ${"%02x".format(n.shr(8).toByte())}")

    val ns = listOf(
        0,
        1,
        2,
        255,
        0xFF,
        Int.MAX_VALUE - 2,
        Int.MAX_VALUE - 1,
        Int.MAX_VALUE,
        Int.MIN_VALUE,
        Int.MIN_VALUE + 1,
        Int.MIN_VALUE + 2,
        -2,
        -1
    )

//    printBinaryString(ns)
//    printHexString(ns)

//    printByte(ns)
//    printByteArray(ns)

    val tests = listOf(
        Pair(0, "00000000"),
        Pair(1, "00000001"),
        Pair(2, "00000002"),
        Pair(255, "000000ff"),
        Pair(Int.MAX_VALUE - 1, "7ffffffe"),
        Pair(Int.MAX_VALUE, "7fffffff"),
        Pair(Int.MIN_VALUE, "80000000"),
        Pair(Int.MIN_VALUE + 1, "80000001"),
        Pair(-2, "fffffffe"),
        Pair(-1, "ffffffff")
    )

    tests.forEach { (input, expect) ->
        val actual = input.toByteArray().joinToString("") { "%02x".format(it) }
        println("expect: $expect <-> actual: $actual")
        if (actual != expect) {
            throw AssertionError()
        }
    }
}



fun printBinaryString(ns: List<Int>) {
    println("printBinaryString - start")
    ns.forEach { println("$it -> ${Integer.toBinaryString(it)} (${Integer.toBinaryString(it).length})") }
    println("printBinaryString - end")
}

fun printHexString(ns: List<Int>) {
    println("printHexString - start")
    ns.forEach { println("$it -> ${Integer.toHexString(it)} (${Integer.toHexString(it).length})") }
    println("printHexString - end")
}

fun printByte(ns: List<Int>) {
    println("printByte - start")
    ns.forEach {
        println("$it -> ${"%02X".format(it.and(0xFF).toByte())} ")
    }
    println("printByte - end")
}

fun printByteArray(ns: List<Int>) {
    println("printByteArray - start")
    ns.forEach {
        println("$it -> ${it.toByteArray().joinToString("") { b -> "%02X".format(b) }} ")
    }
    println("printByteArray - end")
}

fun Int.toByteArray(isBigEndian: Boolean = true): ByteArray {
    var bytes = byteArrayOf()

    var n = this

    if (n == 0 || n == -1) {
        bytes += n.toByte()
    } else if (n > 0) {
        while (n != 0) {
            val b = n.and(0xFF).toByte()

            bytes += b

            n = n.shr(Byte.SIZE_BITS)
        }
    } else {
        while (n != -1) {
            val b = n.and(0xFF).toByte()

            bytes += b

            n = n.shr(Byte.SIZE_BITS)
        }
    }

    val padding = if (n < 0) { 0xFF.toByte() } else { 0x00.toByte() }
    var paddings = byteArrayOf()
    repeat(Int.SIZE_BYTES - bytes.count()) {
        paddings += padding
    }

    return if (isBigEndian) {
        paddings + bytes.reversedArray()
    } else {
        paddings + bytes
    }
}