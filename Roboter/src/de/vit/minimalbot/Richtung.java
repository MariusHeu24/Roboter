package de.vit.minimalbot;

public class Richtung {
	private int anzahl;
	private int x;
	private int y;

	public Richtung(int x, int y, int anzahl) {
		this.x=x;
		this.y=y;
		this.anzahl=anzahl;
	}
	
	public Richtung(int x, int y) {
		this.x=x;
		this.y=y;
	}

	public int getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
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
