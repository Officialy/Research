package mods.officialy.researchmod.client.ui;


import mods.officialy.researchmod.research.ResearchEntry;
import net.minecraft.client.gui.components.Button;

public class ResearchButton extends Button {

    public ResearchButton(Builder builder, ResearchEntry researchEntry) {
        super(builder);
    }

    public static void onPressed(Button button) {

    }
}