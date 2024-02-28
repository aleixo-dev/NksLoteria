package com.nicolas.nkloteria.instance

import com.nicolas.nkloteria.Main
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class Lottery(
    private val plugin: Main
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
                    .appendLine("§2§lEVENTO LOTERIA COMEÇOU!")
                    .appendLine("§6§l| §fEvento loteria no ar!")
                    .appendLine("§6§l| §fpara apostar utilize o comando §6/loteria <numero>")
                    .appendLine("§6§l| §fPrêmio estimado: §e§l[4SS]")
                    .lines()

                for (i in lines) {
                    players.sendMessage(i)
                }

                hasJustStarted = false
            }
        }

        if (countdownSeconds <= 1) {
            for (player in Bukkit.getOnlinePlayers()) {
                player.sendMessage("Evento loteria encerrada!")
            }
        }
        countdownSeconds--
    }


    private fun resetLotteryNumber() {
        plugin.lotteryNumber.clear()
    }
}