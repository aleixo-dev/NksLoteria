package com.nicolas.nkloteria.utils

import org.bukkit.entity.Player

fun String.isNumeric(): Boolean {
    return this.all { it.isDigit() }
}

fun Player.hasPermissionAdmin() = hasPermission(PluginPermission.Admin.permission)
fun Player.hasPermissionBet() = hasPermission(PluginPermission.Bet.permission)