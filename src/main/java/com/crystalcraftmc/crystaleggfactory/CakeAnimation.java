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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class CakeAnimation {
	private Block globalB;
	private Timer tim;
	private int accumulator;
	public final int NUMCAKES = 5; //MAY CHANGE NUMBER - will also automatically check NUMCAKES blocks
									//above the PlayerInteractEvent to make sure clear before proceeding
	
	private final int CAKEFRAMERATE = 153; //cakes show up CAKEFRAMERATE milliseconds inbetween eachother
	
//####################################################################
	public CakeAnimation(Block b) {
		globalB = b;
		accumulator = 0;
		tim = new Timer(CAKEFRAMERATE, new CakeUpdateListener());
		tim.start();
	}
	public CakeAnimation() {
		//to create a low-resource accessFields object in EggThrowListener class
	}
	private class CakeUpdateListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			accumulator++;
			Location loc = globalB.getLocation();
			loc.setY(globalB.getLocation().getY()+accumulator);
			Block bbb = loc.getBlock();
			bbb.setType(Material.CAKE_BLOCK);
			
			if(accumulator >= NUMCAKES)
				stopTim();
		}
	}
	public void stopTim() {
		tim.stop();
	}
}
