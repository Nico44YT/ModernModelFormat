package nico.modernmodelformat.format.deserializer.copy;

import com.google.gson.JsonObject;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelRotation;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import nico.modernmodelformat.format.ModernRotationContainer;

public class ModernModelElementDeserializer extends ModelElement.Deserializer {
    public ModelRotation deserializeModelRotation(JsonObject object) {
        ModernRotationContainer rotationContainer = ModernRotationContainer.fromJson(object);
        return new ModelRotation(new Vec3f(), Direction.Axis.X, 0, JsonHelper.getBoolean(object, "rescale", false))
                .modernRotationFormat$setModernRotation(rotationContainer);
    }
}
