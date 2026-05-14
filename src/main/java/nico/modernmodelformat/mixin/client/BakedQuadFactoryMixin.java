package nico.modernmodelformat.mixin.client;

import net.minecraft.client.render.model.BakedQuadFactory;
import net.minecraft.client.render.model.json.ModelRotation;
import nico.modernmodelformat.format.ModernRotationContainer;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
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

            Vector3f origin = rotationContainer.getOrigin();

            Vector3f rot = new Vector3f(rotationContainer.getRotation()).mul(0.017453292F);

            Quaternionf quaternionf = new Quaternionf().rotateZYX(rot.z, rot.y, rot.x);

            Vector3f scale = new Vector3f(1, 1, 1); // No scaling
            this.transformVertex(vertex, origin, (new Matrix4f()).rotation(quaternionf), scale);

            ci.cancel();
        }
    }
}

