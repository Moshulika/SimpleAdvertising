package com.Moshu.SimpleAdvertising;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;

public class AutoMessage {

	private static Main plugin;
	
	public AutoMessage(Main plugin)
	{
		this.plugin = plugin;
	}
	
	public void start()
	{
		
		int price = plugin.getConfig().getInt("advertising.price");
		String prefix = plugin.getConfig().getString("messages.prefix");
		int interval = plugin.getConfig().getInt("auto-advertiser.chat-interval");
		int mpo = plugin.getConfig().getInt("auto-advertiser.minimum-players-online");
		
		List<String> strings = new ArrayList<>();
		
		for (String string : plugin.getConfig().getStringList("auto-advertiser.chat-messages")) 
				{
			
			String pret = Integer.toString(price);
			string = string.replace("{price}", pret);
			string = string.replace("{prefix}", prefix);
		    strings.add(Utils.format(string));
		}
		

	Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
     	
		 public void run() {
 
			 
			 if(plugin.getConfig().getBoolean("auto-advertiser.chat") == false)
			    {
			    
				 return;
				 
			    }

			 
			 if(Bukkit.getOnlinePlayers().size() >= mpo)
			 {
			 
			 Random r = new Random();
			 String i = strings.get(r.nextInt(strings.size()));
			 
			for(Player p : Bukkit.getOnlinePlayers())
			{			
				if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
				{
				i = PlaceholderAPI.setPlaceholders(p, i);
				}
				
				p.sendMessage(i);
								
			}
			
			}
    	}
    }, 0, interval * 20);  
	}
	
	
}
