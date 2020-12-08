package dev.quae.mods.basis.construct.spec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.quae.mods.basis.construct.core.Layout;
import dev.quae.mods.basis.construct.core.Legend;
import dev.quae.mods.basis.construct.core.Requirements;
import dev.quae.mods.basis.construct.blueprint.IBlueprint;
import dev.quae.mods.basis.construct.blueprint.SimpleBlueprint;

public class ShapedSpec {

  public static final Codec<ShapedSpec> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      SimpleBlueprint.CODEC.fieldOf("blueprint").forGetter(it -> it.blueprint),
      Requirements.CODEC.fieldOf("requirements").forGetter(it -> it.requirements)
  ).apply(instance, ShapedSpec::new));

  private final IBlueprint blueprint;
  private final Requirements requirements;

  public ShapedSpec(IBlueprint blueprint, Requirements requirements) {
    this.blueprint = blueprint;
    this.requirements = requirements;
  }

  public Legend legend() {
    return blueprint.getLegend();
  }

  public Layout layout() {
    return blueprint.getLayout();
  }

  public Requirements getRequirements() {
    return requirements;
  }
}
