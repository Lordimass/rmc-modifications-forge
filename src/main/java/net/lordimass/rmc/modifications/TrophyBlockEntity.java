package net.lordimass.rmc.modifications;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TrophyBlockEntity extends BlockEntity {

    private String label;

    public TrophyBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.TROPHY.get(), pos, state);
    }

    public void setLabel(String label) {
        this.label = label;
        setChanged();

        // sync to client
        this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), TrophyBlock.UPDATE_ALL);
    }
    public String getLabel() {return this.label;}

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        this.label = nbt.getString("label");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putString("label", this.label);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        saveAdditional(nbt);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket () {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
