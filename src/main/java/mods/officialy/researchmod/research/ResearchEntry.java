package mods.officialy.researchmod.research;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.joml.Vector2d;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Represents a specific research entry
// See ResearchData for actual progress tracking/completion
public class ResearchEntry {

    private final ResourceLocation researchName;
    private final List<ResourceLocation> prerequisites;
    private boolean activated;
    private final List<Pair<Ingredient, Integer>> prerequisiteItems;
    private final Component translatedName;
    private final Component translatedDescription;

    private final Vector2f position;

    public ResearchEntry(ResourceLocation name, List<ResourceLocation> prerequisites, List<Pair<Ingredient, Integer>> prerequisiteItems) {
        this.researchName = name;
        this.prerequisites = prerequisites;
        this.activated = false;
        this.prerequisiteItems = prerequisiteItems;
        this.translatedName = Component.translatable(researchName.toLanguageKey() + ".research.title");
        this.translatedDescription = Component.translatable(researchName.toLanguageKey() + ".research.description");
        this.position = new Vector2f(0, 0);
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

    public List<ResourceLocation> getPrerequisites() {
        return prerequisites;
    }

    public List<Pair<Ingredient, Integer>> getPrerequisiteItems() {
        return prerequisiteItems;
    }

    public Component getTranslatedName() {
        return translatedName;
    }

    public Component getTranslatedDescription() {
        return translatedDescription;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2d position) {
        this.position.set(position);
    }

    public float getXPosition() {
        return position.x;
    }

    public float getYPosition() {
        return position.y;
    }

    @Override
    public String toString() {
        List<String> preReqs = new ArrayList<>();
        prerequisiteItems.forEach(ingredientIntegerPair -> preReqs.add(Arrays.toString(ingredientIntegerPair.getFirst().getItems()) + " Count: " + ingredientIntegerPair.getSecond()));
        return "Node{" +
                "researchName=" + researchName +
                ", prerequisites=" + prerequisites +
                ", activated=" + activated +
                ", prerequisiteItems=" + preReqs +
                ", translatedName=" + translatedName +
                ", translatedDescription=" + translatedDescription +
                ", position=" + position +
                '}';
    }
}