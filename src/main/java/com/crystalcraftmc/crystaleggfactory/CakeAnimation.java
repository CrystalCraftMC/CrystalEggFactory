/*
 * Copyright 2015 CrystalCraftMC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.crystalcraftmc.crystaleggfactory;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**The cake animation that is shown upon using a Raeganrr spawnegg*/
public class CakeAnimation {
	private CrystalEggFactory plugin;
	
	
//####################################################################
	
	/**Initializes a cake animation object
	 * @param b the block we're starting from
	 * @param plugin the main plugin class
	 */
	public CakeAnimation(Block b, CrystalEggFactory plugin) {
		this.plugin = plugin;
		Location loc = b.getLocation();
		for(int i = 0; i < 5; i++) {
			loc.setY(b.getLocation().getY()+(i+1));
			this.hailMary(loc.getBlock(), i);
		}
	}
	
	/**Where the actual spawning of the eggs takes place
	 * @param b the block to change to cake
	 * @param z the nth cake we're spawning in (to determine the delay)
	 */
	public void hailMary(Block b, int z) {
		final Block bw = b; //has to be Final for runnable code to work
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                bw.setType(Material.CAKE_BLOCK);
            }
        }, (long)((CrystalEggFactory.CAKEFRAMERATE*z)+1));
	}
	
}
