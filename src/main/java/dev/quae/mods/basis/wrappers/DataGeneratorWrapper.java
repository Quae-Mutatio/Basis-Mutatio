package dev.quae.mods.basis.wrappers;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import dev.quae.mods.basis.construct.SimpleConstruct;
import dev.quae.mods.basis.data.BMBlockLootTables;
import dev.quae.mods.basis.data.ConstructProvider;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.EntityTypeTagsProvider;
import net.minecraft.data.FluidTagsProvider;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.TagsProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class DataGeneratorWrapper {

  private static final Logger LOGGER = LogManager.getLogger(DataGeneratorWrapper.class);

  private final String modId;
  public final Map<String, Set<Consumer<LanguageProvider>>> translations;
  public final Set<Consumer<ItemModelProvider>> itemModelRegisterers;
  public final Set<Consumer<BlockStateProvider>> blockStateAndModelRegisterers;
  public final Set<Consumer<BMBlockLootTables>> blockLootTableRegisterers;
  public final Set<BiConsumer<RecipeProvider, Consumer<IFinishedRecipe>>> recipeRegisterers;
  public final Set<Consumer<Function<INamedTag<Block>, TagsProvider.Builder<Block>>>> blockTagRegisterers;
  public final Set<Consumer<Function<INamedTag<Item>, TagsProvider.Builder<Item>>>> itemTagRegisterers;
  public final Set<Consumer<Function<INamedTag<Fluid>, TagsProvider.Builder<Fluid>>>> fluidTagRegisterers;
  public final Set<Consumer<Function<INamedTag<EntityType<?>>, TagsProvider.Builder<EntityType<?>>>>> entityTypeTagRegisterers;
  public final Map<ResourceLocation, SimpleConstruct.Builder> constructRegisterers;

  private DataGeneratorWrapper(final String modId) {
    this.modId = modId;

    // Client Assets
    translations = new HashMap<>();
    itemModelRegisterers = new HashSet<>();
    blockStateAndModelRegisterers = new HashSet<>();

    // Server Assets
    blockLootTableRegisterers = new HashSet<>();
    recipeRegisterers = new HashSet<>();
    blockTagRegisterers = new HashSet<>();
    itemTagRegisterers = new HashSet<>();
    fluidTagRegisterers = new HashSet<>();
    entityTypeTagRegisterers = new HashSet<>();
    constructRegisterers = new HashMap<>();
  }

  public void processData(final DataGenerator dataGenerator, final ExistingFileHelper existingFileHelper) {

    // Client assets
    for (Entry<String, Set<Consumer<LanguageProvider>>> entry : translations.entrySet()) {
      dataGenerator.addProvider(new LanguageProvider(dataGenerator, modId, entry.getKey()) {
        @Override
        protected void addTranslations() {
          for (Consumer<LanguageProvider> translation : entry.getValue()) {
            translation.accept(this);
          }
        }
      });
      entry.getValue().clear();
    }
    translations.clear();

    dataGenerator.addProvider(new BlockStateProvider(dataGenerator, modId, existingFileHelper) {
      @Override
      protected void registerStatesAndModels() {
        for (Consumer<BlockStateProvider> blockStateAndModelRegisterer : blockStateAndModelRegisterers) {
          blockStateAndModelRegisterer.accept(this);
        }
        blockStateAndModelRegisterers.clear();
      }
    });
    dataGenerator.addProvider(new ItemModelProvider(dataGenerator, modId, existingFileHelper) {
      @Override
      protected void registerModels() {
        for (Consumer<ItemModelProvider> itemModelRegisterer : itemModelRegisterers) {
          itemModelRegisterer.accept(this);
        }
        itemModelRegisterers.clear();
      }
    });

    // Server Data
    dataGenerator.addProvider(new LootTableProvider(dataGenerator) {
      @Override
      protected void validate(@NotNull Map<ResourceLocation, LootTable> map, @NotNull ValidationTracker validationtracker) {
        // NOOP plzno, this will break things... kthxbai
      }

      @NotNull
      @Override
      protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(
            Pair.of(() -> new BMBlockLootTables() {
              @Override
              protected void addTables() {
                for (Consumer<BMBlockLootTables> blockLootTablesRegisterer : blockLootTableRegisterers) {
                  blockLootTablesRegisterer.accept(this);
                }
                blockLootTableRegisterers.clear();
              }
            }, LootParameterSets.BLOCK)
        );
      }
    });
    dataGenerator.addProvider(new RecipeProvider(dataGenerator) {
      @Override
      protected void registerRecipes(@NotNull Consumer<IFinishedRecipe> recipeConsumer) {
        for (BiConsumer<RecipeProvider, Consumer<IFinishedRecipe>> recipeRegisterer : recipeRegisterers) {
          recipeRegisterer.accept(this, recipeConsumer);
        }
        recipeRegisterers.clear();
      }
    });
    final BlockTagsProvider blockTagsProvider;
    dataGenerator.addProvider(blockTagsProvider = new BlockTagsProvider(dataGenerator, modId, existingFileHelper) {
      @Override
      protected void registerTags() {
        for (Consumer<Function<INamedTag<Block>, Builder<Block>>> blockTagRegisterer : blockTagRegisterers) {
          blockTagRegisterer.accept(this::getOrCreateBuilder);
        }
        blockTagRegisterers.clear();
      }
    });
    dataGenerator.addProvider(new ItemTagsProvider(dataGenerator, blockTagsProvider, modId, existingFileHelper) {
      @Override
      protected void registerTags() {
        for (Consumer<Function<INamedTag<Item>, Builder<Item>>> itemTagRegisterer : itemTagRegisterers) {
          itemTagRegisterer.accept(this::getOrCreateBuilder);
        }
        itemTagRegisterers.clear();
      }
    });
    dataGenerator.addProvider(new FluidTagsProvider(dataGenerator, modId, existingFileHelper) {
      @Override
      protected void registerTags() {
        for (Consumer<Function<INamedTag<Fluid>, Builder<Fluid>>> fluidTagRegisterer : fluidTagRegisterers) {
          fluidTagRegisterer.accept(this::getOrCreateBuilder);
        }
        fluidTagRegisterers.clear();
      }
    });
    dataGenerator.addProvider(new EntityTypeTagsProvider(dataGenerator, modId, existingFileHelper) {
      @Override
      protected void registerTags() {
        for (Consumer<Function<INamedTag<EntityType<?>>, Builder<EntityType<?>>>> entityTypeTagRegisterer : entityTypeTagRegisterers) {
          entityTypeTagRegisterer.accept(this::getOrCreateBuilder);
        }
        entityTypeTagRegisterers.clear();
      }
    });

    dataGenerator.addProvider(new ConstructProvider(dataGenerator, modId) {
      @Override
      protected void registerConstructs(BiConsumer<ResourceLocation, SimpleConstruct.Builder> consumer) {
        constructRegisterers.forEach(consumer);
        constructRegisterers.clear();
      }
    });
  }

  public static DataGeneratorWrapper create(String modId) {
    return new DataGeneratorWrapper(modId);
  }
}
