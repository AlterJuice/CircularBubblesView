package com.edu.circularbubblesview

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

const val CIRCLE_CHILD_SIZE = 140
const val CIRCLE_MAIN_SIZE = 160
const val DISTANCE = 30
const val OFFSET_OF_SCREEN_BOUNDS = 200

class MainActivity : AppCompatActivity() {
    private lateinit var circularParams: CircleParams

    private lateinit var mainCircle: View
    private lateinit var circleLayout: RelativeLayout

    private var circlesAreVisible = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        circularParams = CircleParams(
            CIRCLE_CHILD_SIZE.toDouble(),
            CIRCLE_MAIN_SIZE.toDouble(),
            DISTANCE.toDouble(),
            ScreenParams(
                resources.displayMetrics.heightPixels,
                resources.displayMetrics.widthPixels,
                OFFSET_OF_SCREEN_BOUNDS
            )
        )

        circleLayout = findViewById(R.id.mainWindow)
        mainCircle = findViewById(R.id.centerView)
        val layoutParams = RelativeLayout.LayoutParams(CIRCLE_MAIN_SIZE, CIRCLE_MAIN_SIZE)
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 1)
        mainCircle.layoutParams = layoutParams
        mainCircle.setOnClickListener { onCenterCircleClick() }
    }

    private fun onCenterCircleClick(){
        updateCirclesCount()
        animateCenterCircle()
        animateChildCircles()
    }
    private fun animateCenterCircle(){
        mainCircle.animate().scaleY(1.3f).scaleX(1.3f).setDuration(100).withEndAction {
            mainCircle.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
        }.start()
    }

    private fun getMainCircleCenterCoordinates(): FloatArray {
        return floatArrayOf(
            mainCircle.x,
            mainCircle.y
        )
    }
    private fun getChildCircleXY(circleIndex: Int): FloatArray{
        // val angle = circularParams.getEndpointAngleForChildCircle(circleIndex)
        // val dx = d * cos(angle)
        // val dy = d * sin(angle)

        // getEndpointXY has the same calculations
        val dx = circularParams.getEndpointDeltaX(circleIndex)
        val dy = circularParams.getEndpointDeltaY(circleIndex)

        val vX = mainCircle.x + dx
        val vY = mainCircle.y + dy
        return floatArrayOf(vX.toFloat(), vY.toFloat())
    }

    private fun animateChildCircles(){
        for (i in 0..circleLayout.childCount-1){
            if (i == 0) continue
            with(circleLayout.getChildAt(i)){
                if (circlesAreVisible){
                    val xy = getMainCircleCenterCoordinates()
                    animate().translationX(xy[0]).translationY(xy[1])
                        .rotation(360f).scaleX(1f).scaleY(1f).start()
                }else{
                    val xy = getChildCircleXY(i-1)
                    animate().translationX(xy[0]).translationY(xy[1])
                        .rotation(-360f).scaleY(1f).scaleY(1f).start()
                }
            }
        }
        circlesAreVisible = !circlesAreVisible
    }

    private fun updateAllBubbles(){
        updateCirclesCount()

        for (i in 1..circleLayout.childCount-1){
            with(circleLayout.getChildAt(i)){
                val circleCenterXY = getChildCircleXY(i-1)
                this.x = circleCenterXY[0]
                this.y = circleCenterXY[1]
            }
        }
        circleLayout.invalidate()
    }


    private fun generateNewChildCircle(){
        val childCircle = View(this)
        val layoutParams = RelativeLayout.LayoutParams(circularParams.circleRadiusChild.toInt(), circularParams.circleRadiusChild.toInt())
        childCircle.layoutParams = layoutParams
        childCircle.setBackgroundResource(R.drawable.ic_baseline_help_24)
        circleLayout.addView(childCircle)
        circleLayout.invalidate()
    }

    fun onAddCircleClick(v: View){
        generateNewChildCircle()
        updateAllBubbles()
    }

    fun updateCirclesCount(){
        circularParams.countCircles = circleLayout.childCount-1
    }

}