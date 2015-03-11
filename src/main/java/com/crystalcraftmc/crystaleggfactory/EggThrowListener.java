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


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EggThrowListener implements Listener {
	
	/**Holds the data of all the nerfed spawn eggs*/
	private ItemStack[] icon;
	
	/**Holds the data of the rae spawn egg firework*/
	private ItemStack fire;
	
	/**The byte data of all the spawn eggs*/
	private byte[] data = {(byte)50, (byte)51, (byte)52, (byte)54,
						(byte)55, (byte)56, (byte)57, (byte)58,
						(byte)59, (byte)60, (byte)61, (byte)62,
						(byte)65, (byte)66, (byte)67, (byte)68,
						(byte)90, (byte)91, (byte)92, (byte)93,
						(byte)94, (byte)95, (byte)96, (byte)98,
						(byte)100, (byte)101, (byte)120 };
	/**The plugin object*/
	private CrystalEggFactory globalEgg;
	
	/**Initializes the Listener class to prevent egg use
	 * @param egg the plugin we're listening in on
	 * @param fire the data of the rae firework
	 */
	public EggThrowListener(CrystalEggFactory egg, ItemStack fire) {
		egg.getServer().getPluginManager().registerEvents(this, egg);
		globalEgg = egg;
		this.fire = fire;
		
		icon = new ItemStack[27];
		for(int i = 0; i < 27; i++) {
			icon[i] = new ItemStack(Material.MONSTER_EGG, 2, data[i]);
			ItemMeta im2 = icon[i].getItemMeta();
			im2.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
			icon[i].setItemMeta(im2);
		}
	}
	
	/**Prevents the deployment of spawn eggs by dispensers in
	 * banned areas
	 * @param BlockDispenseEvent e the event we're testing
	 */
	@EventHandler
	public void stopDispenserEggs(BlockDispenseEvent e) {
		if(e.getItem().getType() == Material.MONSTER_EGG) {
			if(this.checkOutlawAreas(e)) return;
		}
	}
	
	/**Prevents gen2 eggs from changing spawners, and prevents any eggs from
	 * being used in banned areas (except ops (by default) )
	 * @param e the PlayerInteractEvent we're testing
	 */
	@EventHandler
	public void stopSpawnerChanging(PlayerInteractEvent e) {
		if(e.getPlayer() != null) {
			//action = "RIGHT_CLICK_BLOCK"
			//getClickedBlock() type="MOB_SPAWNER"
			if(e.getAction().toString().equals("RIGHT_CLICK_BLOCK") &&
						e.getPlayer().getItemInHand().isSimilar(fire)) {
				if(checkOutlawAreas(e)) return;

				if(e.getPlayer().getItemInHand().getEnchantmentLevel(Enchantment.ARROW_FIRE) == 1) {
					if(this.theBlocksAboveClear(e.getClickedBlock())) {
						new CakeAnimation(e.getClickedBlock(), globalEgg);
					}
					else {
						e.getPlayer().sendMessage(ChatColor.RED + "Error; " + ChatColor.GOLD +
									"the " + String.valueOf(CrystalEggFactory.NUMCAKES) +
									" blocks above the WildRaeganrr rocket must be clear.");
						e.setCancelled(true);
					}
				}
			}
			else if(e.getAction().toString().equals("RIGHT_CLICK_BLOCK") &&
					this.isAnyKindOfEgg(e.getPlayer().getItemInHand())) {
				
				if(checkOutlawAreas(e)) return;
				ItemStack is = e.getPlayer().getItemInHand();
				if(e.getClickedBlock().getType().toString().equals("MOB_SPAWNER")) {
					if(is.containsEnchantment(Enchantment.ARROW_INFINITE)) {
						e.getPlayer().sendMessage("Cancelled event");
						e.setCancelled(true);
					}
					
				}
			}
		}
	}
	
	/**Tests whether spawning a Raeganrr at a certain block would have enough
	 * verticle free space or not.
	 * @param b the Block we're starting from
	 * @return boolean true if there is enough free area to spawn rae
	 */
	public boolean theBlocksAboveClear(Block b) {
		Location loc = b.getLocation();
		for(int i = 0; i < CrystalEggFactory.NUMCAKES; i++) {
			loc.setY(loc.getY()+1);
			if(!loc.getBlock().isEmpty())
				return false;
		}
		return true;
	}
	
	/**Tests whether an itemstack is any kind of monster spawn egg
	 * @param isE the itemstack we're testing
	 * @return boolean true if it is any kind of spawn egg
	 */
	public boolean isAnyKindOfEgg(ItemStack isE) {
		boolean isSpawnEgg = false;
		for(int i = 0; i < 27; i++) {
			if(isE.getData().getData() == icon[i].getData().getData())
				isSpawnEgg = true;
		}
		return isSpawnEgg;
	}
	
	/**Tests whether a specified block is contained within certain x coordinates
	 * @param x1 the first x coordinate
	 * @param x2 the second x coordinate
	 * @param blckx the block we're testing
	 * @return true if the block is inside the specified coordinates
	 */
	public boolean findDomain(double x1, double x2, double blckx) {
		//if both x coords are (-)
		if(x1 < 0 && x2 < 0) {
			if (Math.abs(x1) > Math.abs(x2)) {
				if (x2 > blckx && blckx > x1)
					return true;
			}
			else if (Math.abs(x1) < Math.abs(x2)) {
				if (x2 < blckx && blckx < x1)
					return true;
			}
		}
		//if both x coords are (+)
		if (x1 >= 0 && x2 >= 0) {
			if (x1 < x2) {
				if (x1 < blckx && blckx < x2)
					return true;
			}
			else if (x2 < x1) {
				if (x2 < blckx && blckx < x1)
					return true;
			}
		}
		//if x1 is (-), and x2 is (+)
		else if (x1 < 0 && x2 >= 0) {
			if (x1 < blckx && blckx < x2)
				return true;
		}
		//if x1 is (+) and x2 is (-)
		else if (x1 >= 0 && x2 < 0) {
			if (x2 < blckx && blckx < x1)
				return true;
		}
		return false;
	}
	
	/**Tests whether a specified block is contained within certain z coordinates
	 * @param z1 the first x coordinate
	 * @param z2 the second x coordinate
	 * @param blckx the block we're testing
	 * @return true if the block is inside the specified coordinates
	 */
	public boolean findRange(double z1, double z2, double blckz) {
		//if both x coords are (-)
		if(z1 < 0 && z2 < 0) {
			if (Math.abs(z1) > Math.abs(z2)) {
				if (z2 > blckz && blckz > z1)
					return true;
			}
			else if (Math.abs(z1) < Math.abs(z2)) {
				if (z2 < blckz && blckz < z1)
					return true;
			}
		}
		//if both x coords are (+)
		if (z1 >= 0 && z2 >= 0) {
			if (z1 < z2) {
				if (z1 < blckz && blckz < z2)
					return true;
			}
			else if (z2 < z1) {
				if (z2 < blckz && blckz < z1)
					return true;
			}
		}
		//if x1 is (-), and x2 is (+)
		else if (z1 < 0 && z2 >= 0) {
			if (z1 < blckz && blckz < z2)
				return true;
		}
		//if x1 is (+) and x2 is (-)
		else if (z1 >= 0 && z2 < 0) {
			if (z2 < blckz && blckz < z1)
				return true;
		}
		return false;
	}
	
	/**tests whether the event should be cancelled or not, taking
	 * permissions, banned areas, and banned worlds into account
	 * @param e the PlayerInteractEvent we're testing
	 * @return true if the event should be cancelled
	 */
	public boolean checkOutlawAreas (PlayerInteractEvent e) {
		if(e.getPlayer().hasPermission("CrystalEggFactory.use")) {
			if(e.getPlayer().getItemInHand().isSimilar(fire) &&
					e.getPlayer().getItemInHand().getEnchantmentLevel(Enchantment.ARROW_FIRE) == 1) {
				if(this.theBlocksAboveClear(e.getClickedBlock()))
					new CakeAnimation(e.getClickedBlock(), globalEgg);
				else {
					e.getPlayer().sendMessage(ChatColor.RED + "Error; " + ChatColor.GOLD +
							"the " + String.valueOf(CrystalEggFactory.NUMCAKES) +
							" blocks above the WildRaeganrr rocket must be clear.");
					e.setCancelled(true);
				}
			}
			return true;
		}
		if(e.getPlayer().getWorld().getEnvironment() == Environment.NORMAL) {
			if(globalEgg.overworldBan) {
				e.setCancelled(true);
				return true;
			}
		}
		else if(e.getPlayer().getWorld().getEnvironment() == Environment.NETHER) {
			if(globalEgg.netherBan) {
				e.setCancelled(true);
				return true;
			}
		}
		else if(e.getPlayer().getWorld().getEnvironment() == Environment.THE_END) {
			if(globalEgg.endBan) {
				e.setCancelled(true);
				return true;
			}
		}
		int x = e.getClickedBlock().getLocation().getBlockX();
		int z = e.getClickedBlock().getLocation().getBlockZ();
		for(EggOutlawArea k : globalEgg.jail) {
				boolean domain = findDomain(k.getX1(), k.getX2(), x);
				boolean range = findRange(k.getZ1(), k.getZ2(), z);
				Environment spawnWorld = e.getPlayer().getWorld().getEnvironment();
				Environment archiveWorld;
				String banworld = k.getWorld();
				if(banworld.equalsIgnoreCase("overworld"))
					archiveWorld = Environment.NORMAL;
				else if(banworld.equalsIgnoreCase("nether"))
					archiveWorld = Environment.NETHER;
				else
					archiveWorld = Environment.THE_END;
			if (domain && range && (archiveWorld == spawnWorld)) {
				e.setCancelled(true);
				return true;
			}
		}
		return false;
	}
	
	/**Tests whether a blockdispenseevent is in a banned area,
	 * and if it is, cancells the event.
	 * @param e the BlockDispenseEvent we're testing
	 * @return true if it is in a banned area
	 */
	public boolean checkOutlawAreas (BlockDispenseEvent e) {
		if(e.getBlock().getWorld().getEnvironment() == Environment.NORMAL) {
			if(globalEgg.overworldBan) {
				e.setCancelled(true);
				return true;
			}
		}
		else if(e.getBlock().getWorld().getEnvironment() == Environment.NETHER) {
			if(globalEgg.netherBan) {
				e.setCancelled(true);
				return true;
			}
		}
		else if(e.getBlock().getWorld().getEnvironment() == Environment.THE_END) {
			if(globalEgg.endBan) {
				e.setCancelled(true);
				return true;
			}
		}
		int x = e.getBlock().getLocation().getBlockX();
		int z = e.getBlock().getLocation().getBlockZ();
		for(EggOutlawArea k : globalEgg.jail) {
				boolean domain = findDomain(k.getX1(), k.getX2(), x);
				boolean range = findRange(k.getZ1(), k.getZ2(), z);
				Environment spawnWorld = e.getBlock().getWorld().getEnvironment();
				Environment archiveWorld;
				String banworld = k.getWorld();
				if(banworld.equalsIgnoreCase("overworld"))
					archiveWorld = Environment.NORMAL;
				else if(banworld.equalsIgnoreCase("nether"))
					archiveWorld = Environment.NETHER;
				else
					archiveWorld = Environment.THE_END;
			if (domain && range && (archiveWorld == spawnWorld)) {
				e.setCancelled(true);
				return true;
			}
		}
		return false;
	}
	
}
