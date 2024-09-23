package org.iapetusservers.sharedVillagers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringWriter;

public class Debug {

    private final JsonObject log = new JsonObject();

    private int messageCount = 0;

    public void log(String property, JsonElement value) {
        log.add(property, value);
    }

    public void log(String property, Number value) {
        log.addProperty(property, value);
    }

    public void log(String property, String value) {
        log.addProperty(property, value);
    }

    public void log(String property, Boolean value) {
        log.addProperty(property, value);
    }

    public void log(String property, Character value) {
        log.addProperty(property, value);
    }

    public void log(String message)
    {
        log.addProperty(
                "message" + (messageCount > 0 ? String.valueOf(messageCount) : ""),
                message);

        ++messageCount;
    }

    public JsonObject getLog() {
        return log;
    }

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    public String toJson() throws IOException {
        return gson.toJson(log);
    }
}
