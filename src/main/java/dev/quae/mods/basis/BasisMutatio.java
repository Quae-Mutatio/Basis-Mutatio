package dev.quae.mods.basis;

import dev.quae.mods.basis.construct.ConstructRegistry;
import dev.quae.mods.basis.construct.IConstruct.Type;
import dev.quae.mods.basis.part.PartManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BasisMutatio.ID)
public final class BasisMutatio {

  private static final Logger LOGGER = LogManager.getLogger();
  public static final String ID = "basismutatio";

  public BasisMutatio() {
    PartManager.registerParts();
    Type.TYPE_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    MinecraftForge.EVENT_BUS.addListener(this::addReloadListener);
  }

  private void newRegistry(final RegistryEvent.NewRegistry event) {
    ConstructRegistry.init();
  }

  private void addReloadListener(final AddReloadListenerEvent event) {
    event.addListener(ConstructRegistry.INSTANCE);
  }
}
