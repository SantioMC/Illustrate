package me.santio.illustrate.player

import com.mongodb.client.model.ReplaceOptions
import me.santio.illustrate.Illustrate
import me.santio.illustrate.database.models.Account
import me.santio.illustrate.palettes.Palette
import me.santio.illustrate.palettes.impl.ConcretePalette
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.litote.kmongo.eq
import org.litote.kmongo.getCollection
import java.util.function.Consumer
import kotlin.math.pow
import kotlin.math.round

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

    // Formula: (level^2)+(0.45*level)+10
    fun getXPRequired(): Int {
        if (data == null) return 10000000
        val level = data!!.level.toFloat()
        return round((level.pow(2))+(0.45*level)+10).toInt()
    }

    fun asyncSave() {
        Bukkit.getScheduler().runTaskAsynchronously(Illustrate.get(), Consumer {
            save()
        })
    }

    fun save() {
        val save = data ?: return

        val collection = Illustrate.database.getCollection<Account>()
        val cutUUID = uuid.toString().replace("-", "")

        collection.replaceOne(Account::uuid eq cutUUID, save, ReplaceOptions().upsert(true))
    }

}
