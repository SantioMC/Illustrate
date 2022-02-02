package me.santio.illustrate

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import me.santio.illustrate.palettes.Palette
import me.santio.illustrate.palettes.impl.ConcretePalette
import me.santio.illustrate.palettes.impl.OrePalette
import me.santio.illustrate.player.PlayerContext
import me.santio.illustrate.player.PlayerListener
import me.santio.illustrate.player.ProtectionListener
import me.santio.illustrate.tools.Tool
import me.santio.illustrate.tools.ToolListener
import me.santio.illustrate.tools.impl.BrushTool
import me.santio.illustrate.tools.impl.PickBlockTool
import me.santio.utils.SantioUtils
import org.bukkit.plugin.java.JavaPlugin
import org.litote.kmongo.KMongo
import java.util.*

class Illustrate : JavaPlugin() {

    companion object {
        lateinit var utils: SantioUtils
        lateinit var mongo: MongoClient
        lateinit var database: MongoDatabase

        val contexts: MutableMap<UUID, PlayerContext> = mutableMapOf()
        val palettes: MutableList<Palette> = mutableListOf()
        val tools: MutableMap<String, Tool> = mutableMapOf()

        fun get(): Illustrate = getPlugin(Illustrate::class.java)
    }

    override fun onEnable() {
        utils = SantioUtils(this)

        // Configuration
        saveDefaultConfig()

        // Connect to MongoDB
        System.setProperty("org.litote.mongo.test.mapping.service", "org.litote.kmongo.jackson.JacksonClassMappingTypeService")
        mongo = KMongo.createClient(config.getString("mongo.uri", "mongodb://localhost/mydb")!!)
        database = mongo.getDatabase(config.getString("mongo.database", "mydb")!!)

        // Register Palettes and Tools (Please teach me a way to automate this w/ reflection)
        registerPalettes(ConcretePalette, OrePalette)
        registerTools(BrushTool, PickBlockTool)

        // Register listeners
        server.pluginManager.registerEvents(PlayerListener, this)
        server.pluginManager.registerEvents(ToolListener, this)
        server.pluginManager.registerEvents(ProtectionListener, this)

        utils.supportReloads()
    }

    private fun registerPalettes(vararg palettes: Palette) {
        Illustrate.palettes.addAll(palettes)
    }

    private fun registerTools(vararg tools: Tool) {
        for (tool in tools) Illustrate.tools[tool.id] = tool
    }

}