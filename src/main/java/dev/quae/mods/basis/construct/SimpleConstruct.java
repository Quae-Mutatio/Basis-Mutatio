package dev.quae.mods.basis.construct;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.quae.mods.basis.construct.task.Amount;
import dev.quae.mods.basis.construct.task.Amount.Const;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleConstruct extends ConstructBase {

  public static final Codec<SimpleConstruct> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Codec.unboundedMap(Type.CODEC, Amount.CODEC).fieldOf("requiredParts").forGetter(it -> it.requiredParts),
      Codec.STRING.fieldOf("controllerLegend").forGetter(it -> String.valueOf(it.controllerLegend)),
      BlockPos.CODEC.fieldOf("shape").forGetter(it -> it.shape),
      Codec.STRING.fieldOf("layout").forGetter(it -> it.layout),
      Codec.unboundedMap(Codec.STRING, Type.CODEC).fieldOf("legend").forGetter(it -> {
        final HashMap<String, Type> map = new HashMap<>(it.legend.size());
        for (Char2ObjectMap.Entry<Type> entry : it.legend.char2ObjectEntrySet()) {
          map.put(String.valueOf(entry.getCharKey()), entry.getValue());
        }
        return map;
      })
  ).apply(instance, (requiredParts, stringControllerLegend, shape, layout, stringLegend) -> {
    final Char2ObjectMap<Type> legend = new Char2ObjectOpenHashMap<>(stringLegend.size());
    for (Entry<String, Type> entry : stringLegend.entrySet()) {
      legend.put(entry.getKey().charAt(0), entry.getValue());
    }
    return new SimpleConstruct(requiredParts, stringControllerLegend.charAt(0), shape, layout, legend);
  }));

  private final Map<Type, Amount> requiredParts;
  private final char controllerLegend;
  private final BlockPos shape;
  private final String layout;
  private final Char2ObjectMap<Type> legend;
  private final Const requiredNumberOfParts;
  private final BlockPos controllerLocalOffset;

  private SimpleConstruct(Map<Type, Amount> requiredParts, char controllerLegend, BlockPos shape, String layout, Char2ObjectMap<Type> legend) {
    this.requiredParts = requiredParts;
    this.controllerLegend = controllerLegend;
    this.shape = shape;
    this.layout = layout;
    this.legend = legend;
    this.requiredNumberOfParts = new Const(getRequiredParts().values().stream().mapToInt(Amount::lowerBound).sum());
    final int controllerLocalIndex = this.layout.indexOf(controllerLegend);
    final int x = shape.getX();
    final int z = shape.getZ();
    final int xz = x * z;
    this.controllerLocalOffset = new BlockPos(controllerLocalIndex % x, (controllerLocalIndex % xz) / z, controllerLocalIndex / xz);
  }

  @Override
  public Map<Type, Amount> getRequiredParts() {
    return this.requiredParts;
  }

  @Override
  public char getControllerLegend() {
    return this.controllerLegend;
  }

  @Override
  public Amount getRequiredNumberOfParts() {
    return this.requiredNumberOfParts;
  }

  @Override
  public BlockPos getShape() {
    return this.shape;
  }

  @Override
  public BlockPos getControllerLocalOffset() {
    return this.controllerLocalOffset;
  }

  @Override
  public String getLayout() {
    return this.layout;
  }

  @Override
  public Char2ObjectMap<Type> getLegend() {
    return this.legend;
  }

  public static class Builder {

    private static final Logger LOGGER = LogManager.getLogger();
    private final char controllerLegend;
    private final BlockPos shape;
    private final Map<Type, Amount> requiredParts;
    private final StringBuilder layout;
    private final Char2ObjectMap<Type> legend;

    public Builder(char controllerLegend, BlockPos shape) {
      this.controllerLegend = controllerLegend;
      this.shape = shape;
      this.requiredParts = new HashMap<>();
      this.layout = new StringBuilder();
      this.legend = new Char2ObjectOpenHashMap<>();
    }

    public Builder addLayer(String layer) {
      if (layer.length() != shape.getX() * shape.getZ()) {
        LOGGER.warn("Added a layer to a SimpleConstruct.Builder which did not match the shape of a layer.");
      }
      this.layout.append(layer);
      return this;
    }

    public Builder addLegend(char key, Type value) {
      this.legend.put(key, value);
      return this;
    }

    public Builder addRequiredPart(Type part, Amount amount) {
      this.requiredParts.put(part, amount);
      return this;
    }

    public SimpleConstruct build() {
      return new SimpleConstruct(requiredParts, controllerLegend, shape, this.layout.toString(), legend);
    }
  }
}
