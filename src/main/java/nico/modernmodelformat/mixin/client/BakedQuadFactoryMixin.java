package nico.modernmodelformat.mixin.client;

import net.minecraft.client.render.model.BakedQuadFactory;
import net.minecraft.client.render.model.json.ModelRotation;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import nico.modernmodelformat.format.ModernRotationContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BakedQuadFactory.class)
public abstract class BakedQuadFactoryMixin {

    @Shadow
    protected abstract void transformVertex(Vec3f vertex, Vec3f origin, Matrix4f transformationMatrix, Vec3f scale);

    @Inject(method = "rotateVertex", at = @At("HEAD"), cancellable = true)
    public void modernModelFormat$rotateVertex(Vec3f vertex, ModelRotation rotation, CallbackInfo ci) {
        if (rotation != null && rotation.modernRotationFormat$hasModernRotation()) {
            ModernRotationContainer rotationContainer = rotation.modernRotationFormat$getModernRotation();

            var transformation = rotationContainer.getAffineTransformation();

            if (transformation != AffineTransformation.identity()) {
                this.transformVertex(vertex, rotationContainer.getOrigin(), transformation.getMatrix(), new Vec3f(1.0F, 1.0F, 1.0F));
            }

            ci.cancel();
        }
    }
}

