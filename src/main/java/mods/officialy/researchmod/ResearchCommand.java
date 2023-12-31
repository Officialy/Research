package mods.officialy.researchmod;

import com.mojang.brigadier.CommandDispatcher;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.data.TeamArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.server.command.EnumArgument;

public class ResearchCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("research").requires((player) -> player.hasPermission(2)).then(Commands.argument("team", TeamArgument.create()).then(Commands.argument("mode", EnumArgument.enumArgument(ResearchCommandModes.class)).executes(context -> {
            Team team = TeamArgument.get(context, "team");
            ResearchCommandModes mode = context.getArgument("mode", ResearchCommandModes.class);
            switch (mode) {
                case list -> ResearchManager.printResearch(team);
                case clear -> ResearchManager.clearResearch(team);
                case remove -> ResearchManager.removeResearch();
                case add -> ResearchManager.addResearch();
            }
            return 0;
        }))));
    }

    // Keep these lower case otherwise they will appear uppercase in game
    public enum ResearchCommandModes {
        list, clear, remove, add
    }
}
