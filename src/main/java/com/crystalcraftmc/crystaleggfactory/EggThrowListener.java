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

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class EggThrowListener implements Listener {
	private CakeAnimation accessFields;
	private ItemStack[] icon;
	private ItemStack fire;
	private byte[] data = {(byte)50, (byte)51, (byte)52, (byte)54,
						(byte)55, (byte)56, (byte)57, (byte)58,
						(byte)59, (byte)60, (byte)61, (byte)62,
						(byte)65, (byte)66, (byte)67, (byte)68,
						(byte)90, (byte)91, (byte)92, (byte)93,
						(byte)94, (byte)95, (byte)96, (byte)98,
						(byte)100, (byte)101, (byte)120 };
	
	public EggThrowListener(CrystalEggFactory egg) {
		egg.getServer().getPluginManager().registerEvents(this, egg);
		
		accessFields = new CakeAnimation();
		fire = new ItemStack(Material.FIREWORK, 1);
		FireworkMeta fm = (FireworkMeta) fire.getItemMeta();
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
		fire.setItemMeta(fm);
		
		icon = new ItemStack[27];
		for(int i = 0; i < 27; i++) {
			icon[i] = new ItemStack(Material.MONSTER_EGG, 2, data[i]);
			ItemMeta im2 = icon[i].getItemMeta();
			im2.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
			icon[i].setItemMeta(im2);
		}
	}
	@EventHandler
	public void stopSpawnerChanging(PlayerInteractEvent e) {
		if(e.getPlayer() != null) {
			//action = "RIGHT_CLICK_BLOCK"
			//getClickedBlock() type="MOB_SPAWNER"
			if(e.getAction().toString().equals("RIGHT_CLICK_BLOCK") &&
						e.getPlayer().getItemInHand().isSimilar(fire)) {
				if(e.getPlayer().getItemInHand().getEnchantmentLevel(Enchantment.ARROW_FIRE) == 1) {
					if(this.theBlocksAboveClear(e.getClickedBlock())) {
						new CakeAnimation(e.getClickedBlock());
					}
					else {
						e.getPlayer().sendMessage(ChatColor.RED + "Error; " + ChatColor.GOLD +
									"the " + String.valueOf(accessFields.NUMCAKES) +
									" blocks above the WildRaeganrr rocket must be clear.");
						e.setCancelled(true);
					}
				}
			}
			else if(e.getAction().toString().equals("RIGHT_CLICK_BLOCK")) {
				if(e.getClickedBlock().getType().toString().equals("MOB_SPAWNER")) {
					Player p = e.getPlayer();
					ItemStack is = p.getItemInHand();
					boolean matchesNerfedEgg = false;
					for(int i = 0; i < 27; i++) {
						if(is.getData().getData() == icon[i].getData().getData())
							matchesNerfedEgg = true;
					}
					
					if(matchesNerfedEgg && is.containsEnchantment(Enchantment.ARROW_INFINITE)) {
						e.getPlayer().sendMessage("Cancelled event");
						e.setCancelled(true);
					}
					
				}
			}
		}
	}
	public boolean theBlocksAboveClear(Block b) {
		Location loc = b.getLocation();
		for(int i = 0; i < accessFields.NUMCAKES; i++) {
			loc.setY(loc.getY()+1);
			if(!loc.getBlock().isEmpty())
				return false;
		}
		return true;
	}
	
}
