package ca.cosc3p91.a2;

import ca.cosc3p91.a2.game.GameEngine;
import ca.cosc3p91.a2.util.Print;

public class Main {

    public static void main(String[] args) {
        GameEngine engine = new GameEngine();

        Print test = new Print("Hello There", 2);

        test.addColumn(new Print.Column("I am value 1"));
        test.addColumn(new Print.Column("Super Value"));
        test.addColumn(new Print.Column("SOLD!"));

        Print.Row row = new Print.Row();
        row.add("HelloThere");
        row.add("Goodbye");
        row.add("3");

        test.addRow(row);
        test.addRow(row);
        test.addRow(row);

        Print.print(test.createTable());

        engine.run();
    }

}
