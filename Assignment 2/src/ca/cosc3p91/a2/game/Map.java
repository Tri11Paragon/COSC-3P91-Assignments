package ca.cosc3p91.a2.game;

import ca.cosc3p91.a2.gameobjects.*;

import java.util.ArrayList;
import java.util.List;

public class Map {

  static int MAXSIZE = 400;

  private Village_Hall townHall;

  private int guardTime;

  public List<Building> contains;

  public List<Inhabitant> inhabitants;

  public Map (Village_Hall villageHall, int gTime) {
    contains = new ArrayList<>();
    inhabitants = new ArrayList<>();
    this.townHall = villageHall;
    this.contains.add(villageHall);
    this.guardTime = gTime;
  }

  public void move(Infantry i, Tile t) {

  }

  public void inRange(Infantry i, Building b) {

  }

  public void build(Tile t, Building b) {
    contains.add(b);
  }

  public int getGuardTime() {
    return guardTime;
  }

  public void setGuardTime(int gTime) {
    this.guardTime = gTime;
  }

}
