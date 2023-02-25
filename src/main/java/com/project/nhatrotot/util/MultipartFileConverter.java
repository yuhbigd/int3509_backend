package com.project.nhatrotot.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class MultipartFileConverter {
    public static File convert(MultipartFile multipartFile, String fileName) throws IOException {
        final File file = new File("./" + fileName);
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (final IOException ex) {
            throw new IOException("Error converting the multi-part file to file " + ex.getMessage());
        }
        return file;
    }
}
