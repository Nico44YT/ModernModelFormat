package nico.modernmodelformat.mixin.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.JsonHelper;
import nico.modernmodelformat.format.deserializer.ModernJsonUnbakedModelDeserializer;
import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.stream.Collectors;

@Mixin(JsonUnbakedModel.class)
public abstract class JsonUnbakedModelMixin {
    @Shadow
    @Final
    static Gson GSON;

    // I don't really like this approach
    @WrapOperation(method = "deserialize(Ljava/io/Reader;)Lnet/minecraft/client/render/model/json/JsonUnbakedModel;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/JsonHelper;deserialize(Lcom/google/gson/Gson;Ljava/io/Reader;Ljava/lang/Class;)Ljava/lang/Object;"))
    private static <T> T modernModelFormat$deserialize(Gson gson, Reader reader, Class<T> type, Operation<T> original) {
        BufferedReader bufferedReader = IOUtils.buffer(reader);
        String content = bufferedReader.lines().collect(Collectors.joining());

        JsonObject jsonObject = GSON.fromJson(content, JsonObject.class);

        boolean matchesFormat = JsonHelper.getString(jsonObject, "format_version", "none").equals("1.21.11");

        return original.call(matchesFormat ? ModernJsonUnbakedModelDeserializer.GSON : GSON, new StringReader(content), type);
    }
}
