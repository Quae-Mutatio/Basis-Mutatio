package dev.quae.mods.basis.construct.data;

import dev.quae.mods.basis.construct.ShapedConstruct;
import dev.quae.mods.basis.construct.blueprint.SimpleBlueprint;
import dev.quae.mods.basis.construct.core.Layout;
import dev.quae.mods.basis.construct.core.Legend;
import dev.quae.mods.basis.construct.core.Requirements;
import dev.quae.mods.basis.construct.spec.ShapedSpec;
import dev.quae.mods.basis.construct.task.Amount;
import dev.quae.mods.basis.construct.type.IConstructType;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.RegistryObject;

public class ShapedConstructBuilder implements IConstructBuilder {

  private final BlockPos shape;
  private final StringBuilder layout;
  private final Char2ObjectMap<RegistryObject<IConstructType>> legend;
  private final Map<RegistryObject<IConstructType>, Amount> requirements;

  public ShapedConstructBuilder(BlockPos shape) {
    this.shape = shape;
    layout = new StringBuilder();
    legend = new Char2ObjectOpenHashMap<>();
    requirements = new HashMap<>();
  }

  public ShapedConstructBuilder addLayer(CharSequence layer) {
    this.layout.append(layer);
    return this;
  }

  public ShapedConstructBuilder addLegend(char ch, RegistryObject<IConstructType> type) {
    this.legend.put(ch, type);
    return this;
  }

  public ShapedConstructBuilder addRequirement(RegistryObject<IConstructType> type, Amount amount) {
    this.requirements.put(type, amount);
    return this;
  }

  @Override
  public ShapedConstruct build() {
    final Legend l = new Legend();
    this.legend.forEach((ch, type) -> l.add(ch, type.get()));
    final Requirements r = new Requirements();
    this.requirements.forEach((type, amount) -> r.add(type.get(), amount));
    return new ShapedConstruct(new ShapedSpec(new SimpleBlueprint(new Layout(layout.toString(), shape), l), r));
  }
}
