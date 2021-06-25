package com.edu.circularbubblesview

import android.util.Log
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

const val MAX_CIRCLE_DEGREES = 360
const val angleStartPoint = PI * 2



class ScreenParams(
    var maxHeight: Int,
    var maxWidth: Int,
    val offset: Int
)


class CircleParams(
    val circleRadius: Double,
    val circleRadiusChild: Double,
    val circleDistanceBetweenMainAndChild: Double,
    val screenParams: ScreenParams
) {
    var countCircles: Int = 0


    fun circlesAreOutOfBounds(mainCircleCenterX: Double, mainCircleCenterY: Double): Boolean {
        val totalRadius = getTotalRadius()
        return mainCircleCenterX-totalRadius < screenParams.offset ||
                mainCircleCenterX+totalRadius > screenParams.maxWidth-screenParams.offset ||
                mainCircleCenterY-totalRadius < screenParams.offset ||
                mainCircleCenterY+totalRadius > screenParams.maxHeight-screenParams.offset
    }

    fun calculateAllowedAngles(centerX: Double , centerY: Double): IntArray{
        val allowedDegrees = intArrayOf(0, 360)
        val positions = Positions()
        var rightAngle = 0.0
        var topAngle = 0.0
        var bottomAngle = 0.0
        var leftAngle = 0.0
        val radius = getTotalRadius()
        if (centerX - radius < screenParams.offset) {
            // Left side is out of bounds (from 3P/2 to P/2)
            leftAngle = calculateAngleOutOfBoundsSegment(radius, screenParams.offset - (centerX - radius))
            positions.add(Positions.posStart)
        } else if (centerX + radius > screenParams.maxWidth) {
            // Right side is out of bounds (from P/2 to 3P/2) (from 90 to 270)
            rightAngle = calculateAngleOutOfBoundsSegment(radius,  (centerX + radius)-screenParams.maxWidth)
            positions.add(Positions.posEnd)
        }
        if (centerY - radius < screenParams.offset) {
            // Top side is out of bounds (from 2P to P) (from 0 to 180)
            topAngle = calculateAngleOutOfBoundsSegment(radius, screenParams.offset - (centerY - radius))
            positions.add(Positions.posTop)
        } else if (centerY + radius > screenParams.maxHeight) {
            // bottom side is out of bounds (from P to 2P)
            bottomAngle = calculateAngleOutOfBoundsSegment(radius, centerY + radius - screenParams.maxHeight)
            positions.add(Positions.posBottom)
        }
        if (positions.hasFlag(Positions.posStart)) {
            allowedDegrees[0] = (270 + leftAngle / 2).toInt()
            allowedDegrees[1] = (270 - leftAngle / 2).toInt()
            if (positions.hasFlag(Positions.posTop)) {
                allowedDegrees[0] = (0 + topAngle / 2).toInt()
            } else if (positions.hasFlag(Positions.posBottom)) {
                allowedDegrees[1] = (180 - bottomAngle / 2).toInt()
            }
        } else if (positions.hasFlag(Positions.posEnd)) {
            allowedDegrees[0] = (90 + rightAngle / 2).toInt()
            allowedDegrees[1] = (90 - rightAngle / 2).toInt()
            if (positions.hasFlag(Positions.posTop)) {
                allowedDegrees[1] = (360 - topAngle / 2).toInt()
            } else if (positions.hasFlag(Positions.posBottom)) {
                allowedDegrees[0] = (180 + bottomAngle / 2).toInt()
            }
        }
        return allowedDegrees

    }


    fun getTotalRadius(): Double{
        // the total radius from mainCenter to child's center
        return circleRadius + circleRadiusChild + circleDistanceBetweenMainAndChild
    }

    // returns the angle depends on step (index of circle)
    fun getEndpointAngleForChildCircle(indexOfCircle: Int): Double{
        // returns the child's angle
        if (indexOfCircle == 0)
            return MAX_CIRCLE_DEGREES.toDouble()
        return ((MAX_CIRCLE_DEGREES / countCircles) * indexOfCircle).toDouble()
    }


    // returns the delta X of child circle with index {indexOfCircle}
    fun getEndpointDeltaX(indexOfCircle: Int): Double {
        return cos(Math.toRadians(getEndpointAngleForChildCircle(indexOfCircle))) * getTotalRadius()
    }


    // returns the delta Y of child circle with index {indexOfCircle}
    fun getEndpointDeltaY(indexOfCircle: Int): Double {
        return sin(Math.toRadians(getEndpointAngleForChildCircle(indexOfCircle))) * getTotalRadius()
    }

    fun getEndpointDeltaXY(indexOfCircle: Int): DoubleArray{
        return doubleArrayOf(
            getEndpointDeltaX(indexOfCircle),
            getEndpointDeltaY(indexOfCircle)
        )
    }

    companion object {
        fun calculateAnglesDifference(angleFrom: Double, angleTo: Double): Double {
            if (angleFrom % MAX_CIRCLE_DEGREES == angleTo % MAX_CIRCLE_DEGREES)
                return MAX_CIRCLE_DEGREES.toDouble()
            return (MAX_CIRCLE_DEGREES - angleFrom + angleTo) % MAX_CIRCLE_DEGREES
        }

        fun calculateAngleOutOfBoundsSegment(radius: Double, sideOutOfBounds: Double): Double{
            return Math.toDegrees(2 * acos(1 - sideOutOfBounds / radius))
        }

    }
}