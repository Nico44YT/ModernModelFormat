package nico.modernmodelformat.format;

import net.minecraft.client.render.model.json.ModelRotation;

public interface ModernRotationWrapper {
    default ModernRotationContainer modernRotationFormat$getModernRotation() {
        return null;
    }

    default ModelRotation modernRotationFormat$setModernRotation(ModernRotationContainer rotationContainer) {
        return null;
    }

    default boolean modernRotationFormat$hasModernRotation() {
        return false;
    }
}
