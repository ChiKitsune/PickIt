package chikitsune.pick_it.commands;

import java.util.Collection;

import com.mojang.brigadier.builder.ArgumentBuilder;

import chikitsune.pick_it.config.Configs;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

public class PickNChoose {
 public static ArgumentBuilder<CommandSource, ?> register() { 
  return Commands.literal(Configs.pickNChooseName).requires((cmd_init) -> { return cmd_init.hasPermissionLevel(Configs.cmdSTPermissionsLevel); })
    .then(Commands.argument("targetedPlayer", EntityArgument.players()).executes((cmd_pickitarg) -> {
     return pickNChooseLogic(cmd_pickitarg.getSource(),EntityArgument.getPlayers(cmd_pickitarg, "targetedPlayer"));
    }));
 }
 
 private static int pickNChooseLogic(CommandSource source,Collection<ServerPlayerEntity> targetPlayers) {
  Configs.bakeConfig();
  
  PickEm.pickEmLogic(source, targetPlayers);
  DoIt.doItLogic(source, targetPlayers);
  
  return 0;
 }

}
