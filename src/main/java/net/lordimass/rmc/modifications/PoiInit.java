package net.lordimass.rmc.modifications;

import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.lordimass.rmc.modifications.BlockInit;

import java.util.Collections;

public class PoiInit {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(
            ForgeRegistries.POI_TYPES,
            "rmcmodifications");

    public static final RegistryObject<PoiType> NO_FLY_ZONE_POI = POI_TYPES.register("no_fly_zone_poi", () ->
            new PoiType(
                    Collections.singleton(BlockInit.NO_FLY_ZONE_MARKER.get().defaultBlockState()),
                    1,
                    16
            ));
}
