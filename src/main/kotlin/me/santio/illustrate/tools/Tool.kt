package me.santio.illustrate.tools

import me.santio.illustrate.Illustrate
import me.santio.illustrate.player.PlayerContext
import me.santio.utils.CustomItem
import org.bukkit.block.Block
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

open class Tool(val id: String, val item: CustomItem): Listener {

    open fun onLeftClick(player: PlayerContext, block: Block?) {}
    open fun onRightClick(player: PlayerContext, block: Block?) {}

    open fun getItem(player: PlayerContext): ItemStack = getItem(player, item)
    open fun getItem(player: PlayerContext, item: ItemStack): ItemStack {
        val i = item
        val itemMeta = item.itemMeta ?: return item
        Illustrate.utils.NBTUtils.set(itemMeta, "tool", id)
        i.itemMeta = itemMeta

        return i
    }

}