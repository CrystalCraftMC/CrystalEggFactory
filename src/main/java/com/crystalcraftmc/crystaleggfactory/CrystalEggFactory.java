/*
+ * Copyright 2015 CrystalCraftMC
+ *
+ *    Licensed under the Apache License, Version 2.0 (the "License");
+ *    you may not use this file except in compliance with the License.
+ *    You may obtain a copy of the License at
+ *
+ *        http://www.apache.org/licenses/LICENSE-2.0
+ *
+ *    Unless required by applicable law or agreed to in writing, software
+ *    distributed under the License is distributed on an "AS IS" BASIS,
+ *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
+ *    See the License for the specific language governing permissions and
+ *    limitations under the License.
+ */
package com.crystalcraftmc.crystaleggfactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

/**CrystalEggFactory is an open source plugin designed to grant ops(by default)
 *  the ability to create spawn eggs that do not have the ability to change spawners. 
 *  (A Minecraft 1.8 Feature)
 * 
 */
public class CrystalEggFactory extends JavaPlugin {
	
	/**Holds the permissions for each player*/
	public static ArrayList<PermissionE> eggPerms = new ArrayList<PermissionE>();
	
	/**Holds all perm type attributes*/
	public static String[][] permTypeStr = { {"hasFullPerms", "all", "full", "fullPerms"},
		{"canEgg", "summonEgg", "summon"}, {"canBan", "ban"}, 
		{"noNerf", "spawnerFreedom"}, {"canThrowInBan", "throw", "ctib"},
		{"canPerms", "eggperms", "perms"} };
	
	/**Holds whether all eggs are currently banned in said worlds*/
	public boolean overworldBan, netherBan, endBan;
	
	/**A list of banned areas (pairs of x/z coords, world, and ID_Name*/
	public ArrayList<EggOutlawArea> jail = new ArrayList<EggOutlawArea>();
	
	/**Different ways a rae egg can be called in the /egg command*/
	private String[] raeAlias = {"WildRaeganrr", "wildRae", "Rae", "CakeQueen", "RaeCaker123", 
						"RaeCaker", "R", "W", "wr"};
	
	/**Holds the data of the rae egg firework*/
	private ItemStack fire;
	
	/**Holds whether spawner changes are only limited to gen2 eggs*/
	public static boolean nerfAll = false;
	
	/**Lists all the aliases one can use when specifying the mobtype attribute in /egg*/
	private String[][] mobTypeList = { {"creeper", "creepers",
				"stalker", "stalkers", "walking-tnt", "               cr"},
				{"skeleton", "skeletons", "skelly",
					"skely", "sk"}, {"spider", "spiders", "spiderman",
					"sp"}, {"zombie", "zombies", "zomb", "walker", "walkers",
					"zo"}, {"slime", "slimes", "ooblek", "sl"}, {"ghast",
					"ghasts", "state-puff-marshmallow", "gh"}, {"zombie-pigman",
					"zombie-pigmen", "zombiepigman", "                zombiepigmen", "man-bear-pig",
					"zp"}, {"enderman", "endermen", "MenInBlack", "end", "en", "em"},
					{"cave-spider", "cave-spiders", "cavespider", "                     cavespiders",
					"cs", "ca"}, {"silverfish", "silvester", "si", "sf"}, {"blaze", "blazes",
					"flareon", "bl"}, {"magma-cube", "magma-cubes", "magmacube", "       " +
					"               magmacubes",
					"magmar", "cube", "mc", "ma"}, {"bat", "batman", "ba"}, {"witch", "witches",
					"wizard", "wizards", "wi"}, {"endermite", "endermites", "ender-mite",
					"ender-mites"}, {"guardian", "guardians", "guard", "lobster-newburg", "gu"},
					{"pig", "pigs", "jporkly7", "pi", "3.14159265"}, {"sheep", "pillow", "sh"},
					{"steak", "cow", "cows", "co"}, {"chicken", "chickens", "eggs-benedict", "ch"},
					{"squidward", "squid", "squids", "rocket-power", "sq"}, {"wolf", "wolfs",
					"dog", "dogs", "spot", "wo"}, {"mooshroom", "mooshrooms", "fungus", "mo", "ms"},
					{"ocelot", "ocelots", "cat", "cats", "oc"}, {"horse", "horses", "stallion",
					"cavalry", "ho"}, {"rabbit", "rabbits", "rabbi", "bunny", "easter", "ra"},
					{"villager", "villagers", "person", "people", "maori", "ppl", "vi"}};
	//###########################################################################################
	//###########################################################################################
	/**A clean way of displaying each mob name*/
	private String[] mobTypeTitles = {"Creeper", "Skeleton", "Spider", "Zombie", "Slime",
									"Ghast", "Zombie Pigman", "Enderman", "Cave Spider",
									"Silverfish", "Blaze", "Magma Cube", "Bat", "Witch",
									"Endermite", "Guardian", "Pig", "Sheep", "Cow", "Chicken",
									"Squid", "Wolf", "Mooshroom", "Ocelot", "Horse", "Rabbit",
									"Villager"};
	//###########################################################################################
	/**Different MobTypes that the spawn eggs can be*/
	private enum MobType { NoMob, Creeper, Skeleton, Spider, Zombie, Slime, Ghast,
						Zombie_Pigman, Enderman, Cave_Spider, Silverfish, Blaze,
						Magma_Cube, Bat, Witch, Endermite, Guardian, Pig, Sheep, Cow,
						Chicken, Squid, Wolf, Mooshroom, Ocelot, Horse, Rabbit, Villager};
	//###########################################################################################
						
