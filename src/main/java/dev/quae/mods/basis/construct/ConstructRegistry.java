package dev.quae.mods.basis.construct;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.quae.mods.basis.BasisMutatio;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConstructRegistry extends JsonReloadListener {

  public static final ConstructRegistry INSTANCE = new ConstructRegistry();
  private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
  private static final Logger LOGGER = LogManager.getLogger();

  public static final IForgeRegistry<IConstruct> REGISTRY = new RegistryBuilder<IConstruct>()
      .setName(new ResourceLocation(BasisMutatio.ID, "constructs"))
      .setType(IConstruct.class)
      .setMaxID(Integer.MAX_VALUE - 1)
      .disableSaving()
      .disableSync()
      .create();

  private ConstructRegistry() {
    super(GSON, "constructs");
  }

  public static void init() {}

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> elements, IResourceManager manager, IProfiler profiler) {
    for (Entry<ResourceLocation, JsonElement> entry : elements.entrySet()) {
      final ResourceLocation key = entry.getKey();
      final JsonElement json = entry.getValue();
      final Function<JsonElement, DataResult<SimpleConstruct>> parser = JsonOps.INSTANCE.withParser(SimpleConstruct.CODEC);
      try {
        final Optional<SimpleConstruct> result = parser.apply(json).result();
        if (result.isPresent()) {
          REGISTRY.register(result.get().setRegistryName(key));
        } else {
          LOGGER.info("Failed to load JSON for {}", key);
        }
      } catch (Exception ex) {
        LOGGER.error("Exception occured while processing JSON for {}", key, ex);
      }
    }
  }
}
