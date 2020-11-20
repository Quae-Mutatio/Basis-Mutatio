package dev.quae.mods.basis.part;

import dev.quae.mods.basis.wrappers.DataGeneratorWrapper;
import dev.quae.mods.basis.wrappers.RegistryWrapper;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.advancements.criterion.EnterBlockTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.TagsProvider.Builder;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantment;
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
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatType;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
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
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DataSerializerEntry;

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

  public void genTranslation(Consumer<LanguageProvider> consumer) {
    dataGeneratorWrapper.en_usTranslations.add(consumer);
  }

  public void genItemModel(Consumer<ItemModelProvider> consumer) {
    dataGeneratorWrapper.itemModelRegisterers.add(consumer);
  }

  public void genBlockState(Consumer<BlockStateProvider> consumer) {
    dataGeneratorWrapper.blockStateAndModelRegisterers.add(consumer);
  }

  public void genBlockLoot(Consumer<BlockLootTables> consumer) {
    dataGeneratorWrapper.blockLootTableRegisterers.add(consumer);
  }

  public void genRecipe(BiConsumer<RecipeProvider, Consumer<IFinishedRecipe>> consumer) {
    dataGeneratorWrapper.recipeRegisterers.add(consumer);
  }

  public void genBlockTag(Consumer<Function<INamedTag<Block>, Builder<Block>>> consumer) {
    dataGeneratorWrapper.blockTagRegisterers.add(consumer);
  }

  public void genItemTag(Consumer<Function<INamedTag<Item>, Builder<Item>>> consumer) {
    dataGeneratorWrapper.itemTagRegisterers.add(consumer);
  }

  public void genFluidTag(Consumer<Function<INamedTag<Fluid>, Builder<Fluid>>> consumer) {
    dataGeneratorWrapper.fluidTagRegisterers.add(consumer);
  }

  public void genEntityTag(Consumer<Function<INamedTag<EntityType<?>>, Builder<EntityType<?>>>> consumer) {
    dataGeneratorWrapper.entityTypeTagRegisterers.add(consumer);
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
   *
   */
  public static Consumer<ItemModelProvider> itemModelFromBlock(String name, RegistryObject<Block> block) {
    return it -> it.withExistingParent(name, blockLoc(block));
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
