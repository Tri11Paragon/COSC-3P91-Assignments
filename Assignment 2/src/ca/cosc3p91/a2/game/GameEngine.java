package ca.cosc3p91.a2.game;

import ca.cosc3p91.a2.gameobjects.*;
import ca.cosc3p91.a2.player.*;
import ca.cosc3p91.a2.util.Print;
import ca.cosc3p91.a2.util.Time;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class GameEngine implements Runnable {

    private Player player;
    boolean running = true;

    private int pillageFactor;

    private int currentTime;

    public Map map;

    public GameEngine() {
        player = new Player();
        map = generateMap();
    }

    private void printState() {
        Print resourcesPrinter = new Print("Current Village Resources", 2);

        resourcesPrinter.addColumn(new Print.Column("Resource Type"));
        resourcesPrinter.addColumn(new Print.Column("Max"));
        resourcesPrinter.addColumn(new Print.Column("Amount"));

        resourcesPrinter.addRow(new Print.Row(
                "Wood",
                Integer.toString(map.getTownHall().getWoodCapacity()),
                Integer.toString(map.getTownHall().getCurrentWood())));

        resourcesPrinter.addRow(new Print.Row(
                "Iron",
                Integer.toString(map.getTownHall().getIronCapacity()),
                Integer.toString(map.getTownHall().getCurrentIron())));

        resourcesPrinter.addRow(new Print.Row(
                "Gold",
                Integer.toString(map.getTownHall().getGoldCapacity()),
                Integer.toString(map.getTownHall().getCurrentGold())));

        Print.print(resourcesPrinter.createTable(true, false, true));

        Print buildingPrinter = new Print("Village Buildings", 2, resourcesPrinter.getWidth());
        buildingPrinter.addColumn(new Print.Column("Name"));
        buildingPrinter.addColumn(new Print.Column("Level"));
        buildingPrinter.addColumn(new Print.Column("Health"));

        for (Building b : map.contains)
            buildingPrinter.addRow(new Print.Row(b.getClass().getSimpleName(),
                    Integer.toString(b.getLevel()),
                    Integer.toString(b.getHealth())));

        Print.print(buildingPrinter.createTable(true, false, true));

        Print inhabs = new Print("Village Inhabitants", 2, buildingPrinter.getWidth());
        inhabs.addColumn(new Print.Column("Name"));
        inhabs.addColumn(new Print.Column("Level"));

        for (Inhabitant i : map.inhabitants)
            inhabs.addRow(new Print.Row(i.getClass().getSimpleName(), Integer.toString(i.getLevel())));

        Print.print(inhabs.createTable(true, true, true));
    }

    public void attackVillage(Map map) {

    }

    private Map generateInitialMap(){
        return new Map(new CasaDeNarino(1, VillageHallStages.villageStages[0]), 30);
    }

    public Map generateMap() {
        Map initialMap = generateInitialMap();
        initialMap.build(new Tile(), new SaulGoodMine(1, new ResourceStage(50, 0, 0, new Time(), 0, 0, 25)));
        return initialMap;
    }

    public void getScore(Map map) {

    }

    @Override
    public void run() {
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        Scanner sc = new Scanner(rd);
        printState();
        System.out.println();
        while (running) {
            for (Building b : map.contains){
                if ((b instanceof ResourceBuilding)) {
                    ((ResourceBuilding) b).update(map.getTownHall());
                }
            }
            //System.out.println("Updating");
            try {
                if (rd.ready()) {
                    char in = sc.next().charAt(0);
                    switch (in) {
                        case 'p':
                            printState();
                            break;
                        case 'q':
                            System.exit(0);
                            break;
                        default:
                            break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
