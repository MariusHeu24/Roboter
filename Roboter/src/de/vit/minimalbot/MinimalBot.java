package de.vit.minimalbot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import de.vitbund.netmaze.connector.Action;
import de.vitbund.netmaze.connector.IBot;
import de.vitbund.netmaze.info.Cell;
import de.vitbund.netmaze.info.GameEndInfo;
import de.vitbund.netmaze.info.GameInfo;
import de.vitbund.netmaze.info.RoundInfo;

public class MinimalBot implements IBot {
	private int posx;
	private int posy;
	private int sizex;
	private int sizey;
	private int variante;
	private Position finish;
	private int sheetnr;
	private int playerid;
//	private Position form;
	private int stand;
	private int nochbenoetigteformulare;

	private List<Position> freie = new ArrayList<>();
	private List<Position> formulare = new ArrayList<>();
	private List<Position> waende = new ArrayList<>();
	private List<Position> besuchte = new ArrayList<>();

	public String getName() {
		return "MinimalBot";
	}

	@Override
	public void onError(Exception ex) {
		ex.printStackTrace();
	}

	@Override
	public void onGameEnd(GameEndInfo gameendinfo) {
		System.out.println("Spiel zueende, " + gameendinfo.getRound() + " Runden");
	}

	@Override
	public void onGameStart(GameInfo gameinfo) {
		int xdim = gameinfo.getSizeX();
		int ydim = gameinfo.getSizeY();
		System.out.println("Spielfeld-Abmessungen: " + xdim + "'" + ydim);
		sizex = gameinfo.getSizeX();
		sizey = gameinfo.getSizeY();
		posx = gameinfo.getStartX();
		posy = gameinfo.getStartY();
		System.out.println(posx+""+posy);
		stand = 0;
		nochbenoetigteformulare=0;

		sheetnr = gameinfo.getSheets();
		System.out.println(sheetnr);
		playerid = gameinfo.getPlayerId();

		if (posy <= (sizey - 2) / 2 && posx <= (sizex - 2) / 2) { // obenlinks
			variante = 1;
		} else if (posy <= (sizey - 2) / 2 && posx > (sizex - 2) / 2) {
			variante = 2;
		} else if (posy > (sizey - 2) / 2 && posx <= (sizex - 2) / 2) {
			variante = 3;
		} else if (posy > (sizey - 2) / 2 && posx > (sizex - 2) / 2) {
			variante = 4;
		} else if (posy == (sizey - 2) / 2 && posx == (sizex - 2) / 2) {
			variante = 5;
		}
	}

	@Override
	public Action onNewRound(RoundInfo roundinfo) {
		Action action = new Action();
		besuchte.add(new Position(posx, posy));
		System.out.println(besuchte.get(besuchte.size()-1).getX()+""+besuchte.get(besuchte.size()-1).getY());
		
		
//		action.moveEast();
		

		
		if (roundinfo.getCellCurrent().getType() == Cell.FINISH && roundinfo.getCellCurrent().getPlayer() == playerid && sheetnr==0 && roundinfo.getCellCurrent().getNumber() == 0) {
			System.out.println("Endefalsch"+roundinfo.getCellCurrent().getNumber());
			action.finish();
			return action;
		}
		else if(roundinfo.getCellCurrent().getType() == Cell.FINISH && roundinfo.getCellCurrent().getPlayer() == playerid) {
			System.out.println("Enderichtiggut"+roundinfo.getCellCurrent().getNumber());
			nochbenoetigteformulare=roundinfo.getCellCurrent().getNumber();
		}
		else if(roundinfo.getCellCurrent().getType() == Cell.FORM && roundinfo.getCellCurrent().getPlayer() == playerid && roundinfo.getCellCurrent().getNumber()==stand) {
			System.out.println("falch2");
			action.take();
			stand++;
			return action;
		}
		System.out.println(posy + "y" + posx + "x" + sizex + "x" + sizey + "y");
		if (variante == 1) { // obenlinks
			return actionobli(roundinfo);
		} else if (variante == 2) { // obenrechts
			actionobre(roundinfo);
		} else if (variante == 3) { // untenlinks
			actionunli(roundinfo);
		} else if (variante == 4) { // untenrechts
			actionunre(roundinfo);
		} else if (variante == 5) { // mitte
			actionobli(roundinfo);
		}

		return action;
	}

