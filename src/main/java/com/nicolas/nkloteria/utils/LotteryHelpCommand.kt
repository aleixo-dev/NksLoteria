package com.nicolas.nkloteria.utils

object LotteryHelpCommand {

    fun showHelpCommand() : StringBuilder {
        return java.lang.StringBuilder()
            .appendLine(" ")
            .appendLine("§e§l--------------- §fAjuda §e§l---------------")
            .appendLine(" ")
            .appendLine("§6<iniciar>: §fIniciar sorteio.")
            .appendLine("§6<iniciar> <numero>: §fIniciar o sorteio com número desejado.")
            .appendLine("§6<numero>: §fApostar em um número.")
            .appendLine(" ")
            .appendLine("§e§l--------------- §fAjuda §e§l---------------")
            .append(" ")
    }
}