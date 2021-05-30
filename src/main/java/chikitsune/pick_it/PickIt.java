package chikitsune.pick_it;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import chikitsune.pick_it.config.Configs;
import chikitsune.pick_it.proxies.ClientProxy;
import chikitsune.pick_it.proxies.IProxy;
import chikitsune.pick_it.proxies.ServerProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PickIt.MODID)
@Mod.EventBusSubscriber(modid = PickIt.MODID, bus = Bus.MOD)
public class PickIt {
 
 public static IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
 public static final Logger LOGGER = LogManager.getLogger();
 
 public static final String MODID = "pick_it";
 public static final String NAME = "Pick It";
 
 public PickIt() {
  IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
  
  eventBus.addListener(this::setup);
  eventBus.addListener(this::enqueueIMC);
  eventBus.addListener(this::processIMC);
  eventBus.addListener(this::doClientStuff);
  eventBus.addListener(this::config);
  
ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configs.PICONFIG_SPEC);
////Register the setup method for modloading
//FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
//// Register the enqueueIMC method for modloading
//FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
//// Register the processIMC method for modloading
//FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
//// Register the doClientStuff method for modloading
//FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

// Register ourselves for server and other game events we are interested in
MinecraftForge.EVENT_BUS.register(this);
}

private void setup(final FMLCommonSetupEvent event) {
}

private void config(final ModConfigEvent evt) {
 if (evt.getConfig().getModId().equals(MODID)) {
   Configs.bakeConfig();
 }
}

private void doClientStuff(final FMLClientSetupEvent event) {
// do something that can only be done on the client
}

private void enqueueIMC(final InterModEnqueueEvent event) {
// some example code to dispatch IMC to another mod
}

private void processIMC(final InterModProcessEvent event) {
// some example code to receive and process InterModComms from other mods
}

// You can use SubscribeEvent and let the Event Bus discover methods to call
@SubscribeEvent
public void onServerStarting(FMLServerStartingEvent event) {
// do something when the server starts
}

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public static class RegistryEvents {
}
}
