package me.wyoslovescoffee.playerinactivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

public final class PlayerInactivity extends JavaPlugin {

    @Override
    public void onEnable() {


        String currentDirectory = System.getProperty("user.dir");

        Date today = new Date();

        try {
            // Read the JSON file
            JsonArray jsonArray = new Gson().fromJson(new FileReader("whitelist.json"), JsonArray.class);

            // Loop through the JSON array
            for (JsonElement jsonElement : jsonArray) {

                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String name = jsonObject.get("name").getAsString();
                String playerUUID = jsonObject.get("uuid").getAsString();

                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);

                if (!offlinePlayer.hasPlayedBefore()) {
                    //Ignores players who have not logged in yet (recently whitelisted)
                    continue;

                } else if (offlinePlayer.isOp()) {

                    continue; //If player is OP, ignore

                }

                long thirtyDaysMillis = 30L * 24L * 60L * 60L * 1000L; // 30 days in milliseconds

                long currentTimeMillis = System.currentTimeMillis(); //Current time the plugin was loading (in ms)

                long lastJoinMillis = offlinePlayer.getLastPlayed(); //Time in ms that player last logged on

               // Only used for testing -----> System.out.println(name + " last login " + lastJoinMillis);

                if (lastJoinMillis < (currentTimeMillis - thirtyDaysMillis)) {
                    //If the player has not logged on in the past 30 days

                    Bukkit.getOfflinePlayer(name).setWhitelisted(false); //This is deprecated, but will work for now

                    System.out.println(name + " has been removed for inactivity!"); //To log removals

                } else {

                    continue;

                }

            }



        } catch (IOException e) {

            e.printStackTrace();

        }




    }





}