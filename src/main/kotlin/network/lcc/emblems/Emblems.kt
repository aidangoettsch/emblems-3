package network.lcc.emblems

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import net.fabricmc.api.ModInitializer
import network.lcc.emblems.blocks.FireEmblemBlock
import network.lcc.emblems.blocks.IceEmblemBlock
import network.lcc.emblems.state.Config
import network.lcc.emblems.state.Game
import java.io.File

object Emblems : ModInitializer {
    private val json = Json(JsonConfiguration.Stable.copy(prettyPrint = true))
    private val configDir = File("config")
    private val configFile = File("config/emblems_config.json")
    private val dataFile = File("Emblems.json")
    lateinit var config: Config
    lateinit var game: Game

    override fun onInitialize() {
        FireEmblemBlock.onInitialize()
        IceEmblemBlock.onInitialize()
        registerCommands()

        configDir.mkdirs()

        if (configFile.isFile) config = json.parse(Config.serializer(), configFile.readText())
        else {
            config = Config()
            writeConfig()
        }
        if (dataFile.isFile) game = json.parse(Game.serializer(), dataFile.readText())
        else {
            game = Game()
            writeGame()
        }
    }

    private fun writeConfig() {
        configDir.mkdirs()

        configFile.writeText(json.stringify(Config.serializer(), config))
    }

    fun writeGame() {
        dataFile.writeText(json.stringify(Game.serializer(), game))
    }
}
