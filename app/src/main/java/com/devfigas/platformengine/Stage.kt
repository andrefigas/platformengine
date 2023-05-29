package com.devfigas.platformengine

import android.graphics.RectF
import kotlin.math.max

typealias GameElement = Map.Entry<RectF, GameObject>

abstract class Stage(private val displayWidth : Float) {

    companion object{
        const val MOVE_PER_FRAME = 10f
    }

    private val elements = LinkedHashMap<RectF, GameObject>()
    private var stagePosition = 0f
    private var controllerDirection : Direction = Direction.NEUTRAL
    private var jump : Float? = null
    private var jumper : GameObject? = null

    fun move(){
        controllerDirection = Direction.RIGHT
    }

    fun stop(){
        controllerDirection = Direction.NEUTRAL
    }

    fun jump(gameObject: GameObject, jumpHeight : Float){
        val entry = element(gameObject)
        jump = entry.position().top - jumpHeight
        jumper = gameObject
    }

    fun update(): Boolean {
        val elements = getElements()
        elements.forEach {element ->
            autoRefresh(element)
            refreshMove(element)
            if(!gravity(element)){
                return false
            }
        }

        scrollStage(elements)
        return true
    }

    private fun scrollStage(elements : Map<RectF, GameObject>){
        if (controllerDirection != Direction.RIGHT){
            return
        }

        val move = stagePosition - MOVE_PER_FRAME

        val colliders = elements.filter{ gameObjectEntry ->
            gameObjectEntry.gameObject().collisionBox != null
        }

        val collision = colliders.any{ element ->
            val gameObjectCollider = element.collisionBox()!!
            //check collision between colliders
            colliders.filter { it.gameObject() != element.gameObject()}
                .any { gameElement ->
                    gameElement.collisionBox()!!.move(x = move)
                        .intersect(gameObjectCollider)
                }
        }

        if(!collision){
            stagePosition = move
        }
    }

    private fun autoRefresh(element : GameElement) {
        if(element.gameObject().autoRefresh){
            element.gameObject().update()
        }
    }

    private fun refreshMove(element : GameElement){
        if (controllerDirection != Direction.RIGHT){
            return
        }

        if(element.gameObject().refreshOnMove){
            element.gameObject().update()
        }
    }

    private fun gravity(element : GameElement): Boolean {
        val gameElement = element.gameObject()
        if(gameElement.gravity){
            if(jumper == gameElement){
                jump(gameElement)
            } else if(!fall(gameElement)){
                return false
            }
        }

        return true
    }

    private fun getElements(): Map<RectF, GameObject> {
        return elements.filter {
            val elementPosition = it.position()
            val boundStart = stagePosition - displayWidth/2f
            val boundEnd = stagePosition + displayWidth/2f
            return@filter elementPosition.left >= boundStart || elementPosition.right <= boundEnd
        }
    }

    fun getUIElements(): Map<RectF, GameObject> {
        return getElements().mapKeys { entry ->
            val move = if (entry.gameObject().horizontalFreeze) 0f else stagePosition
            entry.position().move(x = move)
        }
    }

    // edit elements

    protected fun add(top : Float, left : Float,
            gameObject: GameObject
    ): RectF {
        return add(top, left, top + gameObject.height,left + gameObject.width, gameObject)
    }

    protected fun add(top : Float, left : Float, bottom : Float, right : Float,
            gameObject: GameObject
    ): RectF {
        val key = RectF(
            left, top, right, bottom
        )
        elements[key] = gameObject

        return key
    }

    protected fun add(quantity : Int,gameObject: GameObject, builder : (Int)->Pair<Float, Float>
    ) {
        for (i in 0 until quantity) {
            val pair = builder(i)
            add(pair.first, pair.second, gameObject)
        }

    }

    private fun update(entry: GameElement, position : RectF){
        elements.remove(entry.position())
        elements[position] = entry.gameObject()
    }

    private fun jump(gameObject: GameObject){
        val jump = this.jump ?: return
        val entry = element(gameObject)
        val position = RectF(entry.position())
        val newY = max(position.top - MOVE_PER_FRAME, jump)
        position.top = newY
        position.bottom = newY + gameObject.height

        update(entry, position)

        if(newY == jump){
            this.jump = null
            this.jumper = null
        }
    }
    /**
     * return false if damaged
     */
    private fun fall(gameObject: GameObject) = move(gameObject, 0f, MOVE_PER_FRAME)

    /**
     * return false if damaged
     */
    private fun move(gameObject: GameObject, x : Float, y : Float) : Boolean {
        val entry = element(gameObject)
        val position = entry.position().move(x, y)
        val objectCollider = entry.collisionBox()!!.move(x,y)
        val objectHitBox = entry.hitBox()?.move(x,y)

        if(objectHitBox != null){
            elements.forEach forEach@ { element ->
                if(element.gameObject() == gameObject){
                    return@forEach
                }

                val move = if (element.gameObject().horizontalFreeze) 0f else this.stagePosition

                if(element.hitBox()?.move(x = move)?.intersect(objectHitBox) == true){
                    return false
                }

                if(element.gameObject().collisionBox == null){
                    return@forEach
                }

                if (objectCollider.intersect(element.collisionBox()!!.move(x = move))){
                    return true
                }
            }
        }

        update(entry, position)

        return true
    }

    private fun element(gameObject : GameObject): GameElement {
       return elements.firstNotNullOf { if (it.gameObject() == gameObject) it else null }
    }

}

enum class Direction {
    RIGHT , NEUTRAL
}

// utilities


fun GameElement.position(): RectF {
    return key
}

fun GameElement.gameObject(): GameObject {
    return value
}

fun GameElement.collisionBox(): RectF? {
    if(gameObject().collisionBox == null){
        return null
    }

    return position().padding(gameObject().collisionBox)
}

fun GameElement.hitBox(): RectF? {
    if(gameObject().hitBox == null){
        return null
    }

    return position().padding(gameObject().hitBox)
}

fun RectF.padding(other : RectF?): RectF {
    val rect = RectF(this)
    if(other != null){
        rect.top += other.top
        rect.bottom -= other.bottom
        rect.left += other.left
        rect.right -= other.right
    }

    return rect
}

fun RectF.move(x : Float = 0f, y : Float = 0f): RectF {
    return RectF(this).apply {
        top += y
        bottom += y
        left += x
        right += x
    }
}