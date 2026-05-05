package nico.modernmodelformat.format;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.math.AffineTransformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ModernRotationContainer {
    private final AffineTransformation affineTransformation;
    private final Vector3f origin;
    private final Vector3f rotation;

    public ModernRotationContainer(float x, float y, float z, Vector3f origin) {
        Quaternionf quaternionf = (new Quaternionf()).rotateYXZ((float) Math.toRadians(-y), (float) Math.toRadians(-x), (float) Math.toRadians(-z));

        this.rotation = new Vector3f(x, y, z);
        this.affineTransformation = new AffineTransformation(
                new Vector3f(), // Translation
                quaternionf, // Left-Rotation
                new Vector3f(1, 1, 1), // Scale
                new Quaternionf() // Right-Rotation
        );
        this.origin = origin.mul(1 / 16f); // Idk why I need to scale it
    }

    public static ModernRotationContainer fromJson(JsonObject elementJson) {
        if (!elementJson.has("rotation")) return new ModernRotationContainer(0, 0, 0, new Vector3f());

        JsonObject rotationObject = elementJson.get("rotation").getAsJsonObject();
        Vector3f origin = rotationObject.has("origin") ? getOriginFromJson(rotationObject.get("origin").getAsJsonArray()) : new Vector3f();

        if (rotationObject.has("angle")) {
            float rotation = rotationObject.get("angle").getAsFloat();
            char axis = rotationObject.get("axis").getAsString().toLowerCase().charAt(0);

            return switch (axis) {
                case 'x':
                    yield new ModernRotationContainer(rotation, 0, 0, origin);
                case 'y':
                    yield new ModernRotationContainer(0, rotation, 0, origin);
                case 'z':
                    yield new ModernRotationContainer(0, 0, rotation, origin);
                default:
                    throw new IllegalStateException("Unexpected value: " + axis);
            };
        }

        float x = rotationObject.has("x") ? rotationObject.get("x").getAsFloat() : 0;
        float y = rotationObject.has("y") ? rotationObject.get("y").getAsFloat() : 0;
        float z = rotationObject.has("z") ? rotationObject.get("z").getAsFloat() : 0;

        return new ModernRotationContainer(x, y, z, origin);
    }

    private static Vector3f getOriginFromJson(JsonArray originArray) {
        return new Vector3f(originArray.get(0).getAsFloat(), originArray.get(1).getAsFloat(), originArray.get(2).getAsFloat());
    }

    public AffineTransformation getAffineTransformation() {
        return this.affineTransformation;
    }

    public Vector3f getOrigin() {
        return this.origin;
    }

    public Vector3f getRotation() {
        return this.rotation;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s, %s]", this.affineTransformation, this.rotation, this.origin);
    }
}
