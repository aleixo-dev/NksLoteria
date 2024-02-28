package com.nicolas.nkloteria.utils

import java.lang.StringBuilder

object LotteryMessages {

    fun showHelpCommand(): StringBuilder {
        return StringBuilder()
            .appendLine("§e§l--------------- §bNiksLoteria §e§l---------------")
            .appendLine("§6iniciar: §fIniciar sorteio.")
            .appendLine("§6iniciar numero: §fIniciar o sorteio com número desejado.")
            .appendLine("§6numero: §fApostar em um número.")
            .appendLine("§6sorteio: §fMostra o último sorteio ativo.")
            .appendLine("§6encerrar: §fEncerrar sorteio ativo.")
            .appendLine("§e§l--------------- §bNiksLoteria §e§l---------------")
    }

    fun showLotteryStart(isAdmin: Boolean): StringBuilder {
        return if (isAdmin) {
            StringBuilder()
                .append("")

        } else {
            StringBuilder()
        }
    }
}