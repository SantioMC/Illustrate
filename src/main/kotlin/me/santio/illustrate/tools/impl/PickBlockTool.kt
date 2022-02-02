package me.santio.illustrate.tools.impl

import me.santio.illustrate.palettes.Palette
import me.santio.illustrate.player.PlayerContext
import me.santio.illustrate.tools.Tool
import me.santio.illustrate.utils.named
import me.santio.utils.ChatUtils
import me.santio.utils.CustomItem
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Material
import org.bukkit.block.Block

object PickBlockTool: Tool(
    "pick_block",
    CustomItem(Material.GHAST_TEAR).setDisplayName("&3Pick Block")
) {

    override fun onLeftClick(player: PlayerContext, block: Block?) {
        if (block == null) return
        val palette: Palette = Palette.getPalette(block.type) ?: return

        if (player.hasAccess(palette) || palette.open) {
            player.currentBlock = block.type
            player.currentPalette = palette
            player.get()!!.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(ChatUtils.tacc("&7You set your block to &3${block.type.named()}")))
        } else {
            player.get()!!.sendMessage(ChatUtils.tacc("&cYou do not have this block unlocked!"))
        }
    }

}