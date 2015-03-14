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

import java.io.Serializable;

/**This class is designed to give specific players specific permissions*/
public class PermissionE implements Serializable {
	/**The name of player with special permissions*/
	public String name;
	/**Whether the player has all permissions for CrystalEggFactory*/
	public boolean hasFullPerms = false;
	/**Summoning permission to call eggs into inv && egglist perms*/
	public boolean canEgg = false;
	/**Can use ban commands*/
	public boolean canBan = false;
	/**Can use CrystalEggs in spawners*/
	public boolean canUseCrystalEggsInSpawners = false;
	/**Can Use eggs in banned areas*/
	public boolean canThrowInBan = false;
	/**Can use the eggperms command*/
	public boolean canPerms = false;
	
	/**Initializes perms for a player
	 * @param name of the player
	 * @param perType Permission that the player is gaining access too
	 */
	public PermissionE(String name, PermType permType) {
		this.name = name;
		if(permType == PermType.FULL)
			hasFullPerms = true;
		else if(permType == PermType.EGG)
			canEgg = true;
		else if(permType == PermType.BAN)
			canBan = true;
		else if(permType == PermType.GEN2)
			canUseCrystalEggsInSpawners = true;
		else if(permType == PermType.THROWBAN)
			canThrowInBan = true;
		else if(permType == PermType.PERMS)
			canPerms = true;
	}
	
	//setter methods
	public void setFullPerms(boolean b) {
		hasFullPerms = b;
	}
	public void setEggPerms(boolean b) {
		canEgg = b;
	}
	public void setBanPerms(boolean b) {
		canBan = b;
	}
	public void setGen2(boolean b) {
		canUseCrystalEggsInSpawners = b;
	}
	public void setThrowBan(boolean b) {
		canThrowInBan = b;
	}
	public void setPermsPerms(boolean b) {
		canPerms = b;
	}
	
	/**sets a permission
	 * @param pt the PermType we're looking at
	 * @param b the value of that permission
	 */
	public void setPerm(PermType pt, boolean b) {
		if(pt == PermType.FULL)
			hasFullPerms = b;
		else if(pt == PermType.EGG)
			canEgg = b;
		else if(pt == PermType.BAN)
			canBan = b;
		else if(pt == PermType.GEN2)
			canUseCrystalEggsInSpawners = b;
		else if(pt == PermType.THROWBAN)
			canThrowInBan = b;
		else if(pt == PermType.PERMS)
			canPerms = b;
	}
	
	/**Checks whether there are any current perms on.
	 * if this returns false, the object will be removed from the arraylist.
	 * @return boolean true if there are no permissions
	 */
	public boolean hasNoPerms() {
		if(!hasFullPerms && !canEgg && !canBan &&
				!canUseCrystalEggsInSpawners && !canThrowInBan && !canPerms) {
			return true;
		}
		return false;
	}
	
	/**toString() method to display the data of this object
	 * @return the String of data of this object
	 */
	public String toString() {
		if(hasFullPerms) {
			return name + " Has Full Permissions For CrystalEggFactory (\"hasFullPerms\")";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(name + " Has the following permissions:\n");
		if(canEgg)
			sb.append("\"canEgg\": The ability to spawn in eggs with /egg and use /egglist\n");
		if(canBan)
			sb.append("\"canBan\": The ability to use /eggban, /eggunban, /eggbanlist, /eggNerfAll" +
					" and /eggbanworld commands\n");
		if(canUseCrystalEggsInSpawners)
			sb.append("\"canUseCrystalEggsInSpawners\": The ability to throw CrystalEggs to change spawners\n");
		if(canThrowInBan)
			sb.append("\"canThrowInBan\": The ability to use spawn eggs in banned regions");
		if(canPerms) {
			sb.append("\"canPerms\": The ability to use /eggperms<player> to see what perms a player has, " +
					"/eggperms<player><permtype><true /false> to set perms, /eggpermremove <player>,"
					+ " and /eggpermslist");
		}
		return sb.toString();
	}
}
