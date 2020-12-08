package dev.quae.mods.basis.construct.type;

import com.mojang.serialization.Codec;
import dev.quae.mods.basis.construct.ConstructPart;
import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags.IOptionalNamedTag;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

public interface IConstructType extends Predicate<Block>, IForgeRegistryEntry<IConstructType> {

  Codec<IConstructType> CODEC = ResourceLocation.CODEC
      .fieldOf("type").xmap(ConstructPart.CONSTRUCT_TYPES.get()::getValue, ConstructPart.CONSTRUCT_TYPES.get()::getKey)
      .codec();

  IOptionalNamedTag<Block> getTag();

  @Override
  default boolean test(Block block) {
    return getTag().contains(block);
  }

  @NotNull
  default IConstructType and(@NotNull IConstructType other) {
    Objects.requireNonNull(other);
    return new MergedType(this, other, false);
  }

  @NotNull
  default IConstructType or(@NotNull IConstructType other) {
    Objects.requireNonNull(other);
    return new MergedType(this, other, true);
  }

  @NotNull
  @Override
  default IConstructType negate() {
    return new NegatedType(this);
  }
}
