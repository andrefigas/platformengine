package com.devfigas.platformengine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

class Lava(private val context: Context, private val displayWidth : Float) : GameObject(
    horizontalFreeze = true,
    hitBox = RectF().apply { top = 50f },
    autoRefresh = true
    ) {

    companion object {
        private val spritesRes = listOf(
            R.drawable.lava0,
            R.drawable.lava1
        )
    }

    private var currentSprite = 0
    override fun bitmap(): Bitmap {
        val resources = context.resources
        val bmp = BitmapFactory.decodeResource(resources, spritesRes[currentSprite / 10])
        return Bitmap.createScaledBitmap(bmp, displayWidth.toInt(), bmp.height, false)
    }

    override fun update() {
        currentSprite = (currentSprite + 1) % (spritesRes.size * 10)
    }

}