package dev.quae.mods.basis.construct.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.quae.mods.basis.construct.ShapedConstruct;
import dev.quae.mods.basis.construct.ShapelessConstruct;
import dev.quae.mods.basis.construct.form.ConstructForm;
import dev.quae.mods.basis.construct.form.IConstructForm;
import dev.quae.mods.basis.construct.spec.ShapedSpec;
import dev.quae.mods.basis.construct.spec.ShapelessSpec;
import dev.quae.mods.basis.construct.task.Amount;
import dev.quae.mods.basis.construct.type.IConstructType;
import java.util.Optional;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public interface IConstruct {

  Codec<IConstruct> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      ConstructForm.CODEC.fieldOf("form").forGetter(IConstruct::getForm),
      ShapelessSpec.CODEC.optionalFieldOf("shapeless").forGetter(it -> Optional.ofNullable(it)
          .filter(ShapelessConstruct.class::isInstance)
          .map(ShapelessConstruct.class::cast)
          .map(ShapelessConstruct::getSpec)
      ),
      ShapedSpec.CODEC.optionalFieldOf("shaped").forGetter(it -> Optional.ofNullable(it)
          .filter(ShapedConstruct.class::isInstance)
          .map(ShapedConstruct.class::cast)
          .map(ShapedConstruct::getSpec)
      )
  ).apply(instance, (form, shapelessSpec, shapedSpec) -> {
    if (form == ConstructForm.SHAPED) {
      return new ShapedConstruct(shapedSpec.orElseThrow(() -> new IllegalStateException("Shaped construct is missing a spec")));
    } else if (form == ConstructForm.SHAPELESS) {
      return new ShapelessConstruct(shapelessSpec.orElseThrow(() -> new IllegalStateException("Shapeless construct is missing a spec")));
    } else {
      throw new IllegalStateException("Construct does not have a valid type.");
    }
  }));

  IConstructForm getForm();

  Amount getRequiredNumberOfParts();

  boolean validateConstruct(BlockPos controllerWorldPosition, IWorldReader world);

  interface Part {

    IConstructType getType();
  }

}
