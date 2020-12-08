package dev.quae.mods.basis.construct;

import dev.quae.mods.basis.construct.core.IConstruct;
import dev.quae.mods.basis.construct.form.ConstructForm;
import dev.quae.mods.basis.construct.form.IConstructForm;
import dev.quae.mods.basis.construct.spec.ShapelessSpec;
import dev.quae.mods.basis.construct.task.Amount;
import dev.quae.mods.basis.construct.task.Amount.Const;
import dev.quae.mods.basis.construct.type.IConstructType;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class ShapelessConstruct implements IConstruct {

  private final ShapelessSpec spec;

  public ShapelessConstruct(ShapelessSpec spec) {
    this.spec = spec;
  }

  @Override
  public IConstructForm getForm() {
    return ConstructForm.SHAPELESS;
  }

  @Override
  public Amount getRequiredNumberOfParts() {
    return new Const(spec.requirements().getTypeRequirementsMap().values().stream().mapToInt(Amount::lowerBound).sum());
  }

  @Override
  public boolean validateConstruct(BlockPos controllerWorldPosition, IWorldReader world) {
    final Set<CachedBlockInfo> visited = new HashSet<>();
    final Deque<CachedBlockInfo> queue = new LinkedList<CachedBlockInfo>() {
      @Override
      public void addLast(CachedBlockInfo o) {
        if (visited.add(o)) {
          super.addLast(o);
        }
      }
    };
    final Map<CachedBlockInfo, IConstructType> useful = new HashMap<CachedBlockInfo, IConstructType>() {
      @Override
      public IConstructType put(CachedBlockInfo info, IConstructType type) {
        for (Direction offset : Direction.values()) {
          queue.addLast(blockInfo(world, info.getPos().offset(offset)));
        }
        return super.put(info, type);
      }
    };
    queue.addFirst(blockInfo(world, controllerWorldPosition));
    final Set<IConstructType> types = spec.requirements().getTypeRequirementsMap().keySet();

    while (!queue.isEmpty()) {
      final CachedBlockInfo info = queue.pollFirst();
      final BlockState state = info.getBlockState();
      if (state == null) {
        continue;
      }
      final Block block = state.getBlock();
      types.stream()
          .filter(type -> type.test(block))
          .findFirst()
          .ifPresent(type -> useful.put(info, type));
    }
    return spec.requirements()
        .getTypeRequirementsMap()
        .entrySet()
        .stream()
        .allMatch(entry -> entry.getValue().test((int) useful.values().stream().filter(entry.getKey()::equals).count()));
  }

  private CachedBlockInfo blockInfo(IWorldReader world, BlockPos pos) {
    return new CachedBlockInfo(world, pos, false);
  }

  public ShapelessSpec getSpec() {
    return this.spec;
  }
}
