package mods.officialy.researchmod;

import dev.ftb.mods.ftbteams.api.Team;
import mods.officialy.researchmod.research.ResearchData;
import mods.officialy.researchmod.research.ResearchEntry;
import mods.officialy.researchmod.research.ResearchEvent;
import mods.officialy.researchmod.research.ResearchSavedData;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Map;

public class ResearchManager {

    public static void clearResearch(Team team, MinecraftServer server) {
        ResearchMod.LOGGER.info("Clearing Research for " + team.getId());
        getSavedData(server).get(team).clear();
        getData(server).setDirty();
    }

    public static void printResearch(Team team, MinecraftServer server) {
        ResearchMod.LOGGER.info("Printing Research for " + team.getId());
        ResearchSavedData researchSavedData = getData(server);
        if (researchSavedData.researchData.containsKey(team)) {
            ResearchMod.LOGGER.info(researchSavedData.researchData.getOrDefault(team, new ArrayList<>()).toString());
        } else {
            ResearchMod.LOGGER.info("Cannot print research, Team has no research!");
        }

    }

    public static void addResearch(Team team, MinecraftServer server, ResearchEntry entry) {
        ResearchMod.LOGGER.info("Add Research (%s) for %s".formatted(entry.getResearchName().toString(), team.getId()));
        if (!getSavedData(server).containsKey(team)) {
            getSavedData(server).put(team, new ArrayList<>());
        }
        getSavedData(server).get(team).add(new ResearchData(entry.getResearchName(), 100, ResearchEvent.COMPLETED));
        getData(server).setDirty();
    }

    public static void removeResearch(Team team, MinecraftServer server, ResearchEntry deleteEntry) {
        ResearchMod.LOGGER.info("Removing Research (%s) for %s".formatted(deleteEntry.getResearchName().toString(), team.getId()));
        if (getSavedData(server).containsKey(team)) {
            getSavedData(server).get(team).removeIf(entry -> entry.getEntry().equals(deleteEntry.getResearchName()));
            getData(server).setDirty();
        } else {
            ResearchMod.LOGGER.info("Cannot remove research, Team has no research!");
        }
    }

    public static ResearchSavedData getData(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(tag -> ResearchSavedData.load(server, tag), () -> ResearchSavedData.create(server), Constants.RESEARCH_DATA);
    }

    public static Map<Team, ArrayList<ResearchData>> getSavedData(MinecraftServer server) {
        return getData(server).researchData;
    }
}
