package griglog.harder.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import griglog.harder.capability.PlayerDifficulty;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class CommandGet {
    public static int getPlayer(CommandContext<CommandSourceStack> ctx){
        try {
            MinecraftServer server = ctx.getSource().getServer();
            for (GameProfile p : GameProfileArgument.getGameProfiles(ctx, "player")){
                ServerPlayer player = server.getPlayerList().getPlayer(p.getId());
                if (player != null)
                    printDifficulty(ctx.getSource(), PlayerDifficulty.get(player).value);
            }
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            ctx.getSource().sendFailure(new TextComponent("Command syntax error."));
            return 1;
        }
        return 0;
    }

    public static int getSelf(CommandContext<CommandSourceStack> ctx){
        if (!(ctx.getSource().getEntity() instanceof ServerPlayer)){
            ctx.getSource().sendFailure(new TextComponent("You are not a player. You have no difficulty attached."));
            return 1;
        }
        ServerPlayer player = (ServerPlayer) ctx.getSource().getEntity();
        printDifficulty(ctx.getSource(), PlayerDifficulty.get(player).value);
        return 0;
    }

    private static void printDifficulty(CommandSourceStack src, int d){
        src.sendSuccess(new TextComponent("Player difficulty: " + d), true);
    }
}
