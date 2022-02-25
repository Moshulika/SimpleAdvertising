package com.Moshu.SimpleAdvertising;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class Placeholders extends PlaceholderExpansion {

	Plugin plugin = Bukkit.getPluginManager().getPlugin("SimpleAdvertising");
	
	@Override
	public String getAuthor() {
	
		return "Moshu";
	}

	@Override
	public String getIdentifier() {
	
		return "simplead";
	}

	@Override
	public String getVersion() {
		return plugin.getDescription().getVersion().toString();
	}
	
	@Override
    public boolean persist()
	{
        return true;
    }
	
	@Override
    public boolean canRegister(){
        return true;
    }

	@Override
    public String onPlaceholderRequest(Player player, String identifier){

        if(player == null){
            return "";
        }

        if(identifier.equals("price"))
        {

        	return Integer.toString(plugin.getConfig().getInt("advertising.price"));
        
        }

 
        if(identifier.equals("cooldown"))
        {
        	return Integer.toString(plugin.getConfig().getInt("advertising.cooldown"));
        }
        
        if(identifier.equals("currentad"))
        {
            return Advertising.getCurrentAd();
        }
        
        if(identifier.equals("currentplayer"))
        {
            return Advertising.getCurrentAdPlayer();
        }
        
 

        return null;
    }
	
}
