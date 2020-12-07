package dev.quae.mods.basis.construct.core;

import dev.quae.mods.basis.construct.type.IConstructType;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class ConstructCache {

  private final Map<BlockPos, CachedBlockInfo> infoMap;
  /**
   * Map of offsets and the types found at those offsets
   */
  private final Map<BlockPos, IConstructType> foundTypes;
  private final BlockPos center;
  private final IWorldReader world;

  public ConstructCache(IWorldReader world, BlockPos center) {
    this.world = world;
    this.center = center;
    infoMap = new HashMap<>();
    foundTypes = new HashMap<>();
  }

  public CachedBlockInfo get(BlockPos pos) {
    return infoMap.computeIfAbsent(pos, it -> new CachedBlockInfo(this.world, it, false));
  }

  public BlockState getState(BlockPos pos) {
    return get(pos).getBlockState();
  }

  public Block getBlock(BlockPos pos) {
    return getState(pos).getBlock();
  }

  public TileEntity getTile(BlockPos pos) {
    return get(pos).getTileEntity();
  }

  public void found(BlockPos pos, IConstructType type) {
    if (type.test(getBlock(pos))) {
      foundTypes.put(offset(pos), type);
    }
  }

  public BlockPos offset(BlockPos real) {
    return real.subtract(center);
  }

  public BlockPos real(BlockPos offset) {
    return offset.add(center);
  }
}
