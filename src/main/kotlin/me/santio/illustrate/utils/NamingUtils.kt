@file:Suppress("unused")

package me.santio.illustrate.utils

import org.bukkit.Material

fun Material.named(): String {
    return this.name.replace("_", " ").propercase()
}

fun String.propercase(): String {
    return this.split(" ").joinToString(" ") { it.substring(0, 1).uppercase() + it.substring(1).lowercase() }
}