package chikitsune.pick_it.commands;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;

import chikitsune.pick_it.config.Configs;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

public class ListIt {

 public static ArgumentBuilder<CommandSource, ?> register() { 
  return Commands.literal(Configs.playerPickName).requires((cmd_init) -> { return cmd_init.hasPermissionLevel(Configs.cmdSTPermissionsLevel); })
      .then(Commands.literal("add").then(Commands.argument("targetedPlayer", EntityArgument.players()).then(Commands.argument("listName", StringArgumentType.string()).executes((cmd_addarg) -> {
         return listItLogic(cmd_addarg.getSource(),"ADD",EntityArgument.getPlayers(cmd_addarg, "targetedPlayer"),StringArgumentType.getString(cmd_addarg, "listName"));
      })))).then(Commands.literal("remove").then(Commands.argument("targetedPlayer", EntityArgument.players()).then(Commands.argument("listName", StringArgumentType.string()).executes((cmd_remarg) -> {
       return listItLogic(cmd_remarg.getSource(),"REMOVE",EntityArgument.getPlayers(cmd_remarg, "targetedPlayer"),StringArgumentType.getString(cmd_remarg, "listName"));
    })))).then(Commands.literal("clear").then(Commands.argument("targetedPlayer", EntityArgument.players()).executes((cmd_clearg) -> {
     return listItLogic(cmd_clearg.getSource(),"CLEAR",EntityArgument.getPlayers(cmd_clearg, "targetedPlayer"),null);
  })));
 }
 
 private static int listItLogic(CommandSource source,String listitType, Collection<ServerPlayerEntity> targetPlayers, String listName) {
  Configs.bakeConfig();
  for(ServerPlayerEntity targetedPlayer : targetPlayers) {   
  switch (listitType.toUpperCase()) {
   case "ADD":
    Configs.playerPickList.put(targetedPlayer.getName().getUnformattedComponentText(), listName);
    break;
   case "REMOVE":
    Configs.playerPickList.remove(targetedPlayer.getName().getUnformattedComponentText(), listName);
    break;
   case "CLEAR":
    Configs.playerPickList.removeAll(targetedPlayer.getName().getUnformattedComponentText());
    break;
    default:
     break;
  }
  List<String> chlPickList=Lists.newArrayList();
  
  for (String ke : Configs.playerPickList.keySet()) {   
   chlPickList.add(ke + ":" + Configs.playerPickList.get(ke).stream().collect(Collectors.joining(",")));
  }
  
  Configs.PICONFIG.playerPickList.set(chlPickList);
  }
  
  return 0;
 }
}
