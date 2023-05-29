package com.devfigas.platformengine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Cloud(context: Context) : GameObject() {

    private val bitmap : Bitmap
    override fun bitmap(): Bitmap = bitmap

    init {
        val resources = context.resources
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.cloud)
    }

}