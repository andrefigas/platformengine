package com.devfigas.platformengine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF

class Platform(context: Context) : GameObject() {

    private val bitmap : Bitmap
    override fun bitmap(): Bitmap = bitmap

    init {
        val resources = context.resources
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.platform)
        collisionBox = RectF(0f, 10f,0f, 0f)
    }

}