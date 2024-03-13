package com.nicolas.nkloteria

import com.nicolas.nkloteria.commands.LotteryCommand
import com.nicolas.nkloteria.listener.LotteryGuessListener
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    var lotteryNumber: HashMap<String, Int> = hashMapOf()
    var lotteryQuantity: HashMap<String, String> = hashMapOf()

    override fun onEnable() {

        if (!setupEconomy()) {
            logger.info(
                String.format(
                    "Plugin foi desabilitado por não encontrar dependência do Vault.",
                    description.name
                )
            )
            server.pluginManager.disablePlugin(this)
            return
        }

        server.consoleSender.sendMessage("${ChatColor.YELLOW} _   _ _        _          _            _")
        server.consoleSender.sendMessage("${ChatColor.YELLOW}| \\ | | | _____| |    ___ | |_ ___ _ __(_) __ _")
        server.consoleSender.sendMessage("${ChatColor.YELLOW}|  \\| | |/ / __| |   / _ \\| __/ _ | '__| |/ _` |")
        server.consoleSender.sendMessage("${ChatColor.YELLOW}| |\\  |   <\\__ | |__| (_) | ||  __| |  | | (_| |")
        server.consoleSender.sendMessage("${ChatColor.YELLOW}|_| \\_|_|\\_|___|_____\\___/ \\__\\___|_|  |_|\\__,_|")
        server.consoleSender.sendMessage("§a[NksLoteria] §7- §aPlugin ativo")

        getCommand("loteria")?.setExecutor(LotteryCommand(this))
        Bukkit.getPluginManager().registerEvents(LotteryGuessListener(this), this)

        /** load/create config.yml file */
        saveDefaultConfig()

    }

    override fun onDisable() {
        lotteryNumber.clear()
        lotteryQuantity.clear()
    }

    private fun setupEconomy(): Boolean {

        val economyProvider = server.servicesManager.getRegistration(
            Economy::class.java
        )?.provider

        if (economyProvider == null) return false

        economy = economyProvider
        return true
    }

    companion object {

        private var economy: Economy? = null

        @JvmStatic
        fun getEconomy(): Economy? {
            return economy
        }
    }
}
