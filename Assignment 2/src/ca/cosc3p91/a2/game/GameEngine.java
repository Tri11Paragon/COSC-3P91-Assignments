package ca.cosc3p91.a2.game;

import ca.cosc3p91.a2.gameobjects.Building;
import ca.cosc3p91.a2.gameobjects.VillageStage;
import ca.cosc3p91.a2.gameobjects.Village_Hall;
import ca.cosc3p91.a2.player.Player;

public class GameEngine implements Runnable {

    private final boolean running = true;

    private Player player;

    private int pillageFactor;

    private int currentTime;

<<<<<<< HEAD
  public GameEngine () {
    player = new Player();
    VillageStage vInitialStage = new VillageStage(100,0,2,30,0,
            0, 12,12,12);
    map = new Map(new Village_Hall(1,vInitialStage),30);
  }

  public void printMap() {
    System.out.println("~ Current Map State ~\n\n");
    System.out.println("In Map:\n");
    for (Building b : map.contains) {
      System.out.println("|> "+b.getClass().toString()+" lvl: "+b.getLevel()+" health: "+b.getHealth());
    }
  }

  public void attackVillage(Map map) {
  }
=======
    public Map map;
>>>>>>> abf784868daa920a9ca8b3b9d291a7cf521aa9c7

    public void attackVIllage(Map map) {
    }

    public Map generateMap() {
        return null;
    }

    public void getScore(Map map) {
    }

    @Override
    public void run() {
        while (running) {

        }
    }
}
