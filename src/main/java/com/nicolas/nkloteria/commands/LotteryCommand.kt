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
    private var lottery: Lottery? = null
    private var lotteryRewardQuantity: Double = 0.0

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage("Apenas jogadores ou admin podem executar este comando")
            return true
        }

        if (economy == null) return true
        val player: Player = sender

        if (args.isEmpty()) {
            LotteryMessages.showHelpCommand().lines().forEach {
                player.sendMessage(it)
            }
            return true
        }

        if (args.size == 3 && args[0].equals("iniciar", true)) {

            if (!args[1].isNumeric() || (!isValidMagnitudeMap((args[2])) || args[2].isBlank())) {
                LotteryMessages.showHelpCommand().lines().forEach {
                    player.sendMessage(it)
                }

                return true
            }

            if (!hasLotteryStarted()) {

                plugin.lotteryNumber[LOTTERY_EVENT_KEY] = args[1].toInt()
                plugin.lotteryQuantity[LOTTERY_EVENT_QUANTITY] = args[2]

                if (endsWithMagnitude(args[2])) {

                    lotteryRewardQuantity = parseReward(args[2])

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

        } else if (args.size == 1 && args[0].equals("encerrar", true)) {

            if (hasLotteryStarted()) {
                lottery?.reset()
                player.sendMessage("§eVocê acabou de encerrar um sorteio.")
            } else {
                player.sendMessage("§eNão possui um sorteio ativo")
            }

        } else if (args.size == 1) {

            if (args[0].equals("iniciar", true)) {
                player.sendMessage("§eTente usar /loteria iniciar <número> <quantidade>")
                return true
            }

            if (!args[0].isNumeric()) {
                player.sendMessage("§eVocê pode apostar apenas número!")
                return false
            }

            if (!hasLotteryStarted()) {
                player.sendMessage("§eO sorteio da loteria não está disponível no momento!")
                return false
            }

            if (args[0].toInt() == plugin.lotteryNumber[LOTTERY_EVENT_KEY]) {
                val response =
                    economy.depositPlayer(player, lotteryRewardQuantity)

                if (response.transactionSuccess()) {

                    player.sendMessage(
                        String.format(
                            "§b§lEVENTO §eVocê recebeu %s",
                            plugin.lotteryQuantity[LOTTERY_EVENT_QUANTITY]
                        )
                    )
                }

                Bukkit.getPluginManager()
                    .callEvent(LotteryEventGuess(player, plugin.lotteryQuantity[LOTTERY_EVENT_QUANTITY] ?: ""))
                lottery?.reset()
                lotteryRewardQuantity = 0.0
            } else {
                player.sendMessage("§eQue pena! você errou.")
            }
        } else {
            LotteryMessages.showHelpCommand().lines().forEach {
                player.sendMessage(it)
            }
        }

        return true
    }

    private fun hasLotteryStarted(): Boolean {
        return plugin.lotteryNumber.containsKey(LOTTERY_EVENT_KEY)
    }

    private fun endsWithMagnitude(reward: String): Boolean {
        for (magnitude in plugin.magnitudeMap.keys) {
            if (reward.endsWith(magnitude)) {
                return true
            }
        }
        return false
    }

    private fun parseReward(reward: String): Double {

        for ((key, _) in plugin.magnitudeMap) {
            val regex = Regex("""(\d{1,3})($key{1,3})""")
            val matchResult = regex.find(reward)

            if (matchResult != null && matchResult.value == reward) {

                val (numberPart, abbreviationNumber) = matchResult.destructured
                if (plugin.magnitudeMap.containsKey(abbreviationNumber)) {

                    val magnitudeMultiplier = plugin.magnitudeMap[abbreviationNumber]!!
                    val number = numberPart.toDouble()

                    return number * magnitudeMultiplier
                }
            }
        }

        return try {
            reward.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    private fun isValidMagnitudeMap(input: String): Boolean {
        for ((key, _) in plugin.magnitudeMap) {
            val regex = Regex("""(\d{1,3})($key{1,3})""")
            val matchResult = regex.find(input)

            if (matchResult != null && matchResult.value == input) {
                return true
            }
        }

        return false
    }

    companion object {
        const val LOTTERY_EVENT_KEY = "event"
        const val LOTTERY_EVENT_QUANTITY = "event_quantity"
    }
}