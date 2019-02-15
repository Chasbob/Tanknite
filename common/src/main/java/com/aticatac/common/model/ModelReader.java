package com.aticatac.common.model;

import com.aticatac.common.model.Exception.InvalidBytes;
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
     * To bytes udp byte [ ].
     *
     * @param model the model
     * @return the byte [ ]
     */
    public static byte[] toBytes(Model model) {
        logger.trace("Reading " + model.getClass().getName());
        String json = toJson(model);
        byte[] data = json.getBytes();
        byte[] length = ByteBuffer.allocate(4).putInt(data.length).array();
        return ArrayUtils.addAll(length, data);
    }

    /**
     * To json string.
     *
     * @param model the model
     * @return the string
     */
    public static String toJson(Model model) {
        Gson gson = new Gson();
        return gson.toJson(model);
    }

    /**
     * To model udp t.
     *
     * @param <T>  the type parameter
     * @param in   the in
     * @param type the type
     * @return the t
     * @throws InvalidBytes the invalid bytes
     */
    public static <T extends Model> T toModel(byte[] in, Class<T> type) throws InvalidBytes {
        logger.trace("Converting bytes to " + type.getCanonicalName());
        int length = ByteBuffer.wrap(Arrays.copyOfRange(in, 0, 4)).getInt();
        return fromJson(new String(Arrays.copyOfRange(in, 4, length + 4), StandardCharsets.UTF_8), type);
    }

    /**
     * From json t.
     *
     * @param <T>  the type parameter
     * @param json the json
     * @param type the type
     * @return the t
     * @throws InvalidBytes the invalid bytes
     */
    public static <T extends Model> T fromJson(String json, Class<T> type) throws InvalidBytes {
        Gson gson = new Gson();
        T output;
        output = gson.fromJson(json, type);
        if (!(output.isModelType(type.getName()))) {
            throw new InvalidBytes();
        } else {
            return output;
        }
    }
}
