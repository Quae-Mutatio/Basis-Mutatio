package dev.quae.mods.basis.construct.type;

import net.minecraft.block.Block;
import net.minecraftforge.common.Tags.IOptionalNamedTag;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class MergedType extends ForgeRegistryEntry<IConstructType> implements IConstructType {

  private final IConstructType a;
  private final IConstructType b;
  private final boolean merge;

  public MergedType(IConstructType a, IConstructType b, boolean merge) {
    this.a = a;
    this.b = b;
    this.merge = merge;
  }

  @Override
  public IOptionalNamedTag<Block> getTag() {
    return null;
  }

  @Override
  public boolean test(Block block) {
    return (merge ? a.or(b) : a.and(b)).test(block);
  }
}
