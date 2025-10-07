package ir.maktabsharif.finalproject.security;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

public class Base64ImageEncode {
    public static String imageEncode(MultipartFile image) throws IOException {
        return Base64.getEncoder().encodeToString(image.getBytes());
    }
}
