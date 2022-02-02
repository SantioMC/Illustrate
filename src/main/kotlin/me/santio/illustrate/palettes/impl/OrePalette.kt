package me.santio.illustrate.palettes.impl

import me.santio.illustrate.palettes.Palette
import org.bukkit.Material

object OrePalette: Palette(
    "Ore",
    Material.COAL_ORE,
    false,
    listOf(
        Material.COAL_ORE,
        Material.IRON_ORE,
        Material.GOLD_ORE,
        Material.REDSTONE_ORE,
        Material.LAPIS_ORE,
        Material.EMERALD_ORE,
        Material.DIAMOND_ORE
    )
)