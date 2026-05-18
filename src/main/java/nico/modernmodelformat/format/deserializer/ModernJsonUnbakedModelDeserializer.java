package nico.modernmodelformat.format.deserializer;

import com.google.gson.Gson;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.util.JsonHelper;
import nico.modernmodelformat.mixin.client.accessor.JsonUnbakedModelAccessor;

import java.io.Reader;
import java.io.StringReader;

public class ModernJsonUnbakedModelDeserializer extends JsonUnbakedModel.Deserializer {

    public static final Gson GSON = JsonUnbakedModelAccessor.modernModelFormat$getGSON().newBuilder()
            .registerTypeAdapter((ModelElement.class), new ModernModelElementDeserializer())
            .create();

    public static JsonUnbakedModel deserialize(Reader input) {
        return JsonHelper.deserialize(GSON, input, JsonUnbakedModel.class);
    }

    public static JsonUnbakedModel deserialize(String json) {
        return ModernJsonUnbakedModelDeserializer.deserialize(new StringReader(json));
    }

    public ModernJsonUnbakedModelDeserializer() {
        super();
    }
}