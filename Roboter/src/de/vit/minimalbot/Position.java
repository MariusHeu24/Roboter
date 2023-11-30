package de.vit.minimalbot;

import java.util.List;


public class Position {
	private int x;
	private int y;
	
	public Position(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	public boolean pruefe(List <Position> vergleich) {
		int i=0;
		while(vergleich.isEmpty()==false&&i<vergleich.size()) {
			if(vergleich.get(i).getX()==this.x&&vergleich.get(i).getY()==this.y) {
				return true;
			}
			i++;
		}
		return false;
	}
	
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	
}
