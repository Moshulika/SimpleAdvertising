package com.Moshu.SimpleAdvertising;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {

	private static Main plugin;
	
	public Utils(Main plugin)
	{
		this.plugin = plugin;
	}
	
	
	public static double getUsedCPU()
    {
        try
        {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
            AttributeList list = mbs.getAttributes(name, new String[] { "ProcessCpuLoad" });
            Attribute att = (Attribute)list.get(0);
            Double value = (Double)att.getValue();

            return (int)(value.doubleValue() * 1000.0D) / 10.0D;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0.0D;
    }
	
	public static long getMaxMemory()
    {
        try
        {
            Runtime r = Runtime.getRuntime();
            return r.maxMemory() / 1048576L;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0L;
    }

	public static String format(String message) {

		if (message == null || message.length() == 0) return message;

		Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
		Matcher matcher = pattern.matcher(message);

		String color;

		while (matcher.find()) {
			color = message.substring(matcher.start(), matcher.end());
			message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
			matcher = pattern.matcher(message);
		}


		return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);


	}

    public static long getMemoryUsed()
    {
        try
        {
            Runtime r = Runtime.getRuntime();
            return (r.totalMemory() - r.freeMemory()) / 1048576L;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0L;
    }


	public static void fillWithGlassLegacy(Inventory inv)
	{

		Material mat;

		if(plugin.getConfig().getString("gui.fill-item") == null || Material.matchMaterial(plugin.getConfig().getString("gui.fill-item")) == null)
		{
			mat = Material.BLACK_STAINED_GLASS_PANE;
		}
		else
		{
			mat = Material.matchMaterial(plugin.getConfig().getString("gui.fill-item"));
		}

		ItemStack sticla = new ItemStack(mat);

		ItemMeta sticlam = sticla.getItemMeta();
		sticlam.setDisplayName(" ");
		sticlam.getItemFlags().add(ItemFlag.HIDE_ATTRIBUTES);
		sticla.setItemMeta(sticlam);

		Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
		{
			for(int i = 0; i < inv.getSize(); i++)
			{

				if(inv.getItem(i) == null || inv.getItem(i).getType() == Material.AIR)
				{

					inv.setItem(i, sticla);

				}

			}
		});
	}
	
	public static void createInv(Player p) {

		String title = plugin.getConfig().getString("gui.inventory-name");
		String item = plugin.getConfig().getString("gui.item");
		String name = plugin.getConfig().getString("gui.item-name");
		int price = plugin.getConfig().getInt("advertising.price");

		if (Material.matchMaterial(item) == null) {
			Bukkit.getConsoleSender().sendMessage(Utils.format("&cError: &fItem material name is invalid"));
			return;
		}

		Material m = Material.matchMaterial(item);

		Inventory inv = Bukkit.createInventory(null, 27, title);

		ItemStack itm = new ItemStack(m);
		ItemMeta meta = itm.getItemMeta();

		meta.setDisplayName(Utils.format(name));

		List<String> strings = new ArrayList<>();

		for (String string : plugin.getConfig().getStringList("gui.item-lore")) {
			String pret = Integer.toString(price);
			string = string.replace("{price}", pret);
			strings.add(Utils.format(string));
		}

		meta.setLore(strings);

		itm.setItemMeta(meta);

		inv.setItem(13, itm);

		Utils.fillWithGlassLegacy(inv);

		p.openInventory(inv);

	}
		
	public static void addToData(UUID uuid, String ad)
	{
		int ads = plugin.getData().getInt(uuid + ".ads-created") + 1;
		plugin.getData().set(uuid + ".lastest-ad", ad);
		plugin.getData().set(uuid + ".ads-created", ads);
	}
	
	public static void sendSound(Player p)
	{
		if(plugin.getConfig().getString("enable.sounds").equalsIgnoreCase("true"))
        {
			try {
						
            p.playSound(p.getLocation(), Sound.valueOf(plugin.getConfig().getString("advertising.sound", "BLOCK_NOTE_BLOCK_PLING")), (float) plugin.getConfig().getDouble("advertising.volume", 1), (float) plugin.getConfig().getDouble("advertising.pitch", 1));
        
			}
			catch(Exception e)
			{
				Bukkit.getConsoleSender().sendMessage(Utils.format("&cError: &fInvalid sound. Use one from here: &chttps://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html"));
				p.sendMessage(Utils.format("&c&lOops! &7The sound in your config is invalid, check again."));	
			}
        }
      
	}

	public static void errorAsItem(ItemStack is, String error)
	{

		Material initialmat = is.getType();
		String initialname = is.getItemMeta().getDisplayName();

		ItemMeta im = is.getItemMeta();

		is.setType(Material.BARRIER);
		im.setDisplayName(Utils.format("&c" + error));
		is.setItemMeta(im);

		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
		{
			is.setType(initialmat);
			im.setDisplayName(Utils.format(initialname));
			is.setItemMeta(im);
		}, 100);

		return;


	}
	
	public static String getIp(Player p) {
        String ipAddr = p.getAddress().getHostName();
        return ipAddr;
    }
	
	public static void sendNoAccess(Player p)
	{
		int fadein = plugin.getConfig().getInt("titles.fade-in");
		int stay = plugin.getConfig().getInt("titles.stay");
		int fadeout = plugin.getConfig().getInt("titles.fade-out");
		
		String i = Utils.format(plugin.getConfig().getString("messages.no-permission.title"));
		String m = Utils.format(plugin.getConfig().getString("messages.no-permission.subtitle"));
		sendSound(p);
		p.sendTitle(i, m, fadein, stay, fadeout);
	}
	
	public static void sendTargetNull(Player p)
	{
		String k = plugin.getConfig().getString("messages.target-null.title");
		String c = plugin.getConfig().getString("messages.target-null.subtitle");
		
		int fadein = plugin.getConfig().getInt("titles.fade-in");
		int stay = plugin.getConfig().getInt("titles.stay");
		int fadeout = plugin.getConfig().getInt("titles.fade-out");
		
		String i = Utils.format(k);
		String m = Utils.format(c);
		sendSound(p);
		p.sendTitle(i, m, fadein, stay, fadeout);
	}
	
	public static boolean isInt(String str)
    {
        try
        {
            Integer.parseInt(str);
            return true;
        }
        catch (NumberFormatException e) {}
        return false;
    }
	
	public static void sendNotPlayer()
    {
        Bukkit.getConsoleSender().sendMessage(Utils.format("&c&l* &fTrebuie sa fii jucator pentru a accesa aceasta comanda."));
    }
	
	public void createConfig() { 
	    try {
	        if (!plugin.getDataFolder().exists()) {
	        	plugin.getDataFolder().mkdirs();
	        }
	        File file = new File(plugin.getDataFolder(), "config.yml");
	        if (!file.exists()) {
	        	Bukkit.getConsoleSender().sendMessage(Utils.format("&c&l* &cConfig.yml &fnot found, creating"));
	        	plugin.saveDefaultConfig();
	        } else {
	        	Bukkit.getConsoleSender().sendMessage(Utils.format("&c&l* &cConfig.yml &ffound, loading"));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();

	    }

	}
 
 public static void logToFile(String message)
    
    {
 if (plugin.getConfig().getString("enable.logging").equalsIgnoreCase("true")) {
        try
        {
            File dataFolder = plugin.getDataFolder();
            if(!dataFolder.exists())
            {
                dataFolder.mkdir();
            }
 
            File saveTo = new File(plugin.getDataFolder(), "logs.txt");
            if (!saveTo.exists())
            {
                saveTo.createNewFile();
            }
 
 
            FileWriter fw = new FileWriter(saveTo, true);
 
            PrintWriter pw = new PrintWriter(fw);
 
            pw.println(message);
 
            pw.flush();
 
            pw.close();
 
        } catch (IOException e)
        {
 
            e.printStackTrace();
        } 
        	
                   
        }
 
    }
 
	
}
