package ca.cosc3p91.a2.game;

import ca.cosc3p91.a2.gameobjects.*;
import ca.cosc3p91.a2.player.*;
import ca.cosc3p91.a2.util.Print;
import ca.cosc3p91.a2.util.Time;
import ca.cosc3p91.a2.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;

public class GameEngine<T> implements Runnable {

    public static final double GOLD_FACTOR = 5;
    public static final double IRON_FACTOR = 1;
    public static final double WOOD_FACTOR = 0.1;

    private Player player;
    boolean running = true;

    private float pillageFactor = 0.5f;

    private int currentTime;

    private final Random random = new Random(System.nanoTime());

    public Map map;

    public GameEngine() {
        player = new Player();
        map = generateInitialMap();
    }

    private void printState(Map map, String displayName) {
        Print resourcesPrinter = new Print(displayName, 2);

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
                    Integer.toString(b.getLevel() + 1),
                    Integer.toString(b.getHealth())));

        Print.print(buildingPrinter.createTable(true, false, true));

        Print inhabs = new Print("Village Inhabitants", 2, buildingPrinter.getWidth());
        inhabs.addColumn(new Print.Column("Name"));
        inhabs.addColumn(new Print.Column("Level"));

        for (Inhabitant i : map.inhabitants)
            inhabs.addRow(new Print.Row(i.getClass().getSimpleName(), Integer.toString(i.getLevel() + 1)));

        Print.print(inhabs.createTable(true, true, true));
    }

    private void printMenuOptions() {
        System.out.println("\n~ Player Options:\n" +
                "1. Build {command: '1 <building name>'}\n" +
                "2. Train inhabitants {command: '2 <unit name>'}\n"+
                "3. Upgrade {command: '3 i<index>'} / {command: '3 b<index>'}\n"+
                "4. Explore\n"+
                "5. Print Village Stats\n"+
                "6. Quit\n" +
                "7. Attack last explored\n");
    }

    public void attackVillage(Map map) {
        int defenseiveCounter = 1;
        int inhabCounter = 0;
        for (Building b : map.contains)
            if (b instanceof DefenseBuilding)
                defenseiveCounter++;
        for (Inhabitant i : map.inhabitants)
            if (i instanceof Infantry)
                inhabCounter++;
        pillageFactor = (float) inhabCounter / (float) defenseiveCounter;
        if (pillageFactor < 0)
            pillageFactor = 0;
        if (pillageFactor > 1)
            pillageFactor = 1;
        this.map.getTownHall().addWood((int) (map.getTownHall().getCurrentWood() * pillageFactor));
        this.map.getTownHall().addIron((int) (map.getTownHall().getCurrentIron() * pillageFactor));
        this.map.getTownHall().addGold((int) (map.getTownHall().getCurrentGold() * pillageFactor));
    }

    private Map generateInitialMap(){
        return new Map(new CasaDeNarino(1, VillageHallStages.villageStages[0]), 30);
    }

    public Map generateMap() {
        Map initialMap = generateInitialMap();

        CasaDeNarino hall = initialMap.getTownHall();

        // generate a similar town hall
        int levelChange = random.nextInt(2) - 1;
        int nextLevel = this.map.getTownHall().getLevel() + levelChange;
        // only need to change if the new village level is higher than initial
        if (nextLevel > 0)
            hall.upgrade(VillageHallStages.villageStages[nextLevel]);

        hall.addWood(this.map.getTownHall().getCurrentWood() + random.nextInt(500) - 150);
        hall.addIron(this.map.getTownHall().getCurrentIron() + random.nextInt(500) - 150);
        hall.addGold(this.map.getTownHall().getCurrentGold() + random.nextInt(500) - 150);

        int buildingCount = this.map.contains.size();

        int saulGoodMines = 0;
        int ironMines = 0;
        int woodMines = 0;
        int archerTowers = 0;
        int cannons = 0;

        // count buildings in our map
        for (Building b : this.map.contains){
            if (b instanceof SaulGoodMine)
                saulGoodMines++;
            else if (b instanceof IronMine)
                ironMines++;
            else if (b instanceof LumberMine)
                woodMines++;
            else if (b instanceof ArcherTower)
                archerTowers++;
            else if (b instanceof  Cannon)
                cannons++;
        }

        // variate
        saulGoodMines += random.nextInt();;
        ironMines += random.nextInt();
        woodMines += random.nextInt();
        archerTowers += random.nextInt();
        cannons += random.nextInt();

        // generate a map with a similar number of buildings
        for (int i = 0; i < Math.max(buildingCount + random.nextInt(5) - 2, 1); i++) {
            int selection = random.nextInt(5);
            // select a random building. Doing it this way because if we build based on buildings we have
            // then the maps will be VERY boring as they would be all the same
            switch (selection) {
                case 0:
                    initialMap.contains.add(new LumberMine(ResourceStages.woodStages[random.nextInt(ResourceStages.woodStages.length)]));
                    break;
                case 1:
                    if (ironMines > 0)
                        initialMap.contains.add(new IronMine(ResourceStages.ironStages[random.nextInt(ResourceStages.ironStages.length)]));
                    break;
                case 2:
                    if (saulGoodMines > 0)
                        initialMap.contains.add(new SaulGoodMine(ResourceStages.goldStages[random.nextInt(ResourceStages.goldStages.length)]));
                    break;
                case 3:
                    initialMap.contains.add(new ArcherTower());
                    break;
                case 4:
                    initialMap.contains.add(new Cannon());
                    break;
                default:
                    break;
            }
        }

        return initialMap;
    }

    public int getScore(Map map) {
        CasaDeNarino hall = map.getTownHall();
        int score = (int)(hall.getCurrentGold() * GOLD_FACTOR + hall.getCurrentIron() * IRON_FACTOR + hall.getCurrentWood() * WOOD_FACTOR);
        score += map.contains.size();
        score += map.inhabitants.size();
        return score;
    }

    @Override
    public void run() {
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        Scanner sc = new Scanner(rd);
        printState(this.map,"Current Village State");
        printMenuOptions();
        System.out.println();
        Map exploringMap = null;
        boolean deleteMyHeart = true;
        while (running) {
            for (Building b : this.map.contains){
                if ((b instanceof ResourceBuilding)) {
                    ((ResourceBuilding) b).update(this.map.getTownHall());
                }
            }
            try {
                if (rd.ready()) {
                    String in = sc.nextLine();
                    String[] args = in.split(" ");
                    System.out.println("Your Input: ");
                    System.out.println("\t->" + in + '\n');
                    // reset the map if they aren't exploring
                    if (in.charAt(0) != '4')
                        deleteMyHeart = true;
                    switch (in.charAt(0)) {
                        case '1':
                            if (args.length < 2) {
                                System.err.println("Args must include type!");
                            } else {
                                Building type = determineBuildingType(args[1]);
                                if (type == null)
                                    System.err.println("Args are not a valid building!");
                                else if (this.map.build(new Tile(), type) ) {
                                    System.out.println(type.getClass().getSimpleName()+" successfully built\n");
                                } else
                                    System.out.println("Missing resources to build "+type.getClass().getSimpleName());
                            }
                            break;
                        case '2':
                            if (args.length < 2) {
                                System.err.println("Args must include type!");
                            } else {
                                Inhabitant type = determineInhabitantType(args[1]);
                                if (type == null)
                                    System.err.println("Args are not a valid inhabitant!");
                                else if (this.map.train(type) ) {
                                    System.out.println("successfully trained a(n) "+type.getClass().getSimpleName());
                                } else System.out.println("Missing gold to train "+type.getClass().getSimpleName());
                            }
                            break;
                        case '3':
                            if (args.length < 2) {
                                System.err.println("Args must include type!");
                            } else {
                                int unitIndex = Integer.parseInt(args[1].substring(1));

                                if (unitIndex < 0) {
                                    System.err.println("Invalid Index");
                                    break;
                                }

                                if (args[1].contains("i") && (unitIndex < map.inhabitants.size()) ) {
                                    if ( map.upgradeInhabitant(unitIndex) ) {
                                        System.out.println("successfully upgraded a(n) "+map.inhabitants.get(unitIndex).getClass().getSimpleName());
                                    } else System.out.println("Missing Resources to upgrade "+map.inhabitants.get(unitIndex).getClass().getSimpleName());
                                } else if (args[1].contains("b") && (unitIndex < map.contains.size()) ) {
                                    if ( map.upgradeBuilding(unitIndex) ) {
                                        System.out.println("successfully upgraded a(n) "+map.contains.get(unitIndex).getClass().getSimpleName());
                                    } else System.out.println("Missing Resources to upgrade "+map.contains.get(unitIndex).getClass().getSimpleName());
                                } else {
                                    System.err.println("Args are not a valid unit!");
                                }
                            }
                            break;
                        case '4':
                            deleteMyHeart = false;
                            exploringMap = generateMap();
                            printState(exploringMap, "Other Village");
                            break;
                        case '7':
                            if (exploringMap != null)
                                attackVillage(exploringMap);
                            else
                                System.out.println("Error: Explored map is null. Have you explored last command?");
                            break;
                        case '5':
                            printState(this.map,"Home Village");
                            break;
                        case '6':
                            System.exit(0);
                            break;
                        default:
                            break;
                    }
                    printMenuOptions();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (deleteMyHeart)
                exploringMap = null;
        }
    }

    private static char determineChar(String str){
        char c = ' ';
        if (str.trim().length() == 1)
            c = str.charAt(0);
        return c;
    }

    private static Building determineBuildingType(String argument){
        argument = argument.toLowerCase();
        char c = determineChar(argument);

        if (argument.contains("gold") || argument.contains("good") || c == 'g') {
            return new SaulGoodMine(ResourceStages.goldStages[0]);
        } else if (argument.contains("iron") || c == 'i') {
            return new IronMine(ResourceStages.ironStages[0]);
        } else if (argument.contains("wood") || argument.contains("lumber") || c == 'w' || c == 'l') {
            return new LumberMine(ResourceStages.woodStages[0]);
        } else if (argument.contains("archer") || c == 'a') {
            return new ArcherTower();
        } else if (argument.contains("can") || c == 'c') {
            return new Cannon();
        }

        return null;
    }

    private static Inhabitant determineInhabitantType(String argument) {
        argument = argument.toLowerCase();
        char c = determineChar(argument);

        if (argument.contains("soldier") || c == 's') {
            return new Soldier();
        } else if (argument.contains("knight") || c == 'k') {
            return new Knight();
        } else if (argument.contains("work") || c == 'w') {
            return new Worker();
        } else if (argument.contains("collect") || c == 'c') {
            return new Collector();
        } else if (argument.contains("cat")) {
            return new Catapult();
        } else if (argument.contains("arch") || c == 'a') {
            return new Archer();
        }

        return null;
    }

}
