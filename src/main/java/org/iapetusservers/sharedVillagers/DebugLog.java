package org.iapetusservers.sharedVillagers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringWriter;

public class DebugLog {

    private final JsonObject log = new JsonObject();

    private int messageCount = 0;

    public JsonObject getLog() {
        return log;
    }
    
    public void addMessage(String message)
    {
        log.addProperty(
                "message" + (messageCount > 0 ? String.valueOf(messageCount) : ""),
                message);

        ++messageCount;
    }

    public String getJson() throws IOException {
        StringWriter sw = new StringWriter();
        JsonWriter writer = new JsonWriter(sw);
        writer.setIndent("  ");

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setLenient()
                .setPrettyPrinting().create();

        gson.toJson(log, JsonObject.class, writer);
        writer.flush();

        return sw.toString();
    }
}
