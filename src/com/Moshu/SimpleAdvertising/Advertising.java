package com.Moshu.SimpleAdvertising;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.scheduler.BukkitRunnable;

public class Advertising implements CommandExecutor {

	private static Main plugin;

	public Advertising(Main plugin) {
		this.plugin = plugin;
	}

	private static String currentad = "none";

	public static String getCurrentAd() {
		return currentad;
	}

	private static String currentpl = "none";

	public static String getCurrentAdPlayer() {
		return currentpl;
	}

	private static boolean activead = false;

	public static boolean activeAd() {
		return activead;
	}

	private static void log(String mesaj, Player p, Date date)
	{
		if (plugin.getConfig().getString("enable.logging").equalsIgnoreCase("true")) {
			Utils.logToFile(dateFormat.format(date) + " (Advertising) " + mesaj + " . Made by: " + p.getName());
		}
	}

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy / HH:mm:ss");
	private static ArrayList<String> cooldown = new ArrayList<String>();

	public static boolean hasCooldown(Player p)
	{
		return cooldown.contains(p.getName());
	}

	private static boolean cannotPost()
	{
		return activead && !plugin.getConfig().get("advertising.stay").equals("permanent");
	}

	public static boolean usingMoney()
	{
		return plugin.getConfig().getString("advertising.economy").equalsIgnoreCase("money");
	}

	public static boolean usingPoints()
	{
		return plugin.getConfig().getString("advertising.economy").equalsIgnoreCase("points");
	}

	private static boolean isPermanent()
	{
		return !Utils.isInt(plugin.getConfig().getString("advertising.stay"));
	}

	private static boolean shouldRepeat()
	{
		return plugin.getConfig().getInt("advertising.stay") > 0 && plugin.getConfig().getInt("advertising.repeat") > 0;
	}

	private static void updateCurrentAd(String mesaj, Player p)
	{
		currentad = mesaj;
		currentpl = p.getName();
	}

	private static void setCooldown(Player p, int time)
	{

		Cooldown c = new Cooldown(p.getUniqueId(), "ad", time);
		c.set();

		/*
		cooldown.add(p.getName());

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				cooldown.remove(p.getName());
			}
		}, time);

		 */
	}

