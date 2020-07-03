package network.lcc.emblems

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.command.arguments.EntityArgumentType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.LiteralText
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos

val emblemsCommand = CommandManager.literal("emblems")!!

val joinCommand = emblemsCommand.then(CommandManager.literal("join")
    .then(CommandManager.argument("team", StringArgumentType.greedyString())
        .executes { context ->
            if (context.source.player == null) {
                context.source.sendError(LiteralText("Must be a player to join a team"))
                return@executes -1
            }

            val teamName = StringArgumentType.getString(context, "team")
            val team = Emblems.game.findTeam(teamName)
            if (team == null) {
                context.source.sendError(LiteralText("Could not find team \"${teamName}\""))
                return@executes -2
            }

            if (Emblems.game.findPlayerTeam(context.source.player) != null) {
                context.source.sendError(LiteralText("Can't be a member of multiple teams"))
                return@executes -3
            }

            team.addMember(context.source.player)

            Emblems.writeGame()
            context.source.sendFeedback(LiteralText("Joined team \"${teamName}\""), false)
            1
        }
))!!

val claimCommand = emblemsCommand.then(CommandManager.literal("claim")
    .executes { context ->
        if (context.source.player == null) {
            context.source.sendError(LiteralText("Must be a player to claim chunks"))
            return@executes -1
        }

        val team = Emblems.game.findPlayerTeam(context.source.player)
        if (team == null) {
            context.source.sendError(LiteralText("You aren't a part of any team!"))
            return@executes -2
        }

        val pos = context.source.player.pos
        val world = context.source.player.world

        if (pos == null || world == null) {
            context.source.sendError(LiteralText("You either don't have a position or aren't in a world!"))
            return@executes -3
        }

        val result = team.claim(ChunkPos(BlockPos(pos)), world)
        if (result != "") {
            context.source.sendError(LiteralText(result))
            return@executes -4
        }

        Emblems.writeGame()
        context.source.sendFeedback(LiteralText("Claimed chunk"), false)
        1
    }
)!!

val checkClaimCommand = emblemsCommand.then(CommandManager.literal("checkclaim")
    .executes { context ->
        if (context.source.player == null) {
            context.source.sendError(LiteralText("Must be a player to check claims"))
            return@executes -1
        }

        val pos = context.source.player.pos
        val world = context.source.player.world

        if (pos == null || world == null) {
            context.source.sendError(LiteralText("You either don't have a position or aren't in a world!"))
            return@executes -2
        }

        val result = Emblems.game.checkClaim(ChunkPos(BlockPos(pos)), world)
        if (result == null) context.source.sendFeedback(LiteralText("This chunk is unclaimed"), false)
        else context.source.sendFeedback(LiteralText("This chunk is claimed by \"${result.name}\""), false)
        1
    }
)!!

fun sendPlayerInfo(ctx: CommandContext<ServerCommandSource>, playerEntity: PlayerEntity): Int {
    if (Emblems.game.findPlayerTeam())

    ctx.source.sendFeedback(LiteralText)
    return 1
}

val playerInfoCommand = emblemsCommand.then(CommandManager.literal("player").then(
    CommandManager.argument("player", EntityArgumentType.player()).executes { ctx ->
        sendPlayerInfo(ctx, EntityArgumentType.getPlayer(ctx, "player"))
    }
).executes { ctx ->
    if (ctx.source.player == null) {
        ctx.source.sendError(LiteralText("Must be a player to get own info"))
        return@executes -1
    }

    sendPlayerInfo(ctx, ctx.source.player)
})!!

fun registerCommands() {
    CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback {
        dispatcher: CommandDispatcher<ServerCommandSource?>, _: Boolean ->
            dispatcher.register(emblemsCommand)
            dispatcher.register(joinCommand)
            dispatcher.register(claimCommand)
            dispatcher.register(checkClaimCommand)
            dispatcher.register(playerInfoCommand)
    })
}
