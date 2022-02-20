package griglog.harder.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerDifficulty {
    public int value = 0;
    public boolean toReset = false;

    public CompoundNBT getNbt(){
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("value", value);
        tag.putBoolean("toReset", toReset);
        return tag;
    }

    public PlayerDifficulty setNbt(CompoundNBT nbt){
        value = nbt.getInt("value");
        toReset = nbt.getBoolean("toReset");
        return this;
    }

    public static PlayerDifficulty get(PlayerEntity player){
        return player.getCapability(Provider.PLAYER_DIFFICULTY).resolve().orElse(null);
    }

    public static class Provider implements ICapabilitySerializable<INBT> {
        @CapabilityInject(PlayerDifficulty.class)
        public static Capability<PlayerDifficulty> PLAYER_DIFFICULTY;
        private final LazyOptional<PlayerDifficulty> instance = LazyOptional.of(PlayerDifficulty::new);

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return cap == PLAYER_DIFFICULTY ? instance.cast() : LazyOptional.empty();
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
            return cap == PLAYER_DIFFICULTY ? instance.cast() : LazyOptional.empty();
        }

        @Override
        public INBT serializeNBT() {
            return PLAYER_DIFFICULTY.getStorage().writeNBT(PLAYER_DIFFICULTY, this.instance.resolve().get(), null);
        }

        @Override
        public void deserializeNBT(INBT nbt) {
            PLAYER_DIFFICULTY.getStorage().readNBT(PLAYER_DIFFICULTY, this.instance.resolve().get(), null, nbt);
        }
    }

    public static class Storage implements Capability.IStorage<PlayerDifficulty> {
        @Override
        public INBT writeNBT(Capability<PlayerDifficulty> capability, PlayerDifficulty PlayerDifficulty, Direction side) {
            return PlayerDifficulty.getNbt();
        }

        @Override
        public void readNBT(Capability<PlayerDifficulty> capability, PlayerDifficulty PlayerDifficulty, Direction side, INBT nbt) {
            PlayerDifficulty.setNbt((CompoundNBT)nbt);
        }
    }
}