	public Action actionobli(RoundInfo roundinfo) {
		// System.out.println(besuchte);
		Action action = new Action();

		boolean east = machwas(roundinfo.getCellEast(), new Richtung(1, 0));
		boolean north = machwas(roundinfo.getCellNorth(), new Richtung(0, -1));
		boolean west = machwas(roundinfo.getCellWest(), new Richtung(-1, 0));
		boolean south = machwas(roundinfo.getCellSouth(), new Richtung(0, 1));

		
		if(formulare.isEmpty()==false && formulare.get(stand)!=null) {
			System.out.println("falsch3");
			Position form = new Position(formulare.get(stand).getX(),formulare.get(stand).getY());
			if (posx > form.getX()) {
				action.moveWest();
				return action;
			} else if (posx < form.getX()) {
				action.moveEast();
				return action;
			} else if (posy < form.getY()) {
				action.moveSouth();
				return action;
			} else if (posy > form.getY()) {
				action.moveNorth();
				return action;
			}
		}
		
		
		if (finish != null && sheetnr == 0 && nochbenoetigteformulare==0) {
			if (posx > finish.getX()) {
				action.moveWest();
				return action;
			} else if (posx < finish.getX()) {
				action.moveEast();
				return action;
			} else if (posy < finish.getY()) {
				action.moveSouth();
				return action;
			} else if (posy > finish.getY()) {
				action.moveNorth();
				return action;
			}
		}
		
		

		// new Position(posx+1,posy))==false
		Position ep = new Position(posx + 1, posy);
		Position np = new Position(posx, posy - 1);
		Position wp = new Position(posx - 1, posy);
		Position sp = new Position(posx, posy + 1);

		if (east == true && ep.pruefe(besuchte) == false) {
			action.moveEast();
			posx++;
			return action;
		} else if (north == true && np.pruefe(besuchte) == false) {
			action.moveNorth();
			posy--;
			return action;
		} else if (west == true && wp.pruefe(besuchte) == false) {
			action.moveWest();
			posx--;
			return action;
		} else if (south == true && sp.pruefe(besuchte) == false) {
			action.moveSouth();
			posy++;
			return action;
		}

		// action.moveEast();
		// posx++;
		return action;
	}

	public boolean machwas(Cell test, Richtung richtung) {
		Position p = new Position(posx + richtung.getX(), posy + richtung.getY());
		if (test.getType() == Cell.WALL) {
			waende.add(p);
			return false;
		} 
		else if (test.getType() == Cell.FLOOR) {
			freie.add(p);
			return true;
		} 
		else if (test.getType() == Cell.FORM && test.getPlayer() == playerid && p.pruefe(besuchte)==false) { // 2 ist ein Formular
			System.out.println("ich füge in formulare ein");
			while(test.getNumber()>formulare.size()) {
				formulare.add(null);
			}
			formulare.add(test.getNumber(),p);
			return true;
		} 
		else if(test.getType() == Cell.FORM && test.getPlayer() != playerid) {
			freie.add(p);
			return true;
		}
		else if (test.getType() == Cell.SHEET) {

		} 
		else if (test.getType() == Cell.FINISH && test.getPlayer() == playerid) {
			// prüfen wessen finish Feld das ist
			System.out.println(test.getNumber());
			finish = p;
			return true;
		}
		else if(test.getType() == Cell.FINISH && test.getPlayer() != playerid) {
			freie.add(p);
			return true;
		}
		return false;
	}

	public Action actionobre(RoundInfo roundinfo) {
		Action action = new Action();

		boolean east = machwas(roundinfo.getCellEast(), new Richtung(1, 0));
		boolean north = machwas(roundinfo.getCellNorth(), new Richtung(0, -1));
		boolean west = machwas(roundinfo.getCellWest(), new Richtung(-1, 0));
		boolean south = machwas(roundinfo.getCellSouth(), new Richtung(0, 1));

		if (finish != null && sheetnr == 0) {
			if (posx > finish.getX()) {
				action.moveWest();
				return action;
			} else if (posx < finish.getX()) {
				action.moveEast();
				return action;
			} else if (posy < finish.getY()) {
				action.moveSouth();
				return action;
			} else if (posy > finish.getY()) {
				action.moveNorth();
				return action;
			}
		}

		// new Position(posx+1,posy))==false
		Position ep = new Position(posx + 1, posy);
		Position np = new Position(posx, posy - 1);
		Position wp = new Position(posx - 1, posy);
		Position sp = new Position(posx, posy + 1);

		if (west == true && wp.pruefe(besuchte) == false) {
			action.moveWest();
			posx--;
			return action;
		} else if (north == true && np.pruefe(besuchte) == false) {
			action.moveNorth();
			posy--;
			return action;
		} else if (east == true && ep.pruefe(besuchte) == false) {
			action.moveEast();
			posx++;
			return action;
		} else if (south == true && sp.pruefe(besuchte) == false) {
			action.moveSouth();
			posy++;
			return action;
		}

		// action.moveEast();
		// posx++;
		return action;
	}

