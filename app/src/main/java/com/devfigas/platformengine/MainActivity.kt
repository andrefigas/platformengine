package com.devfigas.platformengine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity(), GameScene.GameListener {

    lateinit var scene: GameScene

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scene = findViewById(R.id.scene)
        scene.listener = this

        findViewById<View>(R.id.jump).setOnClickListener {
            scene.jump()
        }

        findViewById<View>(R.id.run).setOnTouchListener { _, event ->
            when(event.actionMasked){
                MotionEvent.ACTION_DOWN -> {
                    scene.moveRight()
                    true
                }

                MotionEvent.ACTION_UP -> {
                    scene.stopMove()
                    false
                }

                else -> {
                    false
                }
            }

        }
    }

    override fun onGameFinished() {
        val toast = Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}