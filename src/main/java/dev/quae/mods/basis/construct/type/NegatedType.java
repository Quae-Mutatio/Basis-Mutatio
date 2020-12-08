package dev.quae.mods.basis.construct.type;

import net.minecraft.block.Block;
import net.minecraftforge.common.Tags.IOptionalNamedTag;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class NegatedType extends ForgeRegistryEntry<IConstructType> implements IConstructType {

  private final IConstructType negative;

  public NegatedType(IConstructType negative) {
    this.negative = negative;
  }

  @Override
  public IOptionalNamedTag<Block> getTag() {
    return null;
  }

  @Override
  public boolean test(Block block) {
    return negative.negate().test(block);
  }
}
