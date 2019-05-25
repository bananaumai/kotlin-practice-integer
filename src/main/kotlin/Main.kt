package dev.bananaumai.kotlin.practice.integer

import java.lang.AssertionError
import java.nio.ByteBuffer
import java.nio.ByteOrder

fun main() {
    listOf(
        0,
        1,
        2,
        255,
        256,
        Int.MAX_VALUE - 1,
        Int.MAX_VALUE,
        Int.MIN_VALUE,
        Int.MIN_VALUE + 1,
        -256,
        -255,
        -2,
        -1
    ).forEach { n ->
        val bufferSize = Int.SIZE_BYTES
        val buffer = ByteBuffer.allocate(bufferSize)
        buffer.order(ByteOrder.BIG_ENDIAN) // BIG_ENDIAN is default byte order, so it is not necessary.
        buffer.putInt(n)
        val expect = buffer.array().joinToString("") { "%02x".format(it) }
        val actual = n.toByteArray().joinToString("") { "%02x".format(it) }
        println("expect: $expect <-> actual: $actual")
        if (actual != expect) {
            throw AssertionError()
        }
    }

    listOf(
        Pair(UInt.MIN_VALUE, "00000000"),
        Pair(0u, "00000000"),
        Pair(1u, "00000001"),
        Pair(2u, "00000002"),
        Pair(255u, "000000ff"),
        Pair(UInt.MAX_VALUE, "ffffffff")
    ).forEach { (input, expect) ->
        val actual = input.toByteArray().joinToString("") { "%02x".format(it) }
        println("expect: $expect <-> actual: $actual")
        if (actual != expect) {
            throw AssertionError()
        }
    }
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

fun UInt.toByteArray(isBigEndian: Boolean = true): ByteArray {
    var bytes = byteArrayOf()

    var n = this

    if (n == 0x00u) {
        bytes += n.toByte()
    } else {
        while (n != 0x00u) {
            val b = n.toByte()

            bytes += b

            n = n.shr(Byte.SIZE_BITS)
        }
    }

    val padding = 0x00u.toByte()
    var paddings = byteArrayOf()
    repeat(UInt.SIZE_BYTES - bytes.count()) {
        paddings += padding
    }

    return if (isBigEndian) {
        paddings + bytes.reversedArray()
    } else {
        paddings + bytes
    }
}