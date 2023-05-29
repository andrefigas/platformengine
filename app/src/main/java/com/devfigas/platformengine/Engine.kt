package com.devfigas.platformengine

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.annotation.IntRange

object Engine{

    private var initialTime : Long = System.currentTimeMillis()

    var deltaTime : Long = 0
        private set

    fun update(){
        val now = System.currentTimeMillis()
        deltaTime = now - initialTime
    }

    val debugger = DebugSettings()
}

class DebugSettings {
    private var transparency = 50
    private var collisionBoxBaseColor = Color.TRANSPARENT
    private var hitBoxBaseColor = Color.TRANSPARENT
    internal val collisionBoxColor get() = applyTransparency(collisionBoxBaseColor)
    internal val hitBoxColor get() = applyTransparency(hitBoxBaseColor)

    internal fun applyTransparency(color : Int): Int {
        if(color == Color.TRANSPARENT){
            return Color.TRANSPARENT
        }

        return Color.argb(transparency, Color.red(color), Color.green(color), Color.blue(color))
    }
    
    fun alphaLayer(@IntRange(from = 0, to = 255) transparency : Int): DebugSettings {
        this.transparency = transparency
        return this
    }

    fun collisionBox(color : Int = Color.RED) : DebugSettings {
        collisionBoxBaseColor = color
        return this
    }

    fun  hitBox(color : Int = Color.GREEN) : DebugSettings {
        hitBoxBaseColor = color
        return this
    }

    fun draw(canvas: Canvas, element : Map.Entry<RectF, GameObject>){

        fun draw(canvas: Canvas, element : GameObject, position : RectF?, color: Int){
            if(color == Color.TRANSPARENT || position == null){
                return
            }

            val paint = Paint()
            paint.style = Paint.Style.FILL
            paint.color = color
            canvas.drawRect(
                RectF(
                    position.left,
                    position.top,
                    position.left + element.width,
                    position.top + element.height
                ),
                paint
            )
        }

        draw(canvas, element.gameObject(), element.collisionBox(), collisionBoxColor)
        draw(canvas, element.gameObject(), element.hitBox(), hitBoxColor)
    }

}