package chikitsune.pick_it.config;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.Pair;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.SetMultimap;

import chikitsune.pick_it.PickIt;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;

//@Mod.EventBusSubscriber
@Mod.EventBusSubscriber(modid = PickIt.MODID, bus = Bus.MOD)
public class Configs {

 public static final PickItConfig PICONFIG;
 public static final ForgeConfigSpec PICONFIG_SPEC;
 static {
  final Pair<PickItConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(PickItConfig::new);
  PICONFIG_SPEC= specPair.getRight();
  PICONFIG= specPair.getLeft();
 }
 
 public static int cmdSTPermissionsLevel;
 public static String commandGroupName;
 public static String playerPickName;
 public static SetMultimap<String, String> playerPickList;
 public static String playerLastPickedName;
 public static HashMap<String,String> playerLastPickedList;
 public static boolean playerLastPickedRemove;
 public static String commandPickableName;
 public static List<String> commandPickableList2;
 public static TreeMap<Integer, String> commandPickableList;
 public static String pickNChooseName;
 
 public static void loadConfig(ForgeConfigSpec spec, Path path) {

  final CommentedFileConfig configData = CommentedFileConfig.builder(path)
          .sync()
          .autosave()
          .writingMode(WritingMode.REPLACE)
          .build();

  configData.load();
  spec.setConfig(configData);
}
 
 @SubscribeEvent
 public static void onLoad(final ModConfig.Loading configEvent) {

 }

 @SubscribeEvent
 public static void onReload(final ModConfig.Reloading configEvent) {
 }
 
 @SubscribeEvent
 public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
  if (configEvent.getConfig().getSpec() == PICONFIG_SPEC) bakeConfig();
  configEvent.getConfig().save();
 }
 
 public static void bakeConfig() {
  cmdSTPermissionsLevel=PICONFIG.cmdSTPermissionsLevel.get();
  commandGroupName=PICONFIG.commandGroupName.get();
  
  playerPickName=PICONFIG.playerPickName.get();
  
  playerPickList = HashMultimap.create();
  
  PICONFIG.playerPickList.get().forEach(str -> {
   if (str!=null) {
    String player = str.split(":")[0];
    
    for (String str2 : (str.split(":")[1].split(","))) {
     playerPickList.put(player, str2);
    }
   }
  });
  
  playerLastPickedName=PICONFIG.playerLastPickedName.get();
  playerLastPickedList= new HashMap<String,String>();
  PICONFIG.playerLastPickedList.get().forEach(str -> {
   if (str!=null) {
    playerLastPickedList.put(str.split(":")[0], str.split(":")[1]);
   }
  });
  playerLastPickedRemove=PICONFIG.playerLastPickedRemove.get();
  
  commandPickableName=PICONFIG.commandPickableName.get();
  commandPickableList= new TreeMap<Integer, String>();
  
  Integer subTotWeight=0;
  for( String str: PICONFIG.commandPickableList.get()) {
   subTotWeight+=Integer.parseInt(str.split("\\|")[0]);
   commandPickableList.put(subTotWeight,str.split("\\|")[1]);
  }
//  commandPickableList= new ArrayList<>();
//  PICONFIG.commandPickableList.get().forEach(str -> {
//   if (str!=null) {
//    commandPickableList.add(str);
//   }
//  });
  
  pickNChooseName=PICONFIG.pickNChooseName.get();
 }
 
 public static class PickItConfig {
  
  public final IntValue cmdSTPermissionsLevel;
  public final ConfigValue<String> commandGroupName;
  
  public final ConfigValue<String> playerPickName;
  public final ConfigValue<List<? extends String>> playerPickList;
  
  public final ConfigValue<String> playerLastPickedName;
  public final ConfigValue<List<? extends String>> playerLastPickedList;
  public final BooleanValue playerLastPickedRemove;
  
  public final ConfigValue<String> commandPickableName;
  public final ConfigValue<List<? extends String>> commandPickableList;
  
  public final ConfigValue<String> pickNChooseName;
  
  
  public PickItConfig(ForgeConfigSpec.Builder builder) {
   builder.comment("Command Settings").push("commands");
   builder.push("general");
   cmdSTPermissionsLevel=builder.comment("Sets all command to be this permission level.").defineInRange("cmdPermissionsLevel", 2, 0, 4);
   commandGroupName=builder.comment("Command Group Name (aka /text) to group commands under").define("commandGroupName", "pickit");
   builder.pop();
   
//   playerPick config start
   builder.comment("playerPick Command Settings").push("playerPick");
   playerPickName=builder.comment("Command name to add/remove/clear list").define("playerPickName", "listit");
   
   List<String> chlPickList=Lists.newArrayList();
   chlPickList.add("darkphan:darkphan,ChiKitsune");
   playerPickList=builder.comment("List of names with player : list of names in that player's list(player:name1,name2,...) for picking list.").defineList("playerPickList", chlPickList, s -> s instanceof String);
   builder.pop();
//   playerPick config end
   
// playerLastPicked config start
 builder.comment("playerPick Command Settings").push("playerLastPicked");
 playerLastPickedName=builder.comment("Command name to pick from list").define("playerLastPickedName", "pickem");
 
 List<String> chlLastPickList=Lists.newArrayList();
 chlLastPickList.add("darkphan:ChiKitsune");
 playerLastPickedList=builder.comment("List of players with a name (player:name) that have last been picked.").defineList("playerLastPickedList", chlLastPickList, s -> s instanceof String);
 playerLastPickedRemove=builder.comment("Name is removed from list when it is picked.").define("playerLastPickedRemove", false);
 builder.pop();
// playerLastPicked config end
 
//commandPickable config start
builder.comment("commandPickable Command Settings").push("commandPickable");
commandPickableName=builder.comment("Command name for command list").define("commandPickableName", "doit");

List<String> chlPickableList=Lists.newArrayList();
chlPickableList.add("99|/give $player egg{display:{Name:\"\\\"$pickedname\\\"\"}}");
chlPickableList.add("1|/give $player diamond{display:{Name:\"\\\"$pickedname\\\"\"}}");
commandPickableList=builder.comment("List of commands (weighted chance|command). Use $player for player to affect and $pickedname to get their last picked name. i.e. two have two chances to give the player a named egg do this:  2|/give $player egg{display:{Name:\\\"\\\\\\\"$pickedname\\\\\\\"\\\"}}").defineList("commandPickableList", chlPickableList, s -> s instanceof String);
builder.pop();
//commandPickable config end
   
//PickNChoose config start
builder.comment("Pick'n Choose Command Settings").push("pickNChoose");
pickNChooseName=builder.comment("Command name to pick from list and do a command with chosen name").define("pickNChooseName", "picknchoose");

builder.pop();
//PickNChoose config end
   builder.pop();
  }  
 }
 
}