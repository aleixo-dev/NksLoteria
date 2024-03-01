package com.nicolas.nkloteria.listener

import com.nicolas.nkloteria.Main
import com.nicolas.nkloteria.event.LotteryEventGuess
import org.bukkit.Bukkit
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
                    val stringBuilder = StringBuilder()
                        .appendLine(" ")
                        .appendLine("§2§lEVENTO LOTERIA!")
                        .appendLine("§e§l| §fQue sorte! o jogador §6${lotteryEventGuess.player.displayName} §facertou o número da sorte!")
                        .appendLine("§e§l| §fEle recebeu §6${lotteryEventGuess.quantity}")
                        .appendLine(" ")
                        .appendLine("§e§l| §fEvento encerrado!")
                        .appendLine(" ")
                        .lines()

                    for (i in stringBuilder) {
                        player.sendMessage(i)
                    }
                }
            }
        }.runTaskAsynchronously(this.plugin)

        plugin.lotteryNumber.clear()
    }
}