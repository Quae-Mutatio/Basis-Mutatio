package dev.quae.mods.basis.part;

import static net.minecraft.util.registry.Registry.CUSTOM_STAT;
import static net.minecraft.util.registry.Registry.register;

import dev.quae.mods.basis.construct.IConstruct;
import dev.quae.mods.basis.construct.SimpleConstruct;
import dev.quae.mods.basis.data.BMBlockLootTables;
import dev.quae.mods.basis.helpers.LootHelpers;
import dev.quae.mods.basis.wrappers.DataGeneratorWrapper;
import dev.quae.mods.basis.wrappers.RegistryWrapper;
import java.util.HashSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.advancements.criterion.BlockPredicate;
import net.minecraft.advancements.criterion.EnterBlockTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.TagsProvider.Builder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.schedule.Schedule;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.loot.BinomialRange;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.DynamicLootEntry;
import net.minecraft.loot.ILootConditionConsumer;
import net.minecraft.loot.ILootFunctionConsumer;
import net.minecraft.loot.IRandomRange;
import net.minecraft.loot.IntClamper;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.LocationCheck;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.loot.conditions.TableBonus;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.CopyBlockState;
import net.minecraft.loot.functions.CopyName;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.loot.functions.ExplosionDecay;
import net.minecraft.loot.functions.LimitCount;
import net.minecraft.loot.functions.SetContents;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.state.Property;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.properties.SlabType;
import net.minecraft.stats.IStatFormatter;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.blockplacer.BlockPlacerType;
import net.minecraft.world.gen.blockstateprovider.BlockStateProviderType;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.IModBusEvent;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.RegistryBuilder;

public abstract class BasePart implements IPart {

  private RegistryWrapper registryWrapper;
  public DataGeneratorWrapper dataGeneratorWrapper;

