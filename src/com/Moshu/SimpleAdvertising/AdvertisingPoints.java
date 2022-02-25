package com.Moshu.SimpleAdvertising;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatColor;

public class AdvertisingPoints implements CommandExecutor {

	static int points;
	private static Main plugin;
	
	public AdvertisingPoints(Main plugin)
	{
		this.plugin = plugin;
	}
	
	public static File dataf;
	
	public static int lookPoints(Player p)
	{
	
		String uuid = p.getUniqueId().toString();
		points = plugin.getData().getInt(uuid + ".points");
		return points;
	}
	
	public static void takePoints(Player p, int pointsToTake)
	{
		String uuid = p.getUniqueId().toString();
		points = plugin.getData().getInt(uuid + ".points");
		
		int removePoints = points - pointsToTake;
		
		dataf = new File(plugin.getDataFolder(), "data.yml");
		
		plugin.getData().set(uuid + ".points", removePoints);
		
		 plugin.getData().options().copyDefaults(true);
			try {
				plugin.getData().save(dataf);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	}
	
	public static void givePoints(Player p, int pointsToGive)
	{
		String uuid = p.getUniqueId().toString();
		
		dataf = new File(plugin.getDataFolder(), "data.yml");
		
		points = plugin.getData().getInt(uuid + ".points");
		
		int addPoints = points + pointsToGive;
		
		plugin.getData().set(uuid + ".points", addPoints);
		
		 plugin.getData().options().copyDefaults(true);
			try {
				plugin.getData().save(dataf);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	}
	
	public static void setPoints(Player p, int points)
	{		
		
		String uuid = p.getUniqueId().toString();
		
		dataf = new File(plugin.getDataFolder(), "data.yml");
		
		plugin.getData().set(uuid + ".points", points);
		
		 plugin.getData().options().copyDefaults(true);
		 
			try {
				plugin.getData().save(dataf);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
	{
		
		String prefix = plugin.getConfig().getString("messages.prefix");
		int fadein = plugin.getConfig().getInt("titles.fade-in");
		int stay = plugin.getConfig().getInt("titles.stay");
		int fadeout = plugin.getConfig().getInt("titles.fade-out");
		
	if(cmd.getName().equalsIgnoreCase("points"))
	{
		if(!(sender instanceof Player))
		{
			Utils.sendNotPlayer();
			return true;
		}
		
		Player p = (Player) sender;
		
		if(!p.hasPermission("simplead.points"))
		{
			Utils.sendNoAccess(p);
			return true;
		}
		
		if(args.length == 0)
		{
			
			String z = plugin.getConfig().getString("points.messages.look-points.title");
			int i = lookPoints(p);
			
	     	z = z.replace("{player}", p.getName());
	     	z = z.replace("{points}", Integer.toString(i));
	     	z = z.replace("{prefix}", prefix);
	     	z = Utils.format(z);
	     	
			String m = Utils.format(plugin.getConfig().getString("points.messages.look-points.subtitle"));
	     	m = m.replace("{player}", p.getName());
	     	m = m.replace("{points}", Integer.toString(i));
	     	m = m.replace("{prefix}", prefix);
			
			p.sendTitle(z, m, fadein, stay, fadeout);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("give"))
		{
			
			if(args.length == 1)
			{
				String usageGive = plugin.getConfig().getString("points.messages.usage-give");
				p.sendMessage(Utils.format(prefix + usageGive));
				return true;
			}
			
			if(args.length == 3 && args[1].equals("*"))
			{
				if(Utils.isInt(args[2]))
				{
				int i = Integer.parseInt(args[2]);				
				
				for(Player s : Bukkit.getOnlinePlayers())
				{
					
					
					String z = Utils.format(plugin.getConfig().getString("points.messages.give-all-points.received.title"));
					
					z = z.replace("{player}", p.getName());
			     	z = z.replace("{points}", Integer.toString(i));
			     	z = z.replace("{prefix}", prefix);	     	
					
			     	String m = Utils.format(plugin.getConfig().getString("points.messages.give-all-points.received.subtitle"));
					
					m = m.replace("{player}", p.getName());
			     	m = m.replace("{points}", Integer.toString(i));
			     	m = m.replace("{prefix}", prefix);
					

	     	    	 p.sendTitle(z, m, fadein, stay, fadeout);
	     	    	 givePoints(s, i);

					
				}
				
				String z = Utils.format(plugin.getConfig().getString("points.messages.give-all-points.sent.title"));
				
				z = z.replace("{player}", p.getName());
		     	z = z.replace("{points}", Integer.toString(i));
		     	z = z.replace("{prefix}", prefix);
				
		     	String m = Utils.format(plugin.getConfig().getString("points.messages.give-all-points.sent.subtitle"));
				
				m = m.replace("{player}", p.getName());
		     	m = m.replace("{points}", Integer.toString(i));
		     	m = m.replace("{prefix}", prefix);
				
				p.sendTitle(z, m, fadein, stay, fadeout);
				
				}
				return true;
			}
			
			Player t = Bukkit.getPlayer(args[1]);
			
			if(t == null)
			{
				Utils.sendTargetNull(p);
				return true;
			}
			
			if(Utils.isInt(args[2]))
			{
			int i = Integer.parseInt(args[2]);
			
			givePoints(t, i);
			
			String z = Utils.format(plugin.getConfig().getString("points.messages.give-points.title"));
			
			z = z.replace("{player}", p.getName());
	     	z = z.replace("{points}", Integer.toString(i));
	     	z = z.replace("{prefix}", prefix);
			
	     	String m = Utils.format(plugin.getConfig().getString("points.messages.give-points.subtitle"));
			
			m = m.replace("{player}", p.getName());
	     	m = m.replace("{points}", Integer.toString(i));
	     	m = m.replace("{prefix}", prefix);
			
			String s = Utils.format(plugin.getConfig().getString("points.messages.receive-points.title"));
			
			s = s.replace("{player}", p.getName());
	     	s = s.replace("{points}", Integer.toString(i));
	     	s = s.replace("{prefix}", prefix);
			
	     	String j = Utils.format(plugin.getConfig().getString("points.messages.receive-points.subtitle"));
			
			j = j.replace("{player}", p.getName());
	     	j = j.replace("{points}", Integer.toString(i));
	     	j = j.replace("{prefix}", prefix);
			
                     p.sendTitle(z, m, fadein, stay, fadeout);
	     			 t.sendTitle(s, j, fadein, stay, fadeout); 		
			}
			
			else
				
			{
			
				String usageGive = plugin.getConfig().getString("points.messages.usage-give");
				p.sendMessage(Utils.format(prefix + usageGive));	
				return true;
			}
			
			return true;
		}
		else if(args[0].equalsIgnoreCase("take"))
		{
			
			if(args.length == 1)
			{
				String usageTake = plugin.getConfig().getString("points.messages.usage-take");
				p.sendMessage(Utils.format(prefix + usageTake));
				return true;
			}
			
           Player t = Bukkit.getPlayer(args[1]);
			
			if(t == null)
			{
				Utils.sendTargetNull(p);
				return true;
			}
			
			if(Utils.isInt(args[2]))
			{
				int i = Integer.parseInt(args[2]);
				
				if(i <= 0)
				{
					String s = plugin.getConfig().getString("points.messages.negative-int");
					p.sendMessage(Utils.format(prefix + s));					
					return true;
				}
				
				if(lookPoints(t) < i)
				{
					String message2 = Utils.format(plugin.getConfig().getString("points.messages.insufficient-points")); 
					p.sendMessage(message2);	
					return true;
				}
			
				String z = plugin.getConfig().getString("points.messages.take-points.title");
		     	z = z.replace("{player}", p.getName());
		     	z = z.replace("{points}", Integer.toString(i));
		     	z = z.replace("{prefix}", prefix);
		     	z = Utils.format(z);
		     	
				String m = plugin.getConfig().getString("points.messages.take-points.subtitle");
				m = m.replace("{player}", p.getName());
		     	m = m.replace("{points}", Integer.toString(i));
				m = Utils.format(m);
				
				p.sendTitle(z, m, fadein, stay, fadeout);
		     	takePoints(t, i);
			}
			
			else
				
			{
			
				String usageTake = plugin.getConfig().getString("points.messages.usage-take");
				p.sendMessage(Utils.format(prefix + usageTake));
				return true;
			}
			return true;
		}
		else if(args[0].equalsIgnoreCase("set"))
		{
			
			if(args.length == 1)
			{
				String s = plugin.getConfig().getString("points.messages.usage-set");
				p.sendMessage(Utils.format(prefix + s));
				return true;
			}
			
            Player t = Bukkit.getPlayer(args[1]);
			
			if(t == null)
			{
				Utils.sendTargetNull(p);
				return true;
			}
			
			if(Utils.isInt(args[2]))
			{
				int i = Integer.parseInt(args[2]);
		     	setPoints(t, i);
		     	
		     	String z = plugin.getConfig().getString("points.messages.set-points.title");
		     	z = z.replace("{player}", p.getName());
		     	z = z.replace("{points}", Integer.toString(i));
		     	z = z.replace("{prefix}", prefix);
		     	z = Utils.format(z);
		     	
				String m = plugin.getConfig().getString("points.messages.set-points.subtitle");
				m = m.replace("{player}", p.getName());
		     	m = m.replace("{points}", Integer.toString(i));
				m = Utils.format(m);
				
				p.sendTitle(z, m, fadein, stay, fadeout);
			}
		}
		return true;
	}
	return true;
	}
	
}
