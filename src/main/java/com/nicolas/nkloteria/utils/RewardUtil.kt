package com.nicolas.nkloteria.utils

import com.nicolas.nkloteria.Main

class RewardUtil(private val plugin : Main) {

    fun endsWithMagnitude(reward: String): Boolean {
        for (magnitude in plugin.magnitudeMap.keys) {
            if (reward.endsWith(magnitude)) {
                return true
            }
        }
        return false
    }

    fun parseReward(reward: String): Double {

        for ((key, _) in plugin.magnitudeMap) {
            val regex = Regex("""(\d{1,3})($key{1,3})""")
            val matchResult = regex.find(reward)

            if (matchResult != null && matchResult.value == reward) {

                val (numberPart, abbreviationNumber) = matchResult.destructured
                if (plugin.magnitudeMap.containsKey(abbreviationNumber)) {

                    val magnitudeMultiplier = plugin.magnitudeMap[abbreviationNumber]!!
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
        for ((key, _) in plugin.magnitudeMap) {
            val regex = Regex("""(\d{1,3})($key{1,3})""")
            val matchResult = regex.find(input)

            if (matchResult != null && matchResult.value == input) {
                return true
            }
        }

        return false
    }
}