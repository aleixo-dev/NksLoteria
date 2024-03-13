package com.nicolas.nkloteria.utils

object LotteryMessages {

    fun showHelpCommand(): StringBuilder {
        return StringBuilder()
            .appendLine(" ")
            .appendLine("§a§lNksLoteria §8➡ §fAjuda")
            .appendLine(" ")
            .appendLine("§a/loteria iniciar (numero) (prêmio) §8- §7Iniciar o sorteio com um dado número e prêmio")
            .appendLine("§a/loteria (numero) §8- §7Apostar em um número")
            .appendLine("§a/loteria sorteio §8- §7Mostra o último sorteio ativo")
            .appendLine("§a/loteria encerrar §8- §7Encerrar sorteio se tiver ativo")
            .appendLine(" ")
    }

}