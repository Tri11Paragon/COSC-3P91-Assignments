package ca.cosc3p91.a2.game;

import ca.cosc3p91.a2.gameobjects.*;

import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
public class Map {

  static int MAXSIZE = 400;

  private Village_Hall townHall;
=======

public class Map {

    private Village_Hall townHall;
>>>>>>> abf784868daa920a9ca8b3b9d291a7cf521aa9c7

    private int guardTime;

<<<<<<< HEAD
  public List<Building> contains;
=======
    private List<Building> contains;
>>>>>>> abf784868daa920a9ca8b3b9d291a7cf521aa9c7

    public List<Inhabitant> inhabitants;

<<<<<<< HEAD
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

  public void build(Village_Hall hall, Tile t, Building b) {
    contains.add(b);
  }
=======
    public void move(Infantry i, Tile t) {
    }

    public void inRange(Infantry i, Building b) {
    }

    public void build(Village_Hall hall, Tile t, Building b) {
    }
>>>>>>> abf784868daa920a9ca8b3b9d291a7cf521aa9c7

    public int getGuardTime() {
        return guardTime;
    }

<<<<<<< HEAD
  public void setGuardTime(int gTime) {
    this.guardTime = gTime;
  }
=======
    public void setGuardTime() {
    }
>>>>>>> abf784868daa920a9ca8b3b9d291a7cf521aa9c7

}
