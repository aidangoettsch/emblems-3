package network.lcc.emblems.state

import kotlinx.serialization.Serializable
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import network.lcc.emblems.blocks.FireEmblemBlock
import network.lcc.emblems.blocks.IceEmblemBlock

@Serializable
data class Game(
    val teams: MutableList<Team> = mutableListOf(
        Team("Fire", FireEmblemBlock.identifier),
        Team("Ice", IceEmblemBlock.identifier)
    )
) {
    fun findTeam(name: String) : Team? {
        for (t: Team in teams) {
            if (t.name == name) return t
        }
        return null
    }

    fun findPlayerTeam(player: PlayerEntity) : Team? {
        for (t: Team in teams) {
            if (t.members.contains(player.uuid)) return t
        }
        return null
    }

    fun checkClaim(chunk: ChunkPos, world: World): Team? {
        for (t: Team in teams) {
            if (t.claims.contains(Pair(chunk, world.registryKey.value))) return t
            println(t.claims)
        }
        return null
    }
}