package com.nicolas.nkloteria.utils

import java.lang.StringBuilder

object LotteryMessages {

    fun showHelpCommand(): StringBuilder {
        return StringBuilder()
            .appendLine(" ")
            .appendLine("§a§lNiksLoteria §8➡ §fAjuda")
            .appendLine(" ")
            .appendLine("§a/loteria iniciar (numero) (prêmio) §8- §7Iniciar o sorteio com um dado número e prêmio")
            .appendLine("§a/loteria (numero) §8- §7Apostar em um número")
            .appendLine("§a/loteria sorteio §8- §7Mostra o último sorteio ativo")
            .appendLine("§a/loteria encerrar §8- §7Encerrar sorteio se tiver ativo")
            .appendLine(" ")
    }


    fun pluginTag(): StringBuilder {
        return StringBuilder()
            .append("§a§lNiksLoteria §8➡ ")
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