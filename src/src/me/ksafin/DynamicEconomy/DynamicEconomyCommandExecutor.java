package me.ksafin.DynamicEconomy;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import couk.Adamki11s.Extras.Colour.ExtrasColour;
import couk.Adamki11s.Extras.Inventory.ExtrasInventory;

/**
 * 
 * @author BOSS, Megge
 */
public class DynamicEconomyCommandExecutor implements CommandExecutor {

    public static final DecimalFormat decFormat = new DecimalFormat("#.##");
    private static final Logger log = Logger.getLogger("Minecraft");
    private final ExtrasColour color = new ExtrasColour();
    private ExtrasInventory inv = new ExtrasInventory();

    public void addStock(Player player, String[] args) {
	String stringPlay = player.getName();
	if ((args.length < 2) || (args.length > 2)) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Wrong Command Usage. &f/addStock [Item] [AdditionalStock]");
	    Utility.getInstance().writeToLog(stringPlay + " incorrectly called /addstock");
	} else {
	    ItemInfo item = ItemInfo.getItemInfo(args[0], getItemConfig());
	    int addStock = Integer.parseInt(args[1]);

	    getItemConfig().set(item.name + ".stock", String.valueOf(item.stock + addStock));
	    DynamicEconomy.getInstance().saveItemConfig();

	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Stock succesfully added.");
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Previous Stock: &f" + item.stock + "&2 | New Stock: &f" + (item.stock + addStock));
	    Utility.getInstance().writeToLog(stringPlay + " added " + addStock + " stock of " + item + " for a new stock total of " + (item.stock + addStock));
	}

    }

    private static FileConfiguration getItemConfig() {
	return DynamicEconomy.getInstance().itemConfig;
    }

    public void buy(Player player, String[] args) {

	if ((args.length == 0) || (args.length > 2)) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Wrong Command Usage. &f/buy [Item] (Amount)");
	    Utility.getInstance().writeToLog(player.getName() + " incorrectly called /buy");
	    return;
	}

	Location loc = player.getLocation();
	if (DynamicEconomy.useRegions)
	    if (!regionUtils.withinRegion(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You are not within an economy region!");
		Utility.getInstance().writeToLog(player.getName() + " called /buy outside of an economy region.");
		return;
	    }
	if (DynamicEconomy.location_restrict)
	    if (loc.getBlockY() <= DynamicEconomy.minimum_y) {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You are too deep underground to access the economy!.");
		Utility.getInstance().writeToLog(player.getName() + " called /buy but was too deep underground.");
		return;
	    } else if (loc.getBlockY() >= DynamicEconomy.maximum_y) {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You are too high up to access the economy!.");
		Utility.getInstance().writeToLog(player.getName() + " called /sell but was too high up.");
		return;
	    }

	ItemInfo item = ItemInfo.getItemInfo(args[0], getItemConfig());

	if (item == null) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2The item " + args[0] + " does not exist. ");
	    Utility.getInstance().writeToLog(player.getName() + " attempted to buy the non-existent item '" + args[0] + "'");
	    return;
	}

	if (DynamicEconomy.bannedPurchaseItems.contains(item.name)) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2This item is banned, and not allowed to be purchased in the economy.");
	    Utility.getInstance().writeToLog(player.getName() + " attempted to buy the banned item: " + item.name);
	    return;
	}

	int purchaseAmount = DynamicEconomy.defaultAmount;

	if (args.length == 2)
	    if (args[1].equalsIgnoreCase("all")) {
		if (DynamicEconomy.usestock)
		    purchaseAmount = item.stock;
		else {
		    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Stock Disabled, cannot use keyword 'all' ");
		    Utility.getInstance().writeToLog(player.getName() + " attempted to buy 'all' of '" + item + "', but stock is disabled.");
		}
	    } else
		try {
		    purchaseAmount = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
		    color.sendColouredMessage(player, DynamicEconomy.prefix
			    + "&2You entered the command arguments in the wrong order, or your amount was invalid. Try again. ");
		    Utility.getInstance()
			    .writeToLog(player.getName() + " entered an invalid purchase amount, or entered command arguments in the wrong order.");
		    return;
		}

	if (purchaseAmount < 0) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Cannot buy a negative amount!");
	    Utility.getInstance().writeToLog(player.getName() + " attempted to buy " + purchaseAmount + " of '" + item + "', but this amount is invalid.");
	    return;
	}

	DynamicEconomy.getInstance();
	double balance = DynamicEconomy.economy.getBalance(player.getName());
	double totalCost = 0;
	double newPrice = item.price;
	final double oldPrice = item.price;
	for (int x = 0; x < purchaseAmount; x++) {
	    totalCost += newPrice;
	    if (!DynamicEconomy.useboundaries || newPrice < item.ceiling) {
		newPrice = newPrice + item.velocity;
		if (DynamicEconomy.useboundaries && newPrice > item.ceiling)
		    newPrice = item.ceiling;
	    }
	}

	double tax = DynamicEconomy.purchasetax * totalCost;
	totalCost += tax;

	if (balance < totalCost) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You do not have enough money to purchase this.");
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Your balance: &f$" + balance + "   &2Your order total: &f$" + totalCost);
	    Utility.getInstance().writeToLog(
		    player.getName() + " attempted to buy " + purchaseAmount + " of '" + item + "' for " + totalCost + " but could not afford it.");
	    return;
	} else if (DynamicEconomy.usestock)
	    if (item.stock < purchaseAmount) {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2There is not enough stock of this item.");
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Your request: &f" + purchaseAmount + "   &2Current Stock: &f" + item.stock);
		Utility.getInstance().writeToLog(
			player.getName() + " attempted to buy " + purchaseAmount + " of '" + item + "' but there was only " + item.stock + " remaining");
		return;
	    }

	if (DynamicEconomy.depositTax == true)
	    try {
		DynamicEconomy.getInstance();
		DynamicEconomy.economy.depositPlayer(DynamicEconomy.taxAccount, tax);
	    } catch (Exception e) {
		log.info("Tax-Account " + DynamicEconomy.taxAccount + " not found.");
		Utility.getInstance()
			.writeToLog("Attempted to deposit tax of $" + tax + " to account " + DynamicEconomy.taxAccount + " but account not found.");
	    }

	DynamicEconomy.getInstance();
	DynamicEconomy.economy.withdrawPlayer(player.getName(), totalCost);
	getItemConfig().set(item.name + ".price", String.valueOf(newPrice));
	if (DynamicEconomy.usestock)
	    getItemConfig().set(item.name + ".stock", String.valueOf(item.stock - purchaseAmount));

	color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Purchase Success!");
	Utility.getInstance().writeToLog(player.getName() + " bought " + purchaseAmount + " of '" + item + "' for " + totalCost);

	double percentTax = DynamicEconomy.purchasetax * 100;
	color.sendColouredMessage(player, DynamicEconomy.prefix + "&f" + purchaseAmount + " &2" + item + "&f @ $" + oldPrice + "&2  + " + percentTax
		+ "% tax = &f$" + totalCost + " &2TOTAL");

	String notifiyString = DynamicEconomy.prefix + "&2New Price of &f" + item + "&2 is &f$" + newPrice + "&2 (+" + decFormat.format(newPrice - oldPrice)
		+ ")";
	if (DynamicEconomy.globalNotify)
	    for (Player p : Bukkit.getServer().getOnlinePlayers())
		color.sendColouredMessage(p, notifiyString);
	else if (DynamicEconomy.localNotify)
	    color.sendColouredMessage(player, notifiyString);
	Utility.getInstance().writeToLog(
		"[DynamicEconomy] New price of " + item + " changed to " + newPrice + "(+" + decFormat.format(newPrice - oldPrice) + ")");

	getItemConfig().set(item.name + ".time", String.valueOf(new Date().getTime()));

	int mat = 0;
	short dmg = 0;

	if (item.ID > 1000) {
	    mat = getMat(item.ID);
	    dmg = getDmg(item.ID);

	    ItemStack items = new ItemStack(mat, purchaseAmount, dmg);
	    player.getInventory().addItem(items);
	} else
	    inv.addToInventory(player, item.ID, purchaseAmount);

	DynamicEconomy.getInstance().saveItemConfig();
    }

    public void commandList(Player player, String[] args) {
	ArrayList<Command> cmdList = (ArrayList<Command>) PluginCommandYamlParser.parse(DynamicEconomy.getInstance());
	int length = cmdList.size();
	int numPages = (int) ((length / 5.0) + 1);
	int page;

	if (args.length == 0)
	    page = 1;
	else
	    page = Integer.parseInt(args[0]);

	int startCommand = (page * 5) - 5;
	int endCommand = (page * 5);

	if (endCommand > length)
	    endCommand = length;

	color.sendColouredMessage(player, "&2---------&fDynamicEconomy Commands&2---------");
	for (int x = startCommand; x < endCommand; x++) {
	    Command cmd = cmdList.get(x);
	    String command = cmd.getUsage();
	    String desc = cmd.getDescription();
	    String message = "&2" + command + " : &f" + desc;
	    color.sendColouredMessage(player, message);
	}
	color.sendColouredMessage(player, "&2----------------&fPage &f" + page + "/" + numPages + "&2----------------");

    }

    // public static boolean DynamicEconomy.usestock = DynamicEconomy.DynamicEconomy.usestock;
    // public static boolean DynamicEconomy.useboundaries = DynamicEconomy.DynamicEconomy.useboundaries;
    // public static String prefix = DynamicEconomy.prefix;
    // public static int defaultAmount = DynamicEconomy.defaultAmount;

    public void curTaxes(Player player, String[] args) {
	String stringPlay = player.getName();
	if (args.length > 0) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Wrong Command Usage. &f/curTaxes");
	    Utility.getInstance().writeToLog(stringPlay + " incorrectly called /curTaxes");
	} else {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&fSales Tax: &2" + DynamicEconomy.salestax * 100 + "%");
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&fPurchase Tax: &2" + DynamicEconomy.purchasetax * 100 + "%");
	    Utility.getInstance().writeToLog(stringPlay + " called /curtaxes");
	}

    }

    public void getCeiling(Player player, String[] args) {
	String stringPlay = player.getName();
	if (args.length != 1) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Wrong Command Usage. &f/getceiling [Item]");
	    Utility.getInstance().writeToLog(stringPlay + " incorrectly called /getceiling");
	} else if (DynamicEconomy.useboundaries == false) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Boundaries such as floor and ceiling are disabled.");
	    Utility.getInstance().writeToLog(stringPlay + " called /getceiling, but boundaries are disabled.");
	} else {
	    ItemInfo item = ItemInfo.getItemInfo(args[0], getItemConfig());

	    if (item == null) {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2This item does not exist. ");
		Utility.getInstance().writeToLog(stringPlay + " attempted to get the ceiling of the non-existent item '" + args[0] + "'");
	    } else {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Price Ceiling of &f" + item + "&2 is &f$" + decFormat.format(item.ceiling));
		Utility.getInstance().writeToLog(stringPlay + " called /getceiling for item '" + item + "'");
	    }
	}
    }

    public static short getDmg(int itemID) { // I dont really get this
	String idStr = String.valueOf(itemID);
	String[] split = idStr.split("00");
	short dmg = Short.parseShort(split[1]);
	return dmg;
    }

    public void getDurability(Player player, String[] args) {
	if (args.length == 0) {
	    ItemStack playerItem = player.getInventory().getItem(player.getInventory().getHeldItemSlot());
	    if (playerItem.getType().getMaxDurability() != 0)
		printItemDur(playerItem, player);
	    else {
		color.sendColouredMessage(player, "&2This item has no durability.");
		Utility.getInstance().writeToLog(
			player.getName() + " called /getdurability for item with ID of '" + playerItem.getTypeId()
				+ "', but failed because this item has no durability.");
	    }
	} else if (args.length == 1)
	    if (args[0].equalsIgnoreCase("armor")) {
		ItemStack[] armor = player.getInventory().getArmorContents();
		if ((armor[0].getAmount() == 0) && (armor[1].getAmount() == 0) && (armor[2].getAmount() == 0) && (armor[3].getAmount() == 0)) {
		    color.sendColouredMessage(player, "&2You do not have any armor equipped");
		    Utility.getInstance().writeToLog(player.getDisplayName() + " attempted to call /getdurability armor, but has no armor equipped");
		} else
		    for (int x = 0; x < armor.length; x++)
			if (armor[x].getAmount() == 0) {
			    if (x == 3) {
				color.sendColouredMessage(player, "&2You do not have a &fhelmet equipped");
				Utility.getInstance().writeToLog(player.getDisplayName() + " called /getdurability armor without having a helmet equipped");
			    } else if (x == 2) {
				color.sendColouredMessage(player, "&2You do not have a &fchestplate equipped");
				Utility.getInstance().writeToLog(player.getDisplayName() + " called /getdurability armor without having a chestplate equipped");
			    } else if (x == 1) {
				color.sendColouredMessage(player, "&2You do not have &fleggings equipped");
				Utility.getInstance().writeToLog(player.getDisplayName() + " called /getdurability armor without having leggings equipped");
			    } else {
				color.sendColouredMessage(player, "&2You do not have &fboots equipped");
				Utility.getInstance().writeToLog(player.getDisplayName() + " called /getdurability armor without having boots equipped");
			    }
			} else
			    printItemDur(armor[x], player);
	    } else if (args[0].equalsIgnoreCase("helmet")) {
		ItemStack helmet = player.getInventory().getHelmet();
		if (helmet.getAmount() == 1)
		    printItemDur(helmet, player);
		else {
		    color.sendColouredMessage(player, "&2You do not have a helmet equipped");
		    Utility.getInstance().writeToLog(player.getDisplayName() + " attempted to call /getdurability helmet, but did not have a helmet equipped");
		}
	    } else if (args[0].equalsIgnoreCase("chestplate")) {
		ItemStack chestplate = player.getInventory().getChestplate();
		int amount = chestplate.getAmount();
		if (amount == 1)
		    printItemDur(chestplate, player);
		else {
		    color.sendColouredMessage(player, "&2You do not have a chestplate equipped");
		    Utility.getInstance().writeToLog(
			    player.getDisplayName() + " attempted to call /getdurability chestplate, but did not have a helmet equipped");
		}
	    } else if (args[0].equalsIgnoreCase("leggings")) {
		ItemStack leggings = player.getInventory().getLeggings();
		int amount = leggings.getAmount();
		if (amount == 1)
		    printItemDur(leggings, player);
		else {
		    color.sendColouredMessage(player, "&2You do not have leggings equipped");
		    Utility.getInstance()
			    .writeToLog(player.getDisplayName() + " attempted to call /getdurability leggings, but did not have a helmet equipped");
		}
	    } else if (args[0].equalsIgnoreCase("boots")) {
		ItemStack boots = player.getInventory().getBoots();
		int amount = boots.getAmount();
		if (amount == 1)
		    printItemDur(boots, player);
		else {
		    color.sendColouredMessage(player, "&2You do not have boots equipped");
		    Utility.getInstance().writeToLog(player.getDisplayName() + " attempted to call /getdurability boots, but did not have a helmet equipped");
		}
	    } else {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Wrong Command Usage. &f/getdurability (helmet/chestplate/leggings/boots/armor)");
		Utility.getInstance().writeToLog(player.getDisplayName() + " incorrectly called /getdurability");
	    }
    }

    public void getFloor(Player player, String[] args) {
	String stringPlay = player.getName();
	if (args.length != 1) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Wrong Command Usage. &f/getfloor [Item]");
	    Utility.getInstance().writeToLog(stringPlay + " incorrectly called /getfloor");
	} else if (DynamicEconomy.useboundaries == false) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Boundaries such as floor and ceiling are disabled.");
	    Utility.getInstance().writeToLog(stringPlay + " called /getfloor, but boundaries are disabled.");
	} else {
	    ItemInfo item = ItemInfo.getItemInfo(args[0], getItemConfig());

	    if (item == null) {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2This item does not exist. ");
		Utility.getInstance().writeToLog(stringPlay + " attempted to get the floor of the non-existent item '" + args[0] + "'");
	    } else {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Price Floor of &f" + item + " &2is &f$" + decFormat.format(item.floor));
		Utility.getInstance().writeToLog(stringPlay + " called /getfloor for item '" + args[0] + "'");
	    }

	}
    }

    public static int getMat(int itemID) {
	String idStr = String.valueOf(itemID);
	String[] split = idStr.split("00");
	int mat = Integer.parseInt(split[0]);
	return mat;
    }

    public void getPrice(Player player, String[] args) {
	String stringPlay = player.getName();
	if ((args.length < 1) || (args.length > 3)) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Wrong Command Usage. &f/price [Item]");
	    Utility.getInstance().writeToLog(stringPlay + " incorrectly called /price");
	    return;
	}
	ItemInfo item = ItemInfo.getItemInfo(args[0], getItemConfig());
	if (item == null) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2This item does not exist. ");
	    Utility.getInstance().writeToLog(stringPlay + " called /price for the non-existent item '" + args[0] + "'");
	    return;
	}
	if (args.length == 1) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Price of &f" + item.name + "&2 is &f$" + item.price);
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Description: &f" + item.description);
	    if (DynamicEconomy.usestock)
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Stock: &f" + item.stock);
	    Utility.getInstance().writeToLog(stringPlay + " called /price for item " + item.ID);
	    return;
	}

	if (args.length >= 2) {
	    String command = args[1];
	    int amt = DynamicEconomy.defaultAmount;
	    double total = 0;
	    double price = item.price;
	    double tax = 0;
	    if (args.length == 3)
		if (args[2].equalsIgnoreCase("all"))
		    if (command.equals("sell"))
			amt = inv.getAmountOf(player, item.ID) - 1;
		    else if (command.equals("buy"))
			amt = item.stock;
		    else
			amt = Integer.parseInt(args[2]);
	    if (command.equalsIgnoreCase("sell")) {

		for (int x = 0; x < amt; x++) {
		    if (!DynamicEconomy.useboundaries || price > item.floor) {
			price = price - item.velocity;
			if (DynamicEconomy.useboundaries && price < item.floor)
			    price = item.floor;
		    }
		    total += price;
		}

		tax = DynamicEconomy.salestax * total;
		total -= tax;
	    } else if (command.equalsIgnoreCase("buy")) {

		for (int x = 0; x < amt; x++) {
		    total += price;
		    if (!DynamicEconomy.useboundaries || price < item.ceiling) {
			price = price + item.velocity;
			if (DynamicEconomy.useboundaries && price > item.ceiling)
			    price = item.ceiling;
		    }
		}

		tax = DynamicEconomy.purchasetax * total;
		total += tax;
	    } else {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2The type &f" + command + "&2 does not exist. Use either buy or sell.");
		Utility.getInstance().writeToLog(stringPlay + " called invalid type '" + command + "'");
		return;
	    }
	    String priceStr = decFormat.format(price);
	    String totalStr = decFormat.format(total);
	    tax = Double.valueOf(decFormat.format(tax)).doubleValue();

	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Price of &f" + item.name + "&2 is &f$" + priceStr);
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Description: &f" + item.description);
	    if (DynamicEconomy.usestock)
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Stock: &f" + item.stock);
	    if (command.equalsIgnoreCase("sale") || command.equalsIgnoreCase("sell"))
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Selling &f" + amt + "&2 of &f" + item.name + "&2 + $" + tax + " tax &fyields &2"
			+ totalStr);
	    else if (command.equalsIgnoreCase("purchase") || command.equalsIgnoreCase("buy"))
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Buying &f" + amt + "&2 of &f" + item.name + "&2 + $" + tax + " tax &fcosts &2"
			+ totalStr);
	    if (args.length == 2)
		Utility.getInstance().writeToLog(stringPlay + " called /price " + args[0] + " " + args[1]);
	    else if (args.length == 3)
		Utility.getInstance().writeToLog(stringPlay + " called /price " + args[0] + " " + args[1] + " " + args[2]);
	}
    }

    public void getVelocity(Player player, String[] args) {
	String stringPlay = player.getName();
	if (args.length != 1) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Wrong Command Usage. &f/getvelocity [Item]");
	    Utility.getInstance().writeToLog(stringPlay + " incorrectly called /getvelocity");
	} else {
	    ItemInfo item = ItemInfo.getItemInfo(args[0], getItemConfig());
	    if (item == null) {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2This item does not exist. ");
		Utility.getInstance().writeToLog(stringPlay + " attempted to get the velocity of the non-existent item '" + args[0] + "'");
	    } else {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Velocity of &f" + item + " &2is &f" + decFormat.format(item.velocity));
		Utility.getInstance().writeToLog(stringPlay + " called /getvelocity for item '" + item + "'");
	    }
	}
    }

    private void printNoPermissions(Player player, Command cmd) {
	color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You do not have permission to use this command.");
	Utility.getInstance().writeToLog(player.getName() + " called " + cmd.getName() + " but did not have permission.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

	if (sender instanceof Player) {
	    Player player = (Player) sender;

	    // PRICE COMMAND

	    if ((cmd.getName().equalsIgnoreCase("price")) || (cmd.getName().equalsIgnoreCase("deprice"))) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.price"))
		    getPrice(player, args);
		else
		    printNoPermissions(player, cmd);
		return true;
	    }
	    // SETPRICE COMMAND
	    if (cmd.getName().equalsIgnoreCase("setprice")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.setprice"))
		    setPrice(player, args);
		else
		    printNoPermissions(player, cmd);
		return true;
	    }

	    // SETFLOOR COMMAND
	    if (cmd.getName().equalsIgnoreCase("setfloor")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.setfloor"))
		    setFloor(player, args);
		else
		    printNoPermissions(player, cmd);
		return true;
	    }

	    // SETCEILING COMMAND

	    if (cmd.getName().equalsIgnoreCase("setceiling")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.setceiling"))
		    setCeiling(player, args);
		else
		    printNoPermissions(player, cmd);
		return true;
	    }

	    // GETFLOOR COMMAND

	    if (cmd.getName().equalsIgnoreCase("getfloor")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.getfloor"))
		    getFloor(player, args);
		else
		    printNoPermissions(player, cmd);
		return true;
	    }

	    // GETCEILING COMMAND

	    if (cmd.getName().equalsIgnoreCase("getceiling")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.getceiling"))
		    getCeiling(player, args);
		else
		    printNoPermissions(player, cmd);
		return true;
	    }

	    // GETVELOCITY COMMAND

	    if (cmd.getName().equalsIgnoreCase("getvelocity")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.getvelocity"))
		    getVelocity(player, args);
		else
		    printNoPermissions(player, cmd);
		return true;
	    }

	    // SETVELOCITY COMMAND

	    if (cmd.getName().equalsIgnoreCase("setvelocity")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.setvelocity"))
		    setVelocity(player, args);
		else
		    printNoPermissions(player, cmd);
		return true;
	    }

	    // DYNAMICECONOMY COMMAND

	    if (cmd.getName().equalsIgnoreCase("dynamiceconomy")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.dynamiceconomy")) {
		    commandList(player, args);
		    Utility.getInstance().writeToLog(player.getName() + " called /dynamiceconomy for help");
		} else
		    printNoPermissions(player, cmd);
		return true;
	    }

	    if (cmd.getName().equalsIgnoreCase("dynecon")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.dynamiceconomy")) {
		    commandList(player, args);
		    Utility.getInstance().writeToLog(player.getName() + " called /dynamiceconomy for help");
		} else
		    printNoPermissions(player, cmd);
		return true;
	    }

	    // BUY COMMAND

	    if ((cmd.getName().equalsIgnoreCase("buy")) || (cmd.getName().equalsIgnoreCase("debuy"))) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.buy"))
		    buy(player, args);
		else
		    printNoPermissions(player, cmd);
		return true;
	    }

	    // SELL COMMAND

	    if ((cmd.getName().equalsIgnoreCase("sell")) || (cmd.getName().equalsIgnoreCase("desell"))) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.sell"))
		    sell(player, args);
		else
		    printNoPermissions(player, cmd);
		return true;
	    }

	    // ISSTOCK COMMAND

	    if (cmd.getName().equalsIgnoreCase("isstock")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.isstock")) {
		    if (args.length > 0)
			color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Wrong Command Usage. &f/isstock");
		    else {
			boolean isStock = DynamicEconomy.getInstance().getConfig().getBoolean("Use-Stock", true);
			if (isStock)
			    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Stock is turned on.");
			else
			    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Stock is turned off.");
		    }
		    Utility.getInstance().writeToLog(player.getName() + " called /isstock");
		} else
		    printNoPermissions(player, cmd);
		return true;
	    }

	    // ISBOUNDARY COMMAND

	    if (cmd.getName().equalsIgnoreCase("isboundary")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.isboundary")) {
		    if (args.length > 0)
			color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Wrong Command Usage. &f/isboundary");
		    else {
			boolean isStock = DynamicEconomy.getInstance().getConfig().getBoolean("Use-boundaries", true);
			if (isStock)
			    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Boundaries are turned on.");
			else
			    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Boundaries are turned off.");
		    }
		    Utility.getInstance().writeToLog(player.getName() + " called /isboundary");
		} else
		    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You do not have permission to use this command.");
		return true;
	    }

	    // ADDSTOCK COMMAND

	    if (cmd.getName().equalsIgnoreCase("addstock")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.addstock"))
		    addStock(player, args);
		else
		    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You do not have permission to use this command.");
		return true;
	    }

	    // DYNAMICECONOMYRELOADCONFIG COMMAND

	    if (cmd.getName().equalsIgnoreCase("dynamiceconomyreloadconfig")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.dynamiceconomyreloadconfig")) {
		    DynamicEconomy.reloadConfigValues(player, args);
		    Utility.getInstance().writeToLog(player.getName() + " reloaded the DynamicEconomy config.yml");
		} else
		    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You do not have permission to use this command.");
		return true;
	    }

	    // REMOVESTOCK COMMAND

	    if (cmd.getName().equalsIgnoreCase("removestock")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.removestock"))
		    removeStock(player, args);
		else
		    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You do not have permission to use this command.");
		return true;
	    }

	    // GETDURABILITY COMMAND

	    if (cmd.getName().equalsIgnoreCase("getdurability")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.getdurability"))
		    getDurability(player, args);
		else
		    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You do not have permission to use this command.");
		return true;
	    }

	    // HASUPDATE COMMAND

	    if (cmd.getName().equalsIgnoreCase("hasupdate")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.update")) {
		    if (DynamicEconomy.enableUpdateChecker) {
			boolean isLatest = DynamicEconomy.getInstance().checkVersion();
			Utility.getInstance().writeToLog(player.getName() + " called /hasupdate");
			if (!isLatest)
			    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2New Version of DynamicEconomy Available!");
			else
			    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You have the latest version of DynamicEconomy!");
		    }
		} else
		    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You do not have permission to use this command.");
		return true;
	    }

	    // GETUPDATE COMMAND

	    if (cmd.getName().equalsIgnoreCase("dyneconupdate")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.update")) {
		    if (DynamicEconomy.enableUpdateChecker) {
			Utility.getInstance().writeToLog(player.getName() + " called /dyneconupdate and downloaded the latest version of DynamicEconomy");
			DynamicEconomy.getInstance().updater.forceDownload("http://cabin.minecraft.ms/downloads/DynamicEconomy.jar", "DynamicEconomy", player);
		    }
		} else
		    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You do not have permission to use this command.");
		return true;
	    }

	    // GET TAXES COMMAND

	    if (cmd.getName().equalsIgnoreCase("curtaxes")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.curtaxes"))
		    curTaxes(player, args);
		else
		    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You do not have permission to use this command.");
		return true;
	    }

	    // SET TAX COMMAND

	    if (cmd.getName().equalsIgnoreCase("settax")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.settax"))
		    setTaxes(player, args);
		else
		    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You do not have permission to use this command.");
		return true;
	    }

	    // DECLARE REGIONSHOP COMMAND

	    if (cmd.getName().equalsIgnoreCase("shopregion")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.shopregion"))
		    regionUtils.createRegion(player, args);
		else
		    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You do not have permission to use this command.");
		return true;
	    }

	    // REMOVE SHOP REGION COMMAND

	    if (cmd.getName().equalsIgnoreCase("removeshopregion")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.shopregion"))
		    regionUtils.deleteShopRegion(player, args);
		else
		    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You do not have permission to use this command.");
		return true;
	    }

	    // EXPAND SHOP REGION COMMAND

	    if (cmd.getName().equalsIgnoreCase("expandreg")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.shopregion"))
		    regionUtils.expandRegion(player, args);
		else
		    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You do not have permission to use this command.");
		return true;
	    }

	    // CONTRACT SHOP REGION COMMAND

	    if (cmd.getName().equalsIgnoreCase("contractreg")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.shopregion"))
		    regionUtils.contractRegion(player, args);
		else
		    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You do not have permission to use this command.");
		return true;
	    }

	    // SHOP REGION WAND COMMAND

	    if (cmd.getName().equalsIgnoreCase("shopregionwand")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.shopregion"))
		    regionUtils.wand(player, args);
		else
		    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You do not have permission to use this command.");
		return true;
	    }

	    // SHOP CURREGION COMMAND

	    if (cmd.getName().equalsIgnoreCase("curregion")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.shopregion"))
		    regionUtils.getCorners(player, args);
		else
		    printNoPermissions(player, cmd);
		return true;
	    }

	    // LOANS COMMAND

	    if (cmd.getName().equalsIgnoreCase("loan")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.loan")) {
		    if (DynamicEconomy.useLoans)
			loan.lend(player, args);
		    else {
			color.sendColouredMessage(player, DynamicEconomy.prefix + "&2The Bank is not available for loans at this time.");
			Utility.getInstance().writeToLog(player.getName() + " called /loan but loans are disabled.");
		    }
		} else
		    printNoPermissions(player, cmd);
		return true;
	    }

	    // GET INTEREST RATE COMMAND

	    if (cmd.getName().equalsIgnoreCase("curinterest")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.curinterest")) {
		    if (DynamicEconomy.useLoans)
			loan.getInterest(player, args);
		    else {
			color.sendColouredMessage(player, DynamicEconomy.prefix + "&2The Bank is not available for loans at this time.");
			Utility.getInstance().writeToLog(player.getName() + " called /curinterest but loans are disabled.");
		    }
		} else {
		    printNoPermissions(player, cmd);
		    Utility.getInstance().writeToLog(player.getName() + " called /curinterest but didn't have permission");
		}
		return true;
	    }

	    // GET LOANS COMMAND

	    if (cmd.getName().equalsIgnoreCase("curloans")) {
		if (DynamicEconomy.permission.has(player, "dynamiceconomy.loan")) {
		    if (DynamicEconomy.useLoans)
			loan.getLoans(player, args);
		    else {
			color.sendColouredMessage(player, DynamicEconomy.prefix + "&2The Bank is not available for loans at this time.");
			Utility.getInstance().writeToLog(player.getName() + " called /curloans but loans are disabled.");
		    }
		} else
		    printNoPermissions(player, cmd);
		return true;
	    }
	}
	return false;
    }

    public static boolean hasEnough(Inventory inv, Material type, int amount) {
	int totalAmountInInventory = 0;
	for (ItemStack is : inv.getContents())
	    if (is.getType() == type)
		totalAmountInInventory += is.getAmount();

	return totalAmountInInventory >= amount;
    }

    public static void removeInventoryItems(Inventory inv, Material type, int amount) {
	int amountToRemove = amount; // I dislike changing parameters
	for (ItemStack is : inv.getContents())
	    if (is.getType() == type)
		if (amountToRemove > is.getAmount()) {
		    amountToRemove -= is.getAmount();
		    inv.remove(is);
		} else {
		    is.setAmount(amountToRemove);
		    return;
		}
    }

    public void removeStock(Player player, String[] args) {
	String stringPlay = player.getName();
	if ((args.length < 2) || (args.length > 2)) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Wrong Command Usage. &f/addStock [Item] [AdditionalStock]");
	    Utility.getInstance().writeToLog(stringPlay + " incorrectly called /removestock");
	} else {
	    int removeStock = Integer.parseInt(args[1]);
	    if (removeStock <= 0) {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Cannot remove a negative amount (Use /addstock instead) ");
		Utility.getInstance().writeToLog(stringPlay + " tried to remove a negative stock amount");
	    }

	    ItemInfo item = ItemInfo.getItemInfo(args[0], getItemConfig());

	    getItemConfig().set(item.name + ".stock", String.valueOf(item.stock - removeStock));
	    DynamicEconomy.getInstance().saveItemConfig();

	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Stock succesfully added.");
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Previous Stock: &f" + item.stock + "&2 | New Stock: &f" + (item.stock - removeStock));
	    Utility.getInstance().writeToLog(
		    stringPlay + " removed " + removeStock + " stock of " + item.stock + " for a new stock total of " + (item.stock - removeStock));

	}

    }

    public void sell(Player player, String[] args) {
	String stringPlay = player.getName();

	if ((args.length == 0) || (args.length > 2)) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Wrong Command Usage. &f/sell [Item] (Amount)");
	    Utility.getInstance().writeToLog(stringPlay + " incorrectly called /sell");
	    return;
	}
	Location loc = player.getLocation();
	if (DynamicEconomy.useRegions) {
	    int x = loc.getBlockX();
	    int y = loc.getBlockY();
	    int z = loc.getBlockZ();

	    if (!regionUtils.withinRegion(x, y, z)) {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You are not within an economy region!");
		Utility.getInstance().writeToLog(stringPlay + " called /buy outside of an economy region.");
		return;
	    }
	}
	if (DynamicEconomy.location_restrict)
	    if (loc.getBlockY() <= DynamicEconomy.minimum_y) {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You are too deep underground to access the economy!.");
		Utility.getInstance().writeToLog(player.getName() + " called /sell but was too deep underground.");
		return;
	    } else if (loc.getBlockY() >= DynamicEconomy.maximum_y) {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You are too high up to access the economy!.");
		Utility.getInstance().writeToLog(player.getName() + " called /sell but was too high up.");
		return;
	    }

	ItemInfo item = ItemInfo.getItemInfo(args[0], getItemConfig());

	if (item == null) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2The item " + args[0] + " does not exist. ");
	    Utility.getInstance().writeToLog(player.getName() + " attempted to sell the non-existent item '" + args[0] + "'");
	    return;
	}
	if (DynamicEconomy.bannedSaleItems.contains(item.name)) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2This item is banned, and not allowed to be purchased in the economy.");
	    Utility.getInstance().writeToLog(player.getName() + " attempted to buy the banned item: " + item.name);
	    return;
	}

	int saleAmount = DynamicEconomy.defaultAmount;
	if (args.length == 2)
	    if (args[1].equalsIgnoreCase("all"))
		saleAmount = inv.getAmountOf(player, item.ID);
	    else
		try {
		    saleAmount = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
		    color.sendColouredMessage(player, DynamicEconomy.prefix
			    + "&2You entered the command arguments in the wrong order, or your amount was invalid. Try again. ");
		    Utility.getInstance().writeToLog(stringPlay + " entered an invalid purchase amount, or entered command arguments in the wrong order.");
		    return;
		}

	if (saleAmount < 0) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Cannot sell a negative amount!");
	    Utility.getInstance().writeToLog(stringPlay + " attempted to sell " + saleAmount + " of '" + item + "', but this amount is invalid.");
	    return;
	}
	if (!hasEnough(player.getInventory(), Material.getMaterial(item.ID), saleAmount)) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You do not have &f" + saleAmount + " " + item);
	    Utility.getInstance().writeToLog(stringPlay + " attempted to sell " + saleAmount + " of " + item + ", but didn't have that many.");
	    return;
	}

	double totalSale = 0;
	double newPrice = item.price;
	final double oldPrice = item.price;
	if (Material.getMaterial(item.ID).getMaxDurability() > 0) {
	    ItemStack[] invContents = player.getInventory().getContents();
	    for (int i = 0; i < saleAmount; i++) {
		int playerDur = invContents[i].getDurability();
		int maxDur = invContents[i].getType().getMaxDurability();
		double percentDur = 1 - (double) playerDur / maxDur;

		if (!DynamicEconomy.useboundaries || newPrice > item.floor) {
		    newPrice -= item.velocity * percentDur; // TODO feature?
		    if (DynamicEconomy.useboundaries && newPrice < item.floor)
			newPrice = item.floor;
		    totalSale += item.price * percentDur;

		    color.sendColouredMessage(player, DynamicEconomy.prefix + "&f1 &2" + item.name + "&f with " + decFormat.format(percentDur)
			    + "% &2durability = &2$" + decFormat.format(item.price * percentDur));
		    Utility.getInstance().writeToLog(
			    stringPlay + " sold a '" + item.name + "' at " + decFormat.format(percentDur) + "% durability for "
				    + decFormat.format(item.price * percentDur));
		}
	    }
	} else
	    for (int x = 0; x < saleAmount; x++) {
		if (!DynamicEconomy.useboundaries || newPrice > item.floor) {
		    newPrice -= item.velocity;
		    if (DynamicEconomy.useboundaries && newPrice < item.floor)
			newPrice = item.floor;
		}
		totalSale += newPrice;
	    }

	color.sendColouredMessage(player, DynamicEconomy.prefix + "&2TOTAL SALE: &f$" + totalSale);
	// int mat = 0;
	// short dmg = 0;
	// byte data = 0;
	//
	// ItemStack saleItem;
	// int userAmount = 0;
	//
	// if (item.ID > 1000) {
	// mat = getMat(item.ID);
	// dmg = getDmg(item.ID);
	//
	// saleItem = new ItemStack(mat, saleAmount, dmg);
	// userAmount = inv.getAmountOfDataValue(player, saleItem);
	// } else
	// userAmount = inv.getAmountOf(player, item.ID);
	//
	// // boolean containsItem = player.getInventory().contains(saleItem);
	removeInventoryItems(player.getInventory(), Material.getMaterial(item.ID), saleAmount);

	double tax = DynamicEconomy.salestax * totalSale;
	totalSale -= tax;

	color.sendColouredMessage(player, DynamicEconomy.prefix + "&f" + saleAmount + " &2" + item + "&f @ $" + decFormat.format(newPrice) + "&2 - "
		+ decFormat.format(DynamicEconomy.salestax * 100) + "% tax = &f$" + decFormat.format(totalSale) + " &2TOTAL");
	Utility.getInstance().writeToLog(stringPlay + " succesfully sold " + saleAmount + " of '" + item + "' for " + decFormat.format(totalSale));

	DynamicEconomy.economy.depositPlayer(player.getName(), totalSale);
	if (DynamicEconomy.depositTax == true)
	    try {
		DynamicEconomy.economy.depositPlayer(DynamicEconomy.taxAccount, tax);
	    } catch (Exception e) {
		log.info("Tax-Account " + DynamicEconomy.taxAccount + " not found.");
		Utility.getInstance()
			.writeToLog("Attempted to deposit tax of $" + tax + " to account " + DynamicEconomy.taxAccount + " but account not found.");
	    }

	getItemConfig().set(item.name + ".price", String.valueOf(newPrice));
	if (DynamicEconomy.usestock)
	    getItemConfig().set(item.name + ".stock", String.valueOf(item.stock + saleAmount));

	color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Sale Success!");

	String notifyString = DynamicEconomy.prefix + "&2New Price of &f" + item + "&2 is &f$" + decFormat.format(newPrice) + "&2 ("
		+ decFormat.format(oldPrice - newPrice) + ")";
	if (DynamicEconomy.globalNotify)
	    for (Player p : Bukkit.getServer().getOnlinePlayers())
		color.sendColouredMessage(p, notifyString);
	else if (DynamicEconomy.localNotify)
	    color.sendColouredMessage(player, notifyString);
	Utility.getInstance().writeToLog(
		"[DynamicEconomy] New price of " + item + " changed dynamically to " + decFormat.format(newPrice) + "(" + decFormat.format(oldPrice - newPrice)
			+ ")");
	DynamicEconomy.getInstance().saveItemConfig();
    }

    public void setCeiling(Player player, String[] args) {
	String stringPlay = player.getName();
	if (args.length != 2) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Wrong Command Usage. &f/setceiling [Item] [FloorPrice]");
	    Utility.getInstance().writeToLog(stringPlay + " incorrectly called /setceiling");
	} else if (DynamicEconomy.useboundaries == false) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Boundaries such as floor and ceiling are disabled.");
	    Utility.getInstance().writeToLog(stringPlay + " called /setceiling, but boundaries are disabled.");
	} else {
	    ItemInfo item = ItemInfo.getItemInfo(args[0], getItemConfig());
	    double ceiling = Double.parseDouble(args[1]);
	    if (item == null) {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2This item does not exist. ");
		Utility.getInstance().writeToLog(stringPlay + " attempted to set the floor of the non-existent item '" + args[0] + "'");
		return;
	    }
	    if (ceiling < item.price) {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2The price of " + item + " is higher than desired ceiling.");
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You must decrease price to below desired ceiling, or set higher ceiling.");
		color.sendColouredMessage(player,
			DynamicEconomy.prefix + "&2DESIRED CEILING: &f$" + decFormat.format(ceiling) + " PRICE: &f$" + decFormat.format(item.price));
		Utility.getInstance().writeToLog(
			stringPlay + " attempted to set ceiling of " + item + " to " + decFormat.format(ceiling)
				+ ", but the price is higher than the desired ceiling.");
	    } else {
		getItemConfig().set(item.name + ".ceiling", String.valueOf(ceiling));
		DynamicEconomy.getInstance().saveItemConfig();
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Price Ceiling of &f" + item + " set to &f$" + ceiling);
		Utility.getInstance().writeToLog(stringPlay + " set the floor of " + item + " to " + ceiling);
	    }

	}
    }

    public void setFloor(Player player, String[] args) {
	String stringPlay = player.getName();
	if (args.length != 2) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Wrong Command Usage. &f/setfloor [Item] [FloorPrice]");
	    Utility.getInstance().writeToLog(stringPlay + " incorrectly called /setfloor");
	} else if (DynamicEconomy.useboundaries == false) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Boundaries such as floor and ceiling are disabled.");
	    Utility.getInstance().writeToLog(stringPlay + " called /setfloor, but boundaries are disabled");
	} else {
	    ItemInfo item = ItemInfo.getItemInfo(args[0], getItemConfig());
	    if (item == null) {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2This item does not exist. ");
		Utility.getInstance().writeToLog(stringPlay + " attempted to set the floor of the non-existent item '" + args[0] + "'");
		return;
	    }
	    double floor = Double.parseDouble(args[1]);
	    if (floor < item.price) {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2The price of " + item + " is lower than desired floor.");
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2You must increase price above desired floor, or set lower floor.");
		color.sendColouredMessage(player,
			DynamicEconomy.prefix + "&2DESIRED FLOOR: &f$" + decFormat.format(floor) + " PRICE: &f$" + decFormat.format(item.price));
		Utility.getInstance().writeToLog(
			stringPlay + " attempted to set floor of " + item + " to " + floor + ", but the price is lower than the desired floor.");
	    } else {
		getItemConfig().set(item.name + ".floor", String.valueOf(floor));
		DynamicEconomy.getInstance().saveItemConfig();
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Price Floor of &f" + item + " set to&f $" + decFormat.format(floor));
		Utility.getInstance().writeToLog(stringPlay + " set the floor of " + item + " to " + floor);
	    }
	}
    }

    public void setPrice(Player player, String[] args) {
	String stringPlay = player.getName();
	if (args.length != 2) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Wrong Command Usage. &f/setprice [Item] [Price]");
	    Utility.getInstance().writeToLog(stringPlay + " incorrectly called /setprice");
	} else {
	    ItemInfo item = ItemInfo.getItemInfo(args[0], getItemConfig());

	    if (item == null) {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2This item does not exist. ");
		Utility.getInstance().writeToLog(stringPlay + " called /setprice for the non-existent item '" + args[0] + "'");
	    } else {
		double price = Double.parseDouble(args[2]);

		if (!DynamicEconomy.useboundaries || ((price >= item.floor) && (price <= item.ceiling))) {
		    getItemConfig().set(item.name + ".price", String.valueOf(price));
		    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Price of " + item + " set to &f$" + decFormat.format(item.price));
		    DynamicEconomy.getInstance().saveItemConfig();
		    Utility.getInstance().writeToLog(stringPlay + " set the price of " + item + " to " + decFormat.format(item.price));
		} else {
		    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Desired price is not within bounds.");
		    color.sendColouredMessage(player,
			    DynamicEconomy.prefix + "&2MIN: &f$" + decFormat.format(item.floor) + " &2MAX: &f$" + decFormat.format(item.ceiling));
		    Utility.getInstance().writeToLog(stringPlay + " attempted to set the price of " + item + "outside of bounds");
		}

	    }
	}
    }

    public void setTaxes(Player player, String[] args) {
	String stringPlay = player.getName();
	if ((args.length > 2) || (args.length < 2)) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Wrong Command Usage. &f/settax [sale/purchase] [amount]");
	    Utility.getInstance().writeToLog(stringPlay + " incorrectly called /settax");
	} else {
	    double tax = 0;
	    try {
		tax = Double.parseDouble(args[1]);
	    } catch (NumberFormatException e) {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2 " + args[1] + "&f% is an invalid amount");
		return;
	    }
	    if (tax < 0 || tax > 100) {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2 " + tax + "&f% is an invalid amount");
		return;
	    }

	    if (args[0].equalsIgnoreCase("sale"))
		DynamicEconomy.config.set("salestax", String.valueOf(tax));
	    else if (args[0].equalsIgnoreCase("purchase"))
		DynamicEconomy.config.set("purchasetax", String.valueOf(tax));
	    DynamicEconomy.getInstance().saveMainConfig();
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2" + args[0] + "tax set to &f" + (Double.parseDouble(args[1]) * 100) + "%");
	    Utility.getInstance().writeToLog(stringPlay + " set " + args[0] + "tax to " + args[1]);
	    DynamicEconomy.relConfig();
	}
    }

    public void setVelocity(Player player, String[] args) {
	String stringPlay = player.getName();
	if (args.length != 2) {
	    color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Wrong Command Usage. &f/setvelocity [Item] [Velocity]");
	    Utility.getInstance().writeToLog(stringPlay + " incorrectly called /setvelocity");
	} else {
	    ItemInfo item = ItemInfo.getItemInfo(args[0], getItemConfig());
	    double velocity = Double.parseDouble(args[1]);
	    if (item == null) {
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2This item does not exist. ");
		Utility.getInstance().writeToLog(stringPlay + " attempted to set the velocity of the non-existent item '" + args[0] + "'");
	    } else {
		getItemConfig().set(item.name + ".velocity", String.valueOf(velocity));
		DynamicEconomy.getInstance().saveItemConfig();
		color.sendColouredMessage(player, DynamicEconomy.prefix + "&2Velocity of &f" + item + " &2set to &f" + decFormat.format(velocity));
		Utility.getInstance().writeToLog(stringPlay + " set the velocity of " + item + " to " + decFormat.format(velocity));
	    }
	}
    }

    private void printItemDur(ItemStack item, Player player) {
	int playerDur = item.getDurability();
	String itemName = item.getType().toString();
	int maxDur = item.getType().getMaxDurability();
	double percentDur = 100 - (double) playerDur / maxDur * 100;
	color.sendColouredMessage(player, "&2The durability of &f" + itemName + "&2 is at &f" + percentDur + "%.&2 You have &f" + (maxDur - playerDur)
		+ "&2 uses left out of a possible &f" + maxDur);
	Utility.getInstance().writeToLog(
		player.getName() + " called /getdurability and found the durability of their '" + itemName + "' to be " + decFormat.format(percentDur) + "%");
    }
}
