package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;


public class Minecore extends JavaPlugin implements Listener{

	public static Minecore Main;
	public Economy econ = null;
	public Config con;
	private ScoreboardManager newsb;
	public Freeze freezer;
	
	private Boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			econ = economyProvider.getProvider();
			return true;
		}
		return false;
	}

	public static void main(String args[]) {

	}
	
	public List<String> frozen;
	private Rebooter rebooter;

	@Override
	public void onEnable() {
		getLogger().info("Minecore Enabled");
		Main = this;
		con = new Config();
		con.load();
		setupEconomy();
		getServer().getPluginManager().registerEvents(this, this);
		newsb = new ScoreboardManager();
		freezer = new Freeze();
		freezer.Load();
		frozen = new ArrayList<String>();
		//rebooter = new Rebooter(this);
		
	}

	@Override
	public void onDisable() {
		getLogger().info("Minecore Disabled");
		Main = null;
	}
	
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMove(final PlayerMoveEvent event) {
		Player player = (Player) event.getPlayer();
		if (frozen.contains(player.getName().toLowerCase()))
		{
			player.teleport(player);
			player.sendMessage("You have been frozen by an admin");
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(final PlayerJoinEvent event) {
//		Player player = (Player) event.getPlayer();
		Player connectingPlayer = event.getPlayer();
		
		connectingPlayer.sendMessage("ELLO");
//		ConfigurationSection welcome = con.configfile.getConfigurationSection("welcome");
//		if (welcome.getBoolean("enabled") == true)
//		{
//			ProxiedPlayer pp = (ProxiedPlayer) player;
//			Title t = ProxyServer.getInstance().createTitle();
//			t.title(new TextComponent( "Hello world" ));
//			t.subTitle(new TextComponent( "From Flux" ));
//			t.send(pp);
//			/*IChatBaseComponent chatTitle;
//			if (welcome.getString("title")!= "" && welcome.getBoolean("custom-message") == true) //If the custom message is not empty and is enabled.
//			{
//				chatTitle = ChatSerializer.a("{\"text\": \"" + welcome.getString("title").replaceAll("&", "\247") + "\"}"); //Set the custom message
//			}
//			else
//			{
//				chatTitle = ChatSerializer.a("{\"text\": \"Welcome Back, " + player.getDisplayName() + "!\"}"); //Set a default message if custom message is empty yet still enabled.
//			}
//			IChatBaseComponent chatSubTitle = ChatSerializer.a("{\"text\": \"Y U NO CONFIGURE SUBTITLE!\"}");
//			PacketPlayOutTitle playTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
//			PacketPlayOutTitle playSubTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle);
//			PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
//			connection.sendPacket(playTitle);
//			connection.sendPacket(playSubTitle);
//			*/
//		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("web")) {
			player.sendMessage(ChatColor.AQUA + "--- Social Media ---");
			player.sendMessage(ChatColor.WHITE
					+ "Here are a list of all our available social media links.");
			ConfigurationSection social = con.configfile.getConfigurationSection("social");
			for (String i : social.getKeys(false))
			{
				player.sendMessage(ChatColor.AQUA + i + ": " + ChatColor.DARK_GRAY + social.getString(i));
			}
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("vote")) {
			player.sendMessage(ChatColor.AQUA + "--- Vote 4 Rewards ---");
			player.sendMessage(ChatColor.WHITE
					+ "Please ensure you are signed up to our forums and that your minecraft account is linked correctly.");
			ConfigurationSection vote = con.configfile.getConfigurationSection("vote");
			for (String i : vote.getKeys(false))
			{
				player.sendMessage(ChatColor.AQUA + i + ": " + ChatColor.DARK_GRAY + vote.getString(i));
			}
		}
		if (cmd.getName().equalsIgnoreCase("me")) {
			if (!player.hasPermission("minecore.me"))
			{
				player.sendMessage("OI YOU AINT GOT PERMSSION BRO!");
			}
			else
			{
				player.sendMessage("WOW YOU GOT PERMS!");
				int online = Bukkit.getOnlinePlayers().size();
				double bal = econ.getBalance(player);
				
				Location loc = player.getLocation();
				World world = loc.getWorld();
				
				world.getBlockAt(loc).setType(Material.CAKE_BLOCK);
			
				newsb.newScoreboard("Minecore HUD");
				newsb.addScore("Balance", (int)bal);
				newsb.addScore("Players", online);
				newsb.addScore("LP", 99);
				newsb.addScore("X", loc.getBlockX());
				newsb.addScore("Y", loc.getBlockY());
				newsb.addScore("Z", loc.getBlockZ());
				newsb.setScoreboard(player);
			}
			
		}
		
		if(cmd.getName().equalsIgnoreCase("freeze"))
		{
			if (args.length == 1)
			{
				
				if (args[0].equalsIgnoreCase("list"))
				{
					freezer.getList(); //THIS WILL GET LIST
					frozen = freezer.list;
					if (frozen.size() == 0)
					{
						player.sendMessage(ChatColor.RED + "There are no frozen players.");
					}
					else
					{
						player.sendMessage(ChatColor.BLUE+"Frozen players:");
						for(int i = 0; i < frozen.size(); i++)
						{
							player.sendMessage(i + ": " +frozen.get(i));
						}
					}
				}
				else
				{
					String victim = args[0].toLowerCase();
				
					if (frozen.contains(victim))
					{
						frozen.remove(victim);
						//freezer.list = (ArrayList<String>) frozen;
						try {
							freezer.exportList((ArrayList<String>) frozen);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						player.sendMessage(ChatColor.GOLD + victim + " has now been thawed.");
					}
					else
					{
						frozen.add(victim);
						try {
							freezer.exportList((ArrayList<String>) frozen);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						player.sendMessage(ChatColor.GOLD + victim + " has now been frozen.");
					}
					
				}
			}
			else
			{
				player.sendMessage("Usage: /freeze <player>");
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("reboot"))
		{
			if (args.length == 1)
			{
				try
				{
					Integer.parseInt(args[0]);
					int tempdelay = Integer.parseInt(args[0]);
					rebooter = new Rebooter(this);
					rebooter.scheduleReboot(tempdelay);
				} 
				catch (NumberFormatException e)
				{
					player.sendMessage(ChatColor.DARK_RED + "Enter a valid number");
				}
				
				
				//rebooter.runTaskTimer(this, 0, tempdelay * 20);
			}
		}
//			if (args.length == 1)
//			{
//				try {
//					Integer.parseInt(args[0]);
//				} catch (NumberFormatException e) {
//					player.sendMessage(ChatColor.DARK_RED
//							+ "Enter a valid time delay. 1-10 minutes");
//				}
//			
//			time = Integer.parseInt(args[0]);
//			if (time > 0 && time <11)
//			{
//				rebooter.newScoreboard("Restarting...");
//				rebooter.addScore("Minutes", time);
//
//				for(Player player1: Bukkit.getOnlinePlayers()){
//					rebooter.setScoreboard(player1);
//					player1.playSound(player1.getLocation(), Sound.NOTE_PLING, 10, 1);
//				}
//				player.sendMessage("TIME"+time);
//				BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
//		        scheduler.scheduleAsyncRepeatingTask(this, new Runnable() {
//		        	@Override
//		        	public void run()
//		        	{
//		        		
//		        		time--;
//		        		if (time >0){
//		        			rebooter.addScore("Minutes", time);
//		        			for(Player player1: Bukkit.getOnlinePlayers()){
//		        				rebooter.setScoreboard(player1);
//		        				player1.playSound(player1.getLocation(), Sound.NOTE_PLING, 10, 1);
//		        			}
//		        		}
//		        		else
//		        		{
//		        			return;
//		        		}
//		        	}
//		        	
//		        }, 0L, 20L); //1200L = 1min
//		        player.sendMessage("TIME"+time);
//			}     
//			
//			else{
//				player.sendMessage(ChatColor.DARK_RED
//						+ "Enter a valid time delay. 1-10 minutes");
//			}
//		
//			return true;
//			}
//		}
		return true;
	}
}
