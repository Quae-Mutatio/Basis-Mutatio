package dev.quae.mods.basis;

import dev.quae.mods.basis.part.PartManager;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BasisMutatio.ID)
public final class BasisMutatio {

  private static final Logger LOGGER = LogManager.getLogger();
  public static final String ID = "basismutatio";

  public BasisMutatio() {
    PartManager.registerParts();
  }
}