	public Action actionunli(RoundInfo roundinfo) {
		Action action = new Action();

		boolean east = machwas(roundinfo.getCellEast(), new Richtung(1, 0));
		boolean north = machwas(roundinfo.getCellNorth(), new Richtung(0, -1));
		boolean west = machwas(roundinfo.getCellWest(), new Richtung(-1, 0));
		boolean south = machwas(roundinfo.getCellSouth(), new Richtung(0, 1));

		if (finish != null && sheetnr == 0) {
			if (posx > finish.getX()) {
				action.moveWest();
				return action;
			} else if (posx < finish.getX()) {
				action.moveEast();
				return action;
			} else if (posy < finish.getY()) {
				action.moveSouth();
				return action;
			} else if (posy > finish.getY()) {
				action.moveNorth();
				return action;
			}
		}

		// new Position(posx+1,posy))==false
		Position ep = new Position(posx + 1, posy);
		Position np = new Position(posx, posy - 1);
		Position wp = new Position(posx - 1, posy);
		Position sp = new Position(posx, posy + 1);

		if (east == true && ep.pruefe(besuchte) == false) {
			action.moveEast();
			posx++;
			return action;
		} else if (south == true && sp.pruefe(besuchte) == false) {
			action.moveSouth();
			posy++;
			return action;
		} else if (west == true && wp.pruefe(besuchte) == false) {
			action.moveWest();
			posx--;
			return action;
		} else if (north == true && np.pruefe(besuchte) == false) {
			action.moveNorth();
			posy--;
			return action;
		}

		// action.moveEast();
		// posx++;
		return action;
	}

	public Action actionunre(RoundInfo roundinfo) {
		Action action = new Action();

		boolean east = machwas(roundinfo.getCellEast(), new Richtung(1, 0));
		boolean north = machwas(roundinfo.getCellNorth(), new Richtung(0, -1));
		boolean west = machwas(roundinfo.getCellWest(), new Richtung(-1, 0));
		boolean south = machwas(roundinfo.getCellSouth(), new Richtung(0, 1));

		if (finish != null && sheetnr == 0) {
			if (posx > finish.getX()) {
				action.moveWest();
				return action;
			} else if (posx < finish.getX()) {
				action.moveEast();
				return action;
			} else if (posy < finish.getY()) {
				action.moveSouth();
				return action;
			} else if (posy > finish.getY()) {
				action.moveNorth();
				return action;
			}
		}

		// new Position(posx+1,posy))==false
		Position ep = new Position(posx + 1, posy);
		Position np = new Position(posx, posy - 1);
		Position wp = new Position(posx - 1, posy);
		Position sp = new Position(posx, posy + 1);

		if (west == true && wp.pruefe(besuchte) == false) {
			action.moveWest();
			posx--;
			return action;
		} else if (south == true && sp.pruefe(besuchte) == false) {
			action.moveSouth();
			posy++;
			return action;
		} else if (east == true && ep.pruefe(besuchte) == false) {
			action.moveEast();
			posx++;
			return action;
		} else if (north == true && np.pruefe(besuchte) == false) {
			action.moveNorth();
			posy--;
			return action;
		}

		// action.moveEast();
		// posx++;
		return action;
	}

	public int getPosx() {
		return posx;
	}

	public void setPosx(int posx) {
		this.posx = posx;
	}

	public int getPosy() {
		return posy;
	}

	public void setPosy(int posy) {
		this.posy = posy;
	}

	public int getSizex() {
		return sizex;
	}

	public void setSizex(int sizex) {
		this.sizex = sizex;
	}

	public int getSizey() {
		return sizey;
	}

	public void setSizey(int sizey) {
		this.sizey = sizey;
	}
	
	

}
