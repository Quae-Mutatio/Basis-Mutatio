package dev.quae.mods.basis.part;

import dev.quae.mods.basis.wrappers.DataGeneratorWrapper;
import dev.quae.mods.basis.wrappers.RegistryWrapper;
import java.lang.annotation.ElementType;
import java.util.HashMap;
import java.util.Map;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;

public final class PartManager {

  private static final Logger LOGGER = LogManager.getLogger(PartManager.class);

  private static final Type PART = Type.getType(Part.class);
  private static final Map<String, RegistryWrapper> REGISTRY_WRAPPERS = new HashMap<>();
  private static final Map<String, DataGeneratorWrapper> DATA_GENERATOR_WRAPPERS = new HashMap<>();

  private PartManager() {
  }

  public static void registerParts() {
    Map<Type, Class<?>> classCache = new HashMap<>();
    for (ModFileScanData datum : ModList.get().getAllScanData()) {
      for (AnnotationData annotation : datum.getAnnotations()) {
        if (PART.equals(annotation.getAnnotationType()) && annotation.getTargetType() == ElementType.TYPE) {
          registerPart(annotation, classCache);
        }
      }
    }
  }

  private static void registerPart(AnnotationData annotation, Map<Type, Class<?>> classCache) {
    final String modid = (String) annotation.getAnnotationData().get("value");
    final RegistryWrapper registryWrapper = getOrCreateRegistryWrapper(modid);
    final DataGeneratorWrapper dataGeneratorWrapper = getOrCreateDataGeneratorWrapper(modid);
    final Type classType = annotation.getClassType();
    Class<?> cls;
    if (classCache.containsKey(classType)) {
      LOGGER.debug("Using class cache to get the class for {}", classType.getClassName());
      cls = classCache.get(classType);
    } else {
      try {
        LOGGER.debug("Using the classloader and class name to get the class for {}", classType.getClassName());
        cls = Class.forName(classType.getClassName(), false, PartManager.class.getClassLoader());
        classCache.put(classType, cls);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(String.format("Error loading class with name %s while registering it's part.", classType.getClassName()), e);
      }
    }
    if (!IPart.class.isAssignableFrom(cls)) {
      LOGGER.warn("{} is not extending BasePart and as such is being skipped by the auto-register", cls.getSimpleName());
      return;
    }
    try {
      LOGGER.debug("Creating and initializing {}", cls.getSimpleName());
      final IPart part = (IPart) cls.newInstance();
      part.initialize(registryWrapper, dataGeneratorWrapper);
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(String.format("Had a problem creating a %s", cls.getSimpleName()), e);
    }
  }

  private static DataGeneratorWrapper getOrCreateDataGeneratorWrapper(String modId) {
    return DATA_GENERATOR_WRAPPERS.computeIfAbsent(modId, it -> {
      final DataGeneratorWrapper wrapper = DataGeneratorWrapper.create(it);
      final IEventBus eventBus = ModList.get().getModContainerById(it)
          .filter(FMLModContainer.class::isInstance)
          .map(FMLModContainer.class::cast)
          .orElseThrow(() -> new RuntimeException(String.format("Failed to create and register the DataGeneratorWrapper for %s because it does not exist or is not an FML Mod", it))).getEventBus();
      eventBus.addListener((GatherDataEvent e) -> wrapper.processData(e.getGenerator(), e.getExistingFileHelper()));
      return wrapper;
    });
  }

  private static RegistryWrapper getOrCreateRegistryWrapper(String modId) {
    return REGISTRY_WRAPPERS.computeIfAbsent(modId, RegistryWrapper::create);
  }
}
