package com.mateus.burble.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mateus.burble.Burble;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CoreConfig {

    private final File file = new File(Burble.getDataFolder(), "core.cfg");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static CoreConfig instance;
    private CoreConfig() {}
    private JsonObject configJson;
    public static CoreConfig getInstance() {
        if (instance == null) {
            synchronized (CoreConfig.class) {
                instance = new CoreConfig();
            }
        }
        return instance;
    }

    public void setup() {
        if (!file.exists()) {
            try {
                file.createNewFile();
                configJson = new JsonObject();
                configJson.addProperty("prefix", "b!");
                configJson.addProperty("token", "<default-token>");
                configJson.addProperty("owner-id", "set-owner-id");
                Files.write(Paths.get(file.getPath()), gson.toJson(configJson).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String content = String.join("", Files.readAllLines(Paths.get(file.getPath())));
                System.out.println(content);
                configJson = JsonParser.parseString(content).getAsJsonObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public JsonObject getConfig() {
        return configJson;
    }

    public boolean isUserOwner(Member member) {
        return isUserOwner(member.getUser());
    }

    public boolean isUserOwner(User user) {
        return user.getId().equals(configJson.get("owner-id").getAsString());
    }
}
