package dev.quae.mods.basis.construct;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.quae.mods.basis.BasisMutatio;
import dev.quae.mods.basis.construct.task.Amount;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

public interface IConstruct extends IBlueprint, IForgeRegistryEntry<IConstruct> {

  Map<Type, Amount> getRequiredParts();

  char getControllerLegend();

  Amount getRequiredNumberOfParts();

  boolean validateConstruct(BlockPos controllerWorldPosition, IWorldReader world);

  interface Type extends Predicate<Type>, IForgeRegistryEntry<Type> {
    DeferredRegister<Type> TYPE_DEFERRED_REGISTER = DeferredRegister.create(Type.class, BasisMutatio.ID);
    Supplier<IForgeRegistry<Type>> TYPE_REGISTRY = TYPE_DEFERRED_REGISTER.makeRegistry("type", RegistryBuilder::new);

    Codec<Type> CODEC = ResourceLocation.CODEC.comapFlatMap(Type::decodeType, type -> TYPE_REGISTRY.get().getKey(type)).stable();

    static DataResult<Type> decodeType(ResourceLocation encoded) {
      try {
        return DataResult.success(TYPE_REGISTRY.get().getValue(encoded));
      } catch (Exception ex) {
        return DataResult.error("Not a valid Type: " + encoded + " " + ex.getMessage());
      }
    }

    @Override
    default boolean test(Type type) {
      return this == type;
    }

    @Nonnull
    @Override
    ResourceLocation getRegistryName();
  }

  interface Part {

    Type getType();
  }

  interface Controller extends Part {

  }

  interface Component extends Part {

  }

}
