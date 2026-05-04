package nico.modernmodelformat.mixin.client;

import com.google.gson.Gson;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import nico.modernmodelformat.format.deserializer.ModernJsonUnbakedModelDeserializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

@Mixin(JsonUnbakedModel.class)
public abstract class JsonUnbakedModelMixin {
    @Unique
    private static final Pattern VERSION_PATTERN = Pattern.compile("\\\"format_version\\\":\\s\\\"1\\.21\\.11\\\"");

    @WrapOperation(method = "deserialize(Ljava/io/Reader;)Lnet/minecraft/client/render/model/json/JsonUnbakedModel;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/JsonHelper;deserialize(Lcom/google/gson/Gson;Ljava/io/Reader;Ljava/lang/Class;)Ljava/lang/Object;"))
    private static <T> T modernModelFormat$deserialize(Gson gson, Reader reader, Class<T> type, Operation<T> original) throws IOException {
        if (reader instanceof BufferedReader bufferedReader) {
            bufferedReader.mark(8192);

            String line;
            boolean matchesFormat = false;

            while ((line = bufferedReader.readLine()) != null) {
                if (VERSION_PATTERN.matcher(line).find()) {
                    matchesFormat = true;
                    break;
                }
            }

            bufferedReader.reset();

            if (matchesFormat) return original.call(ModernJsonUnbakedModelDeserializer.GSON, reader, type);
        }

        return original.call(gson, reader, type);
    }
}
