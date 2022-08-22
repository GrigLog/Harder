package griglog.harder.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import griglog.harder.capability.PlayerDifficulty;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class CommandReset {
    public static int resetPlayer(CommandContext<CommandSourceStack> ctx){
        try {
            MinecraftServer server = ctx.getSource().getServer();
            for (GameProfile p : GameProfileArgument.getGameProfiles(ctx, "player")){
                ServerPlayer player = server.getPlayerList().getPlayer(p.getId());
                if (player != null)
                    reset(player, PlayerDifficulty.get(player));
                ctx.getSource().sendSuccess(new TextComponent(ChatFormatting.GREEN + "Player difficulty reset."), true);
            }
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            ctx.getSource().sendFailure(new TextComponent("Command syntax error."));
            return 1;
        }
        return 0;
    }

    public static int resetSelf(CommandContext<CommandSourceStack> ctx){
        if (!(ctx.getSource().getEntity() instanceof ServerPlayer)){
            ctx.getSource().sendFailure(new TextComponent("You are not a player. You have no difficulty attached."));
            return 1;
        }
        ServerPlayer player = (ServerPlayer) ctx.getSource().getEntity();
        reset(player, PlayerDifficulty.get(player));
        return 0;
    }

    public static int resetAll(CommandContext<CommandSourceStack> ctx){
        for (ServerPlayer player : ctx.getSource().getServer().getPlayerList().getPlayers()){
            reset(player, PlayerDifficulty.get(player));
        }
        ctx.getSource().sendSuccess(new TextComponent(ChatFormatting.GREEN + "All online players have got their difficulty reset."), true);
        return 0;
    }


    public static void reset(ServerPlayer player, PlayerDifficulty cap){
        cap.value = 0;
        cap.toReset = false;
        player.sendMessage(new TextComponent("Your difficulty was reset."), new UUID(0, 0));
    }
}
