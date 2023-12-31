package mods.officialy.researchmod.research;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import mods.officialy.researchmod.ResearchMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.storage.loot.Deserializers;

import java.util.Map;

public class ResearchReloader extends SimpleJsonResourceReloadListener {

    public static final Gson GSON_INSTANCE = Deserializers.createFunctionSerializer().create();

    public ResearchReloader() {
        super(GSON_INSTANCE, "research");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceList, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        for (Map.Entry<ResourceLocation, JsonElement> entry : resourceList.entrySet()) {
           ResearchMod.LOGGER.info(entry.toString());
            DataResult<ResearchEntry> parse = ResearchMod.NODE_CODEC.parse(JsonOps.INSTANCE, entry.getValue());
            ResearchMod.LOGGER.info(parse.toString());
        }

        ResearchMod.LOGGER.info("Research Reload Completed");
    }
}
