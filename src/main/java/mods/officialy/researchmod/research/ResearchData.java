package mods.officialy.researchmod.research;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import org.checkerframework.checker.units.qual.C;

import java.util.Objects;

// Stores data for a specific research
public final class ResearchData implements INBTSerializable<CompoundTag> {
    private ResourceLocation entry;
    private int progress;
    private ResearchEvent status;

    public ResearchData(ResourceLocation entry, int progress, ResearchEvent status) {
        this.entry = entry;
        this.progress = progress;
        this.status = status;
    }

    public ResearchData() {
        this.entry = new ResourceLocation("invalid:invalid");
        this.progress = 0;
        this.status = ResearchEvent.DEACTIVATED;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", getEntry().toString());
        tag.putInt("progress", getProgress());
        tag.putString("status", getStatus().name());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        entry = ResourceLocation.tryParse(nbt.getString("name"));
        progress = nbt.getInt("progress");
        status = ResearchEvent.valueOf(nbt.getString("status"));
    }

    public ResourceLocation getEntry() {
        return entry;
    }

    public int getProgress() {
        return progress;
    }

    public ResearchEvent getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ResearchData) obj;
        return Objects.equals(this.entry, that.entry) &&
                this.progress == that.progress &&
                Objects.equals(this.status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entry, progress, status);
    }

    @Override
    public String toString() {
        return "ResearchData[" +
                "entry=" + entry + ", " +
                "progress=" + progress + ", " +
                "status=" + status + ']';
    }

}
