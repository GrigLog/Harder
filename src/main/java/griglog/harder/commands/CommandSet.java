package griglog.harder.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import griglog.harder.capability.PlayerDifficulty;
import griglog.harder.config.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class CommandSet {
    public static int setPlayer(CommandContext<CommandSourceStack> ctx){
        try {
            int value = IntegerArgumentType.getInteger(ctx, "value");
            if (!checkValid(value, ctx.getSource()))
                return 1;
            MinecraftServer server = ctx.getSource().getServer();
            for (GameProfile p : GameProfileArgument.getGameProfiles(ctx, "player")){
                ServerPlayer player = server.getPlayerList().getPlayer(p.getId());
                if (player != null) {
                    set(player, value);
                    ctx.getSource().sendSuccess(new TextComponent(ChatFormatting.GREEN + "Player difficulty set to " + value + '.'), true);
                }
            }
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            ctx.getSource().sendFailure(new TextComponent("Command syntax error."));
            return 1;
        }
        return 0;
    }

    public static int setSelf(CommandContext<CommandSourceStack> ctx){
        if (!(ctx.getSource().getEntity() instanceof ServerPlayer)){
            ctx.getSource().sendFailure(new TextComponent("You are not a player. You have no difficulty attached."));
            return 1;
        }
        int value = IntegerArgumentType.getInteger(ctx, "value");
        if (!checkValid(value, ctx.getSource()))
            return 1;
        ServerPlayer player = (ServerPlayer) ctx.getSource().getEntity();
        set(player, value);
        return 0;
    }

    public static int setAll(CommandContext<CommandSourceStack> ctx){ //TODO: also set offline players?
        int value = IntegerArgumentType.getInteger(ctx, "value");
        if (!checkValid(value, ctx.getSource()))
            return 1;
        for (ServerPlayer player : ctx.getSource().getServer().getPlayerList().getPlayers())
            set(player, value);
        ctx.getSource().sendSuccess(new TextComponent(ChatFormatting.GREEN + "All online players have got their difficulty set to " + value + '.'), true);
        return 0;
    }

    static boolean checkValid(int value, CommandSourceStack src){
        int limit = Config.tiers.size();
        if (value >= limit){
            src.sendFailure(new TextComponent("Maximum difficulty is " + (limit - 1) + '!').withStyle(ChatFormatting.RED));
            return false;
        }
        return true;
    }

    public static void set(ServerPlayer player, int value){
        PlayerDifficulty cap = PlayerDifficulty.get(player);
        cap.value = value;
        cap.toReset = false;
        player.sendMessage(new TextComponent("Your difficulty was set to " + value + '.'), new UUID(0, 0));
    }
}
