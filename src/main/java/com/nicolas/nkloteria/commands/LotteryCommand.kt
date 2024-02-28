package com.nicolas.nkloteria.commands

import com.nicolas.nkloteria.Main
import com.nicolas.nkloteria.event.LotteryEventGuess
import com.nicolas.nkloteria.instance.Lottery
import com.nicolas.nkloteria.utils.LotteryMessages
import com.nicolas.nkloteria.utils.isNumeric
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class LotteryCommand(
    private val plugin: Main
) : CommandExecutor {

    private val economy: Economy? = Main.getEconomy()
    private var lottery: Lottery = Lottery(plugin)

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage("Apenas jogadores ou admin podem executar este comando")
            return true
        }

        val player: Player = sender

        if (args.isEmpty()) {
            LotteryMessages.showHelpCommand().lines().forEach {
                player.sendMessage(it)
            }
            return false
        }

        if (args.size == 2 && args[0].equals("iniciar", true)) {

            if (!args[1].isNumeric()) {
                LotteryMessages.showHelpCommand().lines().forEach {
                    player.sendMessage(it)
                }
                return false
            }

            if (!hasLotteryStarted()) {
                lottery.start()
                plugin.lotteryNumber[LOTTERY_EVENT_KEY] = args[1].toInt()

            } else {
                player.sendMessage("Um número já foi sorteado, espere até encerrar!")
            }

        } else if (args.size == 1 && args[0].equals("sorteio", true)) {

            if (hasLotteryStarted()) {
                player.sendMessage("Sorteio ativo:")
                player.sendMessage("-> Número da loteria : ${plugin.lotteryNumber[LOTTERY_EVENT_KEY] ?: 0}")
            } else {
                player.sendMessage("Não existe sorteio ativo.")
            }

        } else if (args.size == 1 && args[0].equals("encerrar", true)) {

            if (hasLotteryStarted()) {
                lottery.reset()
                player.sendMessage("Você acabou de encerrar um sorteio.")
            } else {
                player.sendMessage("Não possui um sorteio ativo")
            }

        } else if (args.size == 1) {

            if (!args[0].isNumeric()) {
                player.sendMessage("Você pode apostar apenas número!")
                return false
            }

            if (!hasLotteryStarted()) {
                player.sendMessage("O sorteio da loteria não está disponível no momento!")
                return false
            }

            val playerLotteryNumber = args[0].toInt()

            if (playerLotteryNumber == plugin.lotteryNumber[LOTTERY_EVENT_KEY]) {
                Bukkit.getPluginManager().callEvent(LotteryEventGuess(player))
            } else {
                player.sendMessage("Que pena! você errou.")
            }
        }

        return true
    }

    private fun hasLotteryStarted(): Boolean {
        return plugin.lotteryNumber.containsKey(LOTTERY_EVENT_KEY)
    }


    companion object {
        const val LOTTERY_EVENT_KEY = "event"
    }
}