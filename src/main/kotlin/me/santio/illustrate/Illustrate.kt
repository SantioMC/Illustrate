package me.santio.illustrate

import me.santio.utils.SantioUtils
import org.bukkit.plugin.java.JavaPlugin

class Illustrate : JavaPlugin() {

    companion object {
        lateinit var utils: SantioUtils
        fun get(): Illustrate = getPlugin(Illustrate::class.java)
    }

    override fun onEnable() {
        utils = SantioUtils(this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}