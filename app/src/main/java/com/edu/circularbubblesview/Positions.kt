package com.edu.circularbubblesview

import kotlin.experimental.or

class Positions {
    var position: Byte

    /*
             |    TOP 0   |
          ---|------------|---
    START 270|            |90 END
          ---|------------|---
             | BOTTOM 180 |
    */
    constructor() {
        position = posNone
    }

    constructor(position: Byte) {
        this.position = position
    }

    fun add(position: Byte) {
        this.position = this.position or position
    }

    fun hasFlag(position: Byte): Boolean {
        return Flags.isFlagSet(position, this.position)
    }

    val degreesFromTo: IntArray
        get() = getAllowedDegreesFromTo(position)

    companion object {
        const val posNone: Byte = 0
        const val posStart: Byte = 1
        const val posTop = (1 shl 1).toByte()
        const val posEnd = (1 shl 2).toByte()
        const val posBottom = (1 shl 3).toByte()
        fun getAllowedDegreesFromTo(positions: Byte): IntArray {
            val angleFromTo: IntArray
            angleFromTo =
                if (Flags.isFlagSet(posStart, positions)) intArrayOf(
                    if (Flags.isFlagSet(posTop, positions)) 90 else 0,
                    if (Flags.isFlagSet(posBottom, positions)) 90 else 180
                ) else if (Flags.isFlagSet(posTop, positions)) intArrayOf(
                    if (Flags.isFlagSet(posEnd, positions)) 180 else 90,
                    if (Flags.isFlagSet(posStart, positions)) 180 else 270
                ) else if (Flags.isFlagSet(posEnd, positions)) intArrayOf(
                    if (Flags.isFlagSet(posBottom, positions)) 270 else 180,
                    if (Flags.isFlagSet(posTop, positions)) 270 else 360
                ) else if (Flags.isFlagSet(posBottom, positions)) intArrayOf(
                    if (Flags.isFlagSet(posStart, positions)) 360 else 270,
                    if (Flags.isFlagSet(posEnd, positions)) 360 else 90
                ) else
                    return intArrayOf(0, 360)
            //angleFromTo[0] -= 45;
            //angleFromTo[1] += 45;
            return angleFromTo
        }
    }
}