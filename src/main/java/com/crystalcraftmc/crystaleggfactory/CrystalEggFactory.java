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

public class CrystalEggFactory extends JavaPlugin {
	
	public boolean overworldBan, netherBan, endBan;
	
	public ArrayList<EggOutlawArea> jail = new ArrayList<EggOutlawArea>();
	
	private String[] raeAlias = {"WildRaeganrr", "wildRae", "Rae", "CakeQueen", "RaeCaker123", 
						"RaeCaker", "R", "W", "wr"};
	private ItemStack raeFire;
	
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
	private String[] mobTypeTitles = {"Creeper", "Skeleton", "Spider", "Zombie", "Slime",
									"Ghast", "Zombie Pigman", "Enderman", "Cave Spider",
									"Silverfish", "Blaze", "Magma Cube", "Bat", "Witch",
									"Endermite", "Guardian", "Pig", "Sheep", "Cow", "Chicken",
									"Squid", "Wolf", "Mooshroom", "Ocelot", "Horse", "Rabbit",
									"Villager"};
	//###########################################################################################
	private enum MobType { NoMob, Creeper, Skeleton, Spider, Zombie, Slime, Ghast,
						Zombie_Pigman, Enderman, Cave_Spider, Silverfish, Blaze,
						Magma_Cube, Bat, Witch, Endermite, Guardian, Pig, Sheep, Cow,
						Chicken, Squid, Wolf, Mooshroom, Ocelot, Horse, Rabbit, Villager};
	//###########################################################################################
	Map<MobType, ItemStack> map = new HashMap<MobType, ItemStack>();
	
