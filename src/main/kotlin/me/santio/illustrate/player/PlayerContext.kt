package me.santio.illustrate.player

import me.santio.illustrate.database.models.Account
import me.santio.illustrate.palettes.Palette
import me.santio.illustrate.palettes.impl.ConcretePalette
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player

class PlayerContext(player: Player) {
    private val uuid = player.uniqueId

    var currentBlock: Material = Material.WHITE_CONCRETE
    var currentPalette: Palette = ConcretePalette
    var brushSize: Int = 1
    var data: Account? = null

    fun hasAccess(palette: Palette): Boolean {
        return if (data == null) false
        else data!!.packs.contains(palette.name)
    }

    fun get(): Player? {
        return Bukkit.getPlayer(uuid)
    }

}