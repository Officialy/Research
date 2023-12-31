package mods.officialy.researchmod.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@Deprecated // May remove later if unneeded for Server->Client Datapack syncing
public class SyncResearchDatapack {
    public SyncResearchDatapack() {
    }

    public void encode(FriendlyByteBuf friendlyByteBuf) {

    }

    public static SyncResearchDatapack decode(FriendlyByteBuf friendlyByteBuf) {
        return new SyncResearchDatapack();
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {

    }
}
