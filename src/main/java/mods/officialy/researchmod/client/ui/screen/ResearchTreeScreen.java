package mods.officialy.researchmod.client.ui.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import mods.officialy.researchmod.client.ui.ResearchButton;
import mods.officialy.researchmod.research.ResearchEntry;
import mods.officialy.researchmod.research.ResearchGraph;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.concurrent.atomic.AtomicInteger;

public class ResearchTreeScreen extends Screen {
    private final ResearchGraph researchGraph = new ResearchGraph(); //temporary, just to test

    public ResearchTreeScreen() {
        super(Component.translatable("research.tree.title"));

        //For each node, add a line to each prerequisite
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);

        graphics.drawCenteredString(font, getTitle(), width / 2, 15, 16777215);

        // Additional rendering logic for your research tree
        renderResearchTree(graphics, mouseX, mouseY);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        super.renderBackground(graphics);

        // Additional background rendering logic if needed
        // We could draw generic textures, maybe allow custom ones? Essentially just background elements here
    }

    // Additional method for rendering your research tree
    private void renderResearchTree(GuiGraphics graphics, int mouseX, int mouseY) {
        // Example: Draw connections between research nodes
        researchGraph.researchNodes.forEach((researchName, researchEntry) -> {
            for (ResourceLocation prerequisite : researchEntry.getPrerequisites()) {
                ResearchEntry prerequisiteEntry = researchGraph.getResearch(prerequisite);

                // Draw a line connecting research nodes
                drawLine(graphics, researchEntry, prerequisiteEntry, mouseX, mouseY);
            }
        });
    }

    // Example method to draw a line connecting two research nodes
    private void drawLine(GuiGraphics graphics, ResearchEntry researchEntry1, ResearchEntry researchEntry2, int mouseX, int mouseY) {
        float startX = researchEntry1.getXPosition(); // Replace with actual X position logic
        float startY = researchEntry1.getYPosition(); // Replace with actual Y position logic
        float endX = researchEntry2.getXPosition(); // Replace with actual X position logic
        float endY = researchEntry2.getYPosition(); // Replace with actual Y position logic

//        poseStack.pushPose();
        //var matrixStack = poseStack.last().pose();


        //Random for testing
        RandomSource random = RandomSource.create();

//        startX += random.nextInt(200);
//        startY += random.nextInt(15);
//        endX += random.nextInt(400);
//        endY += random.nextInt(400);

        startX += 50;
        startY += 50;
        endX += 200;
        endY += 300;

        // Draw a line using the matrixStack
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
//        RenderSystem.disableTexture();
        RenderSystem.lineWidth(6.0F);
        RenderSystem.enableColorLogicOp();
        RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);

        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        drawLine(graphics, bufferBuilder, startX, startY, endX, endY, 1.0F, 0.0F, 0.0F, 1.0F);
        tesselator.end();

        RenderSystem.lineWidth(1.0F);
        RenderSystem.disableColorLogicOp();
        //poseStack.popPose();
//        RenderSystem.enableTexture();
//        ResearchMod.LOGGER.info("Drawing line from " + startX + ", " + startY + " to " + endX + ", " + endY);
    }

    // Example method to draw a line
    private void drawLine(GuiGraphics graphics, BufferBuilder bufferBuilder, float startX, float startY, float endX, float endY, float red, float green, float blue, float alpha) {
        graphics.hLine((int) startX, (int) endX, (int) startY, -1);
        graphics.vLine((int) startX, (int) startY, (int) endY, -1);
//        bufferBuilder.vertex(matrixStack, startX, startY, 1).color(red, green, blue, alpha).endVertex();
//        bufferBuilder.vertex(matrixStack, endX, endY, 1).color(red, green, blue, alpha).endVertex();
//        bufferBuilder.vertex(matrixStack, endX, endY, 1).color(red, green, blue, alpha).endVertex();
//        bufferBuilder.vertex(startX, startY, 1).color(red, green, blue, alpha).endVertex();
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
        AtomicInteger i = new AtomicInteger();
        researchGraph.researchNodes.forEach((researchName, researchEntry) -> {
            //Adds a button for each research entry
            addRenderableWidget(new ResearchButton(new Button.Builder(Component.translatable(researchName.toString()), ResearchButton::onPressed).pos(10, i.get() *50), researchEntry));
            i.getAndIncrement();
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