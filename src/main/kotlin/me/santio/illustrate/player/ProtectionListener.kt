package me.santio.illustrate.player

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.FallingBlock
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockFromToEvent
import org.bukkit.event.block.BlockPhysicsEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerDropItemEvent

object ProtectionListener: Listener {

    @EventHandler
    private fun onBreak(event: BlockBreakEvent) {
        if (event.player.gameMode != GameMode.CREATIVE) event.isCancelled = true
    }

    @EventHandler
    private fun onBreak(event: BlockPlaceEvent) {
        if (event.player.gameMode != GameMode.CREATIVE) event.isCancelled = true
    }

    @EventHandler
    private fun onBreak(event: PlayerDropItemEvent) {
        if (event.player.gameMode != GameMode.CREATIVE) event.isCancelled = true
    }

    @EventHandler
    private fun onBreak(event: EntityPickupItemEvent) {
        if (event.entity !is Player) return
        if ((event.entity as Player).gameMode != GameMode.CREATIVE) event.isCancelled = true
    }

    @EventHandler
    private fun onDamage(event: EntityDamageEvent) {
        if (event.entity is Player) event.isCancelled = true
    }

    @EventHandler
    private fun onPhysics(event: EntityChangeBlockEvent) {
        if (event.entity is FallingBlock) event.isCancelled = true
    }

}