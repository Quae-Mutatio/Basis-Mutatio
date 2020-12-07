package dev.quae.mods.basis.wrappers;

import dev.quae.mods.basis.construct.type.IConstructType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
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
import net.minecraft.tileentity.TileEntityType;
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
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class RegistryWrapper {

  private final DeferredRegister<Block> blocks;
  private final DeferredRegister<Fluid> fluids;
  private final DeferredRegister<Item> items;
  private final DeferredRegister<Effect> potions;
  private final DeferredRegister<SoundEvent> sound_events;
  private final DeferredRegister<Potion> potion_types;
  private final DeferredRegister<Enchantment> enchantments;
  private final DeferredRegister<EntityType<?>> entities;
  private final DeferredRegister<TileEntityType<?>> tile_entities;
  private final DeferredRegister<ParticleType<?>> particleTypes;
  private final DeferredRegister<ContainerType<?>> containers;
  private final DeferredRegister<PaintingType> paintingTypes;
  private final DeferredRegister<IRecipeSerializer<?>> recipeSerializers;
  private final DeferredRegister<Attribute> attributes;
  private final DeferredRegister<StatType<?>> statTypes;
  private final DeferredRegister<VillagerProfession> professions;
  private final DeferredRegister<PointOfInterestType> poiTypes;
  private final DeferredRegister<MemoryModuleType<?>> memoryModuleTypes;
  private final DeferredRegister<SensorType<?>> sensorTypes;
  private final DeferredRegister<Schedule> schedules;
  private final DeferredRegister<Activity> activities;
  private final DeferredRegister<WorldCarver<?>> worldCarvers;
  private final DeferredRegister<SurfaceBuilder<?>> surfaceBuilders;
  private final DeferredRegister<Feature<?>> features;
  private final DeferredRegister<Placement<?>> decorators;
  private final DeferredRegister<ChunkStatus> chunkStatus;
  private final DeferredRegister<Structure<?>> structureFeatures;
  private final DeferredRegister<BlockStateProviderType<?>> blockstateProviderTypes;
  private final DeferredRegister<BlockPlacerType<?>> blockPlacerTypes;
  private final DeferredRegister<FoliagePlacerType<?>> foliagePlacerTypes;
  private final DeferredRegister<TreeDecoratorType<?>> treeDecoratorTypes;
  private final DeferredRegister<DataSerializerEntry> dataSerializers;
  private final DeferredRegister<GlobalLootModifierSerializer<?>> lootModifierSerializers;
  private final DeferredRegister<IConstructType> constructPartTypes;
  private final Set<Runnable> clientSetups;
  private final Map<IBlockColor, Supplier<Block>[]> blockColors;
  private final Map<IItemColor, Supplier<Item>[]> itemColors;
  private final IEventBus bus;

  private RegistryWrapper(final String modId) {
    this.bus = ModList.get().getModContainerById(modId)
        .filter(FMLModContainer.class::isInstance)
        .map(FMLModContainer.class::cast)
        .map(FMLModContainer::getEventBus)
        .orElseThrow(() -> new RuntimeException(String.format("Failed to create a registry wrapper for %s, because it either doesn't exist or is not an FML mod.", modId)));
    (this.blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, modId)).register(bus);
    (this.fluids = DeferredRegister.create(ForgeRegistries.FLUIDS, modId)).register(bus);
    (this.items = DeferredRegister.create(ForgeRegistries.ITEMS, modId)).register(bus);
    (this.potions = DeferredRegister.create(ForgeRegistries.POTIONS, modId)).register(bus);
    (this.sound_events = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, modId)).register(bus);
    (this.potion_types = DeferredRegister.create(ForgeRegistries.POTION_TYPES, modId)).register(bus);
    (this.enchantments = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, modId)).register(bus);
    (this.entities = DeferredRegister.create(ForgeRegistries.ENTITIES, modId)).register(bus);
    (this.tile_entities = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, modId)).register(bus);
    (this.particleTypes = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, modId)).register(bus);
    (this.containers = DeferredRegister.create(ForgeRegistries.CONTAINERS, modId)).register(bus);
    (this.paintingTypes = DeferredRegister.create(ForgeRegistries.PAINTING_TYPES, modId)).register(bus);
    (this.recipeSerializers = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, modId)).register(bus);
    (this.attributes = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, modId)).register(bus);
    (this.statTypes = DeferredRegister.create(ForgeRegistries.STAT_TYPES, modId)).register(bus);
    (this.professions = DeferredRegister.create(ForgeRegistries.PROFESSIONS, modId)).register(bus);
    (this.poiTypes = DeferredRegister.create(ForgeRegistries.POI_TYPES, modId)).register(bus);
    (this.memoryModuleTypes = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, modId)).register(bus);
    (this.sensorTypes = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, modId)).register(bus);
    (this.schedules = DeferredRegister.create(ForgeRegistries.SCHEDULES, modId)).register(bus);
    (this.activities = DeferredRegister.create(ForgeRegistries.ACTIVITIES, modId)).register(bus);
    (this.worldCarvers = DeferredRegister.create(ForgeRegistries.WORLD_CARVERS, modId)).register(bus);
    (this.surfaceBuilders = DeferredRegister.create(ForgeRegistries.SURFACE_BUILDERS, modId)).register(bus);
    (this.features = DeferredRegister.create(ForgeRegistries.FEATURES, modId)).register(bus);
    (this.decorators = DeferredRegister.create(ForgeRegistries.DECORATORS, modId)).register(bus);
    (this.chunkStatus = DeferredRegister.create(ForgeRegistries.CHUNK_STATUS, modId)).register(bus);
    (this.structureFeatures = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, modId)).register(bus);
    (this.blockstateProviderTypes = DeferredRegister.create(ForgeRegistries.BLOCK_STATE_PROVIDER_TYPES, modId)).register(bus);
    (this.blockPlacerTypes = DeferredRegister.create(ForgeRegistries.BLOCK_PLACER_TYPES, modId)).register(bus);
    (this.foliagePlacerTypes = DeferredRegister.create(ForgeRegistries.FOLIAGE_PLACER_TYPES, modId)).register(bus);
    (this.treeDecoratorTypes = DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, modId)).register(bus);
    (this.dataSerializers = DeferredRegister.create(ForgeRegistries.DATA_SERIALIZERS, modId)).register(bus);
    (this.lootModifierSerializers = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, modId)).register(bus);
    (this.constructPartTypes = DeferredRegister.create(IConstructType.class, modId)).register(bus);
    this.clientSetups = new HashSet<>();
    this.blockColors = new HashMap<>();
    this.itemColors = new HashMap<>();
    bus.addListener((FMLClientSetupEvent event) -> this.clientSetups.forEach(event::enqueueWork));
    bus.addListener((ColorHandlerEvent.Block event) -> this.blockColors.forEach((color, suppliers) -> event.getBlockColors().register(color, Arrays.stream(suppliers).map(Supplier::get).toArray(Block[]::new))));
    bus.addListener((ColorHandlerEvent.Item event) -> this.itemColors.forEach((color, suppliers) -> event.getItemColors().register(color, Arrays.stream(suppliers).map(Supplier::get).toArray(Item[]::new))));
  }

  public DeferredRegister<Block> getBlocks() {
    return blocks;
  }

  public DeferredRegister<Fluid> getFluids() {
    return fluids;
  }

  public DeferredRegister<Item> getItems() {
    return items;
  }

  public DeferredRegister<Effect> getPotions() {
    return potions;
  }

  public DeferredRegister<SoundEvent> getSoundEvents() {
    return sound_events;
  }

  public DeferredRegister<Potion> getPotionTypes() {
    return potion_types;
  }

  public DeferredRegister<Enchantment> getEnchantments() {
    return enchantments;
  }

  public DeferredRegister<EntityType<?>> getEntities() {
    return entities;
  }

  public DeferredRegister<TileEntityType<?>> getTileEntities() {
    return tile_entities;
  }

  public DeferredRegister<ParticleType<?>> getParticleTypes() {
    return particleTypes;
  }

  public DeferredRegister<ContainerType<?>> getContainers() {
    return containers;
  }

  public DeferredRegister<PaintingType> getPaintingTypes() {
    return paintingTypes;
  }

  public DeferredRegister<IRecipeSerializer<?>> getRecipeSerializers() {
    return recipeSerializers;
  }

  public DeferredRegister<Attribute> getAttributes() {
    return attributes;
  }

  public DeferredRegister<StatType<?>> getStatTypes() {
    return statTypes;
  }

  public DeferredRegister<VillagerProfession> getProfessions() {
    return professions;
  }

  public DeferredRegister<PointOfInterestType> getPoiTypes() {
    return poiTypes;
  }

  public DeferredRegister<MemoryModuleType<?>> getMemoryModuleTypes() {
    return memoryModuleTypes;
  }

  public DeferredRegister<SensorType<?>> getSensorTypes() {
    return sensorTypes;
  }

  public DeferredRegister<Schedule> getSchedules() {
    return schedules;
  }

  public DeferredRegister<Activity> getActivities() {
    return activities;
  }

  public DeferredRegister<WorldCarver<?>> getWorldCarvers() {
    return worldCarvers;
  }

  public DeferredRegister<SurfaceBuilder<?>> getSurfaceBuilders() {
    return surfaceBuilders;
  }

  public DeferredRegister<Feature<?>> getFeatures() {
    return features;
  }

  public DeferredRegister<Placement<?>> getDecorators() {
    return decorators;
  }

  public DeferredRegister<ChunkStatus> getChunkStatus() {
    return chunkStatus;
  }

  public DeferredRegister<Structure<?>> getStructureFeatures() {
    return structureFeatures;
  }

  public DeferredRegister<BlockStateProviderType<?>> getBlockstateProviderTypes() {
    return blockstateProviderTypes;
  }

  public DeferredRegister<BlockPlacerType<?>> getBlockPlacerTypes() {
    return blockPlacerTypes;
  }

  public DeferredRegister<FoliagePlacerType<?>> getFoliagePlacerTypes() {
    return foliagePlacerTypes;
  }

  public DeferredRegister<TreeDecoratorType<?>> getTreeDecoratorTypes() {
    return treeDecoratorTypes;
  }

  public DeferredRegister<DataSerializerEntry> getDataSerializers() {
    return dataSerializers;
  }

  public DeferredRegister<GlobalLootModifierSerializer<?>> getLootModifierSerializers() {
    return lootModifierSerializers;
  }

  public DeferredRegister<IConstructType> getConstructPartTypes() {
    return constructPartTypes;
  }

  public Set<Runnable> getClientJobList() {
    return clientSetups;
  }

  public Map<IBlockColor, Supplier<Block>[]> getBlockColorMap() {
    return blockColors;
  }

  public Map<IItemColor, Supplier<Item>[]> getItemColorMap() {
    return itemColors;
  }

  public static RegistryWrapper create(String modId) {
    return new RegistryWrapper(modId);
  }

  public IEventBus getBus() {
    return bus;
  }
}
