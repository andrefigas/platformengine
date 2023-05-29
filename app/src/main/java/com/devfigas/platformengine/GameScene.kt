package com.devfigas.platformengine

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

abstract class GameScene : SurfaceView, SurfaceHolder.Callback, Runnable {

    lateinit var gameThread : Thread
    private val mainHandler: Handler = Handler(Looper.getMainLooper())
    var listener : GameListener? = null

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        holder.apply {
            addCallback(this@GameScene)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        gameThread = Thread(this).apply {
            start()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }

    override fun run() {
        while (updateGame()){
            drawElements()
            Engine.update()
        }

        mainHandler.post {
            listener?.onGameFinished()
        }

    }

    abstract fun drawElements()
    abstract fun updateGame() : Boolean

    interface GameListener {

        fun onGameFinished()

    }
}