	private MobType mobType = MobType.NoMob;
	//private EggThrowListener etl;
	public void onEnable() {
		this.initializeWorldBan();
		this.initializeSerFile();
		getLogger().info("Egg plugin enabled.");
		new EggThrowListener(this);
		
		raeFire = new ItemStack(Material.FIREWORK, 1);
		FireworkMeta fm = (FireworkMeta) raeFire.getItemMeta();
		ArrayList<Color> alColor = new ArrayList<Color>();
		alColor.add(Color.PURPLE);
		alColor.add(Color.FUCHSIA);
		ArrayList<Color> alFade = new ArrayList<Color>();
		alFade.add(Color.BLUE);
		alFade.add(Color.AQUA);
		fm.addEffects(FireworkEffect.builder().trail(true).withColor(alColor).withFade(alFade).with(Type.BALL_LARGE).build());
		fm.setPower(2);
//####################################################################
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.LIGHT_PURPLE + "jflory7 isn't here to save you now");
		lore.add(ChatColor.RED + "unleash at your own peril");
		fm.setLore(lore);
		
//####################################################################
		fm.addEnchant(Enchantment.ARROW_FIRE, 1, false);
		raeFire.setItemMeta(fm);
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
	public void onDisable() {
		getLogger().info("Egg plugin disabled.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = ((Player) sender);
			if(p.isOp()) {
				if(args.length == 0 && label.equalsIgnoreCase("egglist")) {
					this.displayMobTypes(p);
					return true;
				}
				else if(args.length == 2 && label.equalsIgnoreCase("egg")) {
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
											p.getInventory().addItem(raeFire);
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
						else if(this.checkMobType(args[0], p)) {
							if(this.isInt(args[1])) {
								int amount = Integer.parseInt(args[1]);
								if(amount == 0)
									p.sendMessage(ChatColor.GOLD + "Hello from Jwood~");
								else if(amount > 0 && amount <= 2304) {
									int inventorySpace = this.countFreeInventorySpace(p);
									if(amount <= inventorySpace) {
			//#####################################################################################
										
										for(int i = 0; i < amount; i++)
											p.getInventory().addItem(map.get(mobType));
										
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
				else if(label.equals("eggbanworld") && args.length == 2) {
					if((args[0].equalsIgnoreCase("overworld") ||
							args[0].equalsIgnoreCase("nether") ||
							args[0].equalsIgnoreCase("end")) && (args[1].equalsIgnoreCase("true") ||
									args[1].equalsIgnoreCase("false"))) {
						File file = new File("eggbanworld.txt");
						if(!file.exists()) {
							try{
								PrintWriter pw = new PrintWriter("eggbanworld.txt");
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
							pw.close();
						}catch(IOException e) { e.printStackTrace(); }
						return true;
					}
					else
						return false;
				}
				else if(label.equals("eggbanlist") && args.length == 0) {
					p.sendMessage(ChatColor.GREEN + "Overworld Egg Ban=" + String.valueOf(overworldBan));
					p.sendMessage(ChatColor.RED + "Nether Egg Ban=" + String.valueOf(netherBan));
					p.sendMessage(ChatColor.BLUE + "End Egg Ban=" + String.valueOf(endBan));
					for(int i = 0; i < jail.size(); i++) {
						p.sendMessage(this.getColor(i) + jail.get(i).toString());
					}
					return true;
				}
				else if(label.equals("eggban") && args.length == 5) {
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
				else if(label.equalsIgnoreCase("eggunban") && args.length == 1) {
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
				return false;
			
			}
			else {
				p.sendMessage(ChatColor.GOLD + "You do not have permission to use that command");
				return true;
			}
			
		}
		return false;
	}
	public boolean checkMobType(String mob, Player p) {
		for(int i = 0; i < mobTypeList.length; i++) {
			for(int ii = 0; ii < mobTypeList[i].length; ii++) {
				if(mob.equalsIgnoreCase((mobTypeList[i][ii].trim()))) {
					switch(i) {
					case 0:
						mobType = MobType.Creeper;
						return true;
					case 1:
						mobType = MobType.Skeleton;
						return true;
					case 2:
						mobType = MobType.Spider;
						return true;
					case 3:
						mobType = MobType.Zombie;
						return true;
					case 4:
						mobType = MobType.Slime;
						return true;
					case 5:
						mobType = MobType.Ghast;
						return true;
					case 6:
						mobType = MobType.Zombie_Pigman;
						return true;
					case 7:
						mobType = MobType.Enderman;
						return true;
					case 8:
						mobType = MobType.Cave_Spider;
						return true;
					case 9:
						mobType = MobType.Silverfish;
						return true;
					case 10:
						mobType = MobType.Blaze;
						return true;
					case 11:
						mobType = MobType.Magma_Cube;
						return true;
					case 12:
						mobType = MobType.Bat;
						return true;
					case 13:
						mobType = MobType.Witch;
						return true;
					case 14:
						mobType = MobType.Endermite;
						return true;
					case 15:
						mobType = MobType.Guardian;
						return true;
					case 16:
						mobType = MobType.Pig;
						return true;
					case 17:
						mobType = MobType.Sheep;
						return true;
					case 18:
						mobType = MobType.Cow;
						return true;
					case 19:
						mobType = MobType.Chicken;
						return true;
					case 20:
						mobType = MobType.Squid;
						return true;
					case 21:
						mobType = MobType.Wolf;
						return true;
					case 22:
						mobType = MobType.Mooshroom;
						return true;
					case 23:
						mobType = MobType.Ocelot;
						return true;
					case 24:
						mobType = MobType.Horse;
						return true;
					case 25:
						mobType = MobType.Rabbit;
						return true;
					case 26:
						mobType = MobType.Villager;
						return true;
					default:
						p.sendMessage(ChatColor.BOLD + "Error 101 in switch statement of checkMobType()" +
									" method.  Please inform the dev team " + ChatColor.GOLD +
									"Sir/Siress " + ChatColor.GOLD + p.getName());
						p.sendMessage(ChatColor.RED + "The Number of rows in the 2-D array mobTypeList" +
										" now exceeds 27.");
						mobType = MobType.NoMob;
						return false;
					}
				}
			}
		}
		return false;
	}
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
	public boolean isInt(String str) {
		try{
			Integer.parseInt(str);
			return true;
		}catch(NumberFormatException e) {
			return false;
		}
	}
	public boolean isDouble(String str) {
		try{
			Double.parseDouble(str);
			return true;
		}catch(NumberFormatException e) {
			return false;
		}
	}
	public void initializeWorldBan() {
		File file = new File("eggbanworld.txt");
		if(!file.exists()) {
			try{
				PrintWriter pw = new PrintWriter("eggbanworld.txt");
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
			CharSequence cs = "false";
			overworldBan = contents.get(0).contains(cs) ? false : true;
			netherBan = contents.get(1).contains(cs) ? false : true;
			endBan = contents.get(2).contains(cs) ? false : true;
		}catch(IOException e) { e.printStackTrace(); }
	}
	/*public boolean addBanArea(double xCorner, double zCorner, String nameArea, String world) {
        EggOutlawArea temp = new EggOutlawArea(xCorner, zCorner, nameArea, world);
        jail.add(temp);
        return true;
	}*/

	public boolean removeBanArea(EggOutlawArea removeArea) {
        for(EggOutlawArea temp : jail) {
                if (temp.equals(removeArea)) {
                        jail.remove(temp);
                        return true;
                }
        }
        return false;
	}
	public void initializeSerFile() {
		File file = new File("eggbanareas.ser");
		if(file.exists()) {
			try{
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				jail = (ArrayList<EggOutlawArea>)ois.readObject();
				ois.close();
			}catch(IOException e) { e.printStackTrace(); 
			}catch(ClassNotFoundException e) { e.printStackTrace(); }
		}
	}
	public void updateSerFile() {
		File file = new File("eggbanareas.ser");
		if(!file.exists() && jail.size() > 0) {
			try{
				FileOutputStream fos = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(jail);
				oos.close();
			}catch(IOException e) { e.printStackTrace(); }
		}
		else if(jail.size() > 0 && file.exists()){
			file.delete();
			try{
				FileOutputStream fos = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(jail);
				oos.close();
			}catch(IOException e) { e.printStackTrace(); }
		}
	}
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
}
