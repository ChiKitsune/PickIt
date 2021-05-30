package chikitsune.pick_it.init;

import chikitsune.pick_it.PickIt;
import chikitsune.pick_it.commands.ArchCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PickIt.MODID)
public class ModCommands {
 @SubscribeEvent
 public static void registerCommands(final RegisterCommandsEvent event) {
  
  ArchCommand.register(event.getDispatcher());
 }
}
