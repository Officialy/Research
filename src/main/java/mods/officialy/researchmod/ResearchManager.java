package mods.officialy.researchmod;

import dev.ftb.mods.ftbteams.api.Team;
import mods.officialy.researchmod.research.ResearchData;
import mods.officialy.researchmod.research.ResearchEntry;
import mods.officialy.researchmod.research.ResearchEvent;
import mods.officialy.researchmod.research.ResearchSavedData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import java.util.ArrayList;
import java.util.Map;

public class ResearchManager {

    public static MutableComponent clearResearch(Team team, CommandSourceStack source) {
        ResearchMod.LOGGER.info("Clearing Research for " + team.getId());
        getSavedData(source.getServer()).get(team).clear();
        getData(source.getServer()).setDirty();
        return Component.literal("Cleared Research");
    }

    public static void printResearch(Team team, CommandSourceStack source) {
//        ResearchMod.LOGGER.info("Printing Research for " + team.getId());
        ResearchSavedData researchSavedData = getData(source.getServer());
        if (researchSavedData.researchData.containsKey(team)) {
            source.sendSuccess(()->Component.literal(researchSavedData.researchData.getOrDefault(team, new ArrayList<>()).toString()), true);
        } else {
            source.sendSuccess(()-> Component.literal("Cannot print research, Team has no research!"), true);
        }
    }

    public static void addResearch(Team team, CommandSourceStack source, ResearchEntry entry) {
        source.sendSuccess(()-> Component.literal("Adding Research (%s) for %s".formatted(entry.getResearchName().toString(), team.getId())), true);
        if (!getSavedData(source.getServer()).containsKey(team)) {
            getSavedData(source.getServer()).put(team, new ArrayList<>());
        }
        getSavedData(source.getServer()).get(team).add(new ResearchData(entry.getResearchName(), 100, ResearchEvent.COMPLETED));
        getData(source.getServer()).setDirty();
    }

    public static void removeResearch(Team team, CommandSourceStack source, ResearchEntry deleteEntry) {
        source.sendSuccess(()-> Component.literal("Removing Research (%s) for %s".formatted(deleteEntry.getResearchName().toString(), team.getId())), true);
        if (getSavedData(source.getServer()).containsKey(team)) {
            getSavedData(source.getServer()).get(team).removeIf(entry -> entry.getEntry().equals(deleteEntry.getResearchName()));
            getData(source.getServer()).setDirty();
        } else {
            source.sendSuccess(()-> Component.literal("Cannot remove research, Team has no research!"), true);
        }
    }

    public static ResearchSavedData getData(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(tag -> ResearchSavedData.load(server, tag), () -> ResearchSavedData.create(server), Constants.RESEARCH_DATA);
    }

    public static Map<Team, ArrayList<ResearchData>> getSavedData(MinecraftServer server) {
        return getData(server).researchData;
    }
}
