package dev.quae.mods.basis.construct.core;

import com.mojang.serialization.Codec;
import dev.quae.mods.basis.BasisMutatio;
import dev.quae.mods.basis.construct.type.IConstructType;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import java.util.Map;

public class Legend {

  public static final Codec<Legend> CODEC = Codec.unboundedMap(BasisMutatio.CHAR_CODEC, IConstructType.CODEC).xmap(Legend::new, it -> it.types);

  private final Char2ObjectMap<IConstructType> types;

  public Legend() {
    types = new Char2ObjectOpenHashMap<>();
  }

  private Legend(Map<Character, IConstructType> map) {
    this();
    map.forEach(this::add);
  }

  public Legend add(char ch, IConstructType type) {
    types.put(ch, type);
    return this;
  }

  public boolean contains(IConstructType type) {
    return this.types.containsValue(type);
  }

  public IConstructType get(char ch) {
    return types.get(ch);
  }
}
