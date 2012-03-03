package me.ksafin.DynamicEconomy;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;

import couk.Adamki11s.Extras.Colour.ExtrasColour;

public class DynamicEconomyPlayerListener implements Listener, EventExecutor {

    final ExtrasColour color = new ExtrasColour();

    @Override
    public void execute(Listener listener, Event event) throws EventException {
	if (event instanceof PlayerJoinEvent)
	    if (DynamicEconomy.enableUpdateChecker) {
		final Player player = ((PlayerJoinEvent) event).getPlayer();
		final String stringPlay = player.getName();
		boolean latest = DynamicEconomy.getInstance().checkVersion();
		Utility.getInstance().writeToLog(stringPlay + " logged in.");
		if (latest == false)
		    if (DynamicEconomy.permission.has(player, "DynamicEconomy.update"))
			DynamicEconomy.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(DynamicEconomy.getInstance(), new Runnable() {
			    @Override
			    public void run() {
				color.sendColouredMessage(player, "&2New Version of DynamicEconomy Available!");
				Utility.getInstance().writeToLog(stringPlay + " notified of new DynamicEconomy version");
			    }
			}, 60L);
	    }
	if (event instanceof PlayerInteractEvent) {
	    PlayerInteractEvent ev = ((PlayerInteractEvent) event);
	    Action action = ev.getAction();
	    Player player = ev.getPlayer();
	    ItemStack inHand = player.getItemInHand();
	    Material item = inHand.getType();
	    if (item.equals(Material.WOOD_SPADE))
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.selectregion")) {
		    Block block = ev.getClickedBlock();
		    String playerName = player.getName();
		    int x = block.getX();
		    int y = block.getY();
		    int z = block.getZ();
		    if (action == Action.RIGHT_CLICK_BLOCK) {
			if (DynamicEconomy.selectedCorners.containsKey(playerName)) {
			    Coordinates coordinates = new Coordinates(DynamicEconomy.selectedCorners.get(playerName));
			    coordinates.setSecond(x, y, z);
			    DynamicEconomy.selectedCorners.put(playerName, coordinates.toString());
			} else
			    DynamicEconomy.selectedCorners.put(playerName, new Coordinates(0, 0, 0, x, y, z).toString());
			color.sendColouredMessage(player, "&2Selected block #2: " + x + " , " + y + " , " + z);
			Utility.getInstance().writeToLog(playerName + " Selected block #2: " + x + " , " + y + " , " + z);
		    } else if (action == Action.LEFT_CLICK_BLOCK) {
			if (DynamicEconomy.selectedCorners.containsKey(playerName)) {
			    Coordinates coordinates = new Coordinates(DynamicEconomy.selectedCorners.get(playerName));
			    coordinates.setFirst(x, y, z);
			    DynamicEconomy.selectedCorners.put(playerName, coordinates.toString());
			} else
			    DynamicEconomy.selectedCorners.put(playerName, new Coordinates(x, y, z, 0, 0, 0).toString());
			color.sendColouredMessage(player, "&2Selected block #1: " + x + " , " + y + " , " + z);
			Utility.getInstance().writeToLog(playerName + " Selected block #1: " + x + " , " + y + " , " + z);
		    }

		}
	}
    }
}
