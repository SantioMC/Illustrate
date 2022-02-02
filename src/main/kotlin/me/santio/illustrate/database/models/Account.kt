package me.santio.illustrate.database.models

import me.santio.illustrate.palettes.impl.ConcretePalette

data class Account (
    var uuid: String,
    var level: Int = 1,
    var xp: Int = 1,
    var color: String = "&7",
    var maxSize: Int = 1,
    var packs: MutableList<String> = mutableListOf(ConcretePalette.name)
)
