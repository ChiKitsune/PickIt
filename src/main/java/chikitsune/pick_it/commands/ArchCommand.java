package chikitsune.pick_it.commands;

import com.mojang.brigadier.CommandDispatcher;

import chikitsune.pick_it.config.Configs;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class ArchCommand {
 
 public static void register(final CommandDispatcher<CommandSource> dispatcher) {
  
  dispatcher.register(
//    Commands.literal("pickit")
    Commands.literal(Configs.commandGroupName)
    .then(DoIt.register())
    .then(ListIt.register())
    .then(PickEm.register())
    .then(PickNChoose.register())
    );
 }
}
