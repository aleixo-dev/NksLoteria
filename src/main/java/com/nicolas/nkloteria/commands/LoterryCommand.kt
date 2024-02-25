package com.nicolas.nkloteria.commands

import com.nicolas.nkloteria.utils.LotteryHelpCommand
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class LotteryCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage("Apenas jogadores ou admin podem executar este comando")
            return true
        }

        val player: Player = sender

        if (args.isNotEmpty() && args.size == 1) {
            if (args[0].equals("iniciar", true)) {

                player.sendMessage("Iniciando o sorteio..")

            } else {

                try {
                    val lotteryNumber = args[0].toInt()
                    player.sendMessage("Você apostou no numero $lotteryNumber")
                } catch (exception : Exception) {
                    LotteryHelpCommand.showHelpCommand().lines().forEach {
                        player.sendMessage(it)
                    }
                }
            }

        } else {
            LotteryHelpCommand.showHelpCommand().lines().forEach {
                player.sendMessage(it)
            }
        }

        return true
    }
}