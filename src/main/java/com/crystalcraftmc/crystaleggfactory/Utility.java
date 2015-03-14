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

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**Provides useful utilities functions*/
public class Utility {
	
	/**Checks if the player has the specified permissions
	 * @param p the Player we're checking
	 * @param the permissiontype we're testing for
	 * @return true if the player has the specified permission
	 */
	public static boolean hasPerms(Player p, PermType pt) {
		if(p.isOp())
			return true;
		String name = p.getName();
		boolean foundMatch = false;
		int index = -1;
		for(int i = 0; i < CrystalEggFactory.eggPerms.size() && !foundMatch; i++) {
			if(name.equalsIgnoreCase(CrystalEggFactory.eggPerms.get(i).name)) {
				if(CrystalEggFactory.eggPerms.get(i).hasFullPerms)
					return true;
				foundMatch = true;
				index = i;
			}
		}
		if(!foundMatch) {
			return false;
		}
		switch(pt.ordinal()) {
		case 1:
			if(CrystalEggFactory.eggPerms.get(index).canEgg) {
				return true;
			}
			return false;
		case 2:
			if(CrystalEggFactory.eggPerms.get(index).canBan) {
				return true;
			}
			return false;
		case 3:
			if(CrystalEggFactory.eggPerms.get(index).canUseCrystalEggsInSpawners) {
				return true;
			}
			return false;
		case 4:
			if(CrystalEggFactory.eggPerms.get(index).canThrowInBan) {
				return true;
			}
			return false;
		case 5:
			if(CrystalEggFactory.eggPerms.get(index).canPerms) {
				return true;
			}
			return false;
		default:
			return false;
		}
	}
	
	/**A method to tell the player they don't have permission*/
	public static void noPerms(Player p) {
		p.sendMessage(ChatColor.RED + "You do not have permission to perform this command");
	}
	
	/**A method that checks whether a String name is the name of someone online
	 * @param name the name we're testing
	 * @param players the arraylist of online Players
	 * @return true if name is the name of a currently online player
	 */
	public static boolean isOnline(String name, ArrayList<Player> players) {
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i).getName().equals(name))
				return true;
		}
		return false;
	}
	
	/**A method that turns a String into a permtype
	 * @param str, the string we're converting
	 * @return PermType the type of perm it is
	 */
	public static PermType getPermType(String str) {
		for(int i = 0; i < CrystalEggFactory.permTypeStr.length; i++) {
			for(int ii = 0; ii < CrystalEggFactory.permTypeStr[i].length; ii++) {
				if(str.equalsIgnoreCase(CrystalEggFactory.permTypeStr[i][ii])) {
					return Utility.getPermType(i);
				}
			}
		}
		return PermType.NULL;
	}
	
	/**A method that turns an int into the proper PermType ordinal value
	 * @param index the ordinal
	 * @return permtype the PermType the ordinal refers to
	 */
	public static PermType getPermType(int index) {
		switch(index) {
		case 0:
			return PermType.FULL;
		case 1:
			return PermType.EGG;
		case 2:
			return PermType.BAN;
		case 3:
			return PermType.GEN2;
		case 4:
			return PermType.THROWBAN;
		case 5:
			return PermType.PERMS;
		default:
			return PermType.NULL;
		}
	}
}
