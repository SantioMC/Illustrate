package me.santio.illustrate.player

import me.santio.illustrate.palettes.Palette
import me.santio.illustrate.palettes.impl.ConcretePalette
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player

class PlayerContext(player: Player) {
    var currentBlock: Material = Material.WHITE_CONCRETE
    var currentPalette: Palette = ConcretePalette
    var brushSize: Int = 1

    val palettes: MutableList<Palette> = mutableListOf(ConcretePalette)
    private val uuid = player.uniqueId

    fun hasAccess(palette: Palette): Boolean {
        return palettes.contains(palette)
    }

    fun hasAccess(material: Material): Boolean {
        for (palette in palettes) if (palette.blocks.contains(material)) return true
        return false
    }

    fun get(): Player? {
        return Bukkit.getPlayer(uuid)
    }

}