package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Freeze {
	
    public File FreezeList;
    public FileConfiguration myFileConfig;
    public ArrayList<String> list;
    YamlConfiguration yamlFile;
    
    public Freeze(){
    	FreezeList = new File("plugins/Minecore/freeze.yml");
    	yamlFile = YamlConfiguration.loadConfiguration(FreezeList);
    	list = new ArrayList<String>();
    }
    
    public void Load(){
        if (!FreezeList.exists()) {
            try {
            	FreezeList.createNewFile();
            	if (!yamlFile.contains("players"))
            	{
            		yamlFile.createSection("players");
            	}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
			yamlFile.save(FreezeList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void getList()
    {
    	yamlFile = YamlConfiguration.loadConfiguration(FreezeList);
    	list = (ArrayList<String>) yamlFile.get("Players");
    }
    
    public void exportList(ArrayList<String> frozen) throws IOException
    {
    	yamlFile.set("players", frozen);
    	yamlFile.save(FreezeList);
    }

}
