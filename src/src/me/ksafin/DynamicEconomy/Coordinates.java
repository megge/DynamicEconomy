package me.ksafin.DynamicEconomy;

public class Coordinates {
    private int[] coords = new int[6];

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	for (int coord : coords)
	    sb.append(coord).append(" ");
	return sb.deleteCharAt(sb.length() - 1).toString(); // To remove the last " "
    }

    public Coordinates(int x1, int y1, int z1, int x2, int y2, int z2) {
	setFirst(x1, y1, z1);
	setSecond(x2, y2, z2);
    }

    public Coordinates(String str) {
	String[] data = str.split(" ");
	if (data.length == 6)
	    try {
		for (int i = 0; i < data.length; i++)
		    coords[i] = Integer.parseInt(data[i]);
	    } catch (NumberFormatException e) {
		for (int i = 0; i < coords.length; i++)
		    coords[i] = 0;
		// TODO: Warning log
	    }
	else {
	    // TODO: Warning log
	}
    }

    public void setFirst(int x, int y, int z) {
	coords[0] = x;
	coords[1] = y;
	coords[2] = z;
    }

    public void setSecond(int x, int y, int z) {
	coords[3] = x;
	coords[4] = y;
	coords[5] = z;
    }
}
