package com.nicolas.nkloteria

import com.nicolas.nkloteria.commands.LotteryCommand
import com.nicolas.nkloteria.listener.LotteryGuessListener
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    var lotteryNumber: HashMap<String, Int> = hashMapOf()
    var lotteryQuantity: HashMap<String, String> = hashMapOf()
    var magnitudeMap: HashMap<String, Double> = hashMapOf(
        "K" to 1E3,
        "M" to 1E6,
        "B" to 1E9,
        "T" to 1E12,
        "Q" to 1E15,
        "QQ" to 1E18,
        "S" to 1E21,
        "SS" to 1E24,
        "OC" to 1E27,
        "N" to 1E30,
        "D" to 1E33,
        "UN" to 1E36,
        "DD" to 1E39,
        "TR" to 1E42,
        "QT" to 1E45,
        "QN" to 1E48,
        "SD" to 1E51,
        "SPD" to 1E54,
        "OD" to 1E57,
        "ND" to 1E60,
        "VG" to 1E63,
        "UVG" to 1E66,
        "DVG" to 1E69,
        "TVG" to 1E72,
        "QTV" to 1E75,
        "QNV" to 1E78,
        "SEV" to 1E81,
        "SPV" to 1E84,
        "OVG" to 1E87,
        "NVG" to 1E90,
        "TG" to 1E93
    )

    override fun onEnable() {

        if (!setupEconomy()) {
            logger.severe(
                String.format(
                    "Plugin foi desabilitador por não encontrar a dependência Vault",
                    description.name
                )
            )
            server.pluginManager.disablePlugin(this)
            return
        }

        Bukkit.getConsoleSender().sendMessage("§6[NKLoteria] §7- §e§lAtivo com sucesso!")
        getCommand("loteria")?.setExecutor(LotteryCommand(this))

        Bukkit.getPluginManager().registerEvents(LotteryGuessListener(this), this)
    }

    override fun onDisable() {
        lotteryNumber.clear()
        magnitudeMap.clear()
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
