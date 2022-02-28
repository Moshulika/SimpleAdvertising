package com.Moshu.SimpleAdvertising;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Events implements Listener {

	public static Main plugin;
	
	public Events(Main plugin)
	{
		this.plugin = plugin;
	}
	
	static File dataf;
	Economy econ;
	
	String joinMessage;
	String quitMessage;
	String prefix;
	Date date = new Date();
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy / HH:mm:ss");
	ArrayList<String> in = new ArrayList<String>();
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
    	if(plugin.getConfig().getString("enable.welcomers").equalsIgnoreCase("true"))
    	{
    	 joinMessage = plugin.getConfig().getString("messages.join");
    	 joinMessage = joinMessage.replace("{player}", e.getPlayer().getName());
		 e.setJoinMessage(Utils.format(joinMessage));
    	}
    	
    	if(!e.getPlayer().hasPlayedBefore() || plugin.getData().getString(e.getPlayer().getUniqueId().toString()) == null)
		{	
			
			String uuid = e.getPlayer().getUniqueId().toString();
			plugin.getData().addDefault(uuid, "");	   
			plugin.getData().addDefault(uuid + ".default-name", e.getPlayer().getName());
			plugin.getData().addDefault(uuid + ".points", plugin.getConfig().getInt("points.default-balance"));  
			plugin.getData().addDefault(uuid + ".latest-ad", "");
			plugin.getData().addDefault(uuid + ".ads-created", 0);
						
			dataf = new File(plugin.getDataFolder(), "data.yml");

				try {
					plugin.getData().save(dataf);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
		}
		
	}
	
	@EventHandler
	 public void onPlayerQuit(PlayerQuitEvent e)
	 {

		if(plugin.getConfig().getString("enable.welcomers").equalsIgnoreCase("true"))
		{
			quitMessage = plugin.getConfig().getString("messages.quit");
			quitMessage = quitMessage.replace("{player}", e.getPlayer().getName());
			e.setQuitMessage(Utils.format(quitMessage));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onChat(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();

		if (in.contains(p.getName()))
		{

			String tf = plugin.getConfig().getString("messages.transaction-cancelled");

			e.setCancelled(true);

			if (e.getMessage().equalsIgnoreCase("cancel")) {
				in.remove(p.getName());
				p.sendMessage(Utils.format(prefix + tf));
				Utils.sendSound(p);
				return;
			}

			RegisteredServiceProvider rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
			econ = (Economy) rsp.getProvider();

			int price = plugin.getConfig().getInt("advertising.price");
			String succes = plugin.getConfig().getString("messages.succes");

			if (Advertising.usingMoney()) {

				EconomyResponse r = econ.withdrawPlayer(p.getName(), price);

				if (r.transactionSuccess())
				{

					Advertising.send(e.getMessage().split(" "), p);

					String priceToString = Integer.toString(price);
					succes = succes.replace("{price}", priceToString);
					p.sendMessage(Utils.format(succes));

					in.remove(p.getName());

				} else {

					p.sendMessage(Utils.format("&cSomething went wrong.."));
					econ.depositPlayer(p.getName(), price); //Traiasca spookie
					return;

				}

			}
			else if (Advertising.usingPoints())
			{

				AdvertisingPoints.takePoints(p, price);

				Advertising.send(e.getMessage().split(" "), p);

				String succesPuncte = plugin.getConfig().getString("messages.succes-points");
				String priceToString = Integer.toString(price);
				succesPuncte = succesPuncte.replace("{price}", priceToString);
				p.sendMessage(Utils.format(succesPuncte));

				in.remove(p.getName());


			}
		}
	}
	
	
	@EventHandler(ignoreCancelled = true)
	public void onClick(InventoryClickEvent e) {

		if (e.getClickedInventory() == null) {
			return;
		}

		String title = plugin.getConfig().getString("gui.inventory-name");

		if (e.getView().getTitle().equals(title)) {
			e.setCancelled(true);

			if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType().equals(Material.AIR))) {
				return;
			}

			if (e.getClickedInventory().equals(e.getView().getBottomInventory())) {
				return;
			}

			RegisteredServiceProvider rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
			econ = (Economy) rsp.getProvider();

			String dialog = plugin.getConfig().getString("messages.dialog");
			String aid = plugin.getConfig().getString("messages.already-in-dialogue");
			int price = plugin.getConfig().getInt("advertising.price");
			prefix = plugin.getConfig().getString("messages.prefix");
			String noMoney = plugin.getConfig().getString("messages.no-money");
			String noPoints = plugin.getConfig().getString("messages.no-points");

			Player p = (Player) e.getWhoClicked();

			if (e.getSlot() == 13)
			{

				if(in.contains(p.getName()))
				{
					Utils.errorAsItem(e.getCurrentItem(), aid);
					return;
				}

				Utils.sendSound(p);

				if(Cooldown.hasCooldown(p.getUniqueId(), "ad"))
				{

					String cooldownMessage = plugin.getConfig().getString("messages.cooldown-message").replace("{cooldown}", Cooldown.formatRemainingTime(Cooldown.getExpiryTime(p.getUniqueId().toString(), "ad")));

					p.sendMessage(Utils.format(prefix + cooldownMessage));
					Utils.sendSound(p);
					return;
				}

				if (Advertising.usingMoney()) {

					if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
						Bukkit.getConsoleSender().sendMessage(Utils.format("&c&lError: &7&oYou don't own a required dependency: &fVault"));
						p.closeInventory();
						return;
					}

					double b = (econ.getBalance(p.getName()));

					if (b < price) {
						p.sendMessage(Utils.format(prefix + noMoney));
						Utils.logToFile(format.format(date) + " - " + "Warn > " + p.getName() + " didn't have enough money.");
						return;
					}

					in.add(p.getName());
					p.sendMessage(Utils.format(prefix + dialog));
					p.closeInventory();


				}
				else if (Advertising.usingPoints())
				{

					if (AdvertisingPoints.lookPoints(p) < price) {
						p.sendMessage(Utils.format(prefix + noPoints));
						Utils.logToFile(format.format(date) + " - " + "Warn > " + p.getName() + " didn't have enough points.");
						p.closeInventory();
						return;
					}

					in.add(p.getName());
					p.sendMessage(Utils.format(prefix + dialog));
					p.closeInventory();

				}
			}

		}
	}
	
}
