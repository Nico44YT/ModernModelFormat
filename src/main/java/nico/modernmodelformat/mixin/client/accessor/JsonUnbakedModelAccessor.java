package nico.modernmodelformat.mixin.client.accessor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(JsonUnbakedModel.class)
public interface JsonUnbakedModelAccessor {
    @Accessor("GSON")
    static Gson modernModelFormat$getGSON() {
        return null;
    }
}
