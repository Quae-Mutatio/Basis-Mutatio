package dev.quae.mods.basis.wrappers;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
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
import net.minecraft.data.loot.BlockLootTables;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class DataGeneratorWrapper {

  private final String modId;
  public final Set<Consumer<LanguageProvider>> en_usTranslations;
  public final Set<Consumer<ItemModelProvider>> itemModelRegisterers;
  public final Set<Consumer<BlockStateProvider>> blockStateAndModelRegisterers;
  public final Set<Consumer<BlockLootTables>> blockLootTableRegisterers;
  public final Set<BiConsumer<RecipeProvider, Consumer<IFinishedRecipe>>> recipeRegisterers;
  public final Set<Consumer<Function<INamedTag<Block>, TagsProvider.Builder<Block>>>> blockTagRegisterers;
  public final Set<Consumer<Function<INamedTag<Item>, TagsProvider.Builder<Item>>>> itemTagRegisterers;
  public final Set<Consumer<Function<INamedTag<Fluid>, TagsProvider.Builder<Fluid>>>> fluidTagRegisterers;
  public final Set<Consumer<Function<INamedTag<EntityType<?>>, TagsProvider.Builder<EntityType<?>>>>> entityTypeTagRegisterers;

  private DataGeneratorWrapper(final String modId) {
    this.modId = modId;

    // Client Assets
    en_usTranslations = new HashSet<>();
    itemModelRegisterers = new HashSet<>();
    blockStateAndModelRegisterers = new HashSet<>();

    // Server Assets
    blockLootTableRegisterers = new HashSet<>();
    recipeRegisterers = new HashSet<>();
    blockTagRegisterers = new HashSet<>();
    itemTagRegisterers = new HashSet<>();
    fluidTagRegisterers = new HashSet<>();
    entityTypeTagRegisterers = new HashSet<>();
  }

  public void processData(final DataGenerator dataGenerator, final ExistingFileHelper existingFileHelper) {

    // Client assets
    dataGenerator.addProvider(new LanguageProvider(dataGenerator, modId, "en_us") {
      @Override
      protected void addTranslations() {
        for (Consumer<LanguageProvider> en_usTranslation : en_usTranslations) {
          en_usTranslation.accept(this);
        }
        en_usTranslations.clear();
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
    dataGenerator.addProvider(new BlockStateProvider(dataGenerator, modId, existingFileHelper) {
      @Override
      protected void registerStatesAndModels() {
        for (Consumer<BlockStateProvider> blockStateAndModelRegisterer : blockStateAndModelRegisterers) {
          blockStateAndModelRegisterer.accept(this);
        }
        blockStateAndModelRegisterers.clear();
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
            Pair.of(() -> new BlockLootTables() {
              @Override
              protected void addTables() {
                for (Consumer<BlockLootTables> blockLootTablesRegisterer : blockLootTableRegisterers) {
                  blockLootTablesRegisterer.accept(this);
                }
                blockLootTableRegisterers.clear();
              }

              @NotNull
              @Override
              protected Iterable<Block> getKnownBlocks() {
                return ForgeRegistries.BLOCKS.getValues().stream().filter(it -> Objects.requireNonNull(it.getRegistryName()).getNamespace().equals(modId)).collect(Collectors.toSet());
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
  }

  public static DataGeneratorWrapper create(String modId) {
    return new DataGeneratorWrapper(modId);
  }
}
