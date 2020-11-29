package dev.quae.mods.basis.part;

import dev.quae.mods.basis.construct.IConstruct;
import dev.quae.mods.basis.construct.SimpleConstruct;
import dev.quae.mods.basis.data.BMBlockLootTables;
import dev.quae.mods.basis.wrappers.DataGeneratorWrapper;
import dev.quae.mods.basis.wrappers.RegistryWrapper;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.TagsProvider.Builder;
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
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tileentity.TileEntityType;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.IModBusEvent;
import net.minecraftforge.registries.DataSerializerEntry;

public interface IPart {

  <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> thing);

  <T extends Fluid> RegistryObject<T> registerFluid(String name, Supplier<T> thing);

  <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> thing);

  <T extends Effect> RegistryObject<T> registerPotion(String name, Supplier<T> thing);

  <T extends SoundEvent> RegistryObject<T> registerSoundEvent(String name, Supplier<T> thing);

  <T extends Potion> RegistryObject<T> registerPotionType(String name, Supplier<T> thing);

  <T extends Enchantment> RegistryObject<T> registerEnchantment(String name, Supplier<T> thing);

  <T extends EntityType<?>> RegistryObject<T> registerEntity(String name, Supplier<T> thing);

  <T extends TileEntityType<?>> RegistryObject<T> registerTileEntity(String name, Supplier<T> thing);

  <T extends ParticleType<?>> RegistryObject<T> registerParticleType(String name, Supplier<T> thing);

  <T extends ContainerType<?>> RegistryObject<T> registerContainer(String name, Supplier<T> thing);

  <T extends PaintingType> RegistryObject<T> registerPaintingType(String name, Supplier<T> thing);

  <T extends IRecipeSerializer<?>> RegistryObject<T> registerRecipeSerializer(String name, Supplier<T> thing);

  <T extends Attribute> RegistryObject<T> registerAttribute(String name, Supplier<T> thing);

  <T extends StatType<?>> RegistryObject<T> registerStatType(String name, Supplier<T> thing);

  <T extends VillagerProfession> RegistryObject<T> registerProfession(String name, Supplier<T> thing);

  <T extends PointOfInterestType> RegistryObject<T> registerPoiType(String name, Supplier<T> thing);

  <T extends MemoryModuleType<?>> RegistryObject<T> registerMemoryModuleType(String name, Supplier<T> thing);

  <T extends SensorType<?>> RegistryObject<T> registerSensorType(String name, Supplier<T> thing);

  <T extends Schedule> RegistryObject<T> registerSchedule(String name, Supplier<T> thing);

  <T extends Activity> RegistryObject<T> registerActivity(String name, Supplier<T> thing);

  <T extends WorldCarver<?>> RegistryObject<T> registerWorldCarver(String name, Supplier<T> thing);

  <T extends SurfaceBuilder<?>> RegistryObject<T> registerSurfaceBuilder(String name, Supplier<T> thing);

  <T extends Feature<?>> RegistryObject<T> registerFeature(String name, Supplier<T> thing);

  <T extends Placement<?>> RegistryObject<T> registerDecorator(String name, Supplier<T> thing);

  <T extends ChunkStatus> RegistryObject<T> registerChunkStatus(String name, Supplier<T> thing);

  <T extends Structure<?>> RegistryObject<T> registerStructureFeature(String name, Supplier<T> thing);

  <T extends BlockStateProviderType<?>> RegistryObject<T> registerBlockstateProviderType(String name, Supplier<T> thing);

  <T extends BlockPlacerType<?>> RegistryObject<T> registerBlockPlacerType(String name, Supplier<T> thing);

  <T extends FoliagePlacerType<?>> RegistryObject<T> registerFoliagePlacerType(String name, Supplier<T> thing);

  <T extends TreeDecoratorType<?>> RegistryObject<T> registerTreeDecoratorType(String name, Supplier<T> thing);

  <T extends DataSerializerEntry> RegistryObject<T> registerDataSerializerEntry(String name, Supplier<T> thing);

  <T extends GlobalLootModifierSerializer<?>> RegistryObject<T> registerGlobalLootTableModifierSerializer(String name, Supplier<T> thing);

  <T extends IConstruct.Type> RegistryObject<T> registerConstructPartType(String name, Supplier<T> type);

  default <T extends Event> void registerForgeEventHandler(Consumer<T> eventHandler) {
    MinecraftForge.EVENT_BUS.addListener(eventHandler);
  }

  <T extends Event & IModBusEvent> void registerModEventHandler(Consumer<T> eventHandler);

  void registerClientJob(Runnable job);

  void registerBlockColors(IBlockColor color, Supplier<Block>[] blocks);

  @SuppressWarnings("unchecked")
  default void registerBlockColors(IBlockColor color, Supplier<Block> block) {
    registerBlockColors(color, new Supplier[]{block});
  }

  void registerItemColors(IItemColor color, Supplier<Item>[] items);

  @SuppressWarnings("unchecked")
  default void registerItemColors(IItemColor color, Supplier<Item> item) {
    registerItemColors(color, new Supplier[]{item});
  }

  void genTranslation(Consumer<LanguageProvider> consumer);

  void genTranslation(String lang, Consumer<LanguageProvider> consumer);

  void genItemModel(Consumer<ItemModelProvider> consumer);

  void genBlockState(Consumer<BlockStateProvider> consumer);

  void genBlockLoot(Consumer<BMBlockLootTables> consumer);

  void genRecipe(BiConsumer<RecipeProvider, Consumer<IFinishedRecipe>> consumer);

  void genBlockTag(Consumer<Function<INamedTag<Block>, Builder<Block>>> consumer);

  void genItemTag(Consumer<Function<INamedTag<Item>, Builder<Item>>> consumer);

  void genFluidTag(Consumer<Function<INamedTag<Fluid>, Builder<Fluid>>> consumer);

  void genEntityTag(Consumer<Function<INamedTag<EntityType<?>>, Builder<EntityType<?>>>> consumer);

  void genConstruct(String name, SimpleConstruct.Builder builder);

  void initialize(RegistryWrapper registryWrapper, DataGeneratorWrapper dataGeneratorWrapper);
}
