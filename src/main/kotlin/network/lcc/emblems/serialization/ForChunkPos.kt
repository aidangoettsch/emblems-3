package network.lcc.emblems.serialization

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.LongDescriptor
import kotlinx.serialization.withName
import net.minecraft.util.math.ChunkPos

@Serializer(forClass = net.minecraft.util.math.ChunkPos::class)
object ForChunkPos : KSerializer<ChunkPos> {
    override val descriptor: SerialDescriptor = LongDescriptor.withName("ChunkPos")
    override fun serialize(encoder: Encoder, obj: ChunkPos) = encoder.encodeLong(obj.toLong())
    override fun deserialize(decoder: Decoder): ChunkPos = ChunkPos(decoder.decodeLong())
}