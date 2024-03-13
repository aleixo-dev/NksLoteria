package com.nicolas.nkloteria.utils

import com.nicolas.nkloteria.Main

class RewardUtil(private val plugin: Main) {

    fun endsWithMagnitude(reward: String): Boolean {
        for (magnitude in plugin.config.getStringList("deposito.premios")) {
            if (reward.endsWith(resolveRewardFormat(magnitude).first)) {
                return true
            }
        }
        return false
    }

    private fun resolveRewardFormat(reward: String): Pair<String, String> {
        val part = reward.split(",".toRegex()).toTypedArray()
        return Pair(part[0], part[1])
    }

    fun parseReward(reward: String): Double {

        for (key in plugin.config.getStringList("deposito.premios")) {
            val rewardFormat = resolveRewardFormat(key)
            val regex = Regex("""(\d{1,3})($rewardFormat.first{1,3})""")
            val matchResult = regex.find(reward)

            if (matchResult != null && matchResult.value == reward) {

                val (numberPart, abbreviationNumber) = matchResult.destructured
                if (rewardFormat.second == abbreviationNumber) {

                    val magnitudeMultiplier = key.split(",".toRegex()).toTypedArray()[1].toDouble()
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

    fun isValidMagnitudeMap(input: String): Boolean {
        for (key in plugin.config.getStringList("deposito.premios")) {
            val rewardKey = resolveRewardFormat(key).first
            val regex = Regex("""(\d{1,3})($rewardKey{1,3})""")
            val matchResult = regex.find(input)

            if (matchResult != null && matchResult.value == input) {
                return true
            }
        }

        return false
    }
}