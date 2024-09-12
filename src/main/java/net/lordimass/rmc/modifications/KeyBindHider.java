package net.lordimass.rmc.modifications;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import java.lang.reflect.Field;
import java.util.Arrays;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(KeyBindHider.MODID)
public class KeyBindHider
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
    };


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

        public KeyBindHider()
        {
            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

            // Register the commonSetup method for modloading
            modEventBus.addListener(this::commonSetup);

            // Register ourselves for server and other game events we are interested in
            MinecraftForge.EVENT_BUS.register(this);

            // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
            ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        }

        private void commonSetup(final FMLCommonSetupEvent event)
        {
            // Some common setup code
            LOGGER.info("HELLO FROM COMMON SETUP");

            if (Config.logDirtBlock)
                LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

            LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

            Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
        }


        // You can use SubscribeEvent and let the Event Bus discover methods to call
        @SubscribeEvent
        public void onServerStarting(ServerStartingEvent event)
        {
            // Do something when the server starts
            LOGGER.info("HELLO from server starting");
        }

    }
