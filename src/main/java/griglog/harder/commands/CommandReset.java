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

import java.util.UUID;

public class CommandReset {
    public static int resetPlayer(CommandContext<CommandSource> ctx){
        try {
            MinecraftServer server = ctx.getSource().getServer();
            for (GameProfile p : GameProfileArgument.getGameProfiles(ctx, "player")){
                ServerPlayerEntity player = server.getPlayerList().getPlayer(p.getId());
                if (player != null)
                    reset(player, PlayerDifficulty.get(player));
                ctx.getSource().sendSuccess(new StringTextComponent(TextFormatting.GREEN + "Player difficulty reset."), true);
            }
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            ctx.getSource().sendFailure(new StringTextComponent("Command syntax error."));
            return 1;
        }
        return 0;
    }

    public static int resetSelf(CommandContext<CommandSource> ctx){
        if (!(ctx.getSource().getEntity() instanceof ServerPlayerEntity)){
            ctx.getSource().sendFailure(new StringTextComponent("You are not a player. You have no difficulty attached."));
            return 1;
        }
        ServerPlayerEntity player = (ServerPlayerEntity) ctx.getSource().getEntity();
        reset(player, PlayerDifficulty.get(player));
        return 0;
    }

    public static int resetAll(CommandContext<CommandSource> ctx){ //TODO: also reset offline players?
        for (ServerPlayerEntity player : ctx.getSource().getServer().getPlayerList().getPlayers()){
            reset(player, PlayerDifficulty.get(player));
        }
        ctx.getSource().sendSuccess(new StringTextComponent(TextFormatting.GREEN + "All online players have got their difficulty reset."), true);
        return 0;
    }


    public static void reset(ServerPlayerEntity player, PlayerDifficulty cap){
        cap.value = 0;
        cap.toReset = false;
        player.sendMessage(new StringTextComponent("Your difficulty was reset."), new UUID(0, 0));
    }
}
