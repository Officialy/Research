package mods.officialy.researchmod;

import mods.officialy.researchmod.capability.lab.LabCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class Constants {
    public static final String RESEARCH_DATA = "research_data";
    // Define mod id in a common place for everything to reference
    public static final String MODID = "research";

   public static final Capability<LabCapability> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});
}
