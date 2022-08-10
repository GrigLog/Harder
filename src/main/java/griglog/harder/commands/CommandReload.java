package griglog.harder.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import griglog.harder.config.Config;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;

public class CommandReload implements Command<CommandSourceStack>{
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Config.reload();
        context.getSource().sendSuccess(new TextComponent("Config reloaded"), true);
        return 0;
    }
}
