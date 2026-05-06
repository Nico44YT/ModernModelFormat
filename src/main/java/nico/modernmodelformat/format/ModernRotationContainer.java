package nico.modernmodelformat.format;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

public class ModernRotationContainer {
    private final AffineTransformation affineTransformation;
    private final Vec3f origin;

    public ModernRotationContainer(float x, float y, float z, Vec3f origin) {
        Quaternion quaternion = Quaternion.IDENTITY.copy();
        quaternion.hamiltonProduct(Vec3f.POSITIVE_Y.getRadialQuaternion((float)Math.toRadians(-y)));
        quaternion.hamiltonProduct(Vec3f.POSITIVE_X.getRadialQuaternion((float)Math.toRadians(-x)));
        quaternion.hamiltonProduct(Vec3f.POSITIVE_Z.getRadialQuaternion((float)Math.toRadians(-z)));

        this.affineTransformation = new AffineTransformation(
                Vec3f.ZERO, // Translation
                quaternion, // Left-Rotation
                new Vec3f(1, 1, 1), // Scale
                Quaternion.IDENTITY // Right-Rotation
        );

        Vec3f scaledOrigin = origin.copy();
        scaledOrigin.scale(1/16f);
        this.origin = scaledOrigin;
    }

    public static ModernRotationContainer fromJson(JsonObject elementJson) {
        if (!elementJson.has("rotation")) return new ModernRotationContainer(0, 0, 0, new Vec3f());

        JsonObject rotationObject = elementJson.get("rotation").getAsJsonObject();
        Vec3f origin = rotationObject.has("origin") ? getOriginFromJson(rotationObject.get("origin").getAsJsonArray()) : new Vec3f();

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

    private static Vec3f getOriginFromJson(JsonArray originArray) {
        return new Vec3f(originArray.get(0).getAsFloat(), originArray.get(1).getAsFloat(), originArray.get(2).getAsFloat());
    }

    public AffineTransformation getAffineTransformation() {
        return this.affineTransformation;
    }

    public Vec3f getOrigin() {
        return this.origin;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", this.affineTransformation, this.origin);
    }
}
