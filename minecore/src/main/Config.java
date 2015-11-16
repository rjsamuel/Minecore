package main;

import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

	public FileConfiguration configfile;

	public Config() {
	}

	public void load() {
		try {
			configfile = Minecore.Main.getConfig();
			if (!configfile.contains("social")) {
				configfile.set("social.website", "http://www.mywebsite.com");
			}
			if (!configfile.contains("vote")) {
				configfile.set("vote.examplepmc", "http://www.planetminecraft.com");
			}
			if (!configfile.contains("welcome")) {
				configfile.set("welcome.enabled", true);
				configfile.set("welcome.custom-message", true);
				configfile.set("welcome.title", "Custom Title");
				configfile.set("welcome.sub-title", "Custom Subtitle");
			}
			
			Minecore.Main.saveConfig();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public Set<String> ReturnKeys(String path)
	{		
		return configfile.getConfigurationSection(path).getKeys(false);	
	}

}
