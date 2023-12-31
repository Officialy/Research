package mods.officialy.researchmod;

import dev.ftb.mods.ftbteams.api.Team;

public class ResearchManager {

    public static void clearResearch(Team team) {
        ResearchMod.LOGGER.info("Clearing Research for " + team.getId());
    }

    public static void printResearch(Team team) {
        ResearchMod.LOGGER.info("Printing Research for " + team.getId());
    }

    public static void addResearch() {

    }

    public static void removeResearch() {
    }
}
