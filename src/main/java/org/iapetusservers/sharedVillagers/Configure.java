package org.iapetusservers.sharedVillagers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.tananaev.jsonpatch.JsonPatch;
import com.tananaev.jsonpatch.JsonPath;
import com.tananaev.jsonpatch.gson.AbsOperationDeserializer;
import com.tananaev.jsonpatch.gson.JsonPathDeserializer;
import com.tananaev.jsonpatch.operation.AbsOperation;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Configure implements CommandExecutor {

    private final Supplier<Settings> getSettings;
    private final Consumer<Settings> setSettings;

    public Configure(Supplier<Settings> get, Consumer<Settings> set) {
        getSettings = get;
        setSettings = set;
    }

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(JsonPath.class, new JsonPathDeserializer())
            .registerTypeAdapter(AbsOperation.class, new AbsOperationDeserializer())
            .create();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player)
            player = (Player) sender;

        JsonElement settings = gson.toJsonTree(getSettings.get(), Settings.class);

        if (Objects.equals(args[0].toLowerCase(), "patch")) {
            String arg = String.join(" ", Arrays.stream(args).skip(1).toList());
            JsonPatch patch = gson.fromJson(arg, JsonPatch.class);

            JsonElement modified = patch.apply(settings);

            setSettings.accept(
                    gson.fromJson(modified, Settings.class));

            String result = gson.toJson(getSettings.get(), Settings.class);
            Objects.requireNonNullElseGet(player, Bukkit::getConsoleSender).sendMessage(result);
        }
        else {
            JsonPath path = new JsonPath(args[0]);

            String result = gson.toJson(path.navigate(settings));
            Objects.requireNonNullElseGet(player, Bukkit::getConsoleSender).sendMessage(result);
        }

        return true;
    }
}
