package mods.officialy.researchmod.research;

import net.minecraft.resources.ResourceLocation;

public interface ResearchEventListener {
    void onResearchUnlocked(ResourceLocation researchName);
    //For research that is has finished unlocking
    void onResearchCompleted(ResourceLocation researchName);

    //For research that is activated by the player or other means
    void onResearchActivated(ResourceLocation researchName);

    //For research that is deactivated / when there are insufficient amount of items to activate research
    void onResearchDeactivated(ResourceLocation researchName);

    //For when a player pauses the research
    void onResearchPaused(ResourceLocation researchName);

    //For research items that is consumed when activated
    void onResearchItemsConsumed(ResourceLocation researchName);


}
