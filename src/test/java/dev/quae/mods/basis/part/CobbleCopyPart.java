package dev.quae.mods.basis.part;

import dev.quae.mods.basis.BasisMutatio;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.Tags.Items;
import net.minecraftforge.fml.RegistryObject;

@Part(BasisMutatio.ID)
public class CobbleCopyPart extends BasePart {

  public static final String NAME = "cobblecopy";
  private RegistryObject<Block> block;
  private RegistryObject<BlockItem> item;

  @Override
  public void initEverything() {
    block = registerBlock(NAME, () -> new Block(Properties.from(Blocks.COBBLESTONE)));
    item = registerItem(NAME, block.lazyMap(it -> new BlockItem(it, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS))));
  }

  @Override
  public void genData() {
    genTranslation(it -> it.addBlock(block, "Cobblestone Copy"));
    genItemModel(itemModelFromBlock(NAME, block));
    genBlockState(it -> it.simpleBlock(block.get()));
    genBlockLoot(it -> it.registerDropSelfLootTable(block.get()));
    genRecipe((p, c) -> new ShapelessRecipeBuilder(item.get(), 1)
        .addIngredient(Tags.Items.COBBLESTONE)
        .addCriterion("get_cobble", hasItem(Items.COBBLESTONE))
        .build(c));
    genBlockTag(it -> it.apply(Tags.Blocks.COBBLESTONE).add(block.get()));
    genItemTag(it -> it.apply(Items.COBBLESTONE).add(item.get()));
  }

}
