package dev.quae.mods.basis.part;

import dev.quae.mods.basis.BasisMutatio;
import dev.quae.mods.basis.construct.data.ShapedConstructBuilder;
import dev.quae.mods.basis.construct.data.ShapelessConstructBuilder;
import dev.quae.mods.basis.construct.task.Amount.Const;
import dev.quae.mods.basis.construct.task.Amount.Range;
import dev.quae.mods.basis.construct.type.BasicType;
import dev.quae.mods.basis.construct.type.IConstructType;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.Tags.Items;
import net.minecraftforge.fml.RegistryObject;

@Part(BasisMutatio.ID)
public class PartTest extends BasePart {

  public static final String NAME = "cobblecopy";
  private RegistryObject<Block> block;
  private RegistryObject<BlockItem> item;
  private RegistryObject<IConstructType> LAPIS;
  private RegistryObject<IConstructType> COBBLE;

  @Override
  public void initEverything() {
    block = registerBlock(NAME, () -> new Block(Properties.from(Blocks.COBBLESTONE)));
    item = registerItem(NAME, block.lazyMap(it -> new BlockItem(it, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS))));

    LAPIS = registerConstructPartType("lapis", () -> new BasicType(Tags.Blocks.STORAGE_BLOCKS_LAPIS));
    COBBLE = registerConstructPartType("cobble", () -> new BasicType(Tags.Blocks.COBBLESTONE));

    // No-op but example of client job.
    registerClientJob(() -> RenderTypeLookup.setRenderLayer(block.get(), RenderType.getSolid()));
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
    genConstruct(NAME, new ShapedConstructBuilder(new BlockPos(1, 2, 1))
        .addLayer("L")
        .addLayer("C")
        .addLegend('L', LAPIS)
        .addLegend('C', COBBLE)
        .addRequirement(LAPIS, new Range(0, 1))
        .addRequirement(COBBLE, new Const(1)));
    genConstruct(NAME + "1", new ShapelessConstructBuilder()
        .addRequirement(COBBLE, new Const(2)));
  }
}
