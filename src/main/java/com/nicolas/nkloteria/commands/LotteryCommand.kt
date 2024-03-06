package com.nicolas.nkloteria.commands

import com.nicolas.nkloteria.Main
import com.nicolas.nkloteria.event.LotteryEventGuess
import com.nicolas.nkloteria.instance.Lottery
import com.nicolas.nkloteria.utils.*
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
    private var lottery: Lottery? = null
    private var lotteryRewardQuantity: Double = 0.0
    private val rewardUtil = RewardUtil(plugin)

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage("Apenas jogadores ou admin podem executar este comando")
            return true
        }

        if (economy == null) return true
        val player: Player = sender

        if (args.isEmpty() && player.hasPermissionAdmin()) {
            LotteryMessages.showHelpCommand().lines().forEach {
                player.sendMessage(it)
            }
            return true
        }

        if (player.hasPermissionAdmin()) {
            if (args.size == 3 && args[0].equals("iniciar", true)) {

                if (!args[1].isNumeric() || (!rewardUtil.isValidMagnitudeMap((args[2])) || args[2].isBlank())) {
                    LotteryMessages.showHelpCommand().lines().forEach {
                        player.sendMessage(it)
                    }

                    return true
                }

                if (!hasLotteryStarted()) {

                    if (args[2].toCharArray()[0] == '0') {
                        LotteryMessages.showHelpCommand().lines().forEach { message ->
                            player.sendMessage(message)
                        }
                        return true
                    }

                    plugin.lotteryNumber[LOTTERY_EVENT_KEY] = args[1].toInt()
                    plugin.lotteryQuantity[LOTTERY_EVENT_QUANTITY] = args[2]

                    if (rewardUtil.endsWithMagnitude(args[2])) {

                        lotteryRewardQuantity = rewardUtil.parseReward(args[2])

                        /**
                         * Always when a bukkit runnable have been canceled,
                         * we need to create a new instance.
                         */
                        lottery = Lottery(plugin, plugin.lotteryQuantity[LOTTERY_EVENT_QUANTITY] ?: "")
                        lottery?.start()
                    }

                } else {
                    player.sendMessage("§eUm número já foi sorteado, espere até encerrar!")
                }

            } else if (args.size == 1 && args[0].equals("sorteio", true)) {

                if (hasLotteryStarted()) {
                    player.sendMessage("| §eSorteio ativo:")
                    player.sendMessage("| §eNúmero da loteria: §7§l${plugin.lotteryNumber[LOTTERY_EVENT_KEY] ?: 0}")
                } else {
                    player.sendMessage("§eNão existe sorteio ativo.")
                }

            } else if (args.size == 1 && args[0].equals("encerrar", true) && player.hasPermission("niks.loteria.*")) {

                if (hasLotteryStarted()) {
                    lottery?.reset()
                    player.sendMessage("§eVocê acabou de encerrar um sorteio.")
                } else {
                    player.sendMessage("§eNão possui um sorteio ativo")
                }
            } else {
                player.sendMessage("${LotteryMessages.pluginTag()}§7Tente usar /loteria iniciar (número) (prêmio)")
                return true
            }

        } else if (args.size == 1 && player.hasPermissionBet()) {

            if (!args[0].isNumeric()) {
                player.sendMessage("§eVocê pode apostar apenas número!")
                return false
            }

            if (!hasLotteryStarted()) {
                player.sendMessage("§eO sorteio da loteria não está disponível no momento!")
                return false
            }

            if (args[0].toInt() == plugin.lotteryNumber[LOTTERY_EVENT_KEY]) {
                economy.depositPlayer(player, lotteryRewardQuantity)

                Bukkit.getPluginManager()
                    .callEvent(LotteryEventGuess(player, plugin.lotteryQuantity[LOTTERY_EVENT_QUANTITY] ?: ""))
                lottery?.reset()
                lotteryRewardQuantity = 0.0
            } else {
                player.sendMessage("§eQue pena! você errou.")
            }
        } else {
            LotteryMessages.messageErrorPermission(player)
        }

        return true
    }

    private fun hasLotteryStarted(): Boolean {
        return plugin.lotteryNumber.containsKey(LOTTERY_EVENT_KEY)
    }

    companion object {
        const val LOTTERY_EVENT_KEY = "event"
        const val LOTTERY_EVENT_QUANTITY = "event_quantity"
    }
}