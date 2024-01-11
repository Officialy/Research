package mods.officialy.researchmod.client.ui.screen;

import mods.officialy.researchmod.client.ui.ResearchButton;
import mods.officialy.researchmod.research.ResearchGraph;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ResearchTreeScreen extends Screen {
    private final ResearchGraph researchGraph = new ResearchGraph(); //temporary, just to test

    public ResearchTreeScreen() {
        super(Component.translatable("research.tree.title"));

        //For each node, add a line to each prerequisite
    }

    @Override
    public void render(GuiGraphics p_281549_, int p_281550_, int p_282878_, float p_282465_) {
        super.render(p_281549_, p_281550_, p_282878_, p_282465_);
    }

    @Override
    public void renderBackground(GuiGraphics p_283688_) {
        super.renderBackground(p_283688_);
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    protected void init() {
        super.init();

        researchGraph.addResearch(new ResourceLocation("researchmod", "test_research"));
        researchGraph.addResearch(new ResourceLocation("researchmod", "test_research2"));
        researchGraph.addPrerequisite(new ResourceLocation("researchmod", "test_research2"), new ResourceLocation("researchmod", "test_research"));
        researchGraph.addResearch(new ResourceLocation("researchmod", "test_research3"));
        researchGraph.addPrerequisite(new ResourceLocation("researchmod", "test_research3"), new ResourceLocation("researchmod", "test_research2"));

        //For each node, add a button
        researchGraph.researchNodes.forEach((researchName, researchEntry) -> {
            //Adds a button for each research entry
            addWidget(new ResearchButton(new Button.Builder(Component.translatable(researchName.toString()), ResearchButton::onPressed), researchEntry));
        });
    }

    @Override
    public Component getTitle() {
        return Component.literal("Research Tree");
    }

    @Override
    protected <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T pWidget) {
        return super.addRenderableWidget(pWidget);
    }

    @Override
    protected <T extends GuiEventListener & NarratableEntry> T addWidget(T pListener) {
        return super.addWidget(pListener);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        super.mouseMoved(pMouseX, pMouseY);
    }

    @Override
    public void tick() {
        super.tick();
    }
}