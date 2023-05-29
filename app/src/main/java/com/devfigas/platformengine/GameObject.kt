package com.devfigas.platformengine

import android.graphics.Bitmap
import android.graphics.RectF

abstract class GameObject(var collisionBox : RectF? = null,
                          var hitBox : RectF? = null,
                          val horizontalFreeze : Boolean = false,
                          val gravity : Boolean = false,
                          val autoRefresh : Boolean = false,
                          val refreshOnMove : Boolean = false) {

    abstract fun bitmap() : Bitmap
    val width : Float get() = bitmap().width.toFloat()
    val height : Float get() = bitmap().height.toFloat()

    open fun update(){

    }
}
