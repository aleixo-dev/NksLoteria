package com.nicolas.nkloteria

import com.nicolas.nkloteria.commands.LotteryCommand
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {

        getCommand("loteria")?.setExecutor(LotteryCommand())

    }

    override fun onDisable() {

    }
}
