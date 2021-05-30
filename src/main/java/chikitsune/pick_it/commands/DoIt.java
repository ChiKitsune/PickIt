package chikitsune.pick_it.commands;

import java.util.Collection;
import java.util.Random;

import com.mojang.brigadier.builder.ArgumentBuilder;

import chikitsune.pick_it.config.Configs;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

public class DoIt {
 public static Random rand= new Random();
 public static ArgumentBuilder<CommandSource, ?> register() { 
  return Commands.literal(Configs.commandPickableName).requires((cmd_init) -> { return cmd_init.hasPermissionLevel(Configs.cmdSTPermissionsLevel); })
    .then(Commands.argument("targetedPlayer", EntityArgument.players()).executes((cmd_doitarg) -> {
     return doItLogic(cmd_doitarg.getSource(),EntityArgument.getPlayers(cmd_doitarg, "targetedPlayer"));
    }));
 }
 
 public static int doItLogic(CommandSource source,Collection<ServerPlayerEntity> targetPlayers) {
  Configs.bakeConfig();
  
  for(ServerPlayerEntity targetedPlayer : targetPlayers) {
   
   if (Configs.playerLastPickedList.get(targetedPlayer.getName().getUnformattedComponentText())==null) continue; 
   
   String pickedName=Configs.playerLastPickedList.get(targetedPlayer.getName().getUnformattedComponentText());
   Integer rndInt=rand.nextInt(Configs.commandPickableList.lastKey())+1;
   String rndComm=Configs.commandPickableList.ceilingEntry(rndInt).getValue();
   String preparedComm=rndComm.replaceAll("\\$player",targetedPlayer.getName().getUnformattedComponentText()).replaceAll("\\$pickedname", pickedName);
   
   source.getServer().getCommandManager().handleCommand(source, preparedComm);
   
  }
  return 0;
 }

}
