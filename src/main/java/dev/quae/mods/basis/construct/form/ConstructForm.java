package dev.quae.mods.basis.construct.form;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.quae.mods.basis.construct.ConstructPart;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * The form of a construct, whether that be {@link ConstructForm#SHAPED shaped} or {@link ConstructForm#SHAPELESS shapeless} other in the future.
 */
public class ConstructForm extends ForgeRegistryEntry<IConstructForm> implements IConstructForm {
  public static final Codec<IConstructForm> CODEC = ResourceLocation.CODEC.comapFlatMap(name -> DataResult.success(ConstructPart.CONSTRUCT_FORMS.get().getValue(name)), IForgeRegistryEntry::getRegistryName);

  public static final IConstructForm SHAPED = new ConstructForm();
  public static final IConstructForm SHAPELESS = new ConstructForm();
}
