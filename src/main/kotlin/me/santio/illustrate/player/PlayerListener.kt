package me.santio.illustrate.player

import me.santio.illustrate.Illustrate
import me.santio.illustrate.database.models.Account
import me.santio.illustrate.utils.HueTools
import me.santio.utils.ChatUtils
import me.santio.utils.SupportReloads
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerGameModeChangeEvent
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
        event.player.allowFlight = true

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
        Illustrate.contexts[event.player.uniqueId]?.save()
        Illustrate.contexts.remove(event.player.uniqueId)
    }

    @EventHandler
    private fun onGamemodeSwitch(event: PlayerGameModeChangeEvent) {
        Bukkit.getScheduler().runTaskLater(Illustrate.get(), Consumer {
            event.player.allowFlight = true
        }, 1)
    }

    @EventHandler
    private fun onChat(event: AsyncPlayerChatEvent) {
        val context = Illustrate.contexts[event.player.uniqueId]!!
        if (context.data == null) {
            event.isCancelled = true
            event.player.sendMessage(ChatUtils.tacc("&cYou are unable to type until your user profile has loaded!"))
            return
        }

        val message = event.message.replace("%", "%%")
        val prefix = if (event.player.isOp) "&c[A] &7" else "&7"
        val chatColor = if (event.player.isOp) "&f" else "&7"
        val level = context.data!!.level

        event.format = ChatUtils.tacc("$prefix${HueTools.hue(level*3)}[$level] &7${event.player.displayName}$chatColor: $message")
    }

}