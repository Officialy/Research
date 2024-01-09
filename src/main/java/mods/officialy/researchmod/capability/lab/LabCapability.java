package mods.officialy.researchmod.capability.lab;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface LabCapability extends INBTSerializable<CompoundTag> {

    String getString();
}
