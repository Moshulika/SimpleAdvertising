package com.Moshu.SimpleAdvertising;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

	Economy econ;
	
	Advertising ad = new Advertising(this);
	
	public Advertising getAdvertisingClass()
	{
		return ad;
	}
	
	Broadcast bc = new Broadcast(this);
	
	public Broadcast getBroadcastClass()
	{
		return bc;
	}
	
	Events evn = new Events(this);
	
	public Events getEventsClass()
	{
		return evn;
	}
	
	Utils ut = new Utils(this);
	
	public Utils getUtilsClass()
	{
		return ut;
	}
	
	Updater up = new Updater(this);
	
	public Updater getUpdaterClass()
	{
		return up;
	}
	
	AutoMessage am = new AutoMessage(this);
	
	public AutoMessage getAutoMessageClass()
	{
		return am;
	}
	
	AutoTitles at = new AutoTitles(this);
	
	public AutoTitles getAutoTitlesClass()
	{
		return at;
	}
	
	AdvertisingPoints pts = new AdvertisingPoints(this);
	
	public AdvertisingPoints getPointsClass()
	{
		return pts;
	}
	
	Debug dbg = new Debug(this);
	
	public Debug getDebugClass()
	{
		return dbg;
	}
		
	
	public boolean hasDependencies()
    {
         
         if(!setupEconomy())
         {
        	 return false;
         }

        else if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null)
        {
            return false;
        }

        return true;
    }
	
	
	public static void consoleMessage(String s)
    {
        Bukkit.getConsoleSender().sendMessage(Utils.format(s));
    }
	
	
	
	public void onEnable() {

		PluginDescriptionFile pdfFile = getDescription();

		String msg = Utils.format("&cSimpleAdvertising &fstarted successfuly");
		String msg1 = Utils.format("&fVersion: &c" + pdfFile.getVersion());
		String msg2 = Utils.format("&cFor news: &7https://www.spigotmc.org/resources/simple-advertising.40414/");
		String msg4 = Utils.format("&fMade by &cMoshu&f, this is the real deal.");

		Bukkit.getConsoleSender().sendMessage(Utils.format("&c&l* " + msg));
		Bukkit.getConsoleSender().sendMessage(Utils.format("&c&l* " + msg1));
		Bukkit.getConsoleSender().sendMessage(Utils.format("&c&l* " + msg2));
		Bukkit.getConsoleSender().sendMessage(Utils.format("&c&l* " + msg4));

		if (!hasDependencies()) {


			if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
				consoleMessage("&6&lWarning: &fYou don't have PAPI installed, placeholders disabled");
			} else if (!setupEconomy()) {
				consoleMessage("&6&lWarning: &FYou don't have an economy handler installed, plugin is shutting down.");
				Bukkit.getPluginManager().disablePlugin(this);

			}

		}

		up.check();

		createFiles();
		metrics();

		if (getConfig().getBoolean("auto-advertiser.chat") == true) {
			am.start();
		}

		if (getConfig().getBoolean("auto-advertiser.titles") == true) {
			at.start();
		}

		setupEconomy();
		ut.createConfig();

		getCommand("ad").setExecutor(ad);

		if(getConfig().getBoolean("broadcast.enabled", true)) {
			getCommand("broadcast").setExecutor(bc);
		}

		getCommand("points").setExecutor(pts);

		getCommand("ad").setTabCompleter(new TabComplete());
		getCommand("points").setTabCompleter(new TabComplete());

		Bukkit.getServer().getPluginManager().registerEvents(evn, this);
		Bukkit.getServer().getPluginManager().registerEvents(up, this);

		RegisteredServiceProvider rsp = this.getServer().getServicesManager().getRegistration(Economy.class);

		econ = (Economy) rsp.getProvider();

		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && Bukkit.getPluginManager().getPlugin("PlaceholderAPI").isEnabled()) {
			new Placeholders().register();
		}

	}
	
	public void onDisable()
	{
		Bukkit.getConsoleSender().sendMessage(Utils.format("&cSimpleAdvertising &fis disabling")); 
	}
	
	public void metrics()
	{
				
		int pluginId = 7360; 
        Metrics metrics = new Metrics(this, pluginId);
        
        if(metrics.isEnabled())
        {
    		Bukkit.getConsoleSender().sendMessage(Utils.format("&c&lMetrics: &fMetrics are enabled"));
        }
        
	}
	
	
	 public boolean setupEconomy() 
	 {
	        if (getServer().getPluginManager().getPlugin("Vault") == null) 
	        {
	            return false;
	        }
	        
	        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
	        
	        if (rsp == null) {
	            return false;
	        }
	        
	         econ = (Economy)rsp.getProvider();
	         
	        if (econ != null) 
	        {
	            return true;
	        }
	        
	        return false;
	 }
	 
	 public FileConfiguration getData() {
	        return this.data;
	    }
	 
	    private File dataf;
		private FileConfiguration data;
	 
	 public void createFiles() {
        
	        dataf = new File(getDataFolder(), "data.yml");
	        
	        if (!dataf.exists()) {
	            dataf.getParentFile().mkdirs();
	            saveResource("data.yml", false);
	            Bukkit.getConsoleSender().sendMessage(Utils.format("&6&l* &cData.yml &fnot found, creating."));
	         }
	        
	        data = new YamlConfiguration();
	        
	        try {
          
	            try {
					data.load(dataf);
				} catch (InvalidConfigurationException e) {
			
					e.printStackTrace();
				}

	            
	        } catch (IOException e) 
	        {
	            e.printStackTrace();
	        }
	    }


	 public static void reloadFiles()
		{

		 Plugin pl = Bukkit.getPluginManager().getPlugin("SimpleAdvertising");
		 
		 File configf = new File(pl.getDataFolder(), "config.yml");
		 
		 YamlConfiguration config = YamlConfiguration.loadConfiguration(configf);
		 
		 try {
			 
			config.save(configf);
			
		} 
		 catch (IOException e) 
		{
			e.printStackTrace();
		}
		 
		 config = YamlConfiguration.loadConfiguration(configf);
		 
		}
	
}
