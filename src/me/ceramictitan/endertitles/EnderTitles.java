package me.ceramictitan.endertitles;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EnderTitles extends JavaPlugin implements Listener{

    protected final FileConfiguration titles = new YamlConfiguration();
    protected Logger log;
    private TitleHandler handler;


    @Override
    public void onEnable(){
	log = getLogger();
	getServer().getPluginManager().registerEvents(this, this);
	handler = new TitleHandler(this, titles);
	try {
	    titles.load("plugins/EnderTitles/titles.yml");
	    log.info("Found titles.yml!");
	    log.info("Loaded: "+handler.getNumberOfMessages() + "messages!");
	} catch (FileNotFoundException e) {
	    log.info("File not found!. Will be generated on restart");
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (InvalidConfigurationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
    @Override
    public void onDisable(){
	try {
	    titles.options().header("Do not edit the id numbers, but you can edit the text!");
	    titles.save("plugins/EnderTitles/titles.yml");
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	for(Player player : getServer().getOnlinePlayers()){
	    PacketHandler.destroy(player);
	}

    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
	if(!(sender instanceof Player)){
	    return false;
	}
	if(cmd.getName().equalsIgnoreCase("endertitle")){
	    Player p = (Player)sender;
	    if(args.length == 2){
		if(args[0].equalsIgnoreCase("create")){
		    handler.registerNewTitle(p,args[1]);
		    return true;
		}else if(args[0].equalsIgnoreCase("delete")){
		    handler.deleteTitle(p,Integer.valueOf(args[1]));
		    return true;
		}else if(args[0].equalsIgnoreCase("set")){
		    for(Player player : getServer().getOnlinePlayers()){
			handler.setTitle(player, Integer.valueOf(args[1]));
			return true;
		    }
		}else if(args[0].equalsIgnoreCase("view")){
		    p.sendMessage("ID "+Integer.valueOf(args[1])+ ": " +handler.getMessage(Integer.valueOf(args[1])));
		    return true;
		}
	    }
	}
	return false;

    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
	handler.setTitle(event.getPlayer(), handler.getLastMessage());
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
	PacketHandler.destroy(event.getPlayer());
    }
}
