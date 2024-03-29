package ca.cosc3p91.a4.util;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Ported from blt::TableFormatter (C++)
 * https://github.com/Tri11Paragon/BLT/
 */
public class Print {

    public static class Row {
        private final ArrayList<String> values;

        public Row(ArrayList<String> row) {
            this.values = row;
        }

        public Row() {
            this.values = new ArrayList<>();
        }

        public Row(String... values){
            this();
            this.values.addAll(Arrays.asList(values));
        }

        public void add(String value) {
            values.add(value);
        }
    }

    public static class Column {
        private String columnName;
        private long maxColumnLength = 0;

        public Column(String columnName) {
            this.columnName = columnName;
        }
    }

    private final ArrayList<Row> rows = new ArrayList<>();
    private final ArrayList<Column> columns = new ArrayList<>();

    private final String tableName;
    private final int columnPadding;
    private int width;
    private final int targetWidth;

    public Print(String tableName, int columnPadding, int targetWidth) {
        this.tableName = tableName;
        this.columnPadding = columnPadding;
        this.targetWidth = targetWidth;
    }

    public Print(String tableName, int columnPadding){this(tableName, columnPadding, -1);}

    public Print(String tableName){this(tableName, 2);}

    public Print() {
        this("");
    }

    private String createPadding(int amount) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            builder.append(' ');
        }
        return builder.toString();
    }

    private String generateTopSelector(long size) {
        long sizeOfName = tableName.isEmpty() ? 0 : tableName.length() + 4;
        long sizeNameRemoved = size - sizeOfName;

        StringBuilder halfWidthLeftSeparator = new StringBuilder();
        StringBuilder halfWidthRightSeparator = new StringBuilder();

        long sizeNameFloor = (long) Math.floor((double) sizeNameRemoved / 2.0);
        long sizeNameCeil = (long) Math.ceil((double) sizeNameRemoved / 2.0);

        halfWidthLeftSeparator.append('+');

        for (int i = 0; i < sizeNameFloor - 1; i++)
            halfWidthLeftSeparator.append('-');
        for (int i = 0; i < sizeNameCeil - 1; i++)
            halfWidthRightSeparator.append('-');

        halfWidthRightSeparator.append('+');

        StringBuilder separator = new StringBuilder();
        separator.append(halfWidthLeftSeparator.toString());
        if (sizeOfName != 0) {
            separator.append("{ ");
            separator.append(tableName);
            separator.append(" }");
        }
        separator.append(halfWidthRightSeparator);
        return separator.toString();
    }

    private String generateColumnHeader() {
        updateColumnLengths();
        StringBuilder header = new StringBuilder();
        header.append('|');

        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            double columnPaddingLength = ((int) (column.maxColumnLength) - (int) (column.columnName.length())) / 2.0;
            header.append(createPadding((int) (columnPadding + (int) Math.floor(columnPaddingLength))));

            header.append(column.columnName);

            header.append(createPadding((int) (columnPadding + (int) Math.ceil(columnPaddingLength))));
            if (i < columns.size() - 1)
                header.append('|');
        }
        header.append('|');

        return header.toString();
    }

    private String generateSeparator(long size) {
        int nextIndex = 0;
        int currentColumnIndex = 0;
        StringBuilder wholeWidthSeparator = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if (i == nextIndex) {
                //System.out.println(currentColumnIndex + " " + nextIndex + " " + size);
                int currentColumnSize = (int) (columns.get(currentColumnIndex++).maxColumnLength + columnPadding * 2);
                nextIndex += currentColumnSize + 1;
                wholeWidthSeparator.append('+');
            } else
                wholeWidthSeparator.append('-');
        }
        wholeWidthSeparator.append('+');
        return wholeWidthSeparator.toString();
    }

    private void updateColumnLengths() {
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            column.maxColumnLength = column.columnName.length();
            for (Row row : rows) {
                column.maxColumnLength = Math.max(column.maxColumnLength, row.values.get(i).length());
            }
        }
    }

    public long columnSize(Column column) {
        return column.columnName.length() + columnPadding * 2L;
    }

    public Print addColumn(Column column) {
        columns.add(column);
        return this;
    }

    public Print addRow(Row row) {
        if (row.values.size() > columns.size())
            throw new RuntimeException("Unable to create row with more values than columns!");
        if (row.values.size() < columns.size())
            for (int i = row.values.size(); i < columns.size(); i++)
                row.values.add("");
        rows.add(row);
        return this;
    }

    public Print addRow(ArrayList<String> row) {
        return addRow(new Row(row));
    }

    public ArrayList<String> createTable(boolean top, boolean bottom, boolean separators) {
        String header = generateColumnHeader();
        String topSeparator = generateTopSelector(header.length());
        String lineSeparator = generateSeparator(header.length() - 1);

        if (targetWidth > 0) {
            int diff = targetWidth - header.length();

            if (diff > 0) {

                int left = (int) Math.floor(diff / 2.0);
                int right = (int) Math.ceil(diff / 2.0);

                int leftleft = (int) Math.floor(left / 2.0);
                int leftright = (int) Math.ceil(left / 2.0);

                int rightleft = (int) Math.floor(right / 2.0);
                int rightright = (int) Math.ceil(right / 2.0);

                columns.get(0).columnName = createPadding(leftleft) + columns.get(0).columnName + createPadding(leftright);
                columns.get(columns.size() - 1).columnName = createPadding(rightleft) + columns.get(columns.size() - 1).columnName + createPadding(rightright);

                header = generateColumnHeader();
                topSeparator = generateTopSelector(header.length());
                lineSeparator = generateSeparator(header.length() - 1);
            }
        }

        width = header.length();

        ArrayList<String> lines = new ArrayList<>();

        if (top)
            lines.add(topSeparator);

        lines.add(header);
        lines.add(lineSeparator);

        for (Row row : rows) {
            StringBuilder rowLine = new StringBuilder();
            rowLine.append('|');
            for (int i = 0; i < row.values.size(); i++) {
                String value = row.values.get(i);
                Column column = columns.get(i);
                int spaceLeft = (int) (column.maxColumnLength - value.length());
                rowLine.append(createPadding((int) Math.floor(spaceLeft / 2.0) + columnPadding));
                rowLine.append(value);
                rowLine.append(createPadding((int) Math.ceil(spaceLeft / 2.0) + columnPadding));
                rowLine.append('|');
            }
            lines.add(rowLine.toString());
        }

        if (bottom)
            lines.add(lineSeparator);

        return lines;
    }

    public ArrayList<String> createBox() {
        return new ArrayList<>();
    }

    public ArrayList<String> createTable() {
        return createTable(true, true, true);
    }

    public int getWidth(){
        return width;
    }

    public static void print(ArrayList<String> lines) {
        for (String line : lines)
            System.out.println(line);
    }

}
