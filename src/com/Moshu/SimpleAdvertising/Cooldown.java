package com.Moshu.SimpleAdvertising;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Cooldown {

    public static Map<String, Long> cooldowns = new HashMap<>();

    public static Main plugin;

    public Cooldown(Main plugin) {
        Cooldown.plugin = plugin;
    }

    private UUID uuid;
    private String name;
    private int seconds;

    /**
     * Easily set cooldowns for anything
     *
     * @param uuid,    the player's uuid
     * @param name,    the cooldown's name
     * @param seconds, the time in seconds for the cooldown
     */
    public Cooldown(UUID uuid, String name, int seconds) {
        this.uuid = uuid;
        this.name = name;
        this.seconds = seconds;
    }

    /**
     * Sets the cooldown as active
     */
    public void set() {
        setCooldowns(uuid, name, seconds);
    }

    /**
     * Checks if the player has an active cooldown of this type
     *
     * @return true/false
     */
    public boolean has() {
        return hasCooldown(uuid, name);
    }

    /**
     * Removes the cooldown if the time ran up
     */
    public void remove() {

        String code = uuid.toString() + name;


        if (cooldowns.containsKey(code)) {
            cooldowns.remove(code);
        }

    }

    public String getName() {
        return name;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMinutes() {
        return (int) TimeUnit.SECONDS.toMinutes(seconds);
    }

    public int getHours() {
        return (int) TimeUnit.SECONDS.toHours(seconds);
    }

    public int getDays() {
        return (int) TimeUnit.SECONDS.toDays(seconds);
    }

    public static long getExpiryTime(String uuid, String name)
    {
        return cooldowns.get(uuid + name);
    }

    /**
     * Sends an error message to the player.
     */
    public void error() {
        getPlayer().sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&c&lHey! &fMai ai de asteptat &c" + formatRemainingTime(cooldowns.get(uuid.toString() + name)) + "&f minute."));
        Utils.sendSound(getPlayer());
    }


    /**
     * This can be used for every case, while
     * the old method works only for Kits.
     *
     * @param uuid    player's uuid
     * @param name    cooldown name
     * @param seconds the cooldown time in seconds
     */

    public static void setCooldowns(UUID uuid, String name, int seconds) {

        String id = uuid.toString();
        long time = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds);
        String code = id + name;

        cooldowns.put(code, time);

    }

    /**
     * @param uuid the player's uuid
     * @param name cooldown name
     * @return true if the player has a cooldown active
     * and false if he doesn't
     */

    public static boolean hasCooldown(UUID uuid, String name) {

        String id = uuid.toString();
        String code = id + name;


        if (cooldowns.containsKey(code)) {

            if (System.currentTimeMillis() >= cooldowns.get(code)) {
                return false;
            }

            return true;

        } else {
            return false;
        }


    }


    public static String formatRemainingTime(long time)
    {

        time = time - System.currentTimeMillis();
        SimpleDateFormat f = new SimpleDateFormat("mm:ss");
        Date d = new Date(time);

        return f.format(d);

    }
}



