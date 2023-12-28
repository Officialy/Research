package mods.officialy.researchmod;

import net.minecraft.resources.ResourceLocation;

interface ResearchEventListener {
    void onResearchUnlocked(ResourceLocation researchName);
}
