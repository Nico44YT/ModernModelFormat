package nico.modernmodelformat.mixin.client;

import net.minecraft.client.render.model.BakedQuadFactory;
import net.minecraft.client.render.model.json.ModelRotation;
import net.minecraft.util.math.MathHelper;
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

            Vector3f degrees = rotationContainer.getRotation();
            Vector3f radians = new Vector3f(degrees).mul(MathHelper.DEGREES_PER_RADIAN);

            boolean rescale = rotationContainer.doRescale();

            Quaternionf quaternionf = new Quaternionf().rotateZYX(radians.z, radians.y, radians.x);

            Vector3f scale = new Vector3f(1, 1, 1);

            if (rescale) {
                float sx = rescaleFactor(degrees.x);
                float sy = rescaleFactor(degrees.y);
                float sz = rescaleFactor(degrees.z);

                scale.set(sy * sz, sx * sz, sx * sy);
            }

            this.transformVertex(vertex, origin, (new Matrix4f()).rotation(quaternionf), scale);

            ci.cancel();
        }
    }

    private float rescaleFactor(float degrees) {
        float clamped = Math.abs(degrees) % 90.0f;
        return 1.0f / (float) Math.cos(clamped * MathHelper.DEGREES_PER_RADIAN);
    }
}

