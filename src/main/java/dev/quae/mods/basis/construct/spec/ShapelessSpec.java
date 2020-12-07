package dev.quae.mods.basis.construct.spec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.quae.mods.basis.construct.core.Requirements;

public class ShapelessSpec {

  public static final Codec<ShapelessSpec> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Requirements.CODEC.fieldOf("requirements").forGetter(it -> it.requirements)
  ).apply(instance, ShapelessSpec::new));

  private final Requirements requirements;

  public ShapelessSpec(Requirements requirements) {
    this.requirements = requirements;
  }

  public Requirements requirements() {
    return requirements;
  }
}
