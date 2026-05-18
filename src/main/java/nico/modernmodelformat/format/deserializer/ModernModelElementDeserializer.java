package nico.modernmodelformat.format.deserializer;

import com.google.gson.JsonObject;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelRotation;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.Direction;
import nico.modernmodelformat.format.ModernRotationContainer;
import org.joml.Vector3f;

public class ModernModelElementDeserializer extends ModelElement.Deserializer {
    public ModelRotation deserializeModelRotation(JsonObject object) {
        ModernRotationContainer rotationContainer = ModernRotationContainer.fromJson(object);
        return new ModelRotation(new Vector3f(), Direction.Axis.X, 0, JsonHelper.getBoolean(object, "rescale", false))
                .modernRotationFormat$setModernRotation(rotationContainer);
    }
}
