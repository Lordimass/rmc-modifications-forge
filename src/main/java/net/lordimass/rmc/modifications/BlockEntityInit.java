package net.lordimass.rmc.modifications;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.rmi.registry.Registry;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
            ForgeRegistries.BLOCK_ENTITY_TYPES,
            "rmcmodifications"
    );

    public static final RegistryObject<BlockEntityType<TrophyBlockEntity>> TROPHY = BLOCK_ENTITIES.register(
            "trophy",
            () -> BlockEntityType.Builder.of(TrophyBlockEntity::new, BlockInit.TROPHY.get()).build(null)
    );
}
