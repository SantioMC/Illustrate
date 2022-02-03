package me.santio.illustrate.utils

import net.md_5.bungee.api.ChatColor
import java.awt.Color

object HueTools {
    fun hue(hue: Int): ChatColor {
        return hue(hue, 100)
    }

    fun hue(hue: Int, brightness: Int): ChatColor {
        return ChatColor.of(Color.getHSBColor(hue / 360f % 1, 1f, brightness / 100f))
    }
}