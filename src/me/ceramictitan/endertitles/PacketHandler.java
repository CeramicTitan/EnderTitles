package me.ceramictitan.endertitles;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PacketHandler {

    public static Integer ENTITY_ID = 6000;
    public static Map<String, PacketHandler> dragonplayers = new HashMap<String, PacketHandler>();
    public static final int MAX_HEALTH = 200;
    public boolean visible;
    public static int EntityID;
    public int x;
    public int y;
    public int z;
    public int pitch = 0;
    public int head_pitch = 0;
    public int yaw = 0;
    public byte xvel = 0;
    public byte yvel = 0;
    public byte zvel = 0;
    public float health;
    public String name;
    public static boolean created;

    public PacketHandler(String name, int EntityID, Location loc){
	this(name, EntityID, (int) Math.floor(loc.getBlockX() * 32.0D), (int) Math.floor(loc.getBlockY() * 32.0D), (int) Math.floor(loc.getBlockZ() * 32.0D));
    }

    public PacketHandler(String name, int EntityID, Location loc, float health, boolean visible){
	this(name, EntityID, (int) Math.floor(loc.getBlockX() * 32.0D), (int) Math.floor(loc.getBlockY() * 32.0D), (int) Math.floor(loc.getBlockZ() * 32.0D), health, visible);
    }

    public PacketHandler(String name, int EntityID, int x, int y, int z){
	this(name, EntityID, x, y, z, MAX_HEALTH, false);
    }

    public PacketHandler(String name, int EntityID, int x, int y, int z, float health, boolean visible){
	this.name=name;
	PacketHandler.EntityID=EntityID;
	this.x=x;
	this.y=y;
	this.z=z;
	this.health=health;
	this.visible=visible;
    }

    @SuppressWarnings("deprecation")
    public Object getMobPacket(){
	Class<?> mob_class = ReflectionHandler.getCraftClass("Packet24MobSpawn");
	Object mobPacket = null;
	try {
	    mobPacket = mob_class.newInstance();

	    Field a = ReflectionHandler.getField(mob_class, "a");
	    a.setAccessible(true);
	    a.set(mobPacket, EntityID);//Entity ID
	    Field b = ReflectionHandler.getField(mob_class, "b");
	    b.setAccessible(true);
	    b.set(mobPacket, EntityType.ENDER_DRAGON.getTypeId());//Mob type (ID: 64)
	    Field c = ReflectionHandler.getField(mob_class, "c");
	    c.setAccessible(true);
	    c.set(mobPacket, x);//X position
	    Field d = ReflectionHandler.getField(mob_class, "d");
	    d.setAccessible(true);
	    d.set(mobPacket, y);//Y position
	    Field e = ReflectionHandler.getField(mob_class, "e");
	    e.setAccessible(true);
	    e.set(mobPacket, z);//Z position
	    Field f = ReflectionHandler.getField(mob_class, "f");
	    f.setAccessible(true);
	    f.set(mobPacket, (byte) ((int) (pitch * 256.0F / 360.0F)));//Pitch
	    Field g = ReflectionHandler.getField(mob_class, "g");
	    g.setAccessible(true);
	    g.set(mobPacket, (byte) ((int) (head_pitch * 256.0F / 360.0F)));//Head Pitch
	    Field h = ReflectionHandler.getField(mob_class, "h");
	    h.setAccessible(true);
	    h.set(mobPacket, (byte) ((int) (yaw * 256.0F / 360.0F)));//Yaw
	    Field i = ReflectionHandler.getField(mob_class, "i");
	    i.setAccessible(true);
	    i.set(mobPacket, xvel);//X velocity
	    Field j = ReflectionHandler.getField(mob_class, "j");
	    j.setAccessible(true);
	    j.set(mobPacket, yvel);//Y velocity
	    Field k = ReflectionHandler.getField(mob_class, "k");
	    k.setAccessible(true);
	    k.set(mobPacket, zvel);//Z velocity

	    Object watcher = getWatcher();
	    Field t = ReflectionHandler.getField(mob_class, "t");
	    t.setAccessible(true);
	    t.set(mobPacket, watcher);
	} catch (InstantiationException e1) {
	    e1.printStackTrace();
	} catch (IllegalAccessException e1) {
	    e1.printStackTrace();
	}

	return mobPacket;
    }

    public Object getDestroyEntityPacket(){
	Class<?> packet_class = ReflectionHandler.getCraftClass("Packet29DestroyEntity");
	Object packet = null;
	try {
	    packet = packet_class.newInstance();

	    Field a = ReflectionHandler.getField(packet_class, "a");
	    a.setAccessible(true);
	    a.set(packet, new int[]{EntityID});
	} catch (InstantiationException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	}

	return packet;
    }

    public Object getMetadataPacket(Object watcher){
	Class<?> packet_class = ReflectionHandler.getCraftClass("Packet40EntityMetadata");
	Object packet = null;
	try {
	    packet = packet_class.newInstance();

	    Field a = ReflectionHandler.getField(packet_class, "a");
	    a.setAccessible(true);
	    a.set(packet, EntityID);

	    Method watcher_c = ReflectionHandler.getMethod(watcher.getClass(), "c");
	    Field b = ReflectionHandler.getField(packet_class, "b");
	    b.setAccessible(true);
	    b.set(packet, watcher_c.invoke(watcher));
	} catch (InstantiationException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	} catch (InvocationTargetException e) {
	    e.printStackTrace();
	}

	return packet;
    }

    public Object getTeleportPacket(Location loc){
	Class<?> packet_class = ReflectionHandler.getCraftClass("Packet34EntityTeleport");
	Object packet = null;
	try {
	    packet = packet_class.newInstance();

	    Field a = ReflectionHandler.getField(packet_class, "a");
	    a.setAccessible(true);
	    a.set(packet, EntityID);
	    Field b = ReflectionHandler.getField(packet_class, "b");
	    b.setAccessible(true);
	    b.set(packet, (int) Math.floor(loc.getX() * 32.0D));
	    Field c = ReflectionHandler.getField(packet_class, "c");
	    c.setAccessible(true);
	    c.set(packet, (int) Math.floor(loc.getY() * 32.0D));
	    Field d = ReflectionHandler.getField(packet_class, "d");
	    d.setAccessible(true);
	    d.set(packet, (int) Math.floor(loc.getZ() * 32.0D));
	    Field e = ReflectionHandler.getField(packet_class, "e");
	    e.setAccessible(true);
	    e.set(packet, (byte) ((int) (loc.getYaw() * 256.0F / 360.0F)));
	    Field f = ReflectionHandler.getField(packet_class, "f");
	    f.setAccessible(true);
	    f.set(packet, (byte) ((int) (loc.getPitch() * 256.0F / 360.0F)));
	} catch (InstantiationException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	}
	return packet;
    }

    public Object getRespawnPacket(){
	Class<?> packet_class = ReflectionHandler.getCraftClass("Packet205ClientCommand");
	Object packet = null;
	try {
	    packet = packet_class.newInstance();

	    Field a = ReflectionHandler.getField(packet_class, "a");
	    a.setAccessible(true);
	    a.set(packet, 1);
	} catch (InstantiationException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	}
	return packet;
    }

    public Object getWatcher(){
	Class<?> watcher_class = ReflectionHandler.getCraftClass("DataWatcher");
	Object watcher = null;
	try {
	    watcher = watcher_class.newInstance();

	    Method a = ReflectionHandler.getMethod(watcher_class, "a", new Class<?>[] {int.class, Object.class});
	    a.setAccessible(true);

	    a.invoke(watcher, 0, visible ? (byte)0 : (byte)0x20);
	    a.invoke(watcher, 6, health);
	    a.invoke(watcher, 7, 0);
	    a.invoke(watcher, 8, (byte) 0);
	    a.invoke(watcher, 10, name);
	    a.invoke(watcher, 11, (byte) 1);
	} catch (InstantiationException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	} catch (InvocationTargetException e) {
	    e.printStackTrace();
	}

	return watcher;
    }
    public static void destroy(Player player) {
	if(dragonplayers.containsKey(player.getName())){

	    setStatus(player, "", 100);
	}
    }
    public static void setStatus(Player player, String text, int healthpercent){
	PacketHandler dragon = null;
	if(dragonplayers.containsKey(player.getName())){
	    dragon = dragonplayers.get(player.getName());
	}else if(!text.equals("")){
	    dragon = new PacketHandler(text, ENTITY_ID, player.getLocation().add(0, -200, 0));

	    dragonplayers.put(player.getName(), dragon);
	}

	if(text.equals("") && dragonplayers.containsKey(player.getName())){
	    Object destroyPacket = dragon.getDestroyEntityPacket();
	    ReflectionHandler.sendPacket(player, destroyPacket);

	    dragonplayers.remove(player.getName());
	}else{
	    Object mobPacket = dragon.getMobPacket();
	    ReflectionHandler.sendPacket(player, mobPacket);

	    dragon.health=(healthpercent/100f)*PacketHandler.MAX_HEALTH;
	    Object metaPacket = dragon.getMetadataPacket(dragon.getWatcher());
	    Object teleportPacket = dragon.getTeleportPacket(player.getLocation().add(0, -200, 0));
	    ReflectionHandler.sendPacket(player, metaPacket);
	    ReflectionHandler.sendPacket(player, teleportPacket);
	}
    }

    public static void displayDragonTextBar(Plugin plugin, String text, final Player player, long length){
	setStatus(player, text, 100);

	new BukkitRunnable(){
	    @Override
	    public void run(){
		setStatus(player, "", 100);
	    }
	}.runTaskLater(plugin, length);
    }

    public static void displayDragonLoadingBar(final Plugin plugin, final String text, final String completeText, final Player player, final int healthAdd, final long delay, final boolean loadUp){
	setStatus(player, "", (loadUp ? 1 : 100));

	new BukkitRunnable(){
	    int health = (loadUp ? 1 : 100);

	    @Override
	    public void run(){
		if((loadUp ? health < 100 : health > 1)){
		    setStatus(player, text, health);
		    if(loadUp){
			health += healthAdd;
		    } else {
			health -= healthAdd;
		    }
		} else {
		    setStatus(player, completeText, (loadUp ? 100 : 1));
		    new BukkitRunnable(){
			@Override
			public void run(){
			    setStatus(player, "", (loadUp ? 100 : 1));
			}
		    }.runTaskLater(plugin, 20);

		    this.cancel();
		}
	    }
	}.runTaskTimer(plugin, delay, delay);
    }

    public static void displayDragonLoadingBar(final Plugin plugin, final String text, final String completeText, final Player player, final int secondsDelay, final boolean loadUp){
	final int healthChangePerSecond = 100 / secondsDelay / 4;

	displayDragonLoadingBar(plugin, text, completeText, player, healthChangePerSecond, 5L, loadUp);
    }
}