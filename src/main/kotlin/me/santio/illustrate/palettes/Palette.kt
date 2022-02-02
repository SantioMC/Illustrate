package me.santio.illustrate.palettes

import me.santio.illustrate.Illustrate
import org.bukkit.Material

open class Palette(
    val name: String,
    val icon: Material,
    val open: Boolean,
    val blocks: List<Material>,
    val reason: String = "&3&oTemporary Available"
) {

    companion object {
        fun getPalette(material: Material): Palette? {
            for (palette in Illustrate.palettes) if (palette.blocks.contains(material)) return palette
            return null
        }
    }

}