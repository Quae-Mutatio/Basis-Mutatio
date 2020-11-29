package dev.quae.mods.basis.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.quae.mods.basis.construct.SimpleConstruct;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.annotation.ParametersAreNonnullByDefault;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class ConstructProvider implements IDataProvider {

  private static final Logger LOGGER = LogManager.getLogger();
  private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

  protected final DataGenerator generator;
  private final String modid;

  public ConstructProvider(DataGenerator generator, String modid) {
    this.generator = generator;
    this.modid = modid;
  }

  @Override
  public void act(DirectoryCache cache) throws IOException {
    final Path rootPath = this.generator.getOutputFolder();
    final Function<SimpleConstruct, DataResult<JsonElement>> encoder = JsonOps.INSTANCE.withEncoder(SimpleConstruct.CODEC);
    BiConsumer<ResourceLocation, SimpleConstruct.Builder> consumer = (constructLocation, builder) -> {
      final Path path = getPath(rootPath, constructLocation);
      try {
        final DataResult<JsonElement> apply = encoder.apply(builder.build());
        Optional<JsonElement> optional = apply.result();
        if (optional.isPresent()) {
          IDataProvider.save(GSON, cache, optional.get(), path);
        } else {
          LOGGER.error("Couldn't serialize construct {}", (Object) path);
          LOGGER.error(apply.error());
        }
      } catch (IOException ex) {
        LOGGER.error("Couldn't save construct {}", path, ex);
      }
    };
    this.registerConstructs(consumer);
  }

  protected abstract void registerConstructs(BiConsumer<ResourceLocation, SimpleConstruct.Builder> consumer);

  private static Path getPath(Path path, ResourceLocation constructLocation) {
    return path.resolve("data/" + constructLocation.getNamespace() + "/constructs/" + constructLocation.getPath() + ".json");
  }

  @Override
  public String getName() {
    return "Construct Provider [" + modid + "]";
  }
}
