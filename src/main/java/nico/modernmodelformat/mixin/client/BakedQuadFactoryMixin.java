package nico.modernmodelformat.mixin.client;

import net.minecraft.client.render.model.BakedQuadFactory;
import net.minecraft.client.render.model.json.ModelRotation;
import net.minecraft.util.math.AffineTransformation;
import nico.modernmodelformat.format.ModernRotationContainer;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BakedQuadFactory.class)
public abstract class BakedQuadFactoryMixin {
    @Shadow
    protected abstract void transformVertex(Vector3f vertex, Vector3f origin, Matrix4f transformationMatrix, Vector3f scale);

    @Inject(method = "rotateVertex", at = @At("HEAD"), cancellable = true)
    public void modernModelFormat$rotateVertex(Vector3f vertex, ModelRotation rotation, CallbackInfo ci) {
        if (rotation != null && rotation.modernRotationFormat$hasModernRotation()) {

            ModernRotationContainer rotationContainer = rotation.modernRotationFormat$getModernRotation();

            Vector3f rotationVector = rotationContainer.getRotation();
            Vector3f origin = rotationContainer.getOrigin();

            float xRad = (float)Math.toRadians(rotationVector.x());
            float yRad = (float)Math.toRadians(rotationVector.y());
            float zRad = (float)Math.toRadians(rotationVector.z());

            Quaternionf quaternion = new Quaternionf().rotateYXZ(yRad, xRad, zRad);

            Matrix4f matrix = new Matrix4f().rotation(quaternion);
            Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f); // No scaling
            this.transformVertex(vertex, origin, matrix, scale);

            ci.cancel();
        }
    }
}

