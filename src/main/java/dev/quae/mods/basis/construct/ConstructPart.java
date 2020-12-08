package dev.quae.mods.basis.construct;

import dev.quae.mods.basis.BasisMutatio;
import dev.quae.mods.basis.construct.core.IConstruct;
import dev.quae.mods.basis.construct.form.ConstructForm;
import dev.quae.mods.basis.construct.form.IConstructForm;
import dev.quae.mods.basis.construct.type.IConstructType;
import dev.quae.mods.basis.part.BasePart;
import dev.quae.mods.basis.part.Part;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent.NewRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Part(BasisMutatio.ID)
public class ConstructPart extends BasePart {

  private static final DeferredRegister<IConstructForm> CONSTRUCT_FORM_DEFERRED_REGISTER = DeferredRegister.create(IConstructForm.class, BasisMutatio.ID);
  private static final DeferredRegister<IConstructType> CONSTRUCT_TYPE_DEFERRED_REGISTER = DeferredRegister.create(IConstructType.class, BasisMutatio.ID);
  public static Supplier<IForgeRegistry<IConstructForm>> CONSTRUCT_FORMS;
  public static Supplier<IForgeRegistry<IConstructType>> CONSTRUCT_TYPES;
  public static final Map<ResourceLocation, IConstruct> CONSTRUCT_REGISTRY = new HashMap<>();

  @Override
  protected void initEverything() {
    registerForgeEventHandler((AddReloadListenerEvent event) -> event.addListener(ConstructRegistry.INSTANCE));
    CONSTRUCT_FORMS = CONSTRUCT_FORM_DEFERRED_REGISTER.makeRegistry("construct_forms", RegistryBuilder::new);
    CONSTRUCT_TYPES = CONSTRUCT_TYPE_DEFERRED_REGISTER.makeRegistry("construct_types", RegistryBuilder::new);
    registerDeferredRegisters(CONSTRUCT_TYPE_DEFERRED_REGISTER, CONSTRUCT_FORM_DEFERRED_REGISTER);
    CONSTRUCT_FORM_DEFERRED_REGISTER.register("shaped", () -> ConstructForm.SHAPED);
    CONSTRUCT_FORM_DEFERRED_REGISTER.register("shapeless", () -> ConstructForm.SHAPELESS);
    // No default types yet
  }

  @Override
  protected void genData() {

  }
}
