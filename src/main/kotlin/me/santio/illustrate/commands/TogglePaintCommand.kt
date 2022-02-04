package me.santio.illustrate.commands

import me.santio.illustrate.Illustrate
import me.santio.utils.ChatUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

object TogglePaintCommand: CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player && args.isEmpty() || (args.isNotEmpty() && args[0].lowercase() != "all" && Bukkit.getWorld(args[0]) == null)) {
            sender.sendMessage(ChatUtils.tacc("&c/togglepaint <world>"))
            return true
        }

        if ((args.isEmpty() && sender is Player) || args.isNotEmpty() && args[0].lowercase() == "all") {
            Illustrate.paintingDisabled = !Illustrate.paintingDisabled
            Bukkit.broadcastMessage(ChatUtils.tacc(
                if (Illustrate.paintingDisabled) "\n&7Painting has been &4disabled &7by &c${sender.name}&7!\n"
                else "\n&7Painting has been &2enabled &7by &a${sender.name}&7!\n"
            ))
        } else {
            val world = args[0].lowercase()
            if (Illustrate.disabledWorlds.contains(world)) Illustrate.disabledWorlds.remove(world)
            else Illustrate.disabledWorlds.add(world)
            Bukkit.getOnlinePlayers().filter { p -> p.world.name.lowercase() == world }.forEach { p ->
                p.sendMessage(ChatUtils.tacc(
                    if (Illustrate.disabledWorlds.contains(world)) "\n&7Painting in this world has been &4disabled&7 by &c${sender.name}\n&f"
                    else "\n&7Painting in this world has been &2enabled&7 by &a${sender.name}\n&f"
                ))
            }
        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        if (args.isEmpty()) return mutableListOf()
        val complete: MutableList<String> = mutableListOf("all")
        complete.addAll(Bukkit.getWorlds().map { w -> w.name })
        return complete.filter { arg -> arg.lowercase().startsWith(args[0].lowercase()) }.toMutableList()
    }
}