  @Override
  public <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> thing) {
    return registryWrapper.getBlocks().register(name, thing);
  }

  @Override
  public <T extends Fluid> RegistryObject<T> registerFluid(String name, Supplier<T> thing) {
    return registryWrapper.getFluids().register(name, thing);
  }

  @Override
  public <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> thing) {
    return registryWrapper.getItems().register(name, thing);
  }

  @Override
  public <T extends Effect> RegistryObject<T> registerPotion(String name, Supplier<T> thing) {
    return registryWrapper.getPotions().register(name, thing);
  }

  @Override
  public <T extends SoundEvent> RegistryObject<T> registerSoundEvent(String name, Supplier<T> thing) {
    return registryWrapper.getSoundEvents().register(name, thing);
  }

  @Override
  public <T extends Potion> RegistryObject<T> registerPotionType(String name, Supplier<T> thing) {
    return registryWrapper.getPotionTypes().register(name, thing);
  }

  @Override
  public <T extends Enchantment> RegistryObject<T> registerEnchantment(String name, Supplier<T> thing) {
    return registryWrapper.getEnchantments().register(name, thing);
  }

  @Override
  public <T extends EntityType<?>> RegistryObject<T> registerEntity(String name, Supplier<T> thing) {
    return registryWrapper.getEntities().register(name, thing);
  }

  @Override
  public <T extends TileEntityType<?>> RegistryObject<T> registerTileEntity(String name, Supplier<T> thing) {
    return registryWrapper.getTileEntities().register(name, thing);
  }

  @Override
  public <T extends ParticleType<?>> RegistryObject<T> registerParticleType(String name, Supplier<T> thing) {
    return registryWrapper.getParticleTypes().register(name, thing);
  }

  @Override
  public <T extends ContainerType<?>> RegistryObject<T> registerContainer(String name, Supplier<T> thing) {
    return registryWrapper.getContainers().register(name, thing);
  }

  @Override
  public <T extends PaintingType> RegistryObject<T> registerPaintingType(String name, Supplier<T> thing) {
    return registryWrapper.getPaintingTypes().register(name, thing);
  }

  @Override
  public <T extends IRecipeSerializer<?>> RegistryObject<T> registerRecipeSerializer(String name, Supplier<T> thing) {
    return registryWrapper.getRecipeSerializers().register(name, thing);
  }

  @Override
  public <T extends Attribute> RegistryObject<T> registerAttribute(String name, Supplier<T> thing) {
    return registryWrapper.getAttributes().register(name, thing);
  }

  @Override
  public <T extends StatType<?>> RegistryObject<T> registerStatType(String name, Supplier<T> thing) {
    return registryWrapper.getStatTypes().register(name, thing);
  }

  @Override
  public <T extends VillagerProfession> RegistryObject<T> registerProfession(String name, Supplier<T> thing) {
    return registryWrapper.getProfessions().register(name, thing);
  }

  @Override
  public <T extends PointOfInterestType> RegistryObject<T> registerPoiType(String name, Supplier<T> thing) {
    return registryWrapper.getPoiTypes().register(name, thing);
  }

  @Override
  public <T extends MemoryModuleType<?>> RegistryObject<T> registerMemoryModuleType(String name, Supplier<T> thing) {
    return registryWrapper.getMemoryModuleTypes().register(name, thing);
  }

  @Override
  public <T extends SensorType<?>> RegistryObject<T> registerSensorType(String name, Supplier<T> thing) {
    return registryWrapper.getSensorTypes().register(name, thing);
  }

  @Override
  public <T extends Schedule> RegistryObject<T> registerSchedule(String name, Supplier<T> thing) {
    return registryWrapper.getSchedules().register(name, thing);
  }

  @Override
  public <T extends Activity> RegistryObject<T> registerActivity(String name, Supplier<T> thing) {
    return registryWrapper.getActivities().register(name, thing);
  }

  @Override
  public <T extends WorldCarver<?>> RegistryObject<T> registerWorldCarver(String name, Supplier<T> thing) {
    return registryWrapper.getWorldCarvers().register(name, thing);
  }

  @Override
  public <T extends SurfaceBuilder<?>> RegistryObject<T> registerSurfaceBuilder(String name, Supplier<T> thing) {
    return registryWrapper.getSurfaceBuilders().register(name, thing);
  }

  @Override
  public <T extends Feature<?>> RegistryObject<T> registerFeature(String name, Supplier<T> thing) {
    return registryWrapper.getFeatures().register(name, thing);
  }

  @Override
  public <T extends Placement<?>> RegistryObject<T> registerDecorator(String name, Supplier<T> thing) {
    return registryWrapper.getDecorators().register(name, thing);
  }

  @Override
  public <T extends ChunkStatus> RegistryObject<T> registerChunkStatus(String name, Supplier<T> thing) {
    return registryWrapper.getChunkStatus().register(name, thing);
  }

  @Override
  public <T extends Structure<?>> RegistryObject<T> registerStructureFeature(String name, Supplier<T> thing) {
    return registryWrapper.getStructureFeatures().register(name, thing);
  }

  @Override
  public <T extends BlockStateProviderType<?>> RegistryObject<T> registerBlockstateProviderType(String name, Supplier<T> thing) {
    return registryWrapper.getBlockstateProviderTypes().register(name, thing);
  }

  @Override
  public <T extends BlockPlacerType<?>> RegistryObject<T> registerBlockPlacerType(String name, Supplier<T> thing) {
    return registryWrapper.getBlockPlacerTypes().register(name, thing);
  }

  @Override
  public <T extends FoliagePlacerType<?>> RegistryObject<T> registerFoliagePlacerType(String name, Supplier<T> thing) {
    return registryWrapper.getFoliagePlacerTypes().register(name, thing);
  }

  @Override
  public <T extends TreeDecoratorType<?>> RegistryObject<T> registerTreeDecoratorType(String name, Supplier<T> thing) {
    return registryWrapper.getTreeDecoratorTypes().register(name, thing);
  }

  @Override
  public <T extends DataSerializerEntry> RegistryObject<T> registerDataSerializerEntry(String name, Supplier<T> thing) {
    return registryWrapper.getDataSerializers().register(name, thing);
  }

  @Override
  public <T extends GlobalLootModifierSerializer<?>> RegistryObject<T> registerGlobalLootTableModifierSerializer(String name, Supplier<T> thing) {
    return registryWrapper.getLootModifierSerializers().register(name, thing);
  }

  @Override
  public <T extends IConstruct.Type> RegistryObject<T> registerConstructPartType(String name, Supplier<T> type) {
    return registryWrapper.getConstructPartTypes().register(name, type);
  }

  public void registerCustomStat(ResourceLocation name, IStatFormatter formatter) {
    register(CUSTOM_STAT, name, name);
    Stats.CUSTOM.get(name, formatter);
  }

  @Override
  public void registerClientJob(Runnable job) {
    registryWrapper.getClientJobList().add(job);
  }

  @Override
  public void registerBlockColors(IBlockColor color, Supplier<Block>[] blocks) {
    registryWrapper.getBlockColorMap().put(color, blocks);
  }

  @Override
  public void registerItemColors(IItemColor color, Supplier<Item>[] items) {
    registryWrapper.getItemColorMap().put(color, items);
  }

  @Override
  public <T extends Event & IModBusEvent> void registerModEventHandler(Consumer<T> eventHandler) {
    registryWrapper.getBus().addListener(eventHandler);
  }

  @Override
  public void genTranslation(Consumer<LanguageProvider> consumer) {
    this.genTranslation("en_us", consumer);
  }

  @Override
  public void genTranslation(String lang, Consumer<LanguageProvider> consumer) {
    dataGeneratorWrapper.translations.computeIfAbsent(lang, it -> new HashSet<>()).add(consumer);
  }

  @Override
  public void genItemModel(Consumer<ItemModelProvider> consumer) {
    dataGeneratorWrapper.itemModelRegisterers.add(consumer);
  }

  @Override
  public void genBlockState(Consumer<BlockStateProvider> consumer) {
    dataGeneratorWrapper.blockStateAndModelRegisterers.add(consumer);
  }

  @Override
  public void genBlockLoot(Consumer<BMBlockLootTables> consumer) {
    dataGeneratorWrapper.blockLootTableRegisterers.add(consumer);
  }

  @Override
  public void genRecipe(BiConsumer<RecipeProvider, Consumer<IFinishedRecipe>> consumer) {
    dataGeneratorWrapper.recipeRegisterers.add(consumer);
  }

  @Override
  public void genBlockTag(Consumer<Function<INamedTag<Block>, Builder<Block>>> consumer) {
    dataGeneratorWrapper.blockTagRegisterers.add(consumer);
  }

  @Override
  public void genItemTag(Consumer<Function<INamedTag<Item>, Builder<Item>>> consumer) {
    dataGeneratorWrapper.itemTagRegisterers.add(consumer);
  }

  @Override
  public void genFluidTag(Consumer<Function<INamedTag<Fluid>, Builder<Fluid>>> consumer) {
    dataGeneratorWrapper.fluidTagRegisterers.add(consumer);
  }

  @Override
  public void genEntityTag(Consumer<Function<INamedTag<EntityType<?>>, Builder<EntityType<?>>>> consumer) {
    dataGeneratorWrapper.entityTypeTagRegisterers.add(consumer);
  }

  @Override
  public void genConstruct(String name, SimpleConstruct.Builder builder) {
    dataGeneratorWrapper.constructRegisterers.put(new ResourceLocation(ModLoadingContext.get().getActiveNamespace(), name), builder);
  }

  // Methods for adding criteria for recipe data gen

  /**
   * Creates a new {@link EnterBlockTrigger} for use with recipe unlock criteria.
   */
  public static EnterBlockTrigger.Instance enteredBlock(Block block) {
    return new EnterBlockTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, block, StatePropertiesPredicate.EMPTY);
  }

  /**
   * Creates a new {@link InventoryChangeTrigger} that checks for a player having a certain item.
   */
  public static InventoryChangeTrigger.Instance hasItem(IItemProvider item) {
    return hasItem(ItemPredicate.Builder.create().item(item).build());
  }

  /**
   * Creates a new {@link InventoryChangeTrigger} that checks for a player having an item within the given tag.
   */
  public static InventoryChangeTrigger.Instance hasItem(ITag<Item> tag) {
    return hasItem(ItemPredicate.Builder.create().tag(tag).build());
  }

  /**
   * Creates a new {@link InventoryChangeTrigger} that checks for a player having a certain item.
   */
  public static InventoryChangeTrigger.Instance hasItem(ItemPredicate... predicate) {
    return new InventoryChangeTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, predicate);
  }

  /**
   * Creates a new {@link ResourceLocation} that points to the a block's model location.
   */
  public static ResourceLocation blockLoc(RegistryObject<Block> block) {
    return block.lazyMap(it -> new ResourceLocation(block.getId().getNamespace(), "block/" + block.getId().getPath())).get();
  }

  /**
   * Generates an item model using the existing block model as a parent
   */
  public static Consumer<ItemModelProvider> itemModelFromBlock(String name, RegistryObject<Block> block) {
    return it -> it.withExistingParent(name, blockLoc(block));
  }

  public static <T> T withExplosionDecay(IItemProvider item, ILootFunctionConsumer<T> function) {
    return (T) (!LootHelpers.IMMUNE_TO_EXPLOSIONS.contains(item.asItem()) ? function.acceptFunction(ExplosionDecay.builder()) : function.cast());
  }

  public static <T> T withSurvivesExplosion(IItemProvider item, ILootConditionConsumer<T> condition) {
    return (T) (!LootHelpers.IMMUNE_TO_EXPLOSIONS.contains(item.asItem()) ? condition.acceptCondition(SurvivesExplosion.builder()) : condition.cast());
  }

  public static LootTable.Builder dropping(IItemProvider item) {
    return LootTable.builder().addLootPool(withSurvivesExplosion(item, LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(item))));
  }

  public static LootTable.Builder dropping(Block block, ILootCondition.IBuilder conditionBuilder, LootEntry.Builder<?> p_218494_2_) {
    return LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(block).acceptCondition(conditionBuilder).alternatively(p_218494_2_)));
  }

  public static LootTable.Builder droppingWithSilkTouch(Block block, LootEntry.Builder<?> builder) {
    return dropping(block, LootHelpers.SILK_TOUCH, builder);
  }

  public static LootTable.Builder droppingWithShears(Block block, LootEntry.Builder<?> noShearAlternativeEntry) {
    return dropping(block, LootHelpers.SHEARS, noShearAlternativeEntry);
  }

  public static LootTable.Builder droppingWithSilkTouchOrShears(Block block, LootEntry.Builder<?> alternativeLootEntry) {
    return dropping(block, LootHelpers.SILK_TOUCH_OR_SHEARS, alternativeLootEntry);
  }

  public static LootTable.Builder droppingWithSilkTouch(Block block, IItemProvider noSilkTouch) {
    return droppingWithSilkTouch(block, withSurvivesExplosion(block, ItemLootEntry.builder(noSilkTouch)));
  }

  public static LootTable.Builder droppingRandomly(IItemProvider item, IRandomRange range) {
    return LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(withExplosionDecay(item, ItemLootEntry.builder(item).acceptFunction(SetCount.builder(range)))));
  }

  public static LootTable.Builder droppingWithSilkTouchOrRandomly(Block block, IItemProvider item, IRandomRange range) {
    return droppingWithSilkTouch(block, withExplosionDecay(block, ItemLootEntry.builder(item).acceptFunction(SetCount.builder(range))));
  }

  public static LootTable.Builder onlyWithSilkTouch(IItemProvider item) {
    return LootTable.builder().addLootPool(LootPool.builder().acceptCondition(LootHelpers.SILK_TOUCH).rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(item)));
  }

  public static LootTable.Builder droppingAndFlowerPot(IItemProvider flower) {
    return LootTable.builder().addLootPool(withSurvivesExplosion(Blocks.FLOWER_POT, LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(Blocks.FLOWER_POT)))).addLootPool(withSurvivesExplosion(flower, LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(flower))));
  }

  public static LootTable.Builder droppingSlab(Block slab) {
    return LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(withExplosionDecay(slab, ItemLootEntry.builder(slab).acceptFunction(SetCount.builder(ConstantRange.of(2)).acceptCondition(BlockStateProperty.builder(slab).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withProp(SlabBlock.TYPE, SlabType.DOUBLE)))))));
  }

  public static <T extends Comparable<T> & IStringSerializable> LootTable.Builder droppingWhen(Block block, Property<T> property, T value) {
    return LootTable.builder().addLootPool(withSurvivesExplosion(block, LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(block).acceptCondition(BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withProp(property, value))))));
  }

  public static LootTable.Builder droppingWithName(Block block) {
    return LootTable.builder().addLootPool(withSurvivesExplosion(block, LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(block).acceptFunction(CopyName.builder(CopyName.Source.BLOCK_ENTITY)))));
  }

  public static LootTable.Builder droppingWithContents(Block shulker) {
    return LootTable.builder().addLootPool(withSurvivesExplosion(shulker, LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(shulker).acceptFunction(CopyName.builder(CopyName.Source.BLOCK_ENTITY)).acceptFunction(CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY).replaceOperation("Lock", "BlockEntityTag.Lock").replaceOperation("LootTable", "BlockEntityTag.LootTable").replaceOperation("LootTableSeed", "BlockEntityTag.LootTableSeed")).acceptFunction(SetContents.builderIn().addLootEntry(DynamicLootEntry.func_216162_a(ShulkerBoxBlock.CONTENTS))))));
  }

  public static LootTable.Builder droppingWithPatterns(Block banner) {
    return LootTable.builder().addLootPool(withSurvivesExplosion(banner, LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(banner).acceptFunction(CopyName.builder(CopyName.Source.BLOCK_ENTITY)).acceptFunction(CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY).replaceOperation("Patterns", "BlockEntityTag.Patterns")))));
  }

  public static LootTable.Builder droppingAndBees(Block nest) {
    return LootTable.builder().addLootPool(LootPool.builder().acceptCondition(LootHelpers.SILK_TOUCH).rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(nest).acceptFunction(CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY).replaceOperation("Bees", "BlockEntityTag.Bees")).acceptFunction(CopyBlockState.func_227545_a_(nest).func_227552_a_(BeehiveBlock.HONEY_LEVEL))));
  }

  public static LootTable.Builder droppingAndBeesWithAlternative(Block hive) {
    return LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(hive).acceptCondition(LootHelpers.SILK_TOUCH).acceptFunction(CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY).replaceOperation("Bees", "BlockEntityTag.Bees")).acceptFunction(CopyBlockState.func_227545_a_(hive).func_227552_a_(BeehiveBlock.HONEY_LEVEL)).alternatively(ItemLootEntry.builder(hive))));
  }

  public static LootTable.Builder droppingItemWithFortune(Block block, Item item) {
    return droppingWithSilkTouch(block, withExplosionDecay(block, ItemLootEntry.builder(item).acceptFunction(ApplyBonus.oreDrops(Enchantments.FORTUNE))));
  }

  /**
   * Creates a builder that drops the given IItemProvider in amounts between 0 and 2, most often 0. Only used in vanilla for huge mushroom blocks.
   */
  public static LootTable.Builder droppingItemRarely(Block block, IItemProvider item) {
    return droppingWithSilkTouch(block, withExplosionDecay(block, ItemLootEntry.builder(item).acceptFunction(SetCount.builder(RandomValueRange.of(-6.0F, 2.0F))).acceptFunction(LimitCount.func_215911_a(IntClamper.func_215848_a(0)))));
  }

  public static LootTable.Builder droppingSeeds(Block block) {
    return droppingWithShears(block, withExplosionDecay(block, ItemLootEntry.builder(Items.WHEAT_SEEDS).acceptCondition(RandomChance.builder(0.125F)).acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE, 2))));
  }

  /**
   * Creates a builder that drops the given IItemProvider in amounts between 0 and 3, based on the AGE property. Only used in vanilla for pumpkin and melon stems.
   */
  public static LootTable.Builder droppingByAge(Block stemFruit, Item item) {
    return LootTable.builder().addLootPool(withExplosionDecay(stemFruit, LootPool.builder().rolls(ConstantRange.of(1)).addEntry(
        ItemLootEntry.builder(item).acceptFunction(SetCount.builder(BinomialRange.of(3, 0.06666667F)).acceptCondition(BlockStateProperty.builder(stemFruit).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(StemBlock.AGE, 0)))).acceptFunction(SetCount.builder(BinomialRange.of(3, 0.13333334F)).acceptCondition(BlockStateProperty.builder(stemFruit).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(StemBlock.AGE, 1)))).acceptFunction(SetCount.builder(BinomialRange.of(3, 0.2F)).acceptCondition(BlockStateProperty.builder(stemFruit).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(StemBlock.AGE, 2)))).acceptFunction(SetCount.builder(BinomialRange.of(3, 0.26666668F)).acceptCondition(BlockStateProperty.builder(stemFruit).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(StemBlock.AGE, 3))))
            .acceptFunction(SetCount.builder(BinomialRange.of(3, 0.33333334F)).acceptCondition(BlockStateProperty.builder(stemFruit).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(StemBlock.AGE, 4)))).acceptFunction(SetCount.builder(BinomialRange.of(3, 0.4F)).acceptCondition(BlockStateProperty.builder(stemFruit).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(StemBlock.AGE, 5)))).acceptFunction(SetCount.builder(BinomialRange.of(3, 0.46666667F)).acceptCondition(BlockStateProperty.builder(stemFruit).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(StemBlock.AGE, 6)))).acceptFunction(SetCount.builder(BinomialRange.of(3, 0.53333336F)).acceptCondition(BlockStateProperty.builder(stemFruit).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(StemBlock.AGE, 7)))))));
  }

  public static LootTable.Builder dropSeedsForStem(Block stem, Item stemSeed) {
    return LootTable.builder().addLootPool(withExplosionDecay(stem, LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(stemSeed).acceptFunction(SetCount.builder(BinomialRange.of(3, 0.53333336F))))));
  }

  public static LootTable.Builder onlyWithShears(IItemProvider item) {
    return LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).acceptCondition(LootHelpers.SHEARS).addEntry(ItemLootEntry.builder(item)));
  }

  /**
   * Used for all leaves, drops self with silk touch, otherwise drops the second Block param with the passed chances for fortune levels, adding in sticks.
   */
  public static LootTable.Builder droppingWithChancesAndSticks(Block block, Block sapling, float... chances) {
    return droppingWithSilkTouchOrShears(block, withSurvivesExplosion(block, ItemLootEntry.builder(sapling)).acceptCondition(TableBonus.builder(Enchantments.FORTUNE, chances))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).acceptCondition(LootHelpers.NOT_SILK_TOUCH_OR_SHEARS).addEntry(withExplosionDecay(block, ItemLootEntry.builder(Items.STICK).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 2.0F)))).acceptCondition(TableBonus.builder(Enchantments.FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F))));
  }

  /**
   * Used for oak and dark oak, same as droppingWithChancesAndSticks but adding in apples.
   */
  public static LootTable.Builder droppingWithChancesSticksAndApples(Block block, Block sapling, float... chances) {
    return droppingWithChancesAndSticks(block, sapling, chances).addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).acceptCondition(LootHelpers.NOT_SILK_TOUCH_OR_SHEARS).addEntry(withSurvivesExplosion(block, ItemLootEntry.builder(Items.APPLE)).acceptCondition(TableBonus.builder(Enchantments.FORTUNE, 0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F))));
  }

  /**
   * Drops the first item parameter always, and the second item parameter plus more of the first when the loot condition is met, applying fortune to only the second argument.
   */
  public static LootTable.Builder droppingAndBonusWhen(Block block, Item itemConditional, Item withBonus, ILootCondition.IBuilder conditionBuilder) {
    return withExplosionDecay(block, LootTable.builder().addLootPool(LootPool.builder().addEntry(ItemLootEntry.builder(itemConditional).acceptCondition(conditionBuilder).alternatively(ItemLootEntry.builder(withBonus)))).addLootPool(LootPool.builder().acceptCondition(conditionBuilder).addEntry(ItemLootEntry.builder(withBonus).acceptFunction(ApplyBonus.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286F, 3)))));
  }

  public static LootTable.Builder droppingSheared(Block sheared) {
    return LootTable.builder().addLootPool(LootPool.builder().acceptCondition(LootHelpers.SHEARS).addEntry(ItemLootEntry.builder(sheared).acceptFunction(SetCount.builder(ConstantRange.of(2)))));
  }

  public static LootTable.Builder droppingSeedsTall(Block block, Block sheared) {
    LootEntry.Builder<?> builder = ItemLootEntry.builder(sheared).acceptFunction(SetCount.builder(ConstantRange.of(2))).acceptCondition(LootHelpers.SHEARS).alternatively(withSurvivesExplosion(block, ItemLootEntry.builder(Items.WHEAT_SEEDS)).acceptCondition(RandomChance.builder(0.125F)));
    return LootTable.builder().addLootPool(LootPool.builder().addEntry(builder).acceptCondition(BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withProp(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER))).acceptCondition(LocationCheck.func_241547_a_(LocationPredicate.Builder.builder().block(BlockPredicate.Builder.createBuilder().setBlock(block).setStatePredicate(StatePropertiesPredicate.Builder.newBuilder().withProp(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER).build()).build()), new BlockPos(0, 1, 0))))
        .addLootPool(LootPool.builder().addEntry(builder).acceptCondition(BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withProp(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER))).acceptCondition(LocationCheck.func_241547_a_(LocationPredicate.Builder.builder().block(BlockPredicate.Builder.createBuilder().setBlock(block).setStatePredicate(StatePropertiesPredicate.Builder.newBuilder().withProp(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER).build()).build()), new BlockPos(0, -1, 0))));
  }

  public static LootTable.Builder blockNoDrop() {
    return LootTable.builder();
  }

  @Override
  public void initialize(RegistryWrapper registryWrapper, DataGeneratorWrapper dataGeneratorWrapper) {
    this.registryWrapper = registryWrapper;
    this.dataGeneratorWrapper = dataGeneratorWrapper;
    initEverything();
    genData();
  }

  /**
   * Registers all the components of this part, from blocks to sensor types; anything with a forge registry should have a methods named "register*" that can be used to register it.
   * <p>
   * NOTE: Make sure that if you are using block, fluid or items in the initialization of other objects, to use {@link RegistryObject#lazyMap(Function)} to map the type rather than accidentally class loading something early.
   */
  protected abstract void initEverything();

  /**
   * Adds all the data generation that this part requires to be done. All supported data generation types can be seen in the methods named "gen*".
   */
  protected abstract void genData();
}
