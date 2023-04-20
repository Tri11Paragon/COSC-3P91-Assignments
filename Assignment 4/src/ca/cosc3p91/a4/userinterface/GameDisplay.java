package ca.cosc3p91.a4.userinterface;

import ca.cosc3p91.a4.game.Map;
import ca.cosc3p91.a4.gameobjects.Building;
import ca.cosc3p91.a4.gameobjects.Inhabitant;
import ca.cosc3p91.a4.util.Print;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class GameDisplay {
    private final BufferedReader reader;
    private String input;

    public GameDisplay() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public String nextInput() throws IOException {
        if (reader.ready()) {
            return (input = reader.readLine().trim());
        } else return null;
    }

    public void printLastInput() {
        System.out.println("\nYour Input: ");
        System.out.println("\t->" + input + '\n');
    }

    public ArrayList<String> getVillageStateTable(Map map, String displayName){
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

        ArrayList<String> total = new ArrayList<>(resourcesPrinter.createTable(true, false, true));

        Print buildingPrinter = new Print("Village Buildings", 2, resourcesPrinter.getWidth());
        buildingPrinter.addColumn(new Print.Column("Name"));
        buildingPrinter.addColumn(new Print.Column("Level"));
        buildingPrinter.addColumn(new Print.Column("Health"));

        for (Building b : map.contains)
            buildingPrinter.addRow(new Print.Row(b.getClass().getSimpleName(),
                    Integer.toString(b.getLevel() + 1),
                    Integer.toString(b.getHealth())));

        total.addAll(buildingPrinter.createTable(true, false, true));

        Print inhabs = new Print("Village Inhabitants", 2, buildingPrinter.getWidth());
        inhabs.addColumn(new Print.Column("Name"));
        inhabs.addColumn(new Print.Column("Level"));

        for (Inhabitant i : map.inhabitants)
            inhabs.addRow(new Print.Row(i.getClass().getSimpleName(), Integer.toString(i.getLevel() + 1)));

        total.addAll(inhabs.createTable(true, true, true));
        System.out.println(buildingPrinter.getWidth());
        System.out.println(resourcesPrinter.getWidth());
        System.out.println(inhabs.getWidth());
        return total;
    }

    public void printVillageState(Map map, String displayName) {
        Print.print(getVillageStateTable(map, displayName));
    }

    public void printGameMenu() {
        System.out.println("\n\033[36m~ Player Options:\n" +
                "1. Build {command: '1 <building name>'}\n" +
                "2. Train inhabitants {command: '2 <unit name>'}\n"+
                "3. Upgrade {command: '3 i<index>'} / {command: '3 b<index>'}\n"+
                "4. Explore Player Villages\n"+
                "5. Print Village Stats\n"+
                "6. Quit\n" +
                "7. Attack last explored/generated\n" +
                "8. Generate Village\n" +
                "9. Generate and Test Army\n" +
                "0. Village Testing" +
                "\033[0m\n");
    }
}