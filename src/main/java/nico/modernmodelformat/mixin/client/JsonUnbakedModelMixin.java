package nico.modernmodelformat.mixin.client;

import com.google.gson.Gson;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import nico.modernmodelformat.format.deserializer.ModernJsonUnbakedModelDeserializer;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

@Mixin(JsonUnbakedModel.class)
public abstract class JsonUnbakedModelMixin {
    @Shadow
    @Final
    private static Logger LOGGER;

    @Unique
    private static final String formatVersionPatternRegex = "\\\"format_version\\\":";
    @Unique
    private static final Pattern FORMAT_VERSION_PATTERN = Pattern.compile(formatVersionPatternRegex);
    @Unique
    private static final Pattern REQUIRED_VERSION_PATTERN = Pattern.compile(formatVersionPatternRegex + "\\s\\\"1\\.21\\.11\\\"");

    @WrapOperation(method = "deserialize(Ljava/io/Reader;)Lnet/minecraft/client/render/model/json/JsonUnbakedModel;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/JsonHelper;deserialize(Lcom/google/gson/Gson;Ljava/io/Reader;Ljava/lang/Class;)Ljava/lang/Object;"))
    private static <T> T modernModelFormat$deserialize(Gson gson, Reader reader, Class<T> type, Operation<T> original) throws IOException {
        if (reader instanceof BufferedReader bufferedReader) {
            int limit = 8192*2;
            bufferedReader.mark(limit);

            int readChars = 0;
            String line;
            boolean matchesFormat = false;

            while ((line = bufferedReader.readLine()) != null) {
                if (FORMAT_VERSION_PATTERN.matcher(line).find()) {
                    matchesFormat = REQUIRED_VERSION_PATTERN.matcher(line).find();
                    break;
                }

                readChars += line.length() + 4;

                if(readChars >= limit) {
                    System.out.println(readChars + " - " + limit);
                    LOGGER.warn("[Modern Model Format] Model file exceeds read limit, make sure \"format_version\" is near the top of the file.");
                    break;
                }
            }

            bufferedReader.reset();

            if (matchesFormat) return original.call(ModernJsonUnbakedModelDeserializer.GSON, reader, type);
        }

        return original.call(gson, reader, type);
    }
}
