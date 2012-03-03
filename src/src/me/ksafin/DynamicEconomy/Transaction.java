package me.ksafin.DynamicEconomy;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import couk.Adamki11s.Extras.Colour.ExtrasColour;

public class Transaction implements Runnable {

    private ExtrasColour color = new ExtrasColour();

    public static DecimalFormat decFormat = new DecimalFormat("#.##");
    public static DecimalFormat changeFormat = new DecimalFormat("#.#####");

    @Override
    public void run() {
	Set<String> itemsSet = DynamicEconomy.getInstance().itemConfig.getKeys(false);
	// ConfigurationSection root = itemConfig.getConfigurationSection("STONE").getParent();

	// log.info(root.toString());

	Object[] itemsObj = (itemsSet.toArray());
	String[] items = new String[itemsObj.length];

	for (int i = 0; i < items.length; i++)
	    items[i] = itemsObj[i].toString();

	long time;
	double price;
	long period = DynamicEconomy.overTimePriceDecayPeriod * 60 * 1000;

	for (int x = 0; x < items.length; x++) {
	    time = DynamicEconomy.getInstance().itemConfig.getLong(items[x] + ".time");
	    long difference = new Date().getTime() - time;

	    if ((difference >= period) && (time != 0)) {
		price = DynamicEconomy.getInstance().itemConfig.getDouble(items[x] + ".price");
		price = price - (price * DynamicEconomy.overTimePriceDecayPercent);
		DynamicEconomy.getInstance().itemConfig.set(items[x] + ".price", String.valueOf(price));

		if (DynamicEconomy.globalNotify)
		    for (Player p : Bukkit.getServer().getOnlinePlayers())
			color.sendColouredMessage(p, DynamicEconomy.prefix + "&2New Price of &f" + items[x] + "&2 is &f$" + decFormat.format(price) + "&2 ( -"
				+ (DynamicEconomy.overTimePriceDecayPercent * 100) + "% )");
		DynamicEconomy.getInstance().itemConfig.set(items[x] + ".time", String.valueOf(new Date().getTime()));
		DynamicEconomy.getInstance().saveItemConfig();

	    }
	}

    }

    public Transaction() {

    }

}
