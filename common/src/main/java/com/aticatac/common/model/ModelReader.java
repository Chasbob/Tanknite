package com.aticatac.common.model;

import com.aticatac.common.model.Exception.InvalidBytes;
import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

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
  public static byte[] toBytes(Model model) throws IOException {
    logger.trace("Reading " + model.getClass().getName());
    String json = toJson(model);
    byte[] data = json.getBytes();
    byte[] length = ByteBuffer.allocate(4).putInt(data.length).array();
    return compress(ArrayUtils.addAll(length, data));
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
  public static <T extends Model> T toModel(byte[] in, Class<T> type) throws InvalidBytes, IOException {
    logger.trace("Converting bytes to " + type.getCanonicalName());
    byte[] decompressed = decompress(in);
    int length = ByteBuffer.wrap(Arrays.copyOfRange(decompressed, 0, 4)).getInt();
    return fromJson(new String(Arrays.copyOfRange(decompressed, 4, length + 4), StandardCharsets.UTF_8), type);
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

  private static byte[] compress(byte[] data) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
    GZIPOutputStream gzip = new GZIPOutputStream(bos);
    gzip.write(data);
    gzip.close();
    byte[] compressed = bos.toByteArray();
    bos.close();
    return compressed;
  }

  private static byte[] decompress(byte[] compressed) throws IOException {
    ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
    GZIPInputStream gis = new GZIPInputStream(bis);
    byte[] output = gis.readAllBytes();
    bis.close();
    gis.close();
    return output;
  }
}
