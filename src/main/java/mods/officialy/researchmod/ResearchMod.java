package mods.officialy.researchmod;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.officialy.researchmod.block.BasicLab;
import mods.officialy.researchmod.command.ResearchEntryArgument;
import mods.officialy.researchmod.entity.BasicLabEntity;
import mods.officialy.researchmod.research.ResearchEntry;
import mods.officialy.researchmod.research.ResearchSavedData;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.SlotArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.*;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;

import static mods.officialy.researchmod.Constants.MODID;

@Mod(MODID)
public class ResearchMod {

    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "research" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "research" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    @SuppressWarnings({"UnstableApiUsage"})
    final // Shush the "beta" annotation
    MutableGraph<Object> RESEARCH_TREE = GraphBuilder.directed().build();

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    // Creates a new Block with the id "research:example_block", combining the namespace and path
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)) {
        // Laziest possible way I could have gotten the research entries
        @Override
        public InteractionResult use(BlockState p_60503_, Level level, BlockPos p_60505_, Player player, InteractionHand p_60507_, BlockHitResult p_60508_) {
            RegistryAccess registries = level.registryAccess();
            LOGGER.info(Arrays.toString(registries.registryOrThrow(RESEARCH_KEY).entrySet().toArray()));
            return super.use(p_60503_, level, p_60505_, player, p_60507_, p_60508_);
        }
    });

    public static final RegistryObject<Block> BASIC_LAB = BLOCKS.register("basic_lab", BasicLab::new);

    // Creates a new BlockItem with the id "research:example_block", combining the namespace and path
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> BASIC_LAB_ITEM = ITEMS.register("basic_lab", () -> new BlockItem(BASIC_LAB.get(), new Item.Properties()));

    // Creates a new food item with the id "examplemod:example_id", nutrition 1 and saturation 2
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEat().nutrition(1).saturationMod(2f).build())));

    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

    public static final RegistryObject<BlockEntityType<BasicLabEntity>> LAB_TYPE = BLOCK_ENTITIES.register("basic_lab",()-> BlockEntityType.Builder.of(BasicLabEntity::new, BASIC_LAB.get()).build(null));


    public static final ResourceKey<Registry<ResearchEntry>> RESEARCH_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MODID, "research"));

    public ResearchMod() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        //modEventBus.addListener(ClientModEvents::onClientTick);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        ARGUMENTS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(ModEvents.class);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::addDataPackRegistry);
        // Frankly unsure if this is needed or not?
//        int i = 0;
//        PACKET_HANDLER.registerMessage(i++, SyncResearchDatapack.class, SyncResearchDatapack::encode, SyncResearchDatapack::decode, SyncResearchDatapack::handle);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(EXAMPLE_BLOCK_ITEM);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
        RegistryAccess registries = event.getServer().registryAccess();
        LOGGER.info(Arrays.toString(registries.registryOrThrow(RESEARCH_KEY).entrySet().toArray()));
        assembleResearchTree(registries.registryOrThrow(RESEARCH_KEY));
        LOGGER.info(RESEARCH_TREE.toString());

        // Even needed?
        event.getServer().overworld().getDataStorage().computeIfAbsent(tag -> ResearchSavedData.load(event.getServer(), tag), () -> ResearchSavedData.create(event.getServer()), "research_data");

    }

    private void assembleResearchTree(Registry<ResearchEntry> nodes) {
        for (Map.Entry<ResourceKey<ResearchEntry>, ResearchEntry> resourceKeyNodeEntry : nodes.entrySet()) {
            RESEARCH_TREE.addNode(resourceKeyNodeEntry.getValue());
            for (ResourceLocation preq : resourceKeyNodeEntry.getValue().getPrerequisites()) {
                RESEARCH_TREE.putEdge(nodes.get(preq), resourceKeyNodeEntry.getValue());
            }
        }

    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server started");
        RegistryAccess registries = event.getServer().registryAccess();
        LOGGER.info(Arrays.toString(registries.registryOrThrow(RESEARCH_KEY).entrySet().toArray()));
    }

    public static final Codec<Ingredient> INGREDIENT_CODEC = Codec.PASSTHROUGH.comapFlatMap(dynamic -> {
                try {
                    Ingredient ingredient = Ingredient.fromJson(dynamic.convert(JsonOps.INSTANCE).getValue());
                    return DataResult.success(ingredient);
                } catch (Exception e) {
                    return DataResult.error(e::getMessage);
                }
            },
            ingredient -> new Dynamic<>(JsonOps.INSTANCE, ingredient.toJson()));
    public static final Codec<ResearchEntry> NODE_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                            // TODO Add fluid and energy support to research entries
                            ResourceLocation.CODEC.fieldOf("researchName").forGetter(ResearchEntry::getResearchName),
                            ResourceLocation.CODEC.listOf().fieldOf("prerequisites").forGetter(ResearchEntry::getPrerequisites),
                            Codec.pair(INGREDIENT_CODEC.fieldOf("ingredient").codec(), Codec.INT.fieldOf("count").codec()).listOf().fieldOf("items").forGetter(ResearchEntry::getPrerequisiteItems))
                    .apply(instance, ResearchEntry::new));

    private void addDataPackRegistry(final DataPackRegistryEvent.NewRegistry event) {
        LOGGER.info("Adding new Registry!");
        event.dataPackRegistry(RESEARCH_KEY, NODE_CODEC, NODE_CODEC);
    }

    private static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENTS = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, MODID);

    public static final RegistryObject<ArgumentTypeInfo> RESEARCH_ARGUMENT = ARGUMENTS.register("research_argument", () -> ArgumentTypeInfos.registerByClass(ResearchEntryArgument.class, SingletonArgumentInfo.contextAware((context) -> ResearchEntryArgument.research(context)))); //new ResearchEntryArgument.Info()));

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        public static final Lazy<KeyMapping> SHOW_TREE_BINDING = Lazy.of(() -> new KeyMapping("key.research.show_screen",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                "key.categories.misc"));

        // Event is on the mod event bus only on the physical client
        @SubscribeEvent
        public static void registerBindings(RegisterKeyMappingsEvent event) {
            event.register(SHOW_TREE_BINDING.get());
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
