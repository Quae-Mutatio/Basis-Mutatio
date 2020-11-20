package dev.quae.mods.basis.wrappers;

import net.minecraft.block.Block;
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
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
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

  private RegistryWrapper(final String modId) {
    final IEventBus modEventBus = ModList.get()
        .getModContainerById(modId)
        .filter(FMLModContainer.class::isInstance)
        .map(FMLModContainer.class::cast)
        .orElseThrow(() -> new RuntimeException(String.format("Failed to create a registry wrapper for %s, because it either doesn't exist or is not an FML mod.", modId)))
        .getEventBus();
    (this.blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, modId)).register(modEventBus);
    (this.fluids = DeferredRegister.create(ForgeRegistries.FLUIDS, modId)).register(modEventBus);
    (this.items = DeferredRegister.create(ForgeRegistries.ITEMS, modId)).register(modEventBus);
    (this.potions = DeferredRegister.create(ForgeRegistries.POTIONS, modId)).register(modEventBus);
    (this.sound_events = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, modId)).register(modEventBus);
    (this.potion_types = DeferredRegister.create(ForgeRegistries.POTION_TYPES, modId)).register(modEventBus);
    (this.enchantments = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, modId)).register(modEventBus);
    (this.entities = DeferredRegister.create(ForgeRegistries.ENTITIES, modId)).register(modEventBus);
    (this.tile_entities = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, modId)).register(modEventBus);
    (this.particleTypes = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, modId)).register(modEventBus);
    (this.containers = DeferredRegister.create(ForgeRegistries.CONTAINERS, modId)).register(modEventBus);
    (this.paintingTypes = DeferredRegister.create(ForgeRegistries.PAINTING_TYPES, modId)).register(modEventBus);
    (this.recipeSerializers = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, modId)).register(modEventBus);
    (this.attributes = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, modId)).register(modEventBus);
    (this.statTypes = DeferredRegister.create(ForgeRegistries.STAT_TYPES, modId)).register(modEventBus);
    (this.professions = DeferredRegister.create(ForgeRegistries.PROFESSIONS, modId)).register(modEventBus);
    (this.poiTypes = DeferredRegister.create(ForgeRegistries.POI_TYPES, modId)).register(modEventBus);
    (this.memoryModuleTypes = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, modId)).register(modEventBus);
    (this.sensorTypes = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, modId)).register(modEventBus);
    (this.schedules = DeferredRegister.create(ForgeRegistries.SCHEDULES, modId)).register(modEventBus);
    (this.activities = DeferredRegister.create(ForgeRegistries.ACTIVITIES, modId)).register(modEventBus);
    (this.worldCarvers = DeferredRegister.create(ForgeRegistries.WORLD_CARVERS, modId)).register(modEventBus);
    (this.surfaceBuilders = DeferredRegister.create(ForgeRegistries.SURFACE_BUILDERS, modId)).register(modEventBus);
    (this.features = DeferredRegister.create(ForgeRegistries.FEATURES, modId)).register(modEventBus);
    (this.decorators = DeferredRegister.create(ForgeRegistries.DECORATORS, modId)).register(modEventBus);
    (this.chunkStatus = DeferredRegister.create(ForgeRegistries.CHUNK_STATUS, modId)).register(modEventBus);
    (this.structureFeatures = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, modId)).register(modEventBus);
    (this.blockstateProviderTypes = DeferredRegister.create(ForgeRegistries.BLOCK_STATE_PROVIDER_TYPES, modId)).register(modEventBus);
    (this.blockPlacerTypes = DeferredRegister.create(ForgeRegistries.BLOCK_PLACER_TYPES, modId)).register(modEventBus);
    (this.foliagePlacerTypes = DeferredRegister.create(ForgeRegistries.FOLIAGE_PLACER_TYPES, modId)).register(modEventBus);
    (this.treeDecoratorTypes = DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, modId)).register(modEventBus);
    (this.dataSerializers = DeferredRegister.create(ForgeRegistries.DATA_SERIALIZERS, modId)).register(modEventBus);
    (this.lootModifierSerializers = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, modId)).register(modEventBus);
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

  public static RegistryWrapper create(String modId) {
    return new RegistryWrapper(modId);
  }
}
