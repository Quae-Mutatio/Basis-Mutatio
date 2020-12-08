package dev.quae.mods.basis.data;

import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.annotation.ParametersAreNonnullByDefault;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BMBlockLootTables extends BlockLootTables {

  private static final Logger LOGGER = LogManager.getLogger(BMBlockLootTables.class);

  private final Map<ResourceLocation, Builder> lootTables = Maps.newHashMap();

  @Override
  public void accept(BiConsumer<ResourceLocation, Builder> consumer) {
    this.lootTables.forEach(consumer);
  }

  @Override
  protected Iterable<Block> getKnownBlocks() {
    return Collections.emptyList();
  }

  public void registerLootTable(RegistryObject<Block> block, Function<Block, Builder> factory) {
    registerLootTable(block, block.lazyMap(factory).get());
  }

  public void registerLootTable(RegistryObject<Block> block, Builder table) {
    lootTables.put(block.get().getLootTable(), table);
  }
}
