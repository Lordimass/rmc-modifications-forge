package net.lordimass.rmc.modifications.mixins;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.narration.NarratableEntry;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(AbstractSelectionList.class)
@Debug(export = true)
public abstract class KeyBindsListMixin extends AbstractContainerEventHandler implements Renderable, NarratableEntry {


    @Unique
    private static final Logger LOGGER = LogUtils.getLogger();

    @Inject(method="addEntry(Lnet/minecraft/client/gui/components/AbstractSelectionList$Entry;)I", at = @At("HEAD"), cancellable = true)
    protected void addEntry(CallbackInfoReturnable<Integer> cir) {
        LOGGER.info("Attempted (possibly succeeded) to add an Entry");
        cir.setReturnValue(0);
    }
};
