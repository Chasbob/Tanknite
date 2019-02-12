package com.aticatac.client.networking;

import com.aticatac.common.Constant;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Random;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            Thread.sleep(getRandomNumberInRange(5000, 10000));
            Logger.getRootLogger().setLevel(Level.FATAL);
            int id = new Random().nextInt();
            Client client = new Client(Integer.toString(id));
            client.connect(Constant.getServer());
        } catch (InterruptedException | IOException e) {
            logger.error(e);
        }
    }

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
