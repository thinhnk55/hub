package com.defi.util.file;

import com.defi.util.json.GsonUtil;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import com.defi.util.log.DebugLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ImageUtil {
    public static String imageToBase64(String filePath) {
        try {
            Path imagePath = Paths.get(filePath);
            byte[] imageBytes = Files.readAllBytes(imagePath);
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            return base64Image;
        } catch (Exception e) {
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    public static void base64ToImage(String base64Image, String outputPath) {
        try {
            if(base64Image.startsWith("data")){
                base64Image = base64Image.split(",")[1];
            }
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            Path imagePath = Paths.get(outputPath);
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, imageBytes);
        } catch (Exception e) {
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
        }
    }
    public static JsonObject uploadToImageBB(String api_key, String base64Image){
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", base64Image)
                    .build();
            Request request = new Request.Builder()
                    .url(new StringBuilder("https://api.imgbb.com/1/upload?key=").append(api_key).toString())
                    .post(requestBody)
                    .build();
            // Execute request
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                return GsonUtil.toJsonObject(responseBody);
            } else {
                return null;
            }
        } catch (Exception e) {
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    public static String imageToBase64FromUrl(String urlPath) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlPath)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                byte[] fileContent = response.body().bytes();
                String base64EncodedFile = Base64.getEncoder().encodeToString(fileContent);
                return base64EncodedFile;
            } else {
                return null;
            }
        } catch (IOException e) {
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }
}
