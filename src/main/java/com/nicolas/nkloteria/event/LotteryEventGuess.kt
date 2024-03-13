package com.nicolas.nkloteria.event

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class LotteryEventGuess(
    var player: Player,
    var quantity : String
) : Event() {

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {

        @JvmStatic
        val handlerList = HandlerList()

        @JvmStatic
        fun getHandlersList(): HandlerList {
            return handlerList
        }
    }
}