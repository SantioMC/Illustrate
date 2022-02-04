package me.santio.illustrate.commands

import me.santio.illustrate.Illustrate
import me.santio.illustrate.utils.HueTools
import me.santio.utils.ChatUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object LevelCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true

        val context = Illustrate.contexts[sender.uniqueId]!!
        if (context.data == null) {
            sender.sendMessage(ChatUtils.tacc("&cYour user data has not loaded yet! Try again later..."))
            return true
        }

        val level = context.data!!.level
        val xp = context.data!!.xp
        val required = context.getXPRequired()

        sender.sendMessage(ChatUtils.tacc("&7Level: ${HueTools.hue(level * 3)}Level $level &7&o($xp/$required)"))

        return true
    }
}