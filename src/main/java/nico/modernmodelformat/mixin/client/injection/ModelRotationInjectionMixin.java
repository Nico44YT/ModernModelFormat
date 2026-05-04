package nico.modernmodelformat.mixin.client.injection;

import net.minecraft.client.render.model.json.ModelRotation;
import nico.modernmodelformat.format.ModernRotationContainer;
import nico.modernmodelformat.format.ModernRotationWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ModelRotation.class)
public abstract class ModelRotationInjectionMixin implements ModernRotationWrapper {

    @Unique
    ModernRotationContainer modernModelFormat$modernRotationContainer;

    @Override
    public ModernRotationContainer modernRotationFormat$getModernRotation() {
        return modernModelFormat$modernRotationContainer;
    }

    @Override
    public ModelRotation modernRotationFormat$setModernRotation(ModernRotationContainer rotationContainer) {
        modernModelFormat$modernRotationContainer = rotationContainer;
        return (ModelRotation) (Object) this;
    }

    @Override
    public boolean modernRotationFormat$hasModernRotation() {
        return modernModelFormat$modernRotationContainer != null;
    }
}
