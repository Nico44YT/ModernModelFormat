package nico.modernmodelformat.mixin.client;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelRotation;
import nico.modernmodelformat.format.deserializer.ModernModelElementDeserializer;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelElement.Deserializer.class)
public abstract class ModelElementDeserializerMixin {
    @Shadow
    protected abstract Vector3f deserializeVec3f(JsonObject object, String name);

    @WrapOperation(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/render/model/json/ModelElement;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/json/ModelElement$Deserializer;deserializeRotation(Lcom/google/gson/JsonObject;)Lnet/minecraft/client/render/model/json/ModelRotation;"))
    public ModelRotation modernModelFormat$deserializeRotation(ModelElement.Deserializer instance, JsonObject object, Operation<ModelRotation> original) {
        if (instance instanceof ModernModelElementDeserializer modernDeserializer) {
            return modernDeserializer.deserializeModelRotation(object);
        }

        return original.call(instance, object);
    }

    @Inject(method = "deserializeTo", at = @At("HEAD"), cancellable = true)
    public void modernModelFormat$deserializeTo(JsonObject object, CallbackInfoReturnable<Vector3f> cir) {
        ModelElement.Deserializer instance = (ModelElement.Deserializer)(Object)this;
        if (instance instanceof ModernModelElementDeserializer modernDeserializer) {
            cir.setReturnValue(this.deserializeVec3f(object, "to"));
        }
    }

    @Inject(method = "deserializeFrom", at = @At("HEAD"), cancellable = true)
    public void modernModelFormat$deserializeFrom(JsonObject object, CallbackInfoReturnable<Vector3f> cir) {
        ModelElement.Deserializer instance = (ModelElement.Deserializer)(Object)this;
        if (instance instanceof ModernModelElementDeserializer modernDeserializer) {
            cir.setReturnValue(this.deserializeVec3f(object, "from"));
        }
    }
}
