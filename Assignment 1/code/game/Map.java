package game;

import java.util.List;
import gameobjects.Village_Hall;
import gameobjects.Building;
import gameobjects.Tile;
import gameobjects.Infantry;
import gameobjects.Inhabitant;

public class Map {

  private Village_Hall townHall;

  private int guardTime;

  private List<Building> contains;

  public List<Inhabitant> inhabitants;

  public void move(Infantry i, Tile t) {
  }

  public void inRange(Infantry i, Building b) {
  }

  public void build(Village_Hall hall, Tile t, Building b) {
  }

  public int getGuardTime() {
    return guardTime;
  }

  public void setGuardTime() {
  }

}
