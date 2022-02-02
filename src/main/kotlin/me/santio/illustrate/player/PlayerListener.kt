package me.santio.illustrate.player

import me.santio.illustrate.Illustrate
import me.santio.utils.SupportReloads
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object PlayerListener: Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    @SupportReloads
    private fun onJoin(event: PlayerJoinEvent) {
        Illustrate.contexts[event.player.uniqueId] = PlayerContext(event.player)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private fun onQuit(event: PlayerQuitEvent) {
        Illustrate.contexts.remove(event.player.uniqueId)
    }

}