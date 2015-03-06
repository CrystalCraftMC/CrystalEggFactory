package com.crystalcraftmc.crystaleggfactory;

public class EggOutlawArea {
	private double x1;
	private double z1;
	private double x2;
	private double z2;
	private String areaID;
	private String world;

	/** Takes and stores the params, into an EggOutlawArea Object that can be
	 * used in an ArrayList to track areas where the plugin is banned.
	 * 
	 * @param xCoord
	 * @param zCoord
	 * @param nameArea
	 * @param worldName
	 */
	public EggOutlawArea(double x1Coord, double z1Coord, double x2Coord, double z2Coord, String nameArea, String worldName) {
		this.x1 = x1Coord;
		this.z1 = z1Coord;
		this.x2 = x2Coord;
		this.z2 = z2Coord;
		this.areaID = nameArea;
		this.world = worldName;
	}
	
	/** Define equals method for EggOutlawArea object.
	 * 
	 * @param area1
	 * @param area2
	 * @return
	 */
	public boolean equals(EggOutlawArea o1, EggOutlawArea o2) {
		if (o1.areaID == o2.areaID) {
			return true;
		}
		return false;	
	}
	
	/**Getter method for x1
	 * @return x1 one corner eggs are disallowed in
	 */
	public double getX1() {
		return x1;
	}
	
	/**Getter method for x2
	 * @return x2 one corner eggs are disallowed in
	 */
	public double getX2() {
		return x2;
	}
	
	/**Getter method for z1
	 * @return z1 one corner eggs are disallowed in
	 */
	public double getZ1() {
		return z1;
	}
	
	/**Getter method for z2
	 * @return z2 one corner eggs are disallowed in
	 */
	public double getZ2() {
		return z2;
	}
	
	/**Getter method
	 * @return ID name of banned area
	 */
	public String getID() {
		return areaID;
	}
	
	/**Getter method
	 * @return world the world of the banned area
	 */
	public String getWorld() {
		return world;
	}
	/**Returns String of EggOulawArea data.
	 * Area Name:
	 * World:
	 * Coord1 (x,z):
	 * Coord2 (x,z):
	 * @return String
	 */
	public String toString() {
		String dataout = "Area name:"+areaID+"\nWorld: "+world+"\nCoord1 (x,z): ("+x1+","+z1+")\nCoord2 (x,z): ("+x2+","+z2+")\n";
		return dataout;
	}
}