	/**A map that will be able to spit out a damage value when given a MobType value*/
	Map<MobType, ItemStack> map = new HashMap<MobType, ItemStack>();
	
	
	/**This final variable will determine how many cakes will spawn.
	 * If any of the blocks that are going to be changed to cake aren't air
	 * blocks, the event is cancelled*/
	public static final int NUMCAKES = 5;
	
	/**cakes show up CAKEFRAMERATE ticks inbetween eachother*/
	public static final int CAKEFRAMERATE = 5;
	
	/**Initialize a mobtype variable*/
	private MobType mobType = MobType.NoMob;
	
	public void onEnable() {
		createFile();
		this.initializeWorldBan();
		this.initializePermsFile();
		this.initializeSerFile();
		this.createRaeFire();
		getLogger().info("Egg plugin enabled.");
		this.createMobTypeToDataMap();
		new EggThrowListener(this, fire);
		
	}
	public void onDisable() {
		getLogger().info("Egg plugin disabled.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = ((Player) sender);
			
				if(args.length == 0 && label.equalsIgnoreCase("egglist") && 
						Utility.hasPerms(p, PermType.EGG)) {
					this.displayMobTypes(p);
					return true;
				}
				else if(args.length == 2 && label.equalsIgnoreCase("egg") && 
						Utility.hasPerms(p, PermType.EGG)) {
					if(label.equalsIgnoreCase("egg")) {
						boolean spawnRae = false;
						for(int i = 0; i < raeAlias.length; i++) {
							if(args[0].equalsIgnoreCase(raeAlias[i]))
								spawnRae = true;
						}
						if(spawnRae) {
							if(this.isInt(args[1])) {
								int raeNum = Integer.parseInt(args[1]);
								if(raeNum == 0) {
									p.sendMessage(ChatColor.GOLD + "Hello from Dr. Jwood~");
									return true;
								}
								else if(raeNum > 0 && raeNum <= 2304) {
									int invSpace = this.countFreeInventorySpace(p);
									if(invSpace >= raeNum) {
										for(int i = 0; i < raeNum; i++) {
											p.getInventory().addItem(fire);
										}
										return true;
									}
									else {
										p.sendMessage(ChatColor.AQUA + "Error; you asked for \'" +
											ChatColor.GOLD + String.valueOf(raeNum) + ChatColor.AQUA +
												"\' Rae spawn eggs, but only have " + ChatColor.RED +
												String.valueOf(invSpace) + ChatColor.AQUA + " free slots in" +
												" your inventory.");
										return true;
									}
								}
								else {
									p.sendMessage(ChatColor.AQUA + 
											"Error; your 2nd argument: \'" +
											ChatColor.GOLD + args[1] + ChatColor.AQUA + "\' is not" +
											" between 0-2304 inclusive.");
									return false;
								}
							}
							else {
								p.sendMessage(ChatColor.AQUA + "Error; your 2nd argument: \'" +
									ChatColor.GOLD + args[1] + ChatColor.AQUA + "\' is not an Integer.");
								return false;
							}
						}
						else if(this.checkMobType(args[0], p) != MobType.NoMob) {
							mobType = checkMobType(args[0], p);
							if(this.isInt(args[1])) {
								int amount = Integer.parseInt(args[1]);
								if(amount == 0)
									p.sendMessage(ChatColor.GOLD + "Hello from Jwood~");
								else if(amount > 0 && amount <= 2304) {
									int inventorySpace = this.countFreeInventorySpace(p);
									if(amount <= inventorySpace) {
			//#####################################################################################
										ItemStack iss = map.get(mobType);
										ItemMeta imm = iss.getItemMeta();
										imm.setDisplayName(ChatColor.BLUE + "CrystalEgg");
										ArrayList<String> loree = new ArrayList<String>();
										loree.add(ChatColor.GREEN + "Made In Flory's Secret Factory");
										loree.add(ChatColor.AQUA + "These Eggs Do Not Change Spawners");
										imm.setLore(loree);
										iss.setItemMeta(imm);
										for(int i = 0; i < amount; i++) {
											
											p.getInventory().addItem(iss);
											
										}
										
										p.sendMessage(ChatColor.GOLD + "Command Executed");
										return true;
		   //#####################################################################################
									}
									else {
										p.sendMessage(ChatColor.RED + "Error; you do not have " +
												"enough inventory space to house " + amount + " spawn eggs.");
										p.sendMessage(ChatColor.AQUA + "You have " + ChatColor.GOLD +
												inventorySpace + ChatColor.AQUA +
												" free spaces in your inventory.");
										return true;
									}
								}
								else {
									p.sendMessage(ChatColor.ITALIC + "Error; " +
										"the amount argument must be between 0-2304 inclusive.");
									return false;
								}
							}
							else {
								p.sendMessage(ChatColor.GOLD + "Error - the amount" +
											" argument is not an integer.");
								return false;
							}
						}
						else {
							p.sendMessage(ChatColor.LIGHT_PURPLE + "Error; invalid mob-type argument." +
												" Enter <\"egglist\"> to view all" +
								" valid mob-type arguments.");
							return false;
						}
					}
					else{
						p.sendMessage(ChatColor.GOLD + "Error; your first argument was not \"egg\".");
						return false;
						}
				}
				else if(label.equals("eggbanworld") && args.length == 2 &&
							Utility.hasPerms(p, PermType.BAN)) {
					if((args[0].equalsIgnoreCase("overworld") ||
							args[0].equalsIgnoreCase("nether") ||
							args[0].equalsIgnoreCase("end")) && (args[1].equalsIgnoreCase("true") ||
									args[1].equalsIgnoreCase("false"))) {
						createFile();
						File file = new File("crystaleggfactory\\eggbanworld.txt");
						if(!file.exists()) {
							try{
								PrintWriter pw = new PrintWriter("crystaleggfactory\\eggbanworld.txt");
								pw.println("overworld=false");
								pw.flush();
								pw.println("nether=false");
								pw.flush();
								pw.println("end=false");
								pw.flush();
								pw.close();
							}catch(IOException e) { e.printStackTrace(); }
						}
						ArrayList<String> contents = new ArrayList<String>();
						try{
							Scanner in = new Scanner(file);
							while(in.hasNext()) {
								contents.add(in.nextLine());
							}
							in.close();
							boolean goingOn = args[1].equalsIgnoreCase("true") ? true : false;
							switch(args[0].charAt(0)) {
							case 'o':
								contents.set(0, "overworld=".concat(args[1].toLowerCase()));
								overworldBan = args[1].charAt(0) == 'f' ? false : true;
								if(goingOn)
									p.sendMessage(ChatColor.GREEN + "Eggs in overworld are now dis-allowed");
								else
									p.sendMessage(ChatColor.GREEN + "Eggs in overworld are now allowed");
								break;
							case 'n':
								contents.set(1, "nether=".concat(args[1].toLowerCase()));
								netherBan = args[1].charAt(0) == 'f' ? false : true;
								if(goingOn)
									p.sendMessage(ChatColor.RED + "Eggs in nether are now dis-allowed");
								else
									p.sendMessage(ChatColor.RED + "Eggs in nether are now allowed");
								break;
							case 'e':
								contents.set(2, "end=".concat(args[1].toLowerCase()));
								endBan = args[1].charAt(0) == 'f' ? false : true;
								p.sendMessage(ChatColor.BLUE + "Eggs in end set to "+args[1]);
								if(goingOn)
									p.sendMessage(ChatColor.BLUE + "Eggs in end are now dis-allowed");
								else
									p.sendMessage(ChatColor.BLUE + "Eggs in end are now allowed");
								break;
							}
							PrintWriter pw = new PrintWriter("eggbanworld.txt");
							pw.println(contents.get(0));
							pw.flush();
							pw.println(contents.get(1));
							pw.flush();
							pw.println(contents.get(2));
							pw.flush();
							pw.println(contents.get(3));
							pw.close();
						}catch(IOException e) { e.printStackTrace(); }
						return true;
					}
					else
						return false;
				}
				else if(label.equals("eggbanlist") && args.length == 0 &&
						Utility.hasPerms(p, PermType.BAN)) {
					p.sendMessage(ChatColor.GREEN + "Overworld Egg Ban=" + String.valueOf(overworldBan));
					p.sendMessage(ChatColor.RED + "Nether Egg Ban=" + String.valueOf(netherBan));
					p.sendMessage(ChatColor.BLUE + "End Egg Ban=" + String.valueOf(endBan));
					p.sendMessage(ChatColor.AQUA + "Only CrystalEggs Are Nerfed=" + String.valueOf(!nerfAll));
					for(int i = 0; i < jail.size(); i++) {
						p.sendMessage(this.getColor(i) + jail.get(i).toString());
					}
					return true;
				}
				else if(label.equals("eggban") && args.length == 5 &&
						Utility.hasPerms(p, PermType.BAN)) {
					boolean isValid = true;
					for(int i = 0; i < 4; i++) {
						if(!isDouble(args[i]))
							isValid = false;
					}
					if(!isValid) {
						p.sendMessage(ChatColor.GOLD + "Error; the first 4 arguments were not all valid numbers");
						return false;
					}
					else {
						for(int i = 0; i < jail.size(); i++) {
							if(args[4].equalsIgnoreCase(jail.get(i).getID())) {
								p.sendMessage(ChatColor.GOLD + "Error; " + ChatColor.RED + args[4] +
										ChatColor.GOLD + " is already an existing ban-area ID.");
								return true;
							}
						}
						String worldName = "";
						if(p.getWorld().getEnvironment() == Environment.NORMAL)
							worldName = "overworld";
						if(p.getWorld().getEnvironment() == Environment.NETHER)
							worldName = "nether";
						if(p.getWorld().getEnvironment() == Environment.THE_END)
							worldName = "end";
						jail.add(new EggOutlawArea(Double.parseDouble(args[0]), Double.parseDouble(args[1]),
								Double.parseDouble(args[2]), Double.parseDouble(args[3]), args[4], worldName));
						updateSerFile();
						String worldS;
						if(p.getWorld().getEnvironment() == Environment.NORMAL)
							worldS = "overworld";
						else if(p.getWorld().getEnvironment() == Environment.THE_END)
							worldS = "end";
						else
							worldS = "nether";
						p.sendMessage(ChatColor.GOLD + "Area " + ChatColor.AQUA + args[4] + ChatColor.GOLD +
								" Between " + ChatColor.BLUE + "(x, z) " + ChatColor.RED + "(" + ChatColor.LIGHT_PURPLE + 
								args[0] + ChatColor.RED +
								", " + ChatColor.LIGHT_PURPLE + args[1] + ChatColor.RED + ")" + ChatColor.GOLD + " to " +
								ChatColor.RED + "(" + ChatColor.LIGHT_PURPLE + args[2] + ChatColor.RED +
								", " + ChatColor.LIGHT_PURPLE + args[3] + ChatColor.RED + ")" + ChatColor.GOLD + " is now" +
								" off limits for the use of spawn eggs in the " + ChatColor.RED + worldS + ChatColor.GOLD + ".");
						return true;
					}
				}
				else if(label.equalsIgnoreCase("eggunban") && args.length == 1 &&
						Utility.hasPerms(p, PermType.BAN)) {
					int removeIndex=-1;
					for(int i = 0; i < jail.size(); i++) {
						if(jail.get(i).getID().equals(args[0]))
							removeIndex = i;
					}
					if(removeIndex != -1) {
						jail.remove(removeIndex);
						this.updateSerFile();
						p.sendMessage(ChatColor.AQUA + args[0] + ChatColor.BLUE + " Has successfully" +
								" been removed from the ban-area-list.");
					}
					else {
						p.sendMessage(ChatColor.GOLD + "Error; " + ChatColor.RED + args[0] +
								ChatColor.GOLD + " is not an existing egg-ban-area ID.");
						p.sendMessage(ChatColor.GREEN + "Use " + ChatColor.AQUA + "/eggbanlist" +
								ChatColor.GREEN + " to see existing banned areas.");
					}
					return true;
				}
				else if(label.equalsIgnoreCase("eggperms") && args.length == 1 &&
							Utility.hasPerms(p, PermType.PERMS)) {
						for(int i = 0; i < eggPerms.size(); i++) {
							if(args[0].equals(eggPerms.get(i).name)) {
								p.sendMessage(this.getColor(i)+ eggPerms.get(i).toString());
								return true;
							}
						}
						p.sendMessage(ChatColor.GOLD + args[0] + ChatColor.AQUA +
								" was not found in the records. The argument is case-sensitive.");
						p.sendMessage(ChatColor.GREEN + "Use /eggperms to see the list of all players and" +
								" their permissions.");
						return true;
				}
				else if(label.equalsIgnoreCase("eggperms") && args.length == 0 &&
						Utility.hasPerms(p, PermType.PERMS)) {
					if(eggPerms.size() > 0) {
						for(int i = 0; i < eggPerms.size(); i++) {
							p.sendMessage(this.getColor(i)+ eggPerms.get(i).toString());
						}
					}
					else {
						p.sendMessage(ChatColor.GOLD + "No players have special permissions at this time.");
					}
					return true;
				}
				else if(label.equalsIgnoreCase("eggperms") && args.length == 3 &&
						Utility.hasPerms(p, PermType.PERMS)) {
						PermType toSet = Utility.getPermType(args[1]);
						if(toSet != PermType.NULL) {
							if(args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")) {
								boolean setPermB = args[2].equalsIgnoreCase("true") ? true : false;
								int index = -1;
								for(int i = 0; i < eggPerms.size() && index == -1; i++) {
									if(args[0].equals(eggPerms.get(i).name)) {
										index = i;
									}
								}
								if(index == -1 && !setPermB) {
									p.sendMessage(ChatColor.GOLD + args[0] + " already has no permissions.");
									return true;
								}
								else if(index == -1) {
									eggPerms.add(new PermissionE(args[0], toSet));
									this.updatePerms();
									p.sendMessage(ChatColor.GOLD + "Success.  " +
											ChatColor.RED + args[0] + ChatColor.GOLD + " current perms:");
									p.sendMessage(ChatColor.AQUA + eggPerms.get(eggPerms.size()-1).toString());
									return true;
								}
								else {
									eggPerms.get(index).setPerm(toSet, setPermB);
									p.sendMessage(ChatColor.GOLD + "Success.  " +
											ChatColor.RED + args[0] + ChatColor.GOLD + " current perms:");
									p.sendMessage(ChatColor.AQUA + eggPerms.get(eggPerms.size()-1).toString());
									if(eggPerms.get(index).hasNoPerms())
										eggPerms.remove(index);
									this.updatePerms();
									return true;
								}
							}
							else {
								p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED +
										" is not \'true\' or \'false\'");
								p.sendMessage("/<command><player_name><perm_type><true / false>");
								return true;
							}
						}
						else {
							p.sendMessage(ChatColor.GOLD + args[1] + ChatColor.RED +
									" is not a valid perm-type.  Use /eggpermslist to see all" +
									" valid perm-types.");
							p.sendMessage("/<command><player_name><perm_type><true / false>");
							return true;
						}
				
				}
				else if(label.equalsIgnoreCase("eggpermremove") && args.length == 1 &&
						Utility.hasPerms(p, PermType.PERMS)) {
					for(int i = 0; i < eggPerms.size(); i++) {
						if(args[0].equalsIgnoreCase(eggPerms.get(i).name)) {
							eggPerms.remove(i);
							p.sendMessage(ChatColor.GOLD + "Perms have successfully been removed from " +
									ChatColor.RED + args[0] + ChatColor.GOLD + ".");
							this.updatePerms();
							return true;
						}
					}
					
					p.sendMessage(ChatColor.GOLD + "Player " + args[0] + " was not found in the Permissions record.");
					p.sendMessage(ChatColor.GREEN + "Note this command is case-sensitive. Type /eggpermsplayerlist " +
							"to view all players and their permissions.");
					return true;
				}
				else if(label.equalsIgnoreCase("eggpermslist") && args.length == 0 &&
						Utility.hasPerms(p, PermType.PERMS)) {
					ChatColor aq = ChatColor.AQUA;
					ChatColor go = ChatColor.GOLD;
					for(int i = 0; i < this.permTypeStr.length; i++) {
						for(int ii = 0; ii < this.permTypeStr[i].length; ii++) {
							if(ii == 0) {
								switch(i) {
								case 0:
									p.sendMessage(aq+permTypeStr[i][ii] + go +
											" Enables Permissions for everything.  Aliases:");
									break;
								case 1:
									p.sendMessage(aq+permTypeStr[i][ii] + go +
											" Enables Permissions for summoning eggs with /egg, " +
											"and using /egglist.  Aliases:");
									break;
								case 2:
									p.sendMessage(aq+permTypeStr[i][ii] + go +
											" Enables Permissions for using the /eggban, " +
											"/eggunban, /eggbanlist, and /eggbanworld Aliases:");
									break;
								case 3:
									p.sendMessage(aq+permTypeStr[i][ii] + go +
											" Enables Permissions for using CrystalEggs" +
											" to change spawners.  Aliases:");
									break;
								case 4:
									p.sendMessage(aq+permTypeStr[i][ii] + go +
											" Enables Permissions for the use of spawn eggs" +
											" in banned areas.  Aliases:");
									break;
								case 5:
									p.sendMessage(aq+permTypeStr[i][ii] + go +
											" Enables Permissions for using the /eggperms,"
											+ " /eggperms<player>, /eggperms<player>" +
											"<perm-type><true/false>, /eggpermremove<player>, and" +
											" /eggpermslist commands  Aliases:");
									break;
								}
							}
							else {
								p.sendMessage(ChatColor.GREEN + permTypeStr[i][ii] + "    ");
							}
						}
					}
					return true;
				}
				else if(label.equalsIgnoreCase("eggnerfall") && args.length == 1 &&
						Utility.hasPerms(p, PermType.BAN)) {
					if(args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("false")) {
						boolean setN = args[0].equalsIgnoreCase("true") ? true : false;
						createFile();
						File file = new File("crystaleggfactory\\eggbanworld.txt");
						if(!file.exists())
							this.initializeWorldBan();
						ArrayList<String> contents = new ArrayList<String>();
						try{
							Scanner in = new Scanner(file);
							while(in.hasNext())
								contents.add(in.nextLine());
							in.close();
							nerfAll = setN;
							if(!setN)
								contents.set(3, "OnlyNerfCrystalEggs=true");
							else
								contents.set(3, "OnlyNerfCrystalEggs=false");
							PrintWriter pw = new PrintWriter("eggbanworld.txt");
							pw.println(contents.get(0));
							pw.flush();
							pw.println(contents.get(1));
							pw.flush();
							pw.println(contents.get(2));
							pw.flush();
							pw.println(contents.get(3));
							pw.close();
							in.close();
						}catch(IOException e) { e.printStackTrace(); }
						if(setN)
							p.sendMessage(ChatColor.GOLD + "Now no eggs work to change spawners.");
						else
							p.sendMessage(ChatColor.GOLD + "Now just CrystalEggs don't work to change spawners.");
						return true;
					}
					else {
						p.sendMessage(ChatColor.GOLD + "Error; " + ChatColor.RED + args[0] +
								ChatColor.GOLD + " is not \'true\' or \'false\'.");
						return false;
					}
				}
				return true;
		}
		return false;
	}
	
	/**Tests whether a mobtype attribute has a valid value,
	 * and if it is, returns the MobType of the specified mob
	 * @param mob the argument we're testing
	 * @param p the Player who sent the command
	 * @return MobType the type of the mob (MobType.NoMob if invalid argument)
	 */
	public MobType checkMobType(String mob, Player p) {
		for(int i = 0; i < mobTypeList.length; i++) {
			for(int ii = 0; ii < mobTypeList[i].length; ii++) {
				if(mob.equalsIgnoreCase((mobTypeList[i][ii].trim()))) {
					switch(i) {
					case 0:
						return MobType.Creeper;
					case 1:
						return mobType = MobType.Skeleton;
					case 2:
						return mobType = MobType.Spider;
					case 3:
						return mobType = MobType.Zombie;
					case 4:
						return mobType = MobType.Slime;
					case 5:
						return mobType = MobType.Ghast;
					case 6:
						return mobType = MobType.Zombie_Pigman;
					case 7:
						return mobType = MobType.Enderman;
					case 8:
						return mobType = MobType.Cave_Spider;
					case 9:
						return mobType = MobType.Silverfish;
					case 10:
						return mobType = MobType.Blaze;
					case 11:
						return mobType = MobType.Magma_Cube;
					case 12:
						return mobType = MobType.Bat;
					case 13:
						return mobType = MobType.Witch;
					case 14:
						return mobType = MobType.Endermite;
					case 15:
						return mobType = MobType.Guardian;
					case 16:
						return mobType = MobType.Pig;
					case 17:
						return mobType = MobType.Sheep;
					case 18:
						return mobType = MobType.Cow;
					case 19:
						return mobType = MobType.Chicken;
					case 20:
						return mobType = MobType.Squid;
					case 21:
						return mobType = MobType.Wolf;
					case 22:
						return mobType = MobType.Mooshroom;
					case 23:
						return mobType = MobType.Ocelot;
					case 24:
						return mobType = MobType.Horse;
					case 25:
						return mobType = MobType.Rabbit;
					case 26:
						return mobType = MobType.Villager;
					default:
						p.sendMessage(ChatColor.RED + "The Number of rows in the 2-D array mobTypeList" +
										" now exceeds 27.");
						return MobType.NoMob;
					}
				}
			}
		}
		return MobType.NoMob;
	}
	
	/**Displays all possible mob type attributes in a colorful, and easy to see manner
	 * called upon the /egglist command
	 * @param p the player who sent the /egglist command
	 */
	public void displayMobTypes(Player p) {
		for(int i = 0, z = mobTypeTitles.length-1; i < mobTypeTitles.length; i++, z--) {
			String message = ChatColor.BOLD +
					mobTypeTitles[i] + ": ";
			for(int ii = 0; ii < mobTypeList[i].length; ii++) {
				if(ii == mobTypeList[i].length-1) {
					message = message.concat(this.getUniqueColor(z) + mobTypeList[i][ii] + ".");
				}
				else {
					message = message.concat(this.getUniqueColor(z) + mobTypeList[i][ii] + ", ");
				}
			}
			p.sendMessage(message);
		}
		String wildraeStr = (ChatColor.RED + "WildRaeganrr: " + ChatColor.GREEN);
		for(int i = 0; i < raeAlias.length; i++) {
			if(i == raeAlias.length-1)
				wildraeStr = wildraeStr.concat(raeAlias[i] + ".");
			else
				wildraeStr = wildraeStr.concat(raeAlias[i] + ", ");
		}
		p.sendMessage(wildraeStr);
		p.sendMessage(ChatColor.BOLD + "Scroll up to see the whole list.");
	}
	
	/**A way to color code a loop of messages in a fancy way
	 * @param a the loop number
	 */
	public ChatColor getUniqueColor(int a) {
		a++;
		if(a%4 == 0)
			return ChatColor.RED;
		else if(a%4 == 1)
			return ChatColor.LIGHT_PURPLE;
		else if(a%4 == 2)
			return ChatColor.GOLD;
		else if(a%4 == 3)
			return ChatColor.AQUA;
		return ChatColor.YELLOW;
	}
	
	/**A way to color code a loop of messages in a fancy way
	 * @param index the loop number
	 */
	public ChatColor getColor(int index) {
		index %= 4;
		switch(index) {
		case 0:
			return ChatColor.LIGHT_PURPLE;
		case 1:
			return ChatColor.BLUE;
		case 2:
			return ChatColor.GOLD;
		default:
			return ChatColor.AQUA;
		}
	}
	
	/**Counts how many slots someone's inventory has empty, and multiplies it by 64
	 * @param player the Player we're counting inventory slots of
	 * @return tally the number of empty slots in inv times 64
	 */
	public int countFreeInventorySpace(Player player) {
		PlayerInventory pi = player.getInventory();
		ItemStack[] is = pi.getContents(); //returns array of ItemStacks[] from inv
		int tally = 0;
		for(int i = 0; i < pi.getSize(); i++) {
			if(is[i] == null || is[i].isSimilar(new ItemStack(Material.AIR))) {
				tally += 64;
			}
		}
		
		return tally;
	}
	
	/**Tests whether a string can be converted to datatype int
	 * @param str the String we're testing an int for
	 * @return true if the String is a valid int value
	 */
	public boolean isInt(String str) {
		try{
			Integer.parseInt(str);
			return true;
		}catch(NumberFormatException e) {
			return false;
		}
	}
	
	/**Tests whether a string can be converted to datatype double
	 * @param str the String we're testing an double for
	 * @return true if the String is a valid double value
	 */
	public boolean isDouble(String str) {
		try{
			Double.parseDouble(str);
			return true;
		}catch(NumberFormatException e) {
			return false;
		}
	}
	
	/**Called onEnable(), reads in the data on the .txt file in the
	 * server folder, and initializes the boolean netherBan, overworldBan, and endBan
	 * variables.  This way data is saved between restarts.
	 * The file is created if it doesn't exist.
	 */
	public void initializeWorldBan() {
		createFile();
		File file = new File("crystaleggfactory\\eggbanworld.txt");
		if(!file.exists()) {
			try{
				PrintWriter pw = new PrintWriter("crystaleggfactory\\eggbanworld.txt");
				pw.println("overworld=false");
				pw.flush();
				pw.println("nether=false");
				pw.flush();
				pw.println("end=false");
				pw.flush();
				pw.println("OnlyNerfCrystalEggs=true");
				pw.close();
			}catch(IOException e) { e.printStackTrace(); }
		}
		ArrayList<String> contents = new ArrayList<String>();
		try{
			Scanner in = new Scanner(file);
			while(in.hasNext()) {
				contents.add(in.nextLine());
			}
			in.close();
			CharSequence cs = "false";
			overworldBan = contents.get(0).contains(cs) ? false : true;
			netherBan = contents.get(1).contains(cs) ? false : true;
			endBan = contents.get(2).contains(cs) ? false : true;
			nerfAll = contents.get(3).contains(cs) ? true : false;
		}catch(IOException e) { e.printStackTrace(); }
	}

	/**Called to remove a bannedArea
	 * @param the EggOutlawArea object we're removing from the arraylist
	 * @return true if the removing was successful
	 */
	public boolean removeBanArea(EggOutlawArea removeArea) {
        for(EggOutlawArea temp : jail) {
                if (temp.equals(removeArea)) {
                        jail.remove(temp);
                        return true;
                }
        }
        return false;
	}
	
	/**Called from CrystalEggFactory.onEnable() to pull in the data from eggbanareas.ser
	 * regarding all of the existing banned areas*/
	public void initializeSerFile() {
		createFile();
		File file = new File("crystaleggfactory\\eggbanareas.ser");
		if(file.exists()) {
			try{
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				jail = (ArrayList<EggOutlawArea>)ois.readObject();
				ois.close();
				fis.close();
			}catch(IOException e) { e.printStackTrace(); 
			}catch(ClassNotFoundException e) { e.printStackTrace(); }
		}
	}
	
	/**Called when a change is made to the global arraylist of EggOutlawArea objects.
	 * When the ban areas are changed, the ban areas in the .ser file are changed too.*/
	public void updateSerFile() {
		createFile();
		File file = new File("crystaleggfactory\\eggbanareas.ser");
		if(!file.exists() && jail.size() > 0) {
			try{
				FileOutputStream fos = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(jail);
				oos.close();
				fos.close();
			}catch(IOException e) { e.printStackTrace(); }
		}
		else if(jail.size() > 0 && file.exists()){
			file.delete();
			try{
				FileOutputStream fos = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(jail);
				oos.close();
				fos.close();
			}catch(IOException e) { e.printStackTrace(); }
		}
	}
	

	
	/**Creates the data of the rae egg firework*/
	public void createRaeFire() {
		fire = new ItemStack(Material.FIREWORK, 1);
		FireworkMeta fm = (FireworkMeta) fire.getItemMeta();
		ArrayList<Color> alColor = new ArrayList<Color>();
		alColor.add(Color.PURPLE);
		alColor.add(Color.FUCHSIA);
		ArrayList<Color> alFade = new ArrayList<Color>();
		alFade.add(Color.BLUE);
		alFade.add(Color.AQUA);
		fm.addEffects(FireworkEffect.builder().trail(true).withColor(alColor).withFade(alFade).with(Type.BALL_LARGE).build());
		fm.setPower(1);
//####################################################################
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.LIGHT_PURPLE + "jflory7 isn't here to save you now");
		lore.add(ChatColor.RED + "unleash at your own peril");
		fm.setLore(lore);
