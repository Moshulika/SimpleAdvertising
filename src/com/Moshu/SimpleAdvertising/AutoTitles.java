package com.Moshu.SimpleAdvertising;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;

public class AutoTitles {

	private static Main plugin;
	
	public AutoTitles(Main plugin)
	{
		this.plugin = plugin;
	}
	
	public void start()
	{
		
		int price = plugin.getConfig().getInt("advertising.price");
		String prefix = plugin.getConfig().getString("messages.prefix");
		int interval = plugin.getConfig().getInt("auto-advertiser.titles-interval");
		
		int fadein = plugin.getConfig().getInt("titles.fade-in");
		int stay = plugin.getConfig().getInt("titles.stay");
		int fadeout = plugin.getConfig().getInt("titles.fade-out");
		
		int mpo = plugin.getConfig().getInt("auto-advertiser.minimum-players-online");
		
		List<String> strings1 = new ArrayList<>();
		for (String string : plugin.getConfig().getStringList("auto-advertiser.title-messages")) 
				{
			
			String pret = Integer.toString(price);
			string = string.replace("{price}", pret);
			string = string.replace("{prefix}", prefix);
		    strings1.add(Utils.format(string));
		}
		
		List<String> strings2 = new ArrayList<>();
		
		for (String string : plugin.getConfig().getStringList("auto-advertiser.subtitle-messages")) 
				{
			
			String pret = Integer.toString(price);
			string = string.replace("{price}", pret);
			string = string.replace("{prefix}", prefix);
		    strings2.add(Utils.format(string));
		}

	Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
     	
		 public void run() {
			 
			    if(plugin.getConfig().getBoolean("auto-advertiser.titles") == false)
			    {
			    return;
			    }
			 
			 if(Bukkit.getOnlinePlayers().size() >= mpo)
			 {
			 
			 Random r = new Random();
				
				int l = r.nextInt(strings1.size());
				
				String i = strings1.get(l);
			
				String m = strings2.get(l);
			 
			for(Player p : Bukkit.getOnlinePlayers())
			{		
				if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
				{
				i = PlaceholderAPI.setPlaceholders(p, i);
				m = PlaceholderAPI.setPlaceholders(p, m);
				}
				
				p.sendTitle(i, m, fadein, stay, fadeout);
			}

			 }
			 else
			 {
				 return;
			 }
    	}
    }, 0, interval * 20);  
	}
}
