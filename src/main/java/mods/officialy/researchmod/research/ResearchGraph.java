package mods.officialy.researchmod.research;

import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class ResearchGraph {

    Map<ResourceLocation, ResearchEntry> researchNodes;
    Set<ResearchEventListener> eventListeners;

    public ResearchGraph() {
        this.researchNodes = new HashMap<>();
        this.eventListeners = new HashSet<>();
    }

    public void addResearch(ResourceLocation researchName) {
        researchNodes.put(researchName, new ResearchEntry(researchName, new ArrayList<>(), new ArrayList<>()));
    }

    public void addPrerequisite(ResourceLocation researchName, ResourceLocation prerequisiteName) {
        ResearchEntry researchEntry = researchNodes.get(researchName);
        ResearchEntry prerequisiteResearchEntry = researchNodes.get(prerequisiteName);

        if (researchEntry != null && prerequisiteResearchEntry != null) {
            researchEntry.getPrerequisites().add(prerequisiteResearchEntry.getResearchName());
        } else {
            System.out.println("Research or prerequisite not found.");
        }
    }

    public void addResearchEventListener(ResearchEventListener listener) {
        eventListeners.add(listener);
    }

    public boolean canResearch(ResourceLocation researchName, Set<ResourceLocation> researched) {
        ResearchEntry researchResearchEntry = researchNodes.get(researchName);

        if (researchResearchEntry == null) {
            System.out.println("Research not found.");
            return false;
        }

        for (ResourceLocation prerequisite : researchResearchEntry.getPrerequisites()) {
            if (!researched.contains(prerequisite) || !canResearch(prerequisite, researched)) {
                return false;
            }
        }

        return true;
    }

    public void unlockResearch(ResourceLocation researchName) {
        ResearchEntry researchResearchEntry = researchNodes.get(researchName);

        if (researchResearchEntry != null && !researchResearchEntry.isActivated()) {
            researchResearchEntry.activate();
            researchResearchEntry.consumeItems();
            notifyListeners(researchName, ResearchEvent.ACTIVATED);
            notifyListeners(researchName, ResearchEvent.ITEMS_CONSUMED);
//            notifyListeners(researchName, ResearchEvent.COMPLETED);
        }
    }

    private void notifyListeners(ResourceLocation researchName, ResearchEvent event) {
        for (ResearchEventListener listener : eventListeners) {
            switch (event) {
                case ACTIVATED:
                    listener.onResearchActivated(researchName);
                    break;
                case ITEMS_CONSUMED:
                    listener.onResearchItemsConsumed(researchName);
                    break;
                case COMPLETED:
                    listener.onResearchCompleted(researchName);
                    break;
                case DEACTIVATED:
                    listener.onResearchDeactivated(researchName);
                    break;
                case PAUSED:
                    listener.onResearchPaused(researchName);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        ResearchGraph researchGraph = new ResearchGraph();

//        researchGraph.addResearch(new ResourceLocation("Advanced Materials"));
//        researchGraph.addResearch("Energy Efficiency"));
//        researchGraph.addResearch("Robotics"));
//        researchGraph.addResearch("AI Integration"));
//
//        researchGraph.addPrerequisite("Robotics", "Advanced Materials");
//        researchGraph.addPrerequisite("AI Integration", "Robotics");
//        researchGraph.addPrerequisite("AI Integration", "Energy Efficiency");
//
//        researchGraph.addResearchEventListener(researchName ->
//                System.out.println("Research unlocked: " + researchName));
//
//        Set<String> researched = new HashSet<>();
//        researched.add("Advanced Materials");
//        researched.add("Energy Efficiency");
//
//        researchGraph.unlockResearch("Robotics"); // This should activate "Robotics" and trigger an event
    }
}