	private static void removeAdFromStay(int stayTime)
	{
		if (!plugin.getConfig().get("advertising.stay").equals("permanent")) {

			activead = true;

			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					activead = false;
				}
			}, stayTime);

		}
	}


	private static String getMessage(String[] args)
	{

		StringBuilder mesaj = new StringBuilder("");

		for (String a : args)
		{
			mesaj.append(a);
			mesaj.append(" ");
		}

		return mesaj.toString().trim();

	}

	private static void sendLines(Player p, Player receiver, List<String> lines, String message, String prefix)
	{

		for(String s : lines)
		{

			if(p.hasPermission("simplead.color")) s = s.replace("{message}", message);
			else s = s.replace("{message}", ChatColor.stripColor(message));

			s = s.replace("{player}", p.getName());
			s = s.replace("{prefix}", prefix);

			receiver.sendMessage(Utils.format(s));
			Bukkit.getConsoleSender().sendMessage(Utils.format(s));

		}


	}

	private static void sendLinesToAll(Player sender, List<String> lines, String message, String prefix)
	{

		for(Player p : Bukkit.getOnlinePlayers())
		{
			sendLines(sender, p, lines, message, prefix);
		}

	}

	public static void send(String[] args, Player p) {


		String mesaj = getMessage(args);

		int repeatInterval = plugin.getConfig().getInt("advertising.repeat") * 20;
		int stayTime = plugin.getConfig().getInt("advertising.stay") * 20;
		int cooldownTime = plugin.getConfig().getInt("advertising.cooldown");

		List<String> lines = plugin.getConfig().getStringList("messages.format");

		String prefix = plugin.getConfig().getString("messages.prefix");

		Date date = new Date();

		if (!isPermanent()) {


			if (shouldRepeat()) {

				BukkitRunnable run = new BukkitRunnable() {

					int i = 0;

					@Override
					public void run() {

						if (i <= plugin.getConfig().getInt("advertising.stay") / plugin.getConfig().getInt("advertising.repeat")) {

							sendLinesToAll(p, lines, mesaj, prefix);

							i++;

						}
						else
						{
							cancel();
						}

					}
				};

				run.runTaskTimerAsynchronously(plugin, 0, repeatInterval);

			}
			else
			{
				sendLinesToAll(p, lines, mesaj, prefix);
			}

		}
		else
		{
			sendLinesToAll(p, lines, mesaj, prefix);
		}

		updateCurrentAd(mesaj, p);
		Utils.addToData(p.getUniqueId(), mesaj);

		log(mesaj, p, date);
		setCooldown(p, cooldownTime);

		removeAdFromStay(stayTime);
		Utils.sendSound(p);

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("ad")) {

			RegisteredServiceProvider rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
			Economy econ = (Economy) rsp.getProvider();

			if (!(sender instanceof Player)) {
				Utils.sendNotPlayer();
				return true;
			}

			Player p = (Player) sender;

			if (!p.hasPermission("simplead.ad")) {
				Utils.sendNoAccess(p);
				Utils.sendSound(p);
				return true;
			}

			int price = plugin.getConfig().getInt("advertising.price");
			String prefix = plugin.getConfig().getString("messages.prefix");
			String noMoney = plugin.getConfig().getString("messages.no-money");
			String noPoints = plugin.getConfig().getString("messages.no-points");
			String succes = plugin.getConfig().getString("messages.succes");
			String cooldownMessage;

			Date date = new Date();


			if (args.length == 0) {

				Utils.createInv(p);
				Utils.sendSound(p);
				return true;

			}
			else
			{

				if (args[0].equalsIgnoreCase("help")) {

					if (!p.hasPermission("simplead.help")) {
						Utils.sendNoAccess(p);
						Utils.sendSound(p);
						return true;
					}

					if (args.length > 1) {
						return true;
					}

					sender.sendMessage(Utils.format("       &8&l---------- &f&l( &bSimpleAdvertising &f&l) &8&l----------"));
					sender.sendMessage(Utils.format("&8&l- &bCommands:"));
					sender.sendMessage(Utils.format("&l&8* &c/ad (Message) &l&8» &bMakes a public ad"));
					sender.sendMessage(Utils.format("&l&8* &c/ad reload &l&8» &bReloads the plugin"));
					sender.sendMessage(Utils.format("&l&8* &c/ad help &l&8» &bShows this page"));
					sender.sendMessage(Utils.format("&l&8* &c/ad debug &l&8» &bShows debug info"));
					sender.sendMessage(Utils.format("&l&8* &c/broadcast &l&8» &bBroadcast using this plugin"));
					sender.sendMessage(Utils.format("&eFor further help check out this page: &6https://www.spigotmc.org/com.Moshu.resources/simple-advertising.40414/"));
					Utils.sendSound(p);
					return true;

				}
				else if (args[0].equalsIgnoreCase("debug"))
				{

					if (!p.hasPermission("simplead.debug")) {
						Utils.sendNoAccess(p);
						Utils.sendSound(p);
						return true;
					}

					Debug.sendServerInfo(p);

				}
				else if (args[0].equalsIgnoreCase("bal") || args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("points"))
				{

					if (!p.hasPermission("simplead.points")) {
						Utils.sendNoAccess(p);
						Utils.sendSound(p);
						return true;
					}

					if (args.length > 1) {
						return true;
					}

					String str = plugin.getConfig().getString("messages.balance");
					int bl = AdvertisingPoints.lookPoints(p);
					String s = Integer.toString(bl);
					str = str.replace("{points}", s);
					p.sendMessage(Utils.format(prefix + str));
					return true;

				}
				else if (args[0].equalsIgnoreCase("reload"))
				{

					if (!p.hasPermission("simplead.reload")) {
						Utils.sendNoAccess(p);
						Utils.logToFile(dateFormat.format(date) + " - " + "Reload > " + sender.getName() + " tried to reload but didn't have permission");
						return true;
					}

					if (args.length != 1) {
						return true;
					}

					Bukkit.getPluginManager().disablePlugin(plugin);
					Bukkit.getPluginManager().enablePlugin(plugin);

					plugin.saveDefaultConfig();
					plugin.reloadConfig();

					Main.reloadFiles();

					plugin.saveConfig();

					p.sendMessage(Utils.format("&f&l(&cAdvertising&f&l) &fSuccesfully reloaded."));

					Utils.logToFile(dateFormat.format(date) + " - " + "Reload > " + sender.getName() + " has reloaded the plugin.");
					Utils.sendSound(p);

					return true;
				}

				if(Cooldown.hasCooldown(p.getUniqueId(), "ad"))
				{

					cooldownMessage = plugin.getConfig().getString("messages.cooldown-message").replace("{cooldown}", Cooldown.formatRemainingTime(Cooldown.getExpiryTime(p.getUniqueId().toString(), "ad")));

					p.sendMessage(Utils.format(prefix + cooldownMessage));
					Utils.sendSound(p);
					return true;
				}

				if (cannotPost()) {
					p.sendMessage(Utils.format(prefix + plugin.getConfig().getString("messages.active-ad")));
					return true;
				}

				if (usingMoney()) {

					if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
						Bukkit.getConsoleSender().sendMessage(Utils.format("&c&lError: &7&oYou don't own a required dependency: &fVault"));
						return true;
					}

					double b = (econ.getBalance(p.getName()));

					if (b < price) {
						p.sendMessage(Utils.format(prefix + noMoney));
						Utils.logToFile(dateFormat.format(date) + " - " + "Warn > " + sender.getName() + " didn't have enough money.");
						return true;
					}

					EconomyResponse r = econ.withdrawPlayer(p.getName(), price);

					if (r.transactionSuccess()) {

						send(args, p);

						String priceToString = Integer.toString(price);
						succes = succes.replace("{price}", priceToString);
						p.sendMessage(Utils.format(succes));

					} else {
						p.sendMessage(Utils.format("&cSomething went wrong.."));
						econ.depositPlayer(p.getName(), price); //Traiasca spookie
						return true;
					}


				} else if (usingPoints()) {

					int bal = plugin.getData().getInt(p.getUniqueId() + ".points");

					if (price > bal) {
						p.sendMessage(Utils.format(prefix + noPoints));
						Utils.logToFile(dateFormat.format(date) + " - " + "Warn > " + sender.getName() + " didn't have enough points.");
						return true;
					}

					send(args, p);

					AdvertisingPoints.takePoints(p, price);

					String succesPuncte = plugin.getConfig().getString("messages.succes-points");
					succesPuncte = succesPuncte.replace("{price}", Integer.toString(price));
					p.sendMessage(Utils.format(succesPuncte));
					return true;

				} else {

					Bukkit.getConsoleSender().sendMessage(Utils.format("&c&lError: &7&oValid economy types are &fpoints &7&oand &fmoney"));
					return true;
				}

			}
		}

		return true;
	}

}
