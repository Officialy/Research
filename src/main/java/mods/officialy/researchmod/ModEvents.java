package mods.officialy.researchmod;

import mods.officialy.researchmod.client.ui.screen.ResearchTreeScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static mods.officialy.researchmod.ResearchMod.ClientModEvents.SHOW_TREE_BINDING;

// Collection of events used that are on the FORGE event bus
public class ModEvents {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            while (SHOW_TREE_BINDING.get().consumeClick()) {
                Minecraft.getInstance().setScreen(new ResearchTreeScreen());
            }
        }
    }

//    @SubscribeEvent
//    public static void addReloadListener(AddReloadListenerEvent event) {
//        event.addListener(new ResearchReloader());
//    }

    @SubscribeEvent
    public static void syncDatapack(OnDatapackSyncEvent event) {
        if (event.getPlayer() != null) {
            //ResearchMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(event::getPlayer), new SyncResearchDatapack());
        }
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        ResearchCommand.register(event.getDispatcher(), event.getBuildContext());
    }

}
