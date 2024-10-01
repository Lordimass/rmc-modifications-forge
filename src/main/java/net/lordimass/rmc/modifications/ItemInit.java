package net.lordimass.rmc.modifications;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "rmcmodifications");

    public static final RegistryObject<BlockItem> NO_FLY_ZONE_MARKER_ITEM = ITEMS.register("no_fly_zone_marker",
            () -> new BlockItem(BlockInit.NO_FLY_ZONE_MARKER.get(),
                    new Item.Properties()
                        .rarity(Rarity.UNCOMMON)
            ));

    public static final RegistryObject<BlockItem> TROPHY = ITEMS.register("trophy",
            () -> new BlockItem(BlockInit.TROPHY.get(),
                    new Item.Properties()
                            .rarity(Rarity.EPIC)
            ));
}
