package dev.quae.mods.basis.construct.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.quae.mods.basis.construct.ShapedConstruct;
import java.util.Arrays;
import java.util.stream.Collectors;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;

public class Layout implements IStringSerializable {

  public static final Codec<Layout> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Codec.STRING.fieldOf("pattern").forGetter(Layout::getString),
      BlockPos.CODEC.fieldOf("shape").forGetter(Layout::getShape)
  ).apply(instance, Layout::new));

  public final char[][][] layout;
  private final BlockPos shape;

  /**
   * @param layout The layout should be char[y][z][x]
   */
  public Layout(char[][][] layout) {
    this.layout = layout;
    this.shape = new BlockPos(layout[0][0].length, layout.length, layout[0].length);
  }

  public Layout(String string, BlockPos shape) {
    this.layout = new char[shape.getY()][shape.getZ()][shape.getX()];
    int counter = 0;
    for (int y = 0; y < shape.getY(); y++) {
      for (int z = 0; z < shape.getZ(); z++) {
        for (int x = 0; x < shape.getX(); x++) {
          this.layout[y][z][x] = string.charAt(counter++);
        }
      }
    }
    this.shape = shape;
  }

  public BlockPos getShape() {
    return this.shape;
  }

  public BlockPos controllerOffset() {
    for (int y = 0; y < layout.length; y++) {
      for (int z = 0; z < layout[y].length; z++) {
        for (int x = 0; x < layout[y][z].length; x++) {
          char c = layout[y][z][x];
          if (c == ShapedConstruct.CONTROLLER_KEY) {
            return new BlockPos(x, y, z);
          }
        }
      }
    }
    throw new IllegalStateException(String.format("Layout is missing a controller, make sure all layouts contain a '%c'", ShapedConstruct.CONTROLLER_KEY));
  }


  @Override
  public String getString() {
    return Arrays.stream(layout).flatMap(Arrays::stream).map(String::valueOf).collect(Collectors.joining());
  }
}
