package com.aticatac.client.networking;

import com.aticatac.client.screens.Screens;
import com.aticatac.common.model.Command;
import com.aticatac.common.model.ServerInformation;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

public class Main {
    private static final SecureRandom random = new SecureRandom();
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting...");
        try {
            Client client = new Client(Screens.INSTANCE.getUpdates());
            ServerInformation information = new BroadcastListener().call();
            logger.trace(information.toString());
            logger.trace("Got address: " + information.getAddress() + ":" + information.getPort());
            client.connect(information, NameGenerator.generateName());
            logger.info("Connected to " + information.getAddress() + ":" + information.getPort());
            while (true) {
                Thread.sleep(400);
                client.sendCommand(randomCommand());
            }
        } catch (InterruptedException | IOException e) {
            logger.error(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T extends Enum<?>> T randomCommand() {
        int x = random.nextInt(((Class<T>) Command.class).getEnumConstants().length);
        return ((Class<T>) Command.class).getEnumConstants()[x];
    }

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}

class NameGenerator {
    private static final String[] Beginning = {"Kr", "Ca", "Ra", "Mrok", "Cru",
            "Ray", "Bre", "Zed", "Drak", "Mor", "Jag", "Mer", "Jar", "Mjol",
            "Zork", "Mad", "Cry", "Zur", "Creo", "Azak", "Azur", "Rei", "Cro",
            "Mar", "Luk"};
    private static final String[] Middle = {"air", "ir", "mi", "sor", "mee", "clo",
            "red", "cra", "ark", "arc", "miri", "lori", "cres", "mur", "zer",
            "marac", "zoir", "slamar", "salmar", "urak"};
    private static final String[] End = {"d", "ed", "ark", "arc", "es", "er", "der",
            "tron", "med", "ure", "zur", "cred", "mur"};
    private static final Random rand = new Random();

    static String generateName() {
        return Beginning[rand.nextInt(Beginning.length)] +
                Middle[rand.nextInt(Middle.length)] +
                End[rand.nextInt(End.length)];
    }
}
