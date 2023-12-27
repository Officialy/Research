package mods.officialy.researchmod;

import java.util.*;

public class ResearchGraph {

    Map<String, Node> researchNodes;
    Set<ResearchEventListener> eventListeners;

    public ResearchGraph() {
        this.researchNodes = new HashMap<>();
        this.eventListeners = new HashSet<>();
    }

    public void addResearch(String researchName) {
        researchNodes.put(researchName, new Node(researchName));
    }

    public void addPrerequisite(String researchName, String prerequisiteName) {
        Node researchNode = researchNodes.get(researchName);
        Node prerequisiteNode = researchNodes.get(prerequisiteName);

        if (researchNode != null && prerequisiteNode != null) {
            researchNode.prerequisites.add(prerequisiteNode);
        } else {
            System.out.println("Research or prerequisite not found.");
        }
    }

    public void addResearchEventListener(ResearchEventListener listener) {
        eventListeners.add(listener);
    }

    public boolean canResearch(String researchName, Set<String> researched) {
        Node researchNode = researchNodes.get(researchName);

        if (researchNode == null) {
            System.out.println("Research not found.");
            return false;
        }

        for (Node prerequisite : researchNode.prerequisites) {
            if (!researched.contains(prerequisite.researchName) || !canResearch(prerequisite.researchName, researched)) {
                return false;
            }
        }

        return true;
    }

    public void unlockResearch(String researchName) {
        Node researchNode = researchNodes.get(researchName);

        if (researchNode != null && !researchNode.isActivated()) {
            researchNode.activate();
            researchNode.consumeItems();
            notifyListeners(researchName);
        }
    }

    private void notifyListeners(String researchName) {
        for (ResearchEventListener listener : eventListeners) {
            listener.onResearchUnlocked(researchName);
        }
    }

    public static void main(String[] args) {
        ResearchGraph researchGraph = new ResearchGraph();

        researchGraph.addResearch("Advanced Materials");
        researchGraph.addResearch("Energy Efficiency");
        researchGraph.addResearch("Robotics");
        researchGraph.addResearch("AI Integration");

        researchGraph.addPrerequisite("Robotics", "Advanced Materials");
        researchGraph.addPrerequisite("AI Integration", "Robotics");
        researchGraph.addPrerequisite("AI Integration", "Energy Efficiency");

        researchGraph.addResearchEventListener(researchName ->
                System.out.println("Research unlocked: " + researchName));

        Set<String> researched = new HashSet<>();
        researched.add("Advanced Materials");
        researched.add("Energy Efficiency");

        researchGraph.unlockResearch("Robotics"); // This should activate "Robotics" and trigger an event
    }
}