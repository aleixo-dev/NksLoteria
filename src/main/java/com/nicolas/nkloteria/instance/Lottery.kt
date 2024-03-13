package com.nicolas.nkloteria.instance

import com.nicolas.nkloteria.Main
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable

class Lottery(
    private val plugin: Main,
    private val lotteryReward: String
) : BukkitRunnable() {

    private var countdownSeconds = 10
    private var hasJustStarted = false

    fun start() {
        hasJustStarted = true
        runTaskTimerAsynchronously(plugin, 0L, plugin.config.getInt("config.tempo-loteria").toLong())
    }

    fun reset() {
        resetLotteryNumber()
        cancel()
        return
    }

    override fun run() {

        if (countdownSeconds == 0) {
            resetLotteryNumber()
            cancel()
            return
        }

        if (hasJustStarted) {
            Bukkit.getOnlinePlayers().forEach { player ->
                for (message in plugin.config.getStringList("mensagens.inicio")) {
                    val messageFormatted = message
                        .replace("{premio_estimado}", lotteryReward)
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageFormatted))
                }
                hasJustStarted = false
            }
        }

        if (countdownSeconds <= MINIMUM_INTERVAL) {
            for (player in Bukkit.getOnlinePlayers()) {

                player.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                        '&',
                        plugin.config.getString("mensagens.sem-ganhador") ?: ""
                    )
                )
            }
        }

        countdownSeconds--
    }

    companion object {
        const val MINIMUM_INTERVAL = 1
    }

    private fun resetLotteryNumber() {
        plugin.lotteryNumber.clear()
        plugin.lotteryQuantity.clear()
    }
}