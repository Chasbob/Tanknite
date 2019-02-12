package com.aticatac.common.model;

import com.google.gson.Gson;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * The type Model reader.
 */
public class ModelReader {
    private static Logger logger = Logger.getLogger(ModelReader.class);

    /**
     * To bytes byte [ ].
     *
     * @param model the model
     * @return the byte [ ]
     */
    public static byte[] toBytes(Model model) {
        logger.info("Reading " + model.getClass().getName());
        Gson gson = new Gson();
        String json = gson.toJson(model);
        return json.getBytes();
    }

    /**
     * To model t.
     *
     * @param <T>  the type parameter
     * @param in   the in
     * @param type the type
     * @return the t
     */
    public static <T extends Model> T toModel(byte[] in, Class<T> type) {
        logger.info("Converting bytes to " + type.getName());
        Gson gson = new Gson();
        String json = new String(in, StandardCharsets.UTF_8);
        logger.info(json);
        logger.info("Returning a " + type.getName());
        Login test = new Login("Test");
//        test.isModelType(type.getClass().getName());
        var t = gson.fromJson(json, type);
        if (!t.getClassName().equals(type.getName())) {
            logger.error("wrong class");
//            throw new InvalidBytes();
        }
        return gson.fromJson(json, type);
    }

    /**
     * To bytes udp byte [ ].
     *
     * @param model the model
     * @return the byte [ ]
     */
    public static byte[] toBytesUDP(Model model) {
        logger.info("Reading " + model.getClass().getName());
        Gson gson = new Gson();
        String json = gson.toJson(model);
        byte[] data = json.getBytes();
//        byte[] name = model.getClass().getName().getBytes();
        byte[] length = ByteBuffer.allocate(4).putInt(data.length).array();
        return ArrayUtils.addAll(length, data);
    }

    /**
     * To model udp t.
     *
     * @param <T>  the type parameter
     * @param in   the in
     * @param type the type
     * @return the t
     */
    public static <T extends Model> T toModelUDP(byte[] in, Class<T> type) {
        logger.info("Converting bytes to " + type.getCanonicalName());
        Gson gson = new Gson();
        int length = ByteBuffer.wrap(Arrays.copyOfRange(in, 0, 4)).getInt();
        return gson.fromJson(new String(Arrays.copyOfRange(in, 4, length + 4), StandardCharsets.UTF_8), type);
    }
}
