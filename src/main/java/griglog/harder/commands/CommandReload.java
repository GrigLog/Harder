package griglog.harder.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import griglog.harder.config.Config;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;

public class CommandReload implements Command<CommandSource>{
    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Config.reload();
        context.getSource().sendSuccess(new StringTextComponent("Config reloaded"), true);
        return 0;
    }
}
