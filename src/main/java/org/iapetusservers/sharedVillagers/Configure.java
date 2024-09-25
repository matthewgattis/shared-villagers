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
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

public class Configure implements CommandExecutor {

    private final Supplier<Settings> settingsSupplier;
    private final Consumer<Settings> settingsConsumer;

    public Configure(Supplier<Settings> get, Consumer<Settings> set) {
        settingsSupplier = get;
        settingsConsumer = set;
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

        try {
            Settings settings = settingsSupplier.get();
            JsonElement settingsElement = gson.toJsonTree(settings, Settings.class);

            if (args.length == 0) {
                Objects.requireNonNullElseGet(
                        player,
                        Bukkit::getConsoleSender).sendMessage("§7" +
                        gson.toJson(new JsonPath("/").navigate(settingsElement)));
            }
            else if (args[0].charAt(0) == '/') {
                Objects.requireNonNullElseGet(
                        player,
                        Bukkit::getConsoleSender).sendMessage("§7" +
                        gson.toJson(new JsonPath(args[0]).navigate(settingsElement)));
            }
            else {
                JsonPatch patch = gson.fromJson(
                        String.join(" ", args),
                        JsonPatch.class);

                settingsConsumer.accept(
                        gson.fromJson(patch.apply(settingsElement), Settings.class));
            }

            return true;
        }
        catch (Exception ex) {
            if (player != null)
                player.sendMessage("§7" + ex.toString());

            throw ex;
        }
    }
}
