@file:UseSerializers(ForUuid::class, ForChunkPos::class, ForIdentifier::class)
package network.lcc.emblems.state

import drawer.ForIdentifier
import drawer.ForUuid
import kotlinx.serialization.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import network.lcc.emblems.Emblems
import network.lcc.emblems.serialization.ForChunkPos
import java.util.UUID

@Serializable
data class Team(
    val name: String,
    val emblemBlock: Identifier,
    val members: MutableList<UUID> = mutableListOf(),
    val powerMap: MutableMap<UUID, Double> = mutableMapOf(),
    val claims: MutableList<Pair<ChunkPos, Identifier>> = mutableListOf()
) {
    val power: Double
        get() = powerMap.values.sum()

    val powerUsed: Double
        get() = claims.size.toDouble()

    fun addMember(p: PlayerEntity) {
        members.add(p.uuid)
        powerMap[p.uuid] = Emblems.config.powerMax
    }

    fun claim(chunk: ChunkPos, world: World): String {
        val claimedBy = Emblems.game.checkClaim(chunk, world)
        if (claimedBy == this) return "This chunk is already claimed by your team!"

        if (powerUsed + 1 > power) return "Not enough power to claim this chunk!"

        if (claimedBy != null) {
            if (claimedBy.powerUsed > claimedBy.power) {
                claimedBy.claims.remove(Pair(chunk, world.registryKey.value))
            } else return "This chunk is already claimed by \"${claimedBy.name}\" and they have the power to defend it!"
        }

        claims.add(Pair(chunk, world.registryKey.value))

        return ""
    }
}
