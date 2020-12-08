package dev.quae.mods.basis.construct.data;

import dev.quae.mods.basis.construct.ShapelessConstruct;
import dev.quae.mods.basis.construct.core.Requirements;
import dev.quae.mods.basis.construct.spec.ShapelessSpec;
import dev.quae.mods.basis.construct.task.Amount;
import dev.quae.mods.basis.construct.type.IConstructType;
import java.util.HashMap;
import java.util.Map;
import net.minecraftforge.fml.RegistryObject;

public class ShapelessConstructBuilder implements IConstructBuilder {

  private final Map<RegistryObject<IConstructType>, Amount> requirements;

  public ShapelessConstructBuilder() {
    requirements = new HashMap<>();
  }

  public ShapelessConstructBuilder addRequirement(RegistryObject<IConstructType> type, Amount amount) {
    this.requirements.put(type, amount);
    return this;
  }

  @Override
  public ShapelessConstruct build() {
    final Requirements r = new Requirements();
    this.requirements.forEach((type, amount) -> r.add(type.get(), amount));
    return new ShapelessConstruct(new ShapelessSpec(r));
  }
}
