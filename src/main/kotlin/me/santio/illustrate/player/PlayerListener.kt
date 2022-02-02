package me.santio.illustrate.player

import com.mongodb.client.model.ReplaceOptions
import me.santio.illustrate.Illustrate
import me.santio.illustrate.database.models.Account
import me.santio.utils.SupportReloads
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import java.util.function.Consumer

object PlayerListener: Listener {

    private val collection = Illustrate.database.getCollection<Account>()

    @EventHandler(priority = EventPriority.LOWEST)
    @SupportReloads
    private fun onJoin(event: PlayerJoinEvent) {
        val uuid = event.player.uniqueId.toString().replace("-", "")
        Illustrate.contexts[event.player.uniqueId] = PlayerContext(event.player)

        // Get data saved from db
        Bukkit.getScheduler().runTaskAsynchronously(Illustrate.get(), Consumer {
            var data = collection.findOne(Account::uuid eq uuid)
            if (data == null) data = Account(uuid)

            Illustrate.contexts[event.player.uniqueId]?.data = data
        })
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private fun onQuit(event: PlayerQuitEvent) {
        val data = Illustrate.contexts[event.player.uniqueId]?.data
        val uuid = event.player.uniqueId.toString().replace("-", "")
        Illustrate.contexts.remove(event.player.uniqueId)

        if (data == null) return
        Bukkit.getScheduler().runTaskAsynchronously(Illustrate.get(), Consumer {
            collection.replaceOne(Account::uuid eq uuid, data, ReplaceOptions().upsert(true))
        })
    }

}