package dev.quae.mods.basis.construct;

import dev.quae.mods.basis.construct.core.IConstruct;
import dev.quae.mods.basis.construct.core.Layout;
import dev.quae.mods.basis.construct.core.Legend;
import dev.quae.mods.basis.construct.form.ConstructForm;
import dev.quae.mods.basis.construct.form.IConstructForm;
import dev.quae.mods.basis.construct.spec.ShapedSpec;
import dev.quae.mods.basis.construct.task.Amount;
import dev.quae.mods.basis.construct.task.Amount.Const;
import dev.quae.mods.basis.construct.type.IConstructType;
import it.unimi.dsi.fastutil.objects.Object2CharMap;
import it.unimi.dsi.fastutil.objects.Object2CharMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2CharOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IWorldReader;

public class ShapedConstruct implements IConstruct {
  public static final char CONTROLLER_KEY = 'C';

  private final ShapedSpec spec;
  private Object2CharMap<BlockPos> offsetMap;

  public ShapedConstruct(ShapedSpec spec) {
    this.spec = spec;
  }

  public BlockPos controllerOffset() {
    return this.spec.layout().controllerOffset();
  }

  public ShapedSpec getSpec() {
    return spec;
  }

  @Override
  public IConstructForm getForm() {
    return ConstructForm.SHAPED;
  }

  @Override
  public Amount getRequiredNumberOfParts() {
    return new Const(getSpec().getRequirements().getTypeRequirementsMap().values().stream().mapToInt(Amount::lowerBound).sum());
  }

  @Override
  public boolean validateConstruct(BlockPos controllerWorldPosition, IWorldReader world) {
    // Check each rotation of the local offsets if the blockstate contains the correct construct type.
    final Legend legend = getSpec().legend();
    final Object2IntMap<IConstructType> foundTypes = new Object2IntOpenHashMap<>();
    boolean matchesFlag = false;
    for (Rotation value : Rotation.values()) {
      matchesFlag = true;
      for (Entry<BlockPos> entry : offsetMap().object2CharEntrySet()) {
        final BlockPos offsetPos = controllerOffset().add(entry.getKey().rotate(value));
        final IConstructType type = matches(new CachedBlockInfo(world, offsetPos, false), legend.get(entry.getCharValue()));
        if (type == null) {
          matchesFlag = false;
          foundTypes.clear();
          break;
        }
        foundTypes.computeInt(type, (t, i) -> i == null ? 1 : ++i);
      }
      if (matchesFlag) {
        break;
      }
    }
    if (!matchesFlag) {
      return false;
    }

    final Map<IConstructType, Amount> requiredParts = this.getSpec().getRequirements().getTypeRequirementsMap();
    for (Map.Entry<IConstructType, Amount> entry : requiredParts.entrySet()) {
      if (!entry.getValue().test(foundTypes.getInt(entry.getKey()))) {
        return false;
      }
    }

    return true;
  }

  private Object2CharMap<BlockPos> offsetMap() {
    if (offsetMap != null) {
      return offsetMap;
    } else {
      offsetMap = new Object2CharOpenHashMap<>();
    }

    final Vector3i controllerLocalOffset = this.controllerOffset();
    final Layout layout = this.spec.layout();
    final BlockPos shape = layout.getShape();
    for (int y = 0; y < shape.getY(); y++) {
      for (int z = 0; z < shape.getZ(); z++) {
        for (int x = 0; x < shape.getX(); x++) {
          final char ch = layout.layout[y][z][x];
          if (ch == ' ') {
            continue;
          }
          final BlockPos offset = new BlockPos(x, y, z).subtract(controllerLocalOffset);
          offsetMap.put(offset, ch);
        }
      }
    }

    return offsetMap;
  }

  private IConstructType matches(CachedBlockInfo cachedBlock, IConstructType typeTest) {
    final BlockState state = cachedBlock.getBlockState();
    //noinspection ConstantConditions
    if (state == null) {
      return null;
    }
    final Block block = state.getBlock();
    typeTest.test(block);
    return typeTest;
  }
}
