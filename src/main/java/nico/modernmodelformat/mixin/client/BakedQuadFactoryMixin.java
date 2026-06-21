package nico.modernmodelformat.mixin.client;

import net.minecraft.client.render.model.BakedQuadFactory;
import net.minecraft.client.render.model.json.ModelRotation;
import net.minecraft.util.math.MathHelper;
import nico.modernmodelformat.format.ModernRotationContainer;
import org.joml.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.Math;

@Mixin(BakedQuadFactory.class)
public abstract class BakedQuadFactoryMixin {
    @Shadow
    protected abstract void transformVertex(Vector3f vertex, Vector3f origin, Matrix4f transformationMatrix, Vector3f scale);

    @Inject(method = "rotateVertex", at = @At("HEAD"), cancellable = true)
    public void modernModelFormat$rotateVertex(Vector3f vertex, ModelRotation rotation, CallbackInfo ci) {
        if (rotation != null && rotation.modernRotationFormat$hasModernRotation()) {

            ModernRotationContainer rotationContainer = rotation.modernRotationFormat$getModernRotation();

            Vector3f origin = rotationContainer.getOrigin();

            Vector3f rotationDegrees = rotationContainer.getRotation();
            Vector3f rotationRadians = new Vector3f(rotationDegrees).mul(MathHelper.RADIANS_PER_DEGREE);

            boolean rescale = rotationContainer.doRescale();

            Quaternionf quaternionf = new Quaternionf().rotateZYX(rotationRadians.z, rotationRadians.y, rotationRadians.x);

            Vector3f scale = new Vector3f(1, 1, 1);

            if (rescale) {
                float sx = rescaleFactor(rotationDegrees.x);
                float sy = rescaleFactor(rotationDegrees.y);
                float sz = rescaleFactor(rotationDegrees.z);

                scale.set(sy * sz, sx * sz, sx * sy);
            }

            this.transformVertex(vertex, origin, (new Matrix4f()).rotation(quaternionf), scale);

            ci.cancel();
        }

    }

    /*
            {
            ModernRotationContainer rotationContainer = rotation.modernRotationFormat$getModernRotation();
            Vector3f origin = rotationContainer.getOrigin();
            Vector3f rot = new Vector3f(rotationContainer.getRotation()).mul((float)Math.PI / 180);
            Quaternionf quaternionf = new Quaternionf().rotateZYX(rot.z, rot.y, rot.x);
            Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f);
            this.transformVertex(vertex, origin, new Matrix4f().rotation(quaternionf), scale);
            ci.cancel();
        }
     */

    private float rescaleFactor(float degrees) {
        float clamped = Math.abs(degrees) % 90.0f;
        return 1.0f / (float) Math.cos(clamped * MathHelper.DEGREES_PER_RADIAN);
    }
}

