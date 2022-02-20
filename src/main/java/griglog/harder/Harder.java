package griglog.harder;

import griglog.harder.config.Config;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Harder.id)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Harder {
    public static final Logger logger = LogManager.getLogger();
    public static final String id = "harder";
    public Harder() {}
    @SubscribeEvent
    static void setup(FMLCommonSetupEvent event){
        Config.reload();
    }
}
