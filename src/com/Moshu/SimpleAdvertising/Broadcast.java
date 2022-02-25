package com.Moshu.SimpleAdvertising;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class Broadcast implements CommandExecutor {
	
	private static Main plugin;
	
	public Broadcast(Main plugin)
	{
		this.plugin = plugin;
	}
   

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
	
		
		if(cmd.getName().equalsIgnoreCase("broadcast"))
		{
			
			
			String prefix = plugin.getConfig().getString("messages.prefix"); 
			
			String broadcastMsg = "";
			
			for (String a : args){
			     broadcastMsg += a+" ";
			}
			
			broadcastMsg = Utils.format(broadcastMsg);
			final String msg = broadcastMsg;
			
			if(!(sender instanceof Player))
			{
				
				
				if(args.length == 0)
            	{
            		sender.sendMessage(Utils.format(prefix + plugin.getConfig().getString("messages.empty-message")));
            		return true;
            	}
				else
				{
					if(plugin.getConfig().getString("broadcast.enable-titles").equalsIgnoreCase("true"))
	            	{
	            	
		            	if(broadcastMsg.length() > 42)
		    			{
		          		
		            		List<String> strings = new ArrayList<>();
		        			
		        			for (String string : plugin.getConfig().getStringList("broadcast.chat")) 
		        					{
		        				string = string.replace("{message}", broadcastMsg);
		        				strings.add(Utils.format(string));
		        					}
		            		
		            		for(Player p : Bukkit.getOnlinePlayers())
		            		{
		            			
		            			
		            			for(String s : strings)
		            			{
		            				p.sendMessage(s);
		            			}
		
		            		}
		           			}
		            			else
		            			{
		            				
		            			
		            				
				        			
				        			BukkitRunnable run = new BukkitRunnable() {
				        				
				        				int fadein = plugin.getConfig().getInt("titles.fade-in");
			        					int stay = plugin.getConfig().getInt("titles.stay");
			        					int fadeout = plugin.getConfig().getInt("titles.fade-out");
			        					
					        			String s = Utils.format(plugin.getConfig().getString("broadcast.title"));
					        			String a = plugin.getConfig().getString("broadcast.subtitle");
				        				
				        				public void run()
				        				{
				        					for(Player p : Bukkit.getOnlinePlayers())
				                    		{
				            					
						            			a = a.replace("{message}", msg);
						            			a = Utils.format(a);
						            			p.sendTitle(s, a, fadein, stay, fadeout);
						            			Utils.sendSound(p);
				                    		}	
				        				}
				        				
				        			};
				        			
				        			run.runTaskAsynchronously(plugin);
		            			
		            			}
	            	}
	            	else
	            	{
	            		
	            		
	            		List<String> strings = new ArrayList<>();
	        			
	        			for (String string : plugin.getConfig().getStringList("broadcast.chat")) 
	        					{
	        				string = string.replace("{message}", broadcastMsg);
	        				strings.add(Utils.format(string));
	        					}
	            		
	            		for(Player p : Bukkit.getOnlinePlayers())
	            		{
	            			
	            			
	            			for(String s : strings)
	            			{
	            				p.sendMessage(s);
	            			}
	            			

	            	
	            		}
	            		}
				}
				
				return true;
			}
			
			Player player = (Player) sender;
			
            if(!sender.hasPermission("simplead.broadcast"))
            {
            	Player p = (Player) sender;	
				Utils.sendNoAccess(p);
            	return true;
            }
            
            	if(args.length == 0)
            	{
            		player.sendMessage(Utils.format(prefix + plugin.getConfig().getString("messages.empty-message")));
            		return true;
            	}
            	else
            	{
            		
            	if(plugin.getConfig().getString("broadcast.enable-titles").equalsIgnoreCase("true"))
            	{
            	
	            	if(broadcastMsg.length() > 42)
	    			{
	          		
	            		List<String> strings = new ArrayList<>();
	        			
	        			for (String string : plugin.getConfig().getStringList("broadcast.chat")) 
	        					{
	        				string = string.replace("{message}", broadcastMsg);
	        				strings.add(Utils.format(string));
	        					}
	            		
	            		for(Player p : Bukkit.getOnlinePlayers())
	            		{
	            			
	            			
	            			for(String s : strings)
	            			{
	            				p.sendMessage(s);
	            			}
	
	            		}
	           			}
	            			else
	            			{
	            				
	            			
	            				
			        			
			        			BukkitRunnable run = new BukkitRunnable() {
			        				
			        				int fadein = plugin.getConfig().getInt("titles.fade-in");
		        					int stay = plugin.getConfig().getInt("titles.stay");
		        					int fadeout = plugin.getConfig().getInt("titles.fade-out");
		        					
				        			String s = Utils.format(plugin.getConfig().getString("broadcast.title"));
				        			String a = plugin.getConfig().getString("broadcast.subtitle");
			        				
			        				public void run()
			        				{
			        					for(Player p : Bukkit.getOnlinePlayers())
			                    		{
			            					
					            			a = a.replace("{message}", msg);
					            			a = Utils.format(a);
					            			p.sendTitle(s, a, fadein, stay, fadeout);
					            			Utils.sendSound(p);
			                    		}	
			        				}
			        				
			        			};
			        			
			        			run.runTaskAsynchronously(plugin);
	            			
	            			}
            	}
            	else
            	{
            		
            		
            		List<String> strings = new ArrayList<>();
        			
        			for (String string : plugin.getConfig().getStringList("broadcast.chat")) 
        					{
        				string = string.replace("{message}", broadcastMsg);
        				strings.add(Utils.format(string));
        					}
            		
            		for(Player p : Bukkit.getOnlinePlayers())
            		{
            			
            			
            			for(String s : strings)
            			{
            				p.sendMessage(s);
            			}
            			

            	
            		}
            		}
            	}
	
            }
	
	return true;
	}

}
