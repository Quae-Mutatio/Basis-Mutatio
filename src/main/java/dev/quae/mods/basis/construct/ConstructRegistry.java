package dev.quae.mods.basis.construct;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.quae.mods.basis.construct.core.IConstruct;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConstructRegistry extends JsonReloadListener {

  private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
  private static final Logger LOGGER = LogManager.getLogger();

  public static final ConstructRegistry INSTANCE = new ConstructRegistry();

  private ConstructRegistry() {
    super(GSON, "constructs");
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> elements, IResourceManager manager, IProfiler profiler) {
    ConstructPart.CONSTRUCT_REGISTRY.clear();
    profiler.startSection(() -> "Parsing all constructs");
    for (Entry<ResourceLocation, JsonElement> entry : elements.entrySet()) {
      profiler.startSection(() -> String.format("Parsing the construct with the key '%s'", entry.getKey().toString()));
      final ResourceLocation key = entry.getKey();
      final JsonElement json = entry.getValue();
      final Function<JsonElement, DataResult<IConstruct>> parser = JsonOps.INSTANCE.withParser(IConstruct.CODEC);
      try {
        final Optional<IConstruct> result = parser.apply(json).result();
        if (result.isPresent()) {
          ConstructPart.CONSTRUCT_REGISTRY.put(key, result.get());
        } else {
          LOGGER.info("Failed to load JSON for {}", key);
        }
      } catch (Exception ex) {
        LOGGER.error("Exception occured while processing JSON for {}", key, ex);
      } finally {
        profiler.endSection();
      }
    }
    profiler.endSection();
  }


}
