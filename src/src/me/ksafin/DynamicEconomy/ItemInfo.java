package me.ksafin.DynamicEconomy;

import org.bukkit.configuration.file.FileConfiguration;

public class ItemInfo {
    public final double price;
    public final double floor;
    public final double ceiling;
    public final double velocity;
    public final int stock;
    public final int ID; // TODO we should add damage types here
    public final String name;
    public final String description;

    // public static Map<String, String> itemNameMap = new HashMap<String, String>(500) {
    // {
    // ;
    // }
    // };

    @Override
    public String toString() {
	return ID + ":" + name;
    }

    public static ItemInfo getItemInfo(String item, FileConfiguration itemConfig) {
	String name = getTrueName(item);
	if (name.equals(""))
	    return null;
	return new ItemInfo(name, itemConfig);
    }

    private ItemInfo(String name, FileConfiguration itemConfig) {
	this.name = name;
	price = itemConfig.getDouble(name + ".price", 0);
	floor = itemConfig.getDouble(name + ".floor", 0);
	ceiling = itemConfig.getDouble(name + ".ceiling", 0);
	velocity = itemConfig.getDouble(name + ".velocity", 0);
	stock = itemConfig.getInt(name + ".stock", 0);
	ID = itemConfig.getInt(name + ".id", 0);
	description = itemConfig.getString(name + ".description", "");
    }

    // public static int getMaxDur(String itemName) {
    // // if (itemName.startsWith("WOOD_") || itemName.startsWith("STONE_") || itemName.startsWith("IRON_") || itemName.startsWith("GOLD_")
    // // || itemName.startsWith("DIAMOND_") || itemName.startsWith("LEATHER_"))
    // // if (itemName.endsWith("_PICKAXE") || itemName.endsWith("_AXE") || itemName.endsWith("_SPADE") || itemName.endsWith("_HOE")
    // // || itemName.endsWith("_SWORD") || itemName.endsWith("_HELMET") || itemName.endsWith("_CHESTPLATE") ||
    // itemName.endsWith("_LEGGINGS")
    // // || itemName.endsWith("_BOOTS"))
    // // return Material.getMaterial(itemName).getMaxDurability();
    // return 0;
    // }

    public static String getTrueName(String item) {
	// Should be done with a hash map
	String itemName = item.toUpperCase();
	if ((itemName.equals("STONE")) || (itemName.equals("SMOOTHSTONE")) || (itemName.equals("1")))
	    return "STONE";
	else if (itemName.equals("DIRT") || (itemName.equals("3")))
	    return "DIRT";
	else if ((itemName.equals("PLANK")) || (itemName.equals("WOODENPLANK")) || (itemName.equals("PLANKS")) || (itemName.equals("WOODENPLANKS"))
		|| (itemName.equals("5")))
	    return "PLANK";
	else if ((itemName.equals("COBBLESTONE")) || (itemName.equals("COBBLE")) || (itemName.equals("4")))
	    return "COBBLESTONE";
	else if (itemName.equals("SAPLING") || (itemName.equals("6")))
	    return "SAPLING";
	else if (itemName.equals("SAND") || (itemName.equals("12")))
	    return "SAND";
	else if (itemName.equals("GRAVEL") || (itemName.equals("13")))
	    return "GRAVEL";
	else if (itemName.equals("WOOD") || (itemName.equals("LOG")) || (itemName.equals("17")) || (itemName.equals("17:0")))
	    return "WOOD";
	else if (itemName.equals("BIRCHWOOD") || (itemName.equals("BIRCHLOG")) || (itemName.equals("17:1")))
	    return "BIRCHWOOD";
	else if (itemName.equals("DARKWOOD") || (itemName.equals("DARKLOG")) || (itemName.equals("PINEWOOD")) || (itemName.equals("SPRUCEWOOD"))
		|| (itemName.equals("17:2")))
	    return "PINEWOOD";
	else if (itemName.equals("JUNGLEWOOD") || (itemName.equals("JUNGLELOG")) || (itemName.equals("17:3")))
	    return "JUNGLEWOOD";
	else if (itemName.equals("GLASS") || (itemName.equals("GLASSBLOCK")) || (itemName.equals("20")))
	    return "GLASS";
	else if (itemName.equals("DISPENSER") || (itemName.equals("23")))
	    return "DISPENSER";
	else if (itemName.equals("SANDSTONE") || (itemName.equals("SANDBRICK")) || (itemName.equals("24")))
	    return "SANDSTONE";
	else if (itemName.equals("NOTEBLOCK") || (itemName.equals("MUSICBLOCK")) || (itemName.equals("25")))
	    return "NOTEBLOCK";
	else if (itemName.equals("POWEREDRAIL") || (itemName.equals("POWERRAIL")) || (itemName.equals("POWERTRACK")) || (itemName.equals("POWEREDTRACK"))
		|| (itemName.equals("27")))
	    return "POWEREDRAIL";
	else if (itemName.equals("DETECTORRAIL") || (itemName.equals("DETECTORTRACK")) || (itemName.equals("28")))
	    return "DETECTORRAIL";
	else if (itemName.equals("STICKYPISTON") || (itemName.equals("29")))
	    return "STICKYPISTON";
	else if (itemName.equals("PISTON") || (itemName.equals("31")))
	    return "PISTON";
	else if (itemName.equals("WOOL") || (itemName.equals("35")))
	    return "WOOL";
	else if (itemName.equals("ORANGEWOOL") || (itemName.equals("35:1")))
	    return "ORANGEWOOL";
	else if (itemName.equals("MAGENTAWOOL") || (itemName.equals("35:2")))
	    return "MAGENTAWOOL";
	else if (itemName.equals("LIGHTBLUEWOOL") || (itemName.equals("35:3")))
	    return "LIGHTBLUEWOOL";
	else if (itemName.equals("YELLOWWOOL") || (itemName.equals("35:4")))
	    return "YELLOWWOOL";
	else if (itemName.equals("LIMEWOOL") || (itemName.equals("35:5")))
	    return "LIMEWOOL";
	else if (itemName.equals("PINKWOOL") || (itemName.equals("35:6")))
	    return "PINKWOOL";
	else if (itemName.equals("GRAYWOOL") || itemName.equals("GREYWOOL") || (itemName.equals("35:7")))
	    return "GRAYWOOL";
	else if (itemName.equals("LIGHTGRAYWOOL") || itemName.equals("LIGHTGREYWOOL") || (itemName.equals("35:8")))
	    return "LIGHTGRAYWOOL";
	else if (itemName.equals("CYANWOOL") || (itemName.equals("35:9")))
	    return "CYANWOOL";
	else if (itemName.equals("PURPLEWOOL") || (itemName.equals("35:10")))
	    return "PURPLEWOOL";
	else if (itemName.equals("BLUEWOOL") || (itemName.equals("35:11")))
	    return "BLUEWOOL";
	else if (itemName.equals("BROWNWOOL") || (itemName.equals("35:12")))
	    return "BROWNWOOL";
	else if (itemName.equals("GREENWOOL") || (itemName.equals("35:13")))
	    return "GREENWOOL";
	else if (itemName.equals("REDWOOL") || (itemName.equals("35:14")))
	    return "REDWOOL";
	else if (itemName.equals("BLACKWOOL") || (itemName.equals("35:15")))
	    return "BLACKWOOL";
	else if (itemName.equals("DANDELION") || (itemName.equals("YELLOWFLOWER")) || (itemName.equals("37")))
	    return "DANDELION";
	else if (itemName.equals("ROSE") || (itemName.equals("REDFLOWER")) || (itemName.equals("38")))
	    return "ROSE";
	else if (itemName.equals("BROWNMUSHROOM") || (itemName.equals("39")))
	    return "BROWNMUSHROOM";
	else if (itemName.equals("REDMUSHROOM") || (itemName.equals("40")))
	    return "REDMUSHROOM";
	else if (itemName.equals("GOLDBLOCK") || (itemName.equals("BLOCKOFGOLD")) || (itemName.equals("41")))
	    return "GOLDBLOCK";
	else if (itemName.equals("IRONBLOCK") || (itemName.equals("BLOCKOFIRON")) || (itemName.equals("42")))
	    return "IRONBLOCK";
	else if (itemName.equals("DOUBLESLABS") || (itemName.equals("DOUBLESLAB")) || (itemName.equals("DOUBLESTEP")) || (itemName.equals("43")))
	    return "DOUBLESLABS";
	else if (itemName.equals("STONESLAB") || (itemName.equals("SLAB")) || (itemName.equals("SLABS")) || (itemName.equals("STONESLABS"))
		|| (itemName.equals("44")))
	    return "SLABS";
	else if (itemName.equals("SANDSTONESLAB") || (itemName.equals("SANDSTONESLABS")) || (itemName.equals("SANDSLABS")) || (itemName.equals("SANDSLAB"))
		|| (itemName.equals("44:1")))
	    return "SANDSTONESLABS";
	else if (itemName.equals("PLANKSLABS") || (itemName.equals("PLANKSLAB")) || (itemName.equals("WOODSLABS")) || (itemName.equals("WOODSLAB"))
		|| (itemName.equals("44:2")))
	    return "PLANKSLABS";
	else if (itemName.equals("COBBLESLAB") || (itemName.equals("COBBLESLABS")) || (itemName.equals("44:3")))
	    return "COBBLESLABS";
	else if (itemName.equals("BRICKSLABS") || (itemName.equals("BRICKSLAB")) || (itemName.equals("44:4")))
	    return "BRICKSLABS";
	else if (itemName.equals("STONEBRICKSLAB") || (itemName.equals("STONEBRICKSLABS")) || (itemName.equals("STONEBRICKSSLABS"))
		|| (itemName.equals("STONEBRICKSSLAB")) || (itemName.equals("44:5")))
	    return "STONEBRICKSLABS";
	else if (itemName.equals("BRICKBLOCK") || (itemName.equals("45")))
	    return "BRICKBLOCK";
	else if (itemName.equals("TNT") || (itemName.equals("47")))
	    return "TNT";
	else if (itemName.equals("BOOKSHELF") || (itemName.equals("48")))
	    return "BOOKSHELF";
	else if (itemName.equals("MOSSYCOBBLE") || (itemName.equals("MOSSCOBBLE")) || (itemName.equals("MOSSYCOBBLESTONE")) || (itemName.equals("48")))
	    return "MOSSYCOBBLE";
	else if (itemName.equals("OBSIDIAN") || (itemName.equals("OBBY")) || (itemName.equals("49")))
	    return "OBSIDIAN";
	else if (itemName.equals("TORCH") || (itemName.equals("50")))
	    return "TORCH";
	else if (itemName.equals("WOODENSTAIRS") || (itemName.equals("WOODSTAIRS")) || (itemName.equals("PLANKSTAIRS")) || (itemName.equals("53")))
	    return "WOODENSTAIRS";
	else if (itemName.equals("CHEST") || (itemName.equals("54")))
	    return "CHEST";
	else if (itemName.equals("DIAMONDBLOCK") || (itemName.equals("BLOCKOFDIAMOND")) || (itemName.equals("57")))
	    return "DIAMONDBLOCK";
	else if (itemName.equals("CRAFTBENCH") || (itemName.equals("CRAFTINGBENCH")) || (itemName.equals("CRAFTINGTABLE")) || (itemName.equals("58")))
	    return "CRAFTBENCH";
	else if (itemName.equals("FURNACE") || (itemName.equals("61")))
	    return "FURNACE";
	else if (itemName.equals("LADDER") || (itemName.equals("LADDERS")) || (itemName.equals("65")))
	    return "LADDER";
	else if (itemName.equals("RAIL") || (itemName.equals("RAILS")) || (itemName.equals("TRACKS")) || (itemName.equals("66")))
	    return "RAIL";
	else if (itemName.equals("COBBLESTAIRS") || (itemName.equals("COBBLESTONESTAIRS")) || (itemName.equals("67")))
	    return "COBBLESTAIRS";
	else if (itemName.equals("LEVER") || (itemName.equals("69")))
	    return "LEVER";
	else if (itemName.equals("STONEPLATE") || (itemName.equals("STONEPRESSUREPLATE")) || (itemName.equals("70")))
	    return "STONEPLATE";
	else if (itemName.equals("WOODENPLATE") || (itemName.equals("WOODENPRESSUREPLATE")) || (itemName.equals("72")))
	    return "WOODENPLATE";
	else if (itemName.equals("BUTTON") || (itemName.equals("STONEBUTTON")) || (itemName.equals("77")))
	    return "BUTTON";
	else if (itemName.equals("CACTUS") || (itemName.equals("CACTI")) || (itemName.equals("81")))
	    return "CACTUS";
	else if (itemName.equals("JUKEBOX") || (itemName.equals("84")))
	    return "JUKEBOX";
	else if (itemName.equals("FENCE") || (itemName.equals("85")))
	    return "FENCE";
	else if (itemName.equals("PUMPKIN") || (itemName.equals("86")))
	    return "PUMPKIN";
	else if (itemName.equals("NETHERRACK") || (itemName.equals("87")))
	    return "NETHERRACK";
	else if (itemName.equals("SOULSAND") || (itemName.equals("SLOWSAND")) || (itemName.equals("QUICKSAND")) || (itemName.equals("88")))
	    return "SOULSAND";
	else if (itemName.equals("GLOWSTONE") || (itemName.equals("GLOWSTONEBLOCK")) || (itemName.equals("89")))
	    return "GLOWSTONE";
	else if (itemName.equals("TRAPDOOR") || (itemName.equals("96")))
	    return "TRAPDOOR";
	else if (itemName.equals("STONEBRICKS") || (itemName.equals("STONEBRICK")) || (itemName.equals("98")))
	    return "STONEBRICKS";
	else if (itemName.equals("IRONBARS") || (itemName.equals("101")))
	    return "IRONBARS";
	else if (itemName.equals("GLASSPANE") || (itemName.equals("102")))
	    return "GLASSPANE";
	else if (itemName.equals("BRICKSTAIRS") || (itemName.equals("108")))
	    return "BRICKSTAIRS";
	else if (itemName.equals("STONEBRICKSTAIRS") || (itemName.equals("109")))
	    return "STONEBRICKSTAIRS";
	else if (itemName.equals("NETHERBRICK") || (itemName.equals("NETHERSTONE")) || (itemName.equals("112")))
	    return "NETHERBRICK";
	else if (itemName.equals("NETHERBRICKFENCE") || (itemName.equals("NETHERSTONEFENCE")) || (itemName.equals("113")))
	    return "NETHERBRICKFENCE";
	else if (itemName.equals("NETHERBRICKSTAIRS") || (itemName.equals("NETHERSTONESTAIRS")) || (itemName.equals("114")))
	    return "NETHERBRICKSTAIRS";
	else if (itemName.equals("ENCHANTMENTTABLE") || (itemName.equals("116")))
	    return "ENCHANTMENTTABLE";
	else if (itemName.equals("IRONSHOVEL") || (itemName.equals("ISHOVEL")) || (itemName.equals("256")))
	    return "IRONSHOVEL";
	else if (itemName.equals("IRONPICKAXE") || (itemName.equals("IPICKAXE")) || (itemName.equals("IPICK")) || (itemName.equals("IRONPICK"))
		|| (itemName.equals("257")))
	    return "IRONPICKAXE";
	else if (itemName.equals("IRONAXE") || (itemName.equals("IAXE")) || (itemName.equals("IRONHATCHET")) || (itemName.equals("IHATCHET"))
		|| (itemName.equals("258")))
	    return "IRONAXE";
	else if (itemName.equals("IRONSHOVEL") || (itemName.equals("ISHOVEL")) || (itemName.equals("256")))
	    return "IRONSHOVEL";
	else if (itemName.equals("FLINTANDSTEEL") || (itemName.equals("FLINTSTEEL")) || (itemName.equals("259")))
	    return "FLINTANDSTEEL";
	else if (itemName.equals("APPLE") || (itemName.equals("REDAPPLE")) || (itemName.equals("260")))
	    return "APPLE";
	else if (itemName.equals("BOW") || (itemName.equals("261")))
	    return "BOW";
	else if (itemName.equals("ARROW") || (itemName.equals("ARROWS")) || (itemName.equals("262")))
	    return "ARROW";
	else if (itemName.equals("COAL") || (itemName.equals("263")) || (itemName.equals("263:0")))
	    return "COAL";
	else if (itemName.equals("CHARCOAL") || (itemName.equals("263:1")))
	    return "CHARCOAL";
	else if (itemName.equals("DIAMOND") || (itemName.equals("264")))
	    return "DIAMOND";
	else if (itemName.equals("IRONINGOT") || (itemName.equals("IRON")) || (itemName.equals("265")))
	    return "IRONINGOT";
	else if (itemName.equals("GOLDINGOT") || (itemName.equals("GOLD")) || (itemName.equals("266")))
	    return "GOLDINGOT";
	else if (itemName.equals("IRONSWORD") || (itemName.equals("ISWORD")) || (itemName.equals("267")))
	    return "IRONSWORD";
	else if (itemName.equals("WOODENSWORD") || (itemName.equals("WSWORD")) || (itemName.equals("268")))
	    return "WOODENSWORD";
	else if (itemName.equals("WOODENSHOVEL") || (itemName.equals("WSHOVEL")) || (itemName.equals("269")))
	    return "WOODENSHOVEL";
	else if (itemName.equals("WOODENPICKAXE") || (itemName.equals("WPICKAXE")) || (itemName.equals("WPICK")) || (itemName.equals("WOODENPICK"))
		|| (itemName.equals("270")))
	    return "WOODENPICKAXE";
	else if (itemName.equals("WOODENAXE") || (itemName.equals("WAXE")) || (itemName.equals("WOODENHATCHET")) || (itemName.equals("WHATCHET"))
		|| (itemName.equals("271")))
	    return "WOODENAXE";
	else if (itemName.equals("STONESWORD") || (itemName.equals("SSWORD")) || (itemName.equals("272")))
	    return "STONESWORD";
	else if (itemName.equals("STONESHOVEL") || (itemName.equals("SSHOVEL")) || (itemName.equals("273")))
	    return "STONESHOVEL";
	else if (itemName.equals("STONEPICKAXE") || (itemName.equals("SPICKAXE")) || (itemName.equals("SPICK")) || (itemName.equals("STONEPICK"))
		|| (itemName.equals("274")))
	    return "STONEPICKAXE";
	else if (itemName.equals("STONEAXE") || (itemName.equals("SAXE")) || (itemName.equals("STONEHATCHET")) || (itemName.equals("SHATCHET"))
		|| (itemName.equals("275")))
	    return "STONEAXE";
	else if (itemName.equals("DIAMONDSWORD") || (itemName.equals("DSWORD")) || (itemName.equals("276")))
	    return "DIAMONDSWORD";
	else if (itemName.equals("DIAMONDSHOVEL") || (itemName.equals("DSHOVEL")) || (itemName.equals("277")))
	    return "DIAMONDSHOVEL";
	else if (itemName.equals("DIAMONDPICKAXE") || (itemName.equals("DPICKAXE")) || (itemName.equals("DPICK")) || (itemName.equals("DIAMONDPICK"))
		|| (itemName.equals("278")))
	    return "DIAMONDPICKAXE";
	else if (itemName.equals("DIAMONDAXE") || (itemName.equals("DAXE")) || (itemName.equals("DIAMONDHATCHET")) || (itemName.equals("DHATCHET"))
		|| (itemName.equals("279")))
	    return "DIAMONDAXE";
	else if (itemName.equals("STICK") || (itemName.equals("WOODENSTICK")) || (itemName.equals("280")))
	    return "STICK";
	else if (itemName.equals("BOWL") || (itemName.equals("281")))
	    return "BOWL";
	else if (itemName.equals("MUSHROOMSOUP") || (itemName.equals("282")))
	    return "MUSHROOMSOUP";
	else if (itemName.equals("GOLDSWORD") || (itemName.equals("GSWORD")) || (itemName.equals("283")))
	    return "GOLDSWORD";
	else if (itemName.equals("GOLDSHOVEL") || (itemName.equals("GSHOVEL")) || (itemName.equals("284")))
	    return "GOLDSHOVEL";
	else if (itemName.equals("GOLDPICKAXE") || (itemName.equals("GPICKAXE")) || (itemName.equals("GPICK")) || (itemName.equals("GOLDPICK"))
		|| (itemName.equals("285")))
	    return "GOLDPICKAXE";
	else if (itemName.equals("GOLDAXE") || (itemName.equals("GAXE")) || (itemName.equals("GOLDHATCHET")) || (itemName.equals("GHATCHET"))
		|| (itemName.equals("286")))
	    return "GOLDAXE";
	else if (itemName.equals("STRING") || (itemName.equals("287")))
	    return "STRING";
	else if (itemName.equals("FEATHER") || (itemName.equals("288")))
	    return "FEATHER";
	else if (itemName.equals("GUNPOWDER") || (itemName.equals("289")))
	    return "GUNPOWDER";
	else if (itemName.equals("WOODENHOE") || (itemName.equals("WHOE")) || (itemName.equals("290")))
	    return "WOODENHOE";
	else if (itemName.equals("STONEHOE") || (itemName.equals("SHOE")) || (itemName.equals("291")))
	    return "STONEHOE";
	else if (itemName.equals("IRONHOE") || (itemName.equals("IHOE")) || (itemName.equals("292")))
	    return "IRONHOE";
	else if (itemName.equals("DIAMONDHOE") || (itemName.equals("DHOE")) || (itemName.equals("293")))
	    return "DIAMONDHOE";
	else if (itemName.equals("GOLDHOE") || (itemName.equals("GHOE")) || (itemName.equals("294")))
	    return "GOLDHOE";
	else if (itemName.equals("SEEDS") || (itemName.equals("295")))
	    return "SEEDS";
	else if (itemName.equals("WHEAT") || (itemName.equals("296")))
	    return "WHEAT";
	else if (itemName.equals("BREAD") || (itemName.equals("297")))
	    return "BREAD";
	else if (itemName.equals("LEATHERCAP") || (itemName.equals("LEATHERHELMET")) || (itemName.equals("298")))
	    return "LEATHERCAP";
	else if (itemName.equals("LEATHERTUNIC") || (itemName.equals("LEATHERCHESTPLATE")) || (itemName.equals("299")))
	    return "LEATHERTUNIC";
	else if (itemName.equals("LEATHERPANTS") || (itemName.equals("LEATHERLEGGINGS")) || (itemName.equals("300")))
	    return "LEATHERPANTS";
	else if (itemName.equals("LEATHERBOOTS") || (itemName.equals("301")))
	    return "LEATHERBOOTS";
	else if (itemName.equals("IRONHELMET") || (itemName.equals("IHELMET")) || (itemName.equals("306")))
	    return "IRONHELMET";
	else if (itemName.equals("IRONCHESTPLATE") || (itemName.equals("ICHESTPLATE")) || (itemName.equals("307")))
	    return "IRONCHESTPLATE";
	else if (itemName.equals("IRONLEGGINGS") || (itemName.equals("ILEGGINGS")) || (itemName.equals("308")))
	    return "IRONLEGGINGS";
	else if (itemName.equals("IRONBOOTS") || (itemName.equals("IBOOTS")) || (itemName.equals("309")))
	    return "IRONBOOTS";
	else if (itemName.equals("DIAMONDHELMET") || (itemName.equals("DHELMET")) || (itemName.equals("310")))
	    return "DIAMONDHELMET";
	else if (itemName.equals("DIAMONDCHESTPLATE") || (itemName.equals("DCHESTPLATE")) || (itemName.equals("311")))
	    return "DIAMONDCHESTPLATE";
	else if (itemName.equals("DIAMONDLEGGINGS") || (itemName.equals("DLEGGINGS")) || (itemName.equals("312")))
	    return "DIAMONDLEGGINGS";
	else if (itemName.equals("DIAMONDBOOTS") || (itemName.equals("DBOOTS")) || (itemName.equals("313")))
	    return "DIAMONDBOOTS";
	else if (itemName.equals("GOLDHELMET") || (itemName.equals("GHELMET")) || (itemName.equals("314")))
	    return "GOLDHELMET";
	else if (itemName.equals("GOLDCHESTPLATE") || (itemName.equals("GCHESTPLATE")) || (itemName.equals("315")))
	    return "GOLDCHESTPLATE";
	else if (itemName.equals("GOLDLEGGINGS") || (itemName.equals("GLEGGINGS")) || (itemName.equals("316")))
	    return "GOLDLEGGINGS";
	else if (itemName.equals("GOLDBOOTS") || (itemName.equals("GBOOTS")) || (itemName.equals("317")))
	    return "GOLDBOOTS";
	else if (itemName.equals("FLINT") || (itemName.equals("318")))
	    return "FLINT";
	else if (itemName.equals("RAWPORKCHOP") || (itemName.equals("RAWPORK")) || (itemName.equals("319")))
	    return "RAWPORKCHOP";
	else if (itemName.equals("COOKEDPORKCHOP") || (itemName.equals("COOKEDPORK")) || (itemName.equals("320")))
	    return "COOKEDPORKCHOP";
	else if (itemName.equals("PAINTING") || (itemName.equals("321")))
	    return "PAINTING";
	else if (itemName.equals("GOLDENAPPLE") || (itemName.equals("GOLDAPPLE")) || (itemName.equals("322")))
	    return "GOLDENAPPLE";
	else if (itemName.equals("SIGN") || (itemName.equals("SIGNPOST")) || (itemName.equals("323")))
	    return "SIGN";
	else if (itemName.equals("WOODENDOOR") || (itemName.equals("WOODDOOR")) || (itemName.equals("324")))
	    return "WOODENDOOR";
	else if (itemName.equals("BUCKET") || (itemName.equals("BUKKIT")) || (itemName.equals("325")))
	    return "BUKKET";
	else if (itemName.equals("WATERBUCKET") || (itemName.equals("326")))
	    return "WATERBUCKET";
	else if (itemName.equals("LAVABUCKET") || (itemName.equals("327")))
	    return "LAVABUCKET";
	else if (itemName.equals("MINECART") || (itemName.equals("328")))
	    return "MINECART";
	else if (itemName.equals("SADDLE") || (itemName.equals("329")))
	    return "SADDLE";
	else if (itemName.equals("IRONDOOR") || (itemName.equals("330")))
	    return "IRONDOOR";
	else if (itemName.equals("NETHERRACK") || (itemName.equals("87")))
	    return "NETHERRACK";
	else if (itemName.equals("REDSTONE") || (itemName.equals("REDSTONEDUST")) || (itemName.equals("RS")) || (itemName.equals("331")))
	    return "REDSTONE";
	else if (itemName.equals("BOAT") || (itemName.equals("333")))
	    return "BOAT";
	else if (itemName.equals("GOLDORE") || (itemName.equals("14")))
	    return "GOLDORE";
	else if (itemName.equals("IRONORE") || (itemName.equals("15")))
	    return "IRONORE";
	else if (itemName.equals("COALORE") || (itemName.equals("16")))
	    return "COALORE";
	else if (itemName.equals("LAPISLAZULIORE") || (itemName.equals("21")))
	    return "LAPISLAZULIORE";
	else if (itemName.equals("DIAMONDORE") || (itemName.equals("56")))
	    return "DIAMONDORE";
	else if (itemName.equals("REDSTONEORE") || (itemName.equals("73")))
	    return "REDSTONEORE";
	else if (itemName.equals("LEATHER") || (itemName.equals("334")))
	    return "LEATHER";
	else if (itemName.equals("MILK") || (itemName.equals("MILKBUCKET")) || (itemName.equals("BUCKETOFMILK")) || (itemName.equals("335")))
	    return "MILK";
	else if (itemName.equals("CLAYBRICK") || (itemName.equals("BRICK")) || (itemName.equals("336")))
	    return "BRICK";
	else if (itemName.equals("CLAY") || (itemName.equals("337")))
	    return "CLAY";
	else if (itemName.equals("SUGARCANE") || (itemName.equals("REED")) || (itemName.equals("338")))
	    return "SUGARCANE";
	else if (itemName.equals("PAPER") || (itemName.equals("339")))
	    return "PAPER";
	else if (itemName.equals("BOOK") || (itemName.equals("340")))
	    return "BOOK";
	else if (itemName.equals("SLIMEBALL") || (itemName.equals("SLIME")) || (itemName.equals("341")))
	    return "SLIMEBALL";
	else if (itemName.equals("MINECARTWITHCHEST") || (itemName.equals("342")))
	    return "MINECARTWITHCHEST";
	else if (itemName.equals("MINECARTWITHFURNACE") || (itemName.equals("POWEREDMINECART")) || (itemName.equals("343")))
	    return "MINECARTWITHFURNACE";
	else if (itemName.equals("EGG") || (itemName.equals("344")))
	    return "EGG";
	else if (itemName.equals("COMPASS") || (itemName.equals("345")))
	    return "COMPASS";
	else if (itemName.equals("FISHINGROD") || (itemName.equals("346")))
	    return "FISHINGROD";
	else if (itemName.equals("CLOCK") || (itemName.equals("347")))
	    return "CLOCK";
	else if (itemName.equals("GLOWSTONEDUST") || (itemName.equals("348")))
	    return "GLOWSTONEDUST";
	else if (itemName.equals("RAWFISH") || (itemName.equals("349")))
	    return "RAWFISH";
	else if (itemName.equals("COOKEDFISH") || (itemName.equals("350")))
	    return "COOKEDFISH";
	else if (itemName.equals("INK") || (itemName.equals("INKSACK")) || (itemName.equals("351")))
	    return "INK";
	else if (itemName.equals("REDINK") || (itemName.equals("REDDYE")) || (itemName.equals("ROSERED")) || (itemName.equals("351:1")))
	    return "REDINK";
	else if (itemName.equals("GREENINK") || (itemName.equals("GREENDYE")) || (itemName.equals("CACTUSGREEN")) || (itemName.equals("351:2")))
	    return "GREENINK";
	else if (itemName.equals("BROWNINK") || (itemName.equals("BROWNDYE")) || (itemName.equals("COCOABEANBROWN")) || (itemName.equals("351:3")))
	    return "BROWNINK";
	else if (itemName.equals("LAPISLAZULI") || (itemName.equals("LAPIS")) || (itemName.equals("LAZULI")) || (itemName.equals("351:4")))
	    return "LAPISLAZULI";
	else if (itemName.equals("PURPLEINK") || (itemName.equals("PURPLEDYE")) || (itemName.equals("351:5")))
	    return "PURPLEINK";
	else if (itemName.equals("CYANINK") || (itemName.equals("CYANDYE")) || (itemName.equals("351:6")))
	    return "CYANINK";
	else if (itemName.equals("LIGHTGRAYINK") || (itemName.equals("LIGHTGRAYDYE")) || (itemName.equals("LIGHTGREYINK")) || (itemName.equals("LIGHTGREYDYE"))
		|| (itemName.equals("351:7")))
	    return "LIGHTGRAYINK";
	else if (itemName.equals("GRAYINK") || (itemName.equals("GRAYDYE")) || (itemName.equals("GREYINK")) || (itemName.equals("GREYDYE"))
		|| (itemName.equals("351:8")))
	    return "GRAYINK";
	else if (itemName.equals("PINKINK") || (itemName.equals("PINKDYE")) || (itemName.equals("351:9")))
	    return "PINKINK";
	else if (itemName.equals("LIMEINK") || (itemName.equals("LIMEDYE")) || (itemName.equals("351:10")))
	    return "LIMEINK";
	else if (itemName.equals("YELLOWINK") || (itemName.equals("YELLOWDYE")) || (itemName.equals("DANDELIONYELLOW")) || (itemName.equals("351:11")))
	    return "YELLOWINK";
	else if (itemName.equals("LIGHTBLUEINK") || (itemName.equals("LIGHTBLUEDYE")) || (itemName.equals("351:12")))
	    return "LIGHTBLUEINK";
	else if (itemName.equals("MAGENTAINK") || (itemName.equals("MAGENTADYE")) || (itemName.equals("351:13")))
	    return "MAGENTAINK";
	else if (itemName.equals("ORANGEINK") || (itemName.equals("ORANGEDYE")) || (itemName.equals("351:14")))
	    return "ORANGEINK";
	else if (itemName.equals("BONEMEAL") || (itemName.equals("351:15")))
	    return "BONEMEAL";
	else if (itemName.equals("BONE") || (itemName.equals("352")))
	    return "BONE";
	else if (itemName.equals("SUGAR") || (itemName.equals("353")))
	    return "SUGAR";
	else if (itemName.equals("CAKE") || (itemName.equals("354")))
	    return "CAKE";
	else if (itemName.equals("BED") || (itemName.equals("355")))
	    return "BED";
	else if (itemName.equals("REDSTONEREPEATER") || (itemName.equals("REPEATER")) || (itemName.equals("DIODE")) || (itemName.equals("356")))
	    return "REPEATER";
	else if (itemName.equals("COOKIE") || (itemName.equals("357")))
	    return "COOKIE";
	else if (itemName.equals("MAP") || (itemName.equals("358")))
	    return "MAP";
	else if (itemName.equals("SHEARS") || (itemName.equals("359")))
	    return "SHEARS";
	else if (itemName.equals("MELONSLICE") || (itemName.equals("MELON")) || (itemName.equals("360")))
	    return "MELONSLICE";
	else if (itemName.equals("PUMPKINSEED") || (itemName.equals("PUMPKINSEEDS")) || (itemName.equals("361")))
	    return "PUMPKINSEED";
	else if (itemName.equals("MELONSEED") || (itemName.equals("MELONSEEDS")) || (itemName.equals("362")))
	    return "MELONSEED";
	else if (itemName.equals("RAWBEEF") || (itemName.equals("363")))
	    return "RAWBEEF";
	else if (itemName.equals("COOKEDBEEF") || (itemName.equals("STEAK")) || (itemName.equals("364")))
	    return "STEAK";
	else if (itemName.equals("RAWCHICKEN") || (itemName.equals("365")))
	    return "RAWCHICKEN";
	else if (itemName.equals("COOKEDCHICKEN") || (itemName.equals("366")))
	    return "COOKEDCHICKEN";
	else if (itemName.equals("ROTTENFLESH") || (itemName.equals("367")))
	    return "ROTTENFLESH";
	else if (itemName.equals("ENDERPEARL") || (itemName.equals("368")))
	    return "ENDERPEARL";
	else if (itemName.equals("BLAZEROD") || (itemName.equals("369")))
	    return "BLAZEROD";
	else if (itemName.equals("GHASTTEAR") || (itemName.equals("370")))
	    return "GHASTTEAR";
	else if (itemName.equals("GOLDNUGGET") || (itemName.equals("371")))
	    return "GOLDNUGGET";
	else if (itemName.equals("NETHERWART") || (itemName.equals("372")))
	    return "NETHERWART";
	else if (itemName.equals("GLASSBOTTLE") || (itemName.equals("GLASSVIAL")) || (itemName.equals("VIAL")) || (itemName.equals("374")))
	    return "GLASSBOTTLE";
	else if (itemName.equals("SPIDEREYE") || (itemName.equals("375")))
	    return "SPIDEREYE";
	else if (itemName.equals("BREWINGSTAND") || (itemName.equals("379")))
	    return "BREWINGSTAND";
	else if (itemName.equals("CAULDRON") || (itemName.equals("380")))
	    return "CAULDRON";
	return "";
    }
}
