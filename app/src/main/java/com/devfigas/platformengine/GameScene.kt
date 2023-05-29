package com.devfigas.platformengine

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameScene : SurfaceView, SurfaceHolder.Callback, Runnable {

    lateinit var stage: Stage1
    lateinit var player: Player
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
        stage = Stage1(context, width.toFloat(), height.toFloat())
        player = Player(context)
        Thread(this).start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }

    override fun run() {
        while (updateGame()){
            drawElements()
        }

        mainHandler.post {
            listener?.onGameFinished()
        }

    }

    private fun drawElements() {
        if (holder.surface.isValid) {
            val canvas = holder.lockCanvas()
            canvas.drawColor(Color.WHITE)
            stage.getUIElements().forEach{ entry ->
                canvas.drawBitmap(
                    entry.gameObject().bitmap(),
                    entry.position().left,
                    entry.position().top, null)
            }

            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun updateGame() = stage.update()

    fun moveRight(){
        stage.move()
    }

    fun stopMove(){
        stage.stop()
    }

    fun jump() {
        stage.jump()
    }

    interface GameListener {

        fun onGameFinished()

    }
}