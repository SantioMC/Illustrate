package me.santio.illustrate.player

import com.mongodb.client.model.ReplaceOptions
import me.santio.illustrate.Illustrate
import me.santio.illustrate.database.models.Account
import me.santio.illustrate.palettes.Palette
import me.santio.illustrate.palettes.impl.ConcretePalette
import me.santio.illustrate.utils.HueTools
import me.santio.utils.ChatUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.litote.kmongo.eq
import org.litote.kmongo.getCollection
import java.util.function.Consumer
import kotlin.math.*

class PlayerContext(player: Player) {
    private val uuid = player.uniqueId
    private val activity: BukkitTask

    var currentBlock: Material = Material.WHITE_CONCRETE
    var currentPalette: Palette = ConcretePalette
    var brushSize: Int = 1
    var data: Account? = null

    init {
        activity = object : BukkitRunnable() {
            override fun run() {
                if (get() == null || !get()!!.isOnline) this.cancel()
                else if (data != null) data!!.xp+=20
            }
        }.runTaskTimer(Illustrate.get(), 1200, 1200)
    }

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

    fun addXP(xp: Int) {
        if (data == null || get() == null) return
        data!!.xp += xp
        if (data!!.xp >= getXPRequired()) {
            data!!.xp -= getXPRequired()
            data!!.level += 1
            val newSize = ceil((data!!.level + 1) / 25f).toInt()
            if (data!!.maxSize < newSize) data!!.maxSize = newSize

            get()!!.sendMessage(ChatUtils.tacc("&7You leveled up to ${HueTools.hue(data!!.level * 3)}Level ${data!!.level}&7!"))
            get()!!.setPlayerListName("${HueTools.hue(data!!.level * 3)}${get()!!.displayName}")
        }

        get()!!.level = data!!.level
        get()!!.exp = min(1f, (data!!.xp / getXPRequired().toFloat()))
    }

    fun asyncSave() {
        Bukkit.getScheduler().runTaskAsynchronously(Illustrate.get(), Consumer {
            save()
        })
    }

    fun save() {
        activity.cancel()
        val save = data ?: return

        val collection = Illustrate.database.getCollection<Account>()
        val cutUUID = uuid.toString().replace("-", "")

        collection.replaceOne(Account::uuid eq cutUUID, save, ReplaceOptions().upsert(true))
    }

}
