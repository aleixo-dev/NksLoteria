package com.nicolas.nkloteria.commands

import com.nicolas.nkloteria.Main
import com.nicolas.nkloteria.event.LotteryEventGuess
import com.nicolas.nkloteria.instance.Lottery
import com.nicolas.nkloteria.utils.*
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.ChatColor
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

        if (args.isEmpty() && player.hasPermission("nks.loteria.admin")) {
            LotteryMessages.showHelpCommand().lines().forEach {
                player.sendMessage(it)
            }
            return true
        }

        if (player.hasPermission("nks.loteria.admin")) {
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
                        return true
                    }

                } else {
                    player.sendMessage("${plugin.config.getString("mensagens.admin.numero-ja-sorteado")}")
                    return true
                }

            } else if (args.size == 1 && args[0].equals("sorteio", true)) {

                if (hasLotteryStarted()) {

                    val message = plugin.config.getStringList("mensagens.sorteio").map { line ->
                        line.replace("{numero_loteria}", plugin.lotteryNumber[LOTTERY_EVENT_KEY].toString())
                    }

                    message.forEach { player.sendMessage(ChatColor.translateAlternateColorCodes('&', it)) }

                    return true

                } else {
                    player.sendMessage("${plugin.config.getString("mensagens.nenhum-sorteio-ativo")}")
                    return true
                }

            } else if (args.size == 1 && args[0].equals("encerrar", true)) {

                if (hasLotteryStarted()) {
                    lottery?.reset()
                    player.sendMessage("${plugin.config.getString("mensagens.admin.sorteio-encerrar")}")
                    return true
                } else {
                    player.sendMessage("${plugin.config.getString("mensagens.nenhum-sorteio-ativo")}")
                    return true
                }
            }
        }
        if (player.hasPermission("nks.apostar")) {

            if (!hasLotteryStarted()) {
                player.sendMessage(" ")
                player.sendMessage("${plugin.config.getString("mensagens.nenhum-sorteio-ativo")}")
                player.sendMessage(" ")
                return true
            }

            if (args.size == 1 && args[0].isNumeric()) {

                if (plugin.config.getBoolean("saque.habilitar")) {

                    if (RewardUtil(plugin).endsWithMagnitude(
                            plugin.config.getString("saque.valor").isNullOrBlank().toString()
                        )
                    ) {
                        return true
                    }

                    val withdrawValue = RewardUtil(plugin).parseReward(plugin.config.getString("saque.valor")!!)

                    val response = economy.withdrawPlayer(player, withdrawValue)
                    if (response.transactionSuccess()) {
                        player.sendMessage(String.format("Foi cobrado de você %s", economy.format(response.amount)))
                    } else {
                        player.sendMessage("Você não possui dinheiro suficiente para fazer isto!")
                        return true
                    }
                }

                if (args[0].toInt() == plugin.lotteryNumber[LOTTERY_EVENT_KEY]) {
                    economy.depositPlayer(player, lotteryRewardQuantity)

                    Bukkit.getPluginManager()
                        .callEvent(LotteryEventGuess(player, plugin.lotteryQuantity[LOTTERY_EVENT_QUANTITY] ?: ""))
                    lottery?.reset()
                    lotteryRewardQuantity = 0.0
                    return true
                } else {
                    player.sendMessage("${plugin.config.getString("mensagens.jogador.tentativa")}")
                    return true
                }
            } else {
                player.sendMessage("${plugin.config.getString("mensagens.jogador.erro-na-aposta")}")
            }
        } else {
            player.sendMessage("${plugin.config.getString("mensagens.jogador.sem-permissao")}")
            return true
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