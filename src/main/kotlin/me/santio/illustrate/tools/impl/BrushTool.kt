package me.santio.illustrate.tools.impl

import me.santio.illustrate.Illustrate
import me.santio.illustrate.player.PlayerContext
import me.santio.illustrate.tools.Tool
import me.santio.illustrate.utils.named
import me.santio.utils.ChatUtils
import me.santio.utils.CustomItem
import me.santio.utils.inventories.CustomInventory
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object BrushTool: Tool(
    "brush",
    CustomItem(Material.ARROW).setDisplayName("&3Brush")
) {

    private fun openPaletteGUI(player: Player) {
        val context = Illustrate.contexts[player.uniqueId] ?: return
        val gui: CustomInventory = Illustrate.utils.createInventory(4, "&3&lBrush &8Select block")
            .fill(CustomItem(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("&f"))

        for ((slot, palette) in Illustrate.palettes.withIndex()) {
            var status = "&4Locked"
            if (context.hasAccess(palette)) status = "&2Unlocked"
            else if (palette.open) status = palette.reason

            gui.setItem(slot+9,
                CustomItem(palette.icon).setDisplayName("&3${palette.name}").setItemLore(
                    "&f",
                    "&7Status: $status",
                    "&aClick to open category",
                    "&f"
                ).setGlowing(context.currentPalette == palette)
            ) {
                it.isCancelled = true
                if (!context.hasAccess(palette) && !palette.open) return@setItem
                val category = Illustrate.utils.createInventory(3, "&3&lBrush &8${palette.name}")
                    .fill(CustomItem(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("&f"))

                for ((index, block) in palette.blocks.withIndex()) {
                    category.setItem(index, CustomItem(block).setDisplayName("&3${block.named()}").setItemLore("&1", "&aClick to select!", "&2").setGlowing(context.currentBlock == block)) { e ->
                        e.isCancelled = true
                        context.currentBlock = block
                        context.currentPalette = palette
                        player.closeInventory()
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(ChatUtils.tacc("&7You set your block to &3${block.named()}")))
                    }
                }

                category.open(player)
            }
        }

        gui.open(player)
    }

    private fun openSizeGUI(player: Player) {
        val context = Illustrate.contexts[player.uniqueId] ?: return
        val gui: CustomInventory = Illustrate.utils.createInventory(3, "&3&lBrush &8Change Size")
            .fill(CustomItem(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("&f"))

        for (size in 1..5) {
            gui.setItem(size + 10, CustomItem(Material.GUNPOWDER)
                .setDisplayName("&3Brush Size $size")
                .setItemLore("&1", "&aClick to select!", "&2")
                .setGlowing(context.brushSize == size)
            ) {
                it.isCancelled = true
                context.brushSize = size
                player.closeInventory()
            }
        }

        gui.open(player)
    }

    override fun onLeftClick(player: PlayerContext, block: Block?) {
        if (block == null) return
        val radius = when(player.brushSize) {
            1 -> 0.1
            2 -> 1.2
            3 -> 1.8
            4 -> 2.3
            5 -> 3.0
            else -> 0.1
        }

        val blocks = getBlocks(block.location, player.brushSize).filter { b -> b.location.distanceSquared(block.location) <= radius*radius }
        for (b in blocks) {
            if (b.type == player.currentBlock || b.type.isAir) continue
            b.type = player.currentBlock
            player.addXP(1)
        }
    }

    override fun onRightClick(player: PlayerContext, block: Block?) {
        if (player.get()!!.isSneaking) openSizeGUI(player.get()!!)
        else openPaletteGUI(player.get()!!)
    }

    override fun getItem(player: PlayerContext, item: ItemStack): ItemStack {
        val itemMeta = item.itemMeta ?: return super.getItem(player, item)

        itemMeta.lore = listOf(
            "§1",
            "§7Block: §3${player.currentBlock.named()}",
            "§7Brush Size: §3${player.brushSize}",
            "§2",
            "§3Left Click §7Paint block",
            "§3Right Click §7Change block",
            "§3Shift Right Click §7Change brush size",
            "§3"
        )
        item.itemMeta = itemMeta

        return super.getItem(player, item)
    }

    private fun getBlocks(location: Location, radius: Int): Set<Block> {
        val blocks: MutableSet<Block> = mutableSetOf()
        val startX = location.blockX
        val startZ = location.blockZ
        for (x in startX-radius..startX+radius) {
            for (z in startZ-radius..startZ+radius) {
                blocks.add(Location(location.world, x.toDouble(), location.y, z.toDouble()).block)
            }
        }
        return blocks
    }
}