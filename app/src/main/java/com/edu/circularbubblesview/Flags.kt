package com.edu.circularbubblesview

import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

object Flags {
    fun isFlagSet(value: Byte, flags: Byte): Boolean {
        return (flags and value) == value
    }

    fun setFlag(value: Byte, flags: Byte): Byte {
        return (flags or value)
    }

    fun unsetFlag(value: Byte, flags: Byte): Byte {
        return (flags and value.inv())
    }
}