package net.lordimass.rmc.modifications;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Matrix4f;
import org.slf4j.Logger;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MODID)
public class Main
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "rmcmodifications";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Keybinds for the Keybind Hider to hide
    final String[] hiddenKeybinds = {
            "key.loadToolbarActivator",
            "key.saveToolbarActivator",
            "key.socialInteractions",
            "key.spectatorOutlines",
            "key.smoothCamera",
            "key.advancements",
            "key.categories.creative",
            "key.categories.multiplayer",
            "key.playerlist",
            "key.chat",
            "key.command",
            "key.categories.ui",
            "key.hammerlib.render_item",
            "key.craftpresence.category",
            "key.craftpresence.config_keycode.name",
            "key.curios.category",
            "key.curios.open.desc",
            "key.categories.deeperdarker",
            "key.deeperdarker.boost",
            "key.deeperdarker.transmit",
            "EntityCulling",
            "key.entityculling.toggle",
            "key.categories.ftbteams",
            "key.ftbteams.open_gui",
            "framedblocks.key.categories.framedblocks",
            "framedblocks.key.update_cull",
            "framedblocks.key.wipe_cache",
            "key.freecam.configGui",
            "key.freecam.playerControl",
            "key.freecam.tripodReset",
            "key.categories.inventoryessentials",
            "key.inventoryessentials.screen_bulk_drop",
            "key.inventoryessentials.single_transfer",
            "key.irons_spellbooks.spell_quick_cast_6",
            "key.irons_spellbooks.spell_quick_cast_7",
            "key.irons_spellbooks.spell_quick_cast_8",
            "key.irons_spellbooks.spell_quick_cast_9",
            "key.irons_spellbooks.spell_quick_cast_10",
            "key.irons_spellbooks.spell_quick_cast_11",
            "key.irons_spellbooks.spell_quick_cast_12",
            "key.irons_spellbooks.spell_quick_cast_13",
            "key.irons_spellbooks.spell_quick_cast_14",
            "key.irons_spellbooks.spell_quick_cast_15",
            "key.journeymap.minimap_type",
            "key.journeymap.minimap_toggle_alt",
            "key.journeymap.minimap_preset",
            "key.journeymap.fullscreen_chat_position",
            "key.journeymap.fullscreen_create_waypoint",
            "key.journeymap.fullscreen.disable_buttons",
            "key.journeymap.fullscreen_options",
            "jm.common.hotkeys_keybinding_fullscreen_category",
            "key.journeymap.fullscreen.east",
            "key.journeymap.fullscreen.north",
            "key.journeymap.fullscreen.south",
            "key.journeymap.fullscreen.west",
            "key.voice_chat_adjust_volumes",
            "key.voice_chat_settings",
            "key.dynamic_fps.toggle_disabled",
            "key.dynamic_fps.toggle_forced",
            "tcdcommons.key.refresh_current_screen:key.keyboard.unknown",
            "tcdcommons",
            "key.voice_chat_toggle_recording",
            "key.whisper",
            "key.toastcontrol.category",
            "key.toastcontrol.clear",
            "key.structurize.categories.general",
            "key.structurize.mirror",
            "key.structurize.move_back",
            "key.structurize.move_down",
            "key.structurize.move_forward",
            "key.structurize.move_left",
            "key.structurize.move_right",
            "key.structurize.move_up",
            "key.structurize.move_down",
            "key.structurize.place",
            "key.structurize.rotate_cw",
            "key.structurize.rotate_ccw",
            "key.structurize.teleport",
            "key.categories.placebo",
            "placebo.toggleTrails",
            "placebo.toggleWings",
            "key.minecolonies.categories.general",
            "key.minecolonies.toggle_goggles",
            "iris.keybind.reload",
            "iris.keybind.toggleShaders",
            "iris.keybind.shaderPackSelection",
            "iris.keybinds"
    };

    public Main()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ItemInit.ITEMS.register(bus);
        BlockInit.BLOCKS.register(bus);
        PoiInit.POI_TYPES.register(bus);
        BlockEntityInit.BLOCK_ENTITIES.register(bus);
    }

    // keybind hiding
    @SubscribeEvent
    public void onScreenInit(ScreenEvent.Init.Post event) throws IllegalAccessException {
        Screen screen = event.getScreen();
        if (screen instanceof KeyBindsScreen) {
            Field field = ObfuscationReflectionHelper.findField(KeyBindsScreen.class, "f_193977_");
            field.setAccessible(true);

            KeyBindsList keyBindsList = (KeyBindsList) field.get(screen);

            for (int i = 0; i < keyBindsList.children().size(); i++) {
                Component name = null;
                String string_name = "";
                if (keyBindsList.children().get(i) instanceof KeyBindsList.KeyEntry entry) {
                    field = ObfuscationReflectionHelper.findField(KeyBindsList.KeyEntry.class, "f_193911_");
                    field.setAccessible(true);
                    name = (Component) field.get(entry);

                } else if (keyBindsList.children().get(i) instanceof KeyBindsList.CategoryEntry entry) {
                    field = ObfuscationReflectionHelper.findField(KeyBindsList.CategoryEntry.class, "f_193882_");
                    field.setAccessible(true);
                    name = (Component) field.get(entry);
                }

                string_name = name.toString();
                //LOGGER.info(string_name);
                string_name = string_name.substring(string_name.indexOf('\'')+1);
                string_name = string_name.substring(0, string_name.indexOf('\''));
                for (String keybind : hiddenKeybinds) {
                    if (keybind.equals(string_name)) {
                        keyBindsList.children().remove(i);
                        i--;
                    }
                }
            }
            LOGGER.info("Hidden keybinds: " + Arrays.toString(hiddenKeybinds));
        }
    }

    // no_fly_zone
    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();
        if (level.getGameTime() % 5 == 0) {
            Iterable<ItemStack> armor = player.getArmorSlots();
            boolean elytra_worn = false;
            boolean head_elytra = false;

            int counter = 0;
            for (ItemStack item : armor) {
                if (item.getItem().toString().equals("elytra") && counter == 2) {
                    elytra_worn = true;
                } else if (item.getItem().toString().equals("elytra") && counter == 3) {
                    head_elytra = true;
                }
                counter++;
            }
            boolean dual_elytra = elytra_worn && head_elytra;

            final ChunkPos chunkPos = player.chunkPosition();
            final LevelChunk chunk = level.getChunk(chunkPos.x, chunkPos.z);

            if (level instanceof ServerLevel) {
                final PoiManager poiManager = ((ServerLevel) level).getPoiManager();

                final ResourceLocation noFlyZoneMarkerLocation = new ResourceLocation(
                        "rmcmodifications",
                        "no_fly_zone_blocks");

                TagKey<PoiType> noFlyZoneTag = ForgeRegistries.POI_TYPES.tags().createTagKey(noFlyZoneMarkerLocation);
                final Predicate<Holder<PoiType>> predicate = (p_218130_) -> {
                    return p_218130_.is(noFlyZoneTag);
                }; // Predicate used to check for the right type of block

                final Stream<PoiRecord> stream = poiManager.getInChunk(
                        predicate, // Predicate as defined before
                        player.chunkPosition(), // The current chunk of the player
                        PoiManager.Occupancy.ANY); // Ignore "occupancy" of block


                //final boolean present = stream.findAny().isPresent(); // Check if there were any matches in range

                Optional<PoiRecord> first_find = stream.findFirst();
                final boolean present = first_find.isPresent();

                Inventory inventory = player.getInventory();
                NonNullList<ItemStack> items = inventory.armor;
                int signal = 0;
                if (present) {
                    BlockPos pos = first_find.get().getPos();
                    signal = level.getDirectSignalTo(pos);
                }
                if (present && elytra_worn && signal==0) { // Disable Flight by swapping helmet and chestplate
                    if (!dual_elytra) { // Standard Case
                        ItemStack temp = inventory.armor.get(2);
                        inventory.armor.set(2, inventory.armor.get(3));
                        inventory.armor.set(3, temp);
                    } else { // Dual Elytra Users
                        player.drop(inventory.armor.get(2), false, false);
                        inventory.armor.set(2, ItemStack.EMPTY);
                    }

                } else if ((!present || signal>0) && head_elytra) {
                    ItemStack temp = inventory.armor.get(2);
                    inventory.armor.set(2, inventory.armor.get(3));
                    inventory.armor.set(3, temp);
                }
            }
        }
    }

    // trophy name tag
    @SubscribeEvent
    public void onRenderLevelStageLast (RenderLevelStageEvent event) {
        int renderDistance = 6;
        Minecraft minecraft = Minecraft.getInstance();

        // Check if block the player is looking at is a trophy
        HitResult hitResult = minecraft.player.pick(5.0D, 0.0F,false);
        BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
        Vec3i trophy = new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        String name = minecraft.level.getBlockState(new BlockPos(trophy.getX(), trophy.getY(), trophy.getZ())).toString();
        if (name.equals("Block{rmcmodifications:trophy}[axis=z]") || name.equals("Block{rmcmodifications:trophy}[axis=x]")) {
            PoseStack pPoseStack = event.getPoseStack();
            pPoseStack.pushPose();

            // Translating to be over the block (based on camera pos)
            Vec3 offset = Vec3.atBottomCenterOf(trophy).subtract(event.getCamera().getPosition());
            offset = new Vec3(offset.x, offset.y + 1.2, offset.z); // Aesthetic Translation
            pPoseStack.translate(offset.x, offset.y, offset.z);

            // Rotating based on camera orientation
            pPoseStack.mulPose(minecraft.getEntityRenderDispatcher().cameraOrientation());

            pPoseStack.scale(-0.02F, -0.02F, -0.02F);
            Matrix4f matrix4f = pPoseStack.last().pose();

            MultiBufferSource pBuffer = minecraft.renderBuffers().bufferSource();
            boolean flag = true;
            int pPackedLight = 15;

            float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int j = (int) (f1 * 255.0F) << 24;
            Font font = minecraft.font;

            ClientLevel clientLevel = minecraft.level;
            TrophyBlockEntity trophyEntity = (TrophyBlockEntity) clientLevel.getBlockEntity(blockPos);
            String text = trophyEntity.getLabel();
            if (text == null) {
                text = "Unregistered";
            }

            float f2 = (float) (-font.width(text) / 2);

            font.drawInBatch(
                    text,
                    f2,
                    (float) 0,
                    553648127,
                    false,
                    matrix4f,
                    pBuffer,
                    Font.DisplayMode.SEE_THROUGH, j, pPackedLight
            );

            pPoseStack.popPose();
        }
    }


    @SubscribeEvent
    public void onBlockPlace (BlockEvent.EntityPlaceEvent event) {
        Iterable<ItemStack> hands = event.getEntity().getHandSlots();
        for (ItemStack stack : hands) {
            if (stack.getItem().toString().equals("trophy")) { // Should be using == not string stuff but meh
                // Get Dependency
                BlockPos pos = event.getPos();

                // Get Lore
                String display = stack.getTag().get("display").toString();
                String lore = display.substring(21, display.length()-21);

                // Save Lore to BlockEntity
                TrophyBlockEntity trophyEntity = (TrophyBlockEntity) event.getLevel().getBlockEntity(pos);
                trophyEntity.setLabel(lore);
                break;
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreak (BlockEvent.BreakEvent event) {
        // Function only applies to trophies, check these only
        BlockState blockState =  event.getState();
        if (!(blockState.getBlock() instanceof TrophyBlock)) {
            return;
        }

        // Getting other dependencies
        BlockPos pos = event.getPos();
        Level level = event.getPlayer().level();

        // Get value for block entity
        TrophyBlockEntity trophyEntity = (TrophyBlockEntity) level.getBlockEntity(pos);
        String text = trophyEntity.getLabel();

        // Creating Itemstack
        ItemStack itemStack = new ItemStack(blockState.getBlock());
        CompoundTag compoundTag = new CompoundTag();
        CompoundTag display = new CompoundTag();
        ListTag Lore = new ListTag();
        Lore.add(StringTag.valueOf("[\"\",{\"text\":\"" + text + "\",\"italic\":false}]"));
        display.put("Lore", Lore);
        compoundTag.put("display", display);
        itemStack.setTag(compoundTag);

        // Creating Entity
        ItemEntity itemEntity = new ItemEntity(
                level,
                (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D,
                itemStack
        );
        itemEntity.setDefaultPickUpDelay();
        level.addFreshEntity(itemEntity);
    }
}
