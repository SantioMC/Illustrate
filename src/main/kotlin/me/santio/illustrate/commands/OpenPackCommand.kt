package me.santio.illustrate.commands

import me.santio.illustrate.Illustrate
import me.santio.utils.ChatUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

object OpenPackCommand: CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(ChatUtils.tacc("&c/openpack <pack>"))
            return true
        }

        val palette = Illustrate.palettes.firstOrNull { p -> p.name.lowercase() == args[0].lowercase() }
        if (palette == null) {
            sender.sendMessage(ChatUtils.tacc("&cThat palette doesn't exist!"))
            return true
        }

        palette.open = !palette.open
        Bukkit.broadcastMessage(ChatUtils.tacc(
            if (palette.open) "\n&7The palette &3${palette.name}&7 is now &2Temporary Available&7!\n&f"
            else "\n&7The palette &3${palette.name}&7 is no longer &4${ChatUtils.strip(ChatUtils.tacc(palette.reason))}&7!\n&f"
        ))

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        if (args.isEmpty()) return mutableListOf()
        val complete: MutableList<String> = mutableListOf()
        complete.addAll(Illustrate.palettes.map { p -> p.name })
        return complete.filter { arg -> arg.lowercase().startsWith(args[0].lowercase()) }.toMutableList()
    }
}