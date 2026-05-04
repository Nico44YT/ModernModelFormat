package nico.modernmodelformat.mixin.client;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelRotation;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.Direction;
import nico.modernmodelformat.format.ModernRotationContainer;
import nico.modernmodelformat.format.deserializer.copy.ModernModelElementDeserializer;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ModelElement.Deserializer.class)
public abstract class ModelElementDeserializerMixin {
    @WrapOperation(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/render/model/json/ModelElement;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/json/ModelElement$Deserializer;deserializeRotation(Lcom/google/gson/JsonObject;)Lnet/minecraft/client/render/model/json/ModelRotation;"))
    public ModelRotation modernModelFormat$deserializeRotation(ModelElement.Deserializer instance, JsonObject object, Operation<ModelRotation> original) {
        if (instance instanceof ModernModelElementDeserializer modernDeserializer) {
            return modernDeserializer.deserializeModelRotation(object);
        }

        return original.call(instance, object);
    }
}
