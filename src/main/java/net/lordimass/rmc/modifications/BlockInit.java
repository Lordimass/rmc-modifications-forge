package net.lordimass.rmc.modifications;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.logging.Logger;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "rmcmodifications");

    public static final RegistryObject<Block> NO_FLY_ZONE_MARKER = BLOCKS.register("no_fly_zone_marker", () ->
            new Block(BlockBehaviour.Properties
                    .copy(Blocks.GLASS)
                    .strength(1.5f, 6.0f)
                    .mapColor(DyeColor.GRAY)
                    .lightLevel(value -> 2)
                    .dynamicShape()
                    .pushReaction(PushReaction.DESTROY)
                    .noOcclusion()
                    .sound(SoundType.STONE)
            ));

    public static final RegistryObject<TrophyBlock> TROPHY = BLOCKS.register("trophy", () ->
            new TrophyBlock(BlockBehaviour.Properties
                    .copy(Blocks.GLASS)
                    .strength(1.5f, 6.0f)
                    .mapColor(DyeColor.YELLOW)
                    .dynamicShape()
                    .pushReaction(PushReaction.DESTROY)
                    .noOcclusion()
                    .sound(SoundType.STONE)
            ));
}
