package com.abeltramo.twsuggestions;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by ale on 29/12/16.
 */
public class CacheManager {
    File cacheDir;
    public CacheManager(){
        cacheDir  = new File("Cache");
        cacheDir.mkdir();                               // Create the cache folder if not exists
    }

    private String getCachePath(){
        return cacheDir.getAbsolutePath() + File.separator;
    }

    public void set(String filename, String content, boolean delete){
        File f = new File( getCachePath() + filename);
        if (f.exists() && delete) {
            f.delete();
        }
        try {
            FileWriter out;
            out = new FileWriter(f, true);
            out.write(content);
            out.close();
        } catch(Exception e) {
            System.out.println("Error appending to file " + filename);
        }
    }

    public String get(String filename){
        String content = "";
        try {
            content = readFile(getCachePath() + filename, Charset.defaultCharset());
        } catch (IOException e) {
            System.out.println("File input error");
        }
        return content;
    }

    private static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
