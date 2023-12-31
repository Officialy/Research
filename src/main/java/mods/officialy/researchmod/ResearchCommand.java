package mods.officialy.researchmod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.data.TeamArgument;
import mods.officialy.researchmod.command.ResearchEntryArgument;
import mods.officialy.researchmod.command.ResearchParser;
import mods.officialy.researchmod.research.ResearchEntry;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.server.command.EnumArgument;

public class ResearchCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        dispatcher.register(Commands.literal("research").requires((player) -> player.hasPermission(2))
                .then(Commands.argument("team", TeamArgument.create())
                        .then(Commands.argument("mode", EnumArgument.enumArgument(ResearchCommandModes.class)).executes(context -> {
                                    Team team = TeamArgument.get(context, "team");
                                    ResearchCommandModes mode = context.getArgument("mode", ResearchCommandModes.class);
                                    switch (mode) {
                                        case list -> ResearchManager.printResearch(team, context.getSource());
                                        case clear -> ResearchManager.clearResearch(team, context.getSource());
                                        case remove, add ->
                                                throw new SimpleCommandExceptionType(Component.translatable("text.research.command.missing")).create();
                                    }
                                    return 0;
                                })
                                .then(Commands.argument("research_name", new ResearchEntryArgument(buildContext)).executes(context -> {
                                    Team team = TeamArgument.get(context, "team");
                                    ResearchCommandModes mode = context.getArgument("mode", ResearchCommandModes.class);
                                    ResearchEntry entry = ResearchEntryArgument.getResearch(context, "research_name");

                                    switch (mode) {
                                        case list -> ResearchManager.printResearch(team, context.getSource());
                                        case clear ->
                                                ResearchManager.clearResearch(team, context.getSource());
                                        case remove ->
                                                ResearchManager.removeResearch(team, context.getSource(), entry);
                                        case add ->
                                                ResearchManager.addResearch(team, context.getSource(), entry);
                                    }
                                    return 0;
                                })))));
    }

    // Keep these lower case otherwise they will appear uppercase in game
    public enum ResearchCommandModes {
        list, clear, remove, add
    }

}
