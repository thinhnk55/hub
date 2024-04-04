package com.defi.util.file;

import org.apache.commons.lang3.exception.ExceptionUtils;
import com.defi.util.log.DebugLogger;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUtil {
    public static String getString(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if(!path.toFile().exists()){
                return null;
            }
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (Exception e) {
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    public static void writeStringToFile(String fileName, String data) {
        Path path = Paths.get(fileName);
        try {
            Files.createDirectories(path.getParent());
            BufferedWriter writer = Files.newBufferedWriter(path,StandardCharsets.UTF_8);
            writer.append(data);
            writer.close();
        } catch (Exception e) {
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public static boolean isExist(String filePath) {
        Path path = Paths.get(filePath);
        return path.toFile().exists();
    }

    public static void downloadFile(String url, String fileName) {
        try {
            Path path = Paths.get(fileName);
            Files.createDirectories(path.getParent());
            InputStream in = new URL(url).openStream();
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public static void createParrent(String fileName) {
        try {
            Path path = Paths.get(fileName);
            Files.createDirectories(path.getParent());
        } catch (Exception e) {
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
        }
    }
}
