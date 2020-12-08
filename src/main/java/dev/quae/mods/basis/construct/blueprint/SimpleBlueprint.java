package dev.quae.mods.basis.construct.blueprint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.quae.mods.basis.construct.core.Layout;
import dev.quae.mods.basis.construct.core.Legend;

public class SimpleBlueprint implements IBlueprint {

  public static final Codec<IBlueprint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Layout.CODEC.fieldOf("layout").forGetter(IBlueprint::getLayout),
      Legend.CODEC.fieldOf("legend").forGetter(IBlueprint::getLegend)
  ).apply(instance, SimpleBlueprint::new));

  private final Layout layout;
  private final Legend legend;

  public SimpleBlueprint(Layout layout, Legend legend) {
    this.layout = layout;
    this.legend = legend;
  }

  @Override
  public Layout getLayout() {
    return this.layout;
  }

  @Override
  public Legend getLegend() {
    return this.legend;
  }
}
