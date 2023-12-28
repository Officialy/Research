package mods.officialy.researchmod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private final ResourceLocation researchName;
    private final List<Node> prerequisites;
    private boolean activated;
    private final List<ItemStack> prerequisiteItems;

    Node(ResourceLocation name, List<ItemStack> prerequisiteItems) {
        this.researchName = name;
        this.prerequisites = new ArrayList<>();
        this.activated = false;
        this.prerequisiteItems = prerequisiteItems;
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

    // Getters

    public ResourceLocation getResearchName() {
        return researchName;
    }

    public List<Node> getPrerequisites() {
        return prerequisites;
    }

    public List<ItemStack> getPrerequisiteItems() {
        return prerequisiteItems;
    }

    @Override
    public String toString() {
        return "Node{" +
                "researchName=" + researchName +
                ", prerequisites=" + prerequisites +
                ", activated=" + activated +
                ", prerequisiteItems=" + prerequisiteItems +
                '}';
    }
}