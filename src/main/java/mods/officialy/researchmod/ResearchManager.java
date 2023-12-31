package mods.officialy.researchmod;

import dev.ftb.mods.ftbteams.api.Team;
import mods.officialy.researchmod.research.ResearchData;
import mods.officialy.researchmod.research.ResearchEntry;
import mods.officialy.researchmod.research.ResearchEvent;
import mods.officialy.researchmod.research.ResearchSavedData;
import net.minecraft.server.MinecraftServer;

public class ResearchManager {

    public static void clearResearch(Team team, MinecraftServer server) {
        ResearchMod.LOGGER.info("Clearing Research for " + team.getId());
        getData(server).researchData.get(team).clear();
    }

    public static void printResearch(Team team, MinecraftServer server) {
        ResearchMod.LOGGER.info("Printing Research for " + team.getId());
        ResearchSavedData researchSavedData = getData(server);
        ResearchMod.LOGGER.info(researchSavedData.researchData.get(team).toString());

    }

    public static void addResearch(Team team, MinecraftServer server, ResearchEntry entry) {
        ResearchMod.LOGGER.info("Add Research (%s) for %s".formatted(entry.getResearchName().toString(), team.getId()));
        getData(server).researchData.get(team).add(new ResearchData(entry.getResearchName(), 100, ResearchEvent.COMPLETED));
    }

    public static void removeResearch(Team team, MinecraftServer server, ResearchEntry entry) {
        ResearchMod.LOGGER.info("Removing Research (%s) for %s".formatted(entry.getResearchName().toString(), team.getId()));
    }

    public static ResearchSavedData getData(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(tag -> ResearchSavedData.load(server, tag), () -> ResearchSavedData.create(server), Constants.RESEARCH_DATA);
    }
}
