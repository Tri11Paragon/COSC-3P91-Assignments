package ca.cosc3p91.a2.game;

import ca.cosc3p91.a2.gameobjects.*;
import ca.cosc3p91.a2.player.*;

public class GameEngine implements Runnable {

  private Player player;

  private int pillageFactor;

  private int currentTime;

  public Map map;

  public GameEngine () {
    player = new Player();
    VillageStage vInitialStage = new VillageStage(100,0,2,30,0,
            0, 12,12,12);
    map = new Map(new Village_Hall(1,vInitialStage),30);
  }

  public void printState() {
    // Print toPrint = new Print("~ Current Village Buildings ~",2);

    System.out.println("In Map:\n");
    System.out.println("\t~ Current Village Buildings ~\n");
    for (Building b : map.contains) {
      System.out.println("\t|> "+b.getClass().getSimpleName()+" lvl: "+b.getLevel()+" health: "+b.getHealth());
    }
    System.out.println("\n\t~ Current Village Inhabitants ~\n\n");
    for (Inhabitant i : map.inhabitants) {
      System.out.println("\t|> "+i.getClass().getSimpleName()+" lvl: "+i.getLevel());
    }
  }

  public void attackVillage(Map map) {
  }

  public Map generateMap() {
    return null;
  }

  public void getScore(Map map) {
  }

  @Override
  public void run() {

  }
}
