package me.santio.illustrate.tools

import me.santio.illustrate.Illustrate
import me.santio.utils.ChatUtils
import me.santio.utils.CustomItem
import me.santio.utils.SupportReloads
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import java.util.*
import java.util.function.Consumer

object ToolListener: Listener {

    private val cooldown: MutableList<UUID> = mutableListOf()

    @EventHandler
    @SupportReloads
    private fun onJoin(event: PlayerJoinEvent) {
        event.player.inventory.clear()
        Illustrate.tools.values.forEach { event.player.inventory.addItem(it.getItem(Illustrate.contexts[event.player.uniqueId]!!)) }
    }

    @EventHandler
    private fun onInteract(event: PlayerInteractEvent) {
        if (cooldown.contains(event.player.uniqueId)) return
        if (event.action == Action.PHYSICAL) return

        val context = Illustrate.contexts[event.player.uniqueId] ?: return
        val item = event.player.inventory.itemInMainHand
        val meta = item.itemMeta ?: return
        val tool = Illustrate.tools[Illustrate.utils.NBTUtils.get(meta, "tool")] ?: return

        if (context.data == null) {
            event.player.sendMessage(ChatUtils.tacc("&cYou are not able to use tools until your user data has loaded!"))
            return
        }

        if (event.action == Action.LEFT_CLICK_AIR || event.action == Action.LEFT_CLICK_BLOCK) tool.onLeftClick(context, event.clickedBlock)
        else tool.onRightClick(context, event.clickedBlock)

        cooldown.add(event.player.uniqueId)
        Bukkit.getScheduler().runTaskLater(Illustrate.get(), Consumer { cooldown.remove(event.player.uniqueId) }, 1)
    }

}