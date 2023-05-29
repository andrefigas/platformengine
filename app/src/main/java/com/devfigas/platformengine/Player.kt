package com.devfigas.platformengine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF

class Player(private val context: Context) : GameObject(
    horizontalFreeze = true,
    collisionBox = RectF(10f, 0f, 10f, 20f),
    hitBox = RectF(),
    gravity = true,
    refreshOnMove = true
) {

    companion object {
        private val spritesRes = arrayOf(
            R.drawable.cat0,
            R.drawable.cat1,
            R.drawable.cat2,
            R.drawable.cat3,
            R.drawable.cat4,
            R.drawable.cat5,
        )
    }

    private var currentSprite = 0

    override fun bitmap() : Bitmap = BitmapFactory.decodeResource(context.resources, spritesRes[currentSprite])

    override fun update() {
        currentSprite = getFrame(spritesRes, 100)
    }

}