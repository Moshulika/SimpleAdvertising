package com.Moshu.SimpleAdvertising;

import org.bukkit.entity.Player;

/**
 * Everything you need
 */
public class AdvertisingAPI {

    /**
     * Send all the players online an ad, using the format defined in the config
     * @param args the arguments of the ad, you can just use String#split(" ") in order to get the array
     * @param p the player which sends the ad
     */
    public static void sendAd(String[] args, Player p)
    {
        Advertising.send(args, p);
    }

    /**
     * Checks if the player has an active cooldown
     * @param p the player to check
     * @return true/false
     */
    public static boolean hasCooldown(Player p)
    {
        return Cooldown.hasCooldown(p.getUniqueId(), "ad");
    }

    /**
     * Open the advertising GUI
     * @param p the player affected
     */
    public static void openGUI(Player p)
    {
        Utils.createInv(p);
    }

    /**
     * Get a player's adevrtising points
     * @param p the player affected
     * @return the points the player has
     */
    public static int getPoints(Player p)
    {
        return AdvertisingPoints.lookPoints(p);
    }

    /**
     * Take points from a player
     * @param p the player
     * @param pointsToTake the number of points to take
     */
    public static void takePoints(Player p, int pointsToTake)
    {
        AdvertisingPoints.takePoints(p, pointsToTake);
    }

    /**
     * Give points to a player
     * @param p the player
     * @param pointsToGive the number of points to give
     */
    public static void givePoints(Player p, int pointsToGive)
    {
        AdvertisingPoints.givePoints(p, pointsToGive);
    }

    /**
     * Set the player's points
     * @param p the player
     * @param points the points the player will have
     */
    public static void setPoints(Player p, int points)
    {
        AdvertisingPoints.setPoints(p, points);
    }

}
