package dev.quae.mods.basis.construct.core;

import com.mojang.serialization.Codec;
import dev.quae.mods.basis.construct.ConstructPart;
import dev.quae.mods.basis.construct.task.Amount;
import dev.quae.mods.basis.construct.type.IConstructType;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.util.ResourceLocation;

public class Requirements {

  public static final Codec<Requirements> CODEC = Codec.unboundedMap(Codec.STRING, Amount.CODEC)
      .xmap(map -> map.entrySet().stream().collect(Collectors.toMap(entry -> ConstructPart.CONSTRUCT_TYPES.get().getValue(new ResourceLocation(entry.getKey())), Entry::getValue, (a, b) -> b, HashMap::new)),
          map -> map.entrySet().stream().collect(Collectors.toMap(entry -> ConstructPart.CONSTRUCT_TYPES.get().getKey(entry.getKey()).toString(), Entry::getValue, (a, b) -> b, HashMap::new)))
      .xmap(Requirements::new, it -> it.typeRequirementsMap);

  private final HashMap<IConstructType, Amount> typeRequirementsMap;

  public Requirements() {
    this(new HashMap<>());
  }

  public Requirements(HashMap<IConstructType, Amount> typeRequirementsMap) {
    this.typeRequirementsMap = typeRequirementsMap;
  }

  public Requirements add(IConstructType type, Amount amount) {
    typeRequirementsMap.put(type, amount);
    return this;
  }

  public HashMap<IConstructType, Amount> getTypeRequirementsMap() {
    return typeRequirementsMap;
  }
}
