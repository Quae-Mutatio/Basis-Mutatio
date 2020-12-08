package dev.quae.mods.basis.construct.blueprint;

import dev.quae.mods.basis.construct.core.Layout;
import dev.quae.mods.basis.construct.core.Legend;
import net.minecraft.util.math.BlockPos;

public interface IBlueprint {

  default BlockPos getShape() {
    return this.getLayout().getShape();
  }

  default BlockPos getControllerLocalOffset() {
    return this.getLayout().controllerOffset();
  }

  Layout getLayout();

  Legend getLegend();
}
