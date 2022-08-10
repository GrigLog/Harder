package griglog.harder.capability;

import griglog.harder.Harder;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerDifficulty {
    public static Capability<PlayerDifficulty> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    public static ResourceLocation ID = new ResourceLocation(Harder.id, "difficulty");

    public int value = 0;
    public boolean toReset = false;

    public CompoundTag getNbt(){
        CompoundTag tag = new CompoundTag();
        tag.putInt("value", value);
        tag.putBoolean("toReset", toReset);
        return tag;
    }

    public void setNbt(CompoundTag nbt){
        value = nbt.getInt("value");
        toReset = nbt.getBoolean("toReset");
    }

    public static PlayerDifficulty get(Player player){
        return player.getCapability(CAPABILITY).resolve().orElse(null);
    }

    public static class Provider implements ICapabilitySerializable<Tag> {
        private PlayerDifficulty instance = new PlayerDifficulty();
        private final LazyOptional<PlayerDifficulty> provider = LazyOptional.of(() -> instance);

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return cap == CAPABILITY ? provider.cast() : LazyOptional.empty();
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
            return cap == CAPABILITY ? provider.cast() : LazyOptional.empty();
        }

        @Override
        public Tag serializeNBT() {
            return instance.getNbt();
        }

        @Override
        public void deserializeNBT(Tag tag) {
            instance.setNbt((CompoundTag) tag);
        }
    }
}

