package network.lcc.emblems.blocks

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object IceEmblemBlock : Block(FabricBlockSettings.of(Material.METAL).hardness(40.0f)) {
    val identifier = Identifier("emblems", "ice_emblem")
    private val item = BlockItem(this, Item.Settings().group(ItemGroup.MISC).maxCount(1))

    fun onInitialize() {
        Registry.register(Registry.BLOCK, identifier, this)
        Registry.register(Registry.ITEM, identifier, item)
    }
}