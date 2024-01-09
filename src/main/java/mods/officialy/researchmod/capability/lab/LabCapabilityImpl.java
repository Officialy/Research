package mods.officialy.researchmod.capability.lab;

import net.minecraft.nbt.CompoundTag;

public class LabCapabilityImpl implements LabCapability {
    @Override
    public String getString() {
        return "yeet";
    }

    @Override
    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }
}
