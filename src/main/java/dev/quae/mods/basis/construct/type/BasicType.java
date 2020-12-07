package dev.quae.mods.basis.construct.type;

import net.minecraft.block.Block;
import net.minecraftforge.common.Tags.IOptionalNamedTag;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class BasicType extends ForgeRegistryEntry<IConstructType> implements IConstructType {

  private final IOptionalNamedTag<Block> tag;

  public BasicType(IOptionalNamedTag<Block> tag) {
    this.tag = tag;
  }

  @Override
  public IOptionalNamedTag<Block> getTag() {
    return this.tag;
  }
}
