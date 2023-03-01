package ca.cosc3p91.a2.game;

import ca.cosc3p91.a2.gameobjects.*;

import java.util.List;



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
