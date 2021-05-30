package chikitsune.pick_it.commands;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.mojang.brigadier.builder.ArgumentBuilder;

import chikitsune.pick_it.config.Configs;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

public class PickEm {
 public static Random rand= new Random();
 
 public static ArgumentBuilder<CommandSource, ?> register() { 
  return Commands.literal(Configs.playerLastPickedName).requires((cmd_init) -> { return cmd_init.hasPermissionLevel(Configs.cmdSTPermissionsLevel); })
    .then(Commands.argument("targetedPlayer", EntityArgument.players()).executes((cmd_pickitarg) -> {
     return pickEmLogic(cmd_pickitarg.getSource(),EntityArgument.getPlayers(cmd_pickitarg, "targetedPlayer"));
    }));
 }
 
 public static int pickEmLogic(CommandSource source,Collection<ServerPlayerEntity> targetPlayers) {
  Configs.bakeConfig();
  
  for(ServerPlayerEntity targetedPlayer : targetPlayers) {
   Set<String> curList= Configs.playerPickList.get(targetedPlayer.getName().getUnformattedComponentText());
//   targetedPlayer.sendMessage(new StringTextComponent("got list "+curList),targetedPlayer.getUniqueID());
   if (curList!=null && curList.size()>0) {
    String resul=curList.stream().skip(new Random().nextInt(curList.size())).findFirst().orElse(null);
    Configs.playerLastPickedList.put(targetedPlayer.getName().getUnformattedComponentText(), resul);
    
    if (Configs.playerLastPickedRemove) Configs.playerPickList.remove(targetedPlayer.getName().getUnformattedComponentText(), resul);
   }
  }
  
  List<String> chlLastPickList=Lists.newArrayList();
  for (String ke : Configs.playerLastPickedList.keySet()) {   
   chlLastPickList.add(ke + ":" + Configs.playerLastPickedList.get(ke));
  }
  Configs.PICONFIG.playerLastPickedList.set(chlLastPickList);
  
  if (Configs.playerLastPickedRemove) {
  List<String> chlPickList=Lists.newArrayList();
  for (String ke : Configs.playerPickList.keySet()) {   
   chlPickList.add(ke + ":" + Configs.playerPickList.get(ke).stream().collect(Collectors.joining(",")));
  }
  Configs.PICONFIG.playerPickList.set(chlPickList);
  }
  return 0;
 }

}
