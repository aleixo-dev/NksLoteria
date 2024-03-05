package com.nicolas.nkloteria.instance

import com.nicolas.nkloteria.Main
import com.nicolas.nkloteria.utils.LotteryMessages
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class Lottery(
    private val plugin: Main,
    private val lotteryReward: String
) : BukkitRunnable() {

    private var countdownSeconds = 100
    private var hasJustStarted = false

    fun start() {
        hasJustStarted = true
        runTaskTimerAsynchronously(plugin, 0L, 20)
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
            Bukkit.getOnlinePlayers().forEach { players ->
                val lines = StringBuilder()
                    .appendLine(" ")
                    .appendLine("§a§lEVENTO LOTERIA COMEÇOU!")
                    .appendLine(" ")
                    .appendLine("§6§l| §fEvento loteria ativo agora!")
                    .appendLine("§6§l| §fBasta usar §a/loteria (número) para apostar")
                    .appendLine("§6§l| §fPrêmio estimado: §e§lR$[$lotteryReward]")
                    .appendLine(" ")
                    .lines()

                for (i in lines) {
                    players.sendMessage(i)
                }

                hasJustStarted = false
            }
        }

        if (countdownSeconds <= 1) {
            for (player in Bukkit.getOnlinePlayers()) {
                StringBuilder()
                    .appendLine(" ")
                    .appendLine("${LotteryMessages.pluginTag()}§6Evento loteria encerrado, não houve ganhador")
                    .appendLine(" ")
                    .lines()
                    .forEach { t ->
                        player.sendMessage(t)
                    }
            }
        }
        countdownSeconds--
    }


    private fun resetLotteryNumber() {
        plugin.lotteryNumber.clear()
        plugin.lotteryQuantity.clear()
    }
}