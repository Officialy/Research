package mods.officialy.researchmod;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static mods.officialy.researchmod.ResearchMod.ClientModEvents.SHOW_TREE_BINDING;
// Just tossing this here since I'm tired and cant be bothered
public class ModEvents {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            while (SHOW_TREE_BINDING.get().consumeClick()) {
                Minecraft.getInstance().setScreen(new ResearchTreeScreen());
            }
        }
    }
}
