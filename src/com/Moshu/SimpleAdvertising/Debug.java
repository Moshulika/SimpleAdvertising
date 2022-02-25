package com.Moshu.SimpleAdvertising;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class Debug {
	
	private static Main plugin;
	
	public Debug(Main plugin)
	{
		this.plugin = plugin;
	}
	
	public static void sendServerInfo(Player p)
    {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable()
        {
            public void run()
            {
            	
            	PluginDescriptionFile pdf = plugin.getDescription();
            	
                p.sendMessage("");
                p.sendMessage(Utils.format("&8&l&m-----------------------------"));

                p.sendMessage(Utils.format("&aServer name: &7" + Bukkit.getServer().getName()));
                p.sendMessage(Utils.format("&aGamemode: &7" + Bukkit.getDefaultGameMode().toString()));
                p.sendMessage(Utils.format("&aVersion: &7" + Bukkit.getVersion().replace("-SNAPSHOT", "")));
                p.sendMessage(Utils.format("&aPlayers: &7" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers()));
                p.sendMessage(Utils.format("&aPort: &7" + Bukkit.getPort()));
                p.sendMessage(Utils.format("&aPlugins &8(&7" + Bukkit.getPluginManager().getPlugins().length + "&8) "));
                p.sendMessage(Utils.format("&aPlugin Version: &7" + pdf.getVersion()));
                p.sendMessage(Utils.format("&aDependencies: &7" + plugin.hasDependencies()));

                int loadedChunks = 0;
                int entities = 0;

                for (World w : Bukkit.getWorlds())
                {
                    loadedChunks += w.getLoadedChunks().length;
                    entities += w.getEntities().size();
                }
                p.sendMessage(Utils.format("&aChunks &8(&7" + loadedChunks + "&8) "));
                p.sendMessage(Utils.format("&aEntities &8(&7" + entities + "&8) "));

                p.sendMessage(Utils.format("&aWorlds &8(&7" + Bukkit.getWorlds().size() + "&8) "));

                for (World w : Bukkit.getWorlds())
                {
                    p.sendMessage(Utils.format("&8  - &7" + w.getName() + ", &f" + w.getLoadedChunks().length + "&7 Chunks, &f" + w.getEntities().size() + "&7 Entities."));
                }
                try
                {
                    p.sendMessage(Utils.format("&aCPU: &7" + Utils.getUsedCPU() + "%"));
                }
                catch (Exception e)
                {
                    p.sendMessage(Utils.format("&aCPU: &7Error"));
                }
                try
                {
                    long memUsed = Utils.getMemoryUsed();
                    long maxMem = Utils.getMaxMemory();
                    p.sendMessage(Utils.format("&aRAM: &7" + memUsed + "MB/" + maxMem + "MB"));
                }
                catch (Exception e)
                {
                    p.sendMessage(Utils.format("&aRAM: &7Error"));
                }
                p.sendMessage(Utils.format("&8&l&m-----------------------------"));
                p.sendMessage("");
            }
        });
    }
	
	

}
