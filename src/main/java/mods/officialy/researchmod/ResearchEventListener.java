package mods.officialy.researchmod;

import net.minecraft.resources.ResourceLocation;

public interface ResearchEventListener {
    void onResearchUnlocked(ResourceLocation researchName);
}
