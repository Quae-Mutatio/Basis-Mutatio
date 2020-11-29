package dev.quae.mods.basis.construct;

import dev.quae.mods.basis.construct.IConstruct.Type;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import net.minecraft.util.math.BlockPos;

public interface IBlueprint {

  BlockPos getShape();

  BlockPos getControllerLocalOffset();

  String getLayout();

  Char2ObjectMap<Type> getLegend();
}
