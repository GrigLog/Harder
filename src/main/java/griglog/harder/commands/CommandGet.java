package griglog.harder.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import griglog.harder.capability.PlayerDifficulty;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.GameProfileArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandGet {
    public static int getPlayer(CommandContext<CommandSource> ctx){
        try {
            MinecraftServer server = ctx.getSource().getServer();
            for (GameProfile p : GameProfileArgument.getGameProfiles(ctx, "player")){
                ServerPlayerEntity player = server.getPlayerList().getPlayer(p.getId());
                if (player != null)
                    printDifficulty(ctx.getSource(), PlayerDifficulty.get(player).value);
            }
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            ctx.getSource().sendFailure(new StringTextComponent("Command syntax error."));
            return 1;
        }
        return 0;
    }

    public static int getSelf(CommandContext<CommandSource> ctx){
        if (!(ctx.getSource().getEntity() instanceof ServerPlayerEntity)){
            ctx.getSource().sendFailure(new StringTextComponent("You are not a player. You have no difficulty attached."));
            return 1;
        }
        ServerPlayerEntity player = (ServerPlayerEntity) ctx.getSource().getEntity();
        printDifficulty(ctx.getSource(), PlayerDifficulty.get(player).value);
        return 0;
    }

    private static void printDifficulty(CommandSource src, int d){
        src.sendSuccess(new StringTextComponent("Player difficulty: " + d), true);
    }
}
