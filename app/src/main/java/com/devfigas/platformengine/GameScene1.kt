package com.devfigas.platformengine

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.SurfaceHolder

class GameScene1(context: Context?, attrs: AttributeSet?) : GameScene(context, attrs) {

    private lateinit var stage: Stage1

    override fun surfaceCreated(holder: SurfaceHolder) {
        stage = Stage1(context, width.toFloat(), height.toFloat())
        super.surfaceCreated(holder)
    }

    override fun drawElements() {
        if (holder.surface.isValid) {
            val canvas = holder.lockCanvas()
            canvas.drawColor(Color.WHITE)
            stage.getUIElements().forEach{ entry ->
                canvas.drawBitmap(
                    entry.gameObject().bitmap(),
                    entry.position().left,
                    entry.position().top, null)
                Engine.debugger.draw(canvas, entry)
            }

            holder.unlockCanvasAndPost(canvas)
        }
    }

    override fun updateGame() = stage.update()

    fun moveRight(){
        stage.move()
    }

    fun stopMove(){
        stage.stop()
    }

    fun jump() {
        stage.jump()
    }

}