//####################################################################
		fm.addEnchant(Enchantment.ARROW_FIRE, 1, false);
		fm.setDisplayName(ChatColor.RED + "Wild Raeganrr Spawn Egg");
		fire.setItemMeta(fm);
	}
	
	/**This will initilize the list of nonop permissions*/
	public void initializePermsFile() {
		createFile();
		File file = new File("crystaleggfactory\\CrystalEggFactoryPerms.ser");
		if(file.exists()) {
			try{
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				eggPerms = (ArrayList<PermissionE>)ois.readObject();
				ois.close();
				fis.close();
			}catch(IOException e) { e.printStackTrace(); 
			}catch(ClassNotFoundException e) { e.printStackTrace(); }
		}
	}
	
	/**update the Perms*/
	public void updatePerms() {
		createFile();
		File file = new File("crystaleggfactory\\CrystalEggFactoryPerms.ser");
		if(file.exists()) {
			file.delete();
		}
		try{
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(eggPerms);
			oos.close();
			fos.close();
		}catch(IOException e) { e.printStackTrace(); }
	}
	
	
	
	/**This will create a map linking mobtypes to their specified damage value*/
	public void createMobTypeToDataMap() {
		ItemStack god;
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)50);
		ItemMeta im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Creeper, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)51);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Skeleton, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)52);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Spider, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)54);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Zombie, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)55);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Slime, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)56);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Ghast, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)57);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Zombie_Pigman, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)58);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Enderman, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)59);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Cave_Spider, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)60);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Silverfish, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)61);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Blaze, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)62);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Magma_Cube, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)65);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Bat, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)66);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Witch, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)67);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Endermite, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)68);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Guardian, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)90);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Pig, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)91);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Sheep, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)92);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Cow, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)93);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Chicken, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)94);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Squid, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)95);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Wolf, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)96);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Mooshroom, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)98);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Ocelot, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)100);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Horse, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)101);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Rabbit, god);
		god = new ItemStack(Material.MONSTER_EGG, 1, (short)120);
		im = god.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		god.setItemMeta(im);
		map.put(MobType.Villager, god);
	}
	
	/**This will create the folder where files are stored if it doesn't exist*/
	public void createFile() {
		File file = new File("crystaleggfactory");
		if(!file.exists())
			file.mkdir();
	}
}
