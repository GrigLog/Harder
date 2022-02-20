package griglog.harder.events;

import griglog.harder.capability.PlayerDifficulty;
import griglog.harder.commands.CommandReload;
import griglog.harder.commands.CommandReset;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.GameProfileArgument;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class RegistryEvents {
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    static class ModEvents{
        @SubscribeEvent
        static void setup(FMLCommonSetupEvent event){
            CapabilityManager.INSTANCE.register(PlayerDifficulty.class, new PlayerDifficulty.Storage(), PlayerDifficulty::new);
        }
    }


    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
    static class ForgeEvents {
        @SubscribeEvent
        static void registerCommands(RegisterCommandsEvent event){
            event.getDispatcher().register(Commands
                    .literal("harder")
                    .requires(src -> src.hasPermission(4))
                    .then(
                            Commands.literal("reload").executes(new CommandReload())
                    ).then(
                            Commands.literal("reset")
                                    .executes(CommandReset::resetSelf)
                                    .then(  //TODO: execute for player, not myself...
                                            Commands.argument("player", GameProfileArgument.gameProfile()).executes(CommandReset::resetPlayer)
                                    ).then(
                                            Commands.literal("all").executes(CommandReset::resetAll)
                                    )
                    ));
        }
    }
}
