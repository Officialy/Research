package mods.officialy.researchmod.research;

import com.google.common.collect.Maps;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class ResearchSavedData extends SavedData {
   public final Map<Team, ArrayList<ResearchData>> researchData = Maps.newHashMap();

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        for (Team team : researchData.keySet()) {
            ListTag list = new ListTag();
            for (ResearchData data : researchData.get(team)) {
                list.add(data.serializeNBT());
            }
            compoundTag.put(team.getId().toString(), list);
        }
        return compoundTag;
    }

    public static ResearchSavedData load(MinecraftServer server, CompoundTag compoundTag) {
        ResearchSavedData rsd = create(server);
        for (String str : compoundTag.getAllKeys()) {
            ArrayList<ResearchData> list = new ArrayList<>();
            for (Tag tag : compoundTag.getList(str, 7)) {
                ResearchData data = new ResearchData();
                data.deserializeNBT((CompoundTag) tag);
                list.add(data);
            }
            rsd.researchData.put(FTBTeamsAPI.api().getManager().getTeamByID(UUID.fromString(str)).get(), list);
        }
        return rsd;
    }

    public static ResearchSavedData create(MinecraftServer server) {
        return new ResearchSavedData();
    }
}
