package com.aticatac.client.scratch;

import com.aticatac.client.objectsystem.ContainerWrapper;
import com.aticatac.common.components.Component;
import com.aticatac.common.model.ModelReader;
import com.aticatac.common.objectsystem.Converter;
import com.aticatac.common.objectsystem.GameObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import java.util.HashMap;

class Scratch {
    public static void main(String[] args) throws Exception {
        HashMap<Class<?>, Component> components = new HashMap<>();
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Test", 1);

        var root = new GameObject("Test");
        System.out.println("Generating Objects...");

        for (int i = 0;i<1000;i++){
            var obj = new GameObject("Test", root);
            obj.transform.SetTransform((float)(Math.random()*1000),(float)(Math.random()*1000));
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        ContainerWrapper wrapper = new ContainerWrapper(Converter.Deconstructor(root));

        String json = gson.toJson(wrapper);
        System.out.println("Generating json...");

        byte[] b = compress(json);
        System.out.println("Compressing json...");

        System.out.println(b.length);
        Converter.Constructor(Converter.Deconstructor(root));
    }

    public static byte[] compress(String data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
        GZIPOutputStream gzip = new GZIPOutputStream(bos);
        gzip.write(data.getBytes());
        gzip.close();
        byte[] compressed = bos.toByteArray();
        bos.close();
        return compressed;
    }

    public static String decompress(byte[] compressed) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(bis);
        BufferedReader br = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        gis.close();
        bis.close();
        return sb.toString();
    }
}