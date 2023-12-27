package mods.officialy.researchmod;

import java.util.ArrayList;
import java.util.List;

class Node {
    String researchName;
    List<Node> prerequisites;
    boolean activated;

    Node(String name) {
        this.researchName = name;
        this.prerequisites = new ArrayList<>();
        this.activated = false;
    }

    boolean isActivated() {
        return activated;
    }

    void activate() {
        this.activated = true;
    }

    void consumeItems() {
        // Logic to consume items
        System.out.println("Items consumed for research: " + researchName);
    }
}