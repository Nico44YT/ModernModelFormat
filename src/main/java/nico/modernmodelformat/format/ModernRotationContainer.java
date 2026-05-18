package nico.modernmodelformat.format;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.AffineTransformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ModernRotationContainer {
    private final AffineTransformation affineTransformation;
    private final Vector3f origin;
    private final Vector3f rotation;
    private final boolean rescale;

    public ModernRotationContainer(float x, float y, float z, Vector3f origin, boolean rescale) {
        Quaternionf quaternionf = (new Quaternionf()).rotateYXZ((float) Math.toRadians(y), (float) Math.toRadians(x), (float) Math.toRadians(z));

        this.rotation = new Vector3f(x, y, z);

        this.affineTransformation = new AffineTransformation(
                new Vector3f(), // Translation
                quaternionf, // Left-Rotation
                new Vector3f(1, 1, 1), // Scale
                new Quaternionf() // Right-Rotation
        );
        this.origin = origin.mul(1 / 16f);
        this.rescale = rescale;
    }

    public static ModernRotationContainer fromJson(JsonObject elementJson) {
        if (!elementJson.has("rotation")) return new ModernRotationContainer(0, 0, 0, new Vector3f(), false);

        JsonObject rotationObject = elementJson.get("rotation").getAsJsonObject();
        Vector3f origin = rotationObject.has("origin") ? getOriginFromJson(rotationObject.get("origin").getAsJsonArray()) : new Vector3f();

        if (rotationObject.has("angle")) {
            float rotation = JsonHelper.getFloat(rotationObject, "angle", 0);
            char axis = JsonHelper.getString(rotationObject, "axis", "x").toLowerCase().charAt(0);

            boolean rescale = JsonHelper.getBoolean(rotationObject, "rescale", false);

            return switch (axis) {
                case 'x' -> new ModernRotationContainer(rotation, 0, 0, origin, rescale);
                case 'y' -> new ModernRotationContainer(0, rotation, 0, origin, rescale);
                case 'z' -> new ModernRotationContainer(0, 0, rotation, origin, rescale);
                default -> throw new IllegalStateException("Unexpected value: " + axis);
            };
        }

        float x = JsonHelper.getFloat(rotationObject, "x", 0);
        float y = JsonHelper.getFloat(rotationObject, "y", 0);
        float z = JsonHelper.getFloat(rotationObject, "z", 0);

        boolean rescale = JsonHelper.getBoolean(rotationObject, "rescale", false);

        return new ModernRotationContainer(x, y, z, origin, rescale);
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

    public boolean doRescale() {
        return this.rescale;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s, %s, %s]", this.affineTransformation, this.origin, this.rotation, this.rescale);
    }
}
