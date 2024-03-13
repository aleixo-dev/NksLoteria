package com.nicolas.nkloteria.listener

import com.nicolas.nkloteria.Main
import com.nicolas.nkloteria.event.LotteryEventGuess
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitRunnable

class LotteryGuessListener(
    private val plugin: Main
) : Listener {

    @EventHandler
    fun onLotteryGuess(lotteryEventGuess: LotteryEventGuess) {

        object : BukkitRunnable() {
            override fun run() {

                Bukkit.getOnlinePlayers().forEach { player ->
                    for (message in plugin.config.getStringList("mensagens.acerto")) {
                        val messageFormatted = message
                            .replace("{apostador}", lotteryEventGuess.player.name)
                            .replace("{premio_quantidade}", lotteryEventGuess.quantity)

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageFormatted))
                    }
                }
            }
        }.runTaskAsynchronously(this.plugin)

        plugin.lotteryNumber.clear()
        plugin.lotteryQuantity.clear()
    }
}