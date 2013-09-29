package me.ceramictitan.endertitles;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TitleHandler {

    private final  EnderTitles plugin;
    private final  FileConfiguration titles;
    private int id;
    private final String et = ChatColor.DARK_PURPLE+"||"+ChatColor.GOLD+"Ender"+ChatColor.DARK_GRAY+"Tiles"+ChatColor.DARK_PURPLE+"||";

    public TitleHandler(EnderTitles plugin, FileConfiguration titles){
	this.plugin=plugin;
	this.titles = titles;
    }

    public String getMessage(int id){
	return ChatColor.translateAlternateColorCodes('&',titles.getString(String.valueOf(id)));
    }

    public void registerNewTitle(Player player, String title){
	if(titles != null){
	    if(!titles.contains(title)){
		if(titles.contains(String.valueOf(titles.getKeys(false).size() + 1))){
		    try{titles.set(String.valueOf(titles.getKeys(false).size() + 2), title);}catch(Exception e){e.printStackTrace();}
		    player.sendMessage(ChatColor.GREEN+"Successfully created message."+ChatColor.DARK_AQUA+" ID: "+String.valueOf(getNumberOfMessages()+1));
		}else{
		    try{titles.set(String.valueOf(titles.getKeys(false).size() + 1), title);}catch(Exception e){e.printStackTrace();}
		    player.sendMessage(et+ChatColor.GREEN+"Successfully created message."+ChatColor.DARK_AQUA+" ID: "+String.valueOf(getNumberOfMessages()));
		}
	    }else{
		player.sendMessage(et+ChatColor.YELLOW+"title already registered.");
	    }
	}else{
	    player.sendMessage(et+ChatColor.RED+"titles file is null");
	}


    }
    public  void deleteTitle(Player player,int id){

	if(titles != null){
	    if(titles.contains(String.valueOf(id))){
		titles.set(String.valueOf(id), null);
		player.sendMessage(et+ChatColor.GREEN+"successfully deleted message."+ChatColor.DARK_AQUA+" ID: "+ id);
	    }else{
		player.sendMessage(et+ChatColor.YELLOW+"ID doesn't exists");
	    }
	}else{
	    player.sendMessage(et+ChatColor.RED+"titles file is null");
	}

    }
    public  void setTitle(Player player, int id){
	if(titles !=null){
	    PacketHandler.destroy(player);
	    if(titles.contains(String.valueOf(id))){

		PacketHandler.displayDragonTextBar(plugin, ChatColor.translateAlternateColorCodes('&', titles.getString(String.valueOf(id))), player, 999999999L);
		player.sendMessage(et+ChatColor.GREEN+"Message bar updated!");

		this.id = id;
	    }else{
		player.sendMessage(et+ChatColor.YELLOW+"ID doesn't exists");
	    }
	}else{
	    player.sendMessage(et+ChatColor.RED+"titles file is null");
	}
    }
    public int getLastMessage(){
	return id;
    }
    public int getNumberOfMessages(){
	return titles.getKeys(false).size();
    }
}
