package com.devfigas.platformengine

import android.content.Context
import java.lang.IndexOutOfBoundsException

class Stage1(context: Context, displayWidth : Float, displayHeight : Float) : Stage(displayWidth){

    private val platform : Platform = Platform(context)
    private val player : Player = Player(context)
    private val cloud : Cloud = Cloud(context)
    private val lava : Lava = Lava(context, displayWidth)

    init {
       // Cloud
       add(3, cloud){ i ->
            displayHeight / 4f to cloud.width * i * 1.5f
       }

       // Platforms
       add(5 , platform) { i ->
           generatePlatformPosition(displayWidth, displayHeight, i)
        }

        // Lava
        add(displayHeight - lava.height, 0f, displayHeight, displayWidth, lava)

        // Player
        add(0f , displayWidth / 2 - player.width/2f, player)
    }

    private fun generatePlatformPosition(displayWidth: Float, displayHeight: Float, index : Int): Pair<Float, Float> {
        val platformX = displayWidth / 2 - platform.width/2f
        val platformY = displayHeight / 2

        return when(index){
            0 -> platformY to platformX
            1 ->  platformY + platform.height * 1 to platformX + platform.width * 1.5f
            2 -> platformY + platform.height * 2 to platformX + platform.width * 3f
            3 -> platformY + platform.height * 1 to platformX + platform.width * 4f
            4 -> platformY to platformX + platform.width * 5.5f
            else -> throw IndexOutOfBoundsException()
        }
    }

    fun jump(){
        jump(player, 200f)
    }

}