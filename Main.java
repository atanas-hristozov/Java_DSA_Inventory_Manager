import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static Set<Item> items = new TreeSet<>();
    private static Map<String, Set<Item>> itemsByType = new HashMap<>(); //HashMap with TreeSets

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String commandLine = scanner.nextLine();
            String[] commandArgs = commandLine.split(" ");

            switch (commandArgs[0]) {
                case "add":
                    add(commandArgs);
                    break;
                case "filter":
                    filter(commandArgs);
                    break;
                case "end":
                    return;
            }
        }
    }

    //Complexity: O(log N)
    private static void add(String[] commandArgs) {
        String name = commandArgs[1];
        double price = Double.parseDouble(commandArgs[2]);
        String type = commandArgs[3];

        Item item = new Item(name, price, type);
        if (items.contains(item)) { //O(log N)
            System.out.printf("Error: Item %s already exists%n", name);
        } else {
            items.add(item); //O(log N)
            if (!itemsByType.containsKey(type)) { //O(1)
                itemsByType.put(type, new TreeSet<>()); //O(1)
            }
            itemsByType.get(type).add(item); //O(log N)

            System.out.printf("Ok: Item %s added successfully%n", name);
        }
    }

    private static void filter(String[] commandArgs) {
        if (commandArgs[2].equals("type")) {
            filterByType(commandArgs[3]);
        } else if (commandArgs.length == 7) {
            filterByPrice(Double.parseDouble(commandArgs[4]), Double.parseDouble(commandArgs[6]));
        } else if (commandArgs[3].equals("from")) {
            filterByPrice(Double.parseDouble(commandArgs[4]), Double.MAX_VALUE);
        } else {
            filterByPrice(Double.MIN_VALUE, Double.parseDouble(commandArgs[4]));
        }
    }

    //Complexity: O(N)
    private static void filterByType(String type) {
        if (!itemsByType.containsKey(type)) {
            System.out.printf("Error: Type %s does not exist%n", type);
            return;
        }

        String result = itemsByType.get(type)//O(1)
                .stream()//O(N)
                .limit(10)
                .map(Item::toString)
                .collect(Collectors.joining(", "));

        System.out.printf("Ok: %s%n", result);
    }

    //Complexity: O(N)
    private static void filterByPrice(double min, double max) {
        String result = items.stream()//O(N)
                .filter(i -> i.price >= min && i.price <= max)//O(N)
                .limit(10)
                .map(Item::toString)
                .collect(Collectors.joining(", "));

        System.out.printf("Ok: %s%n",result);
    }

    private static class Item implements Comparable<Item> {
        String name;
        double price;
        String type;

        public Item(String name, double price, String category) {
            this.name = name;
            this.price = price;
            this.type = category;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return String.format("%s(%.2f)", name, price);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof Item)) return false;
            Item item = (Item) obj;
            return item.name.equals(name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public int compareTo(Item o) {
            return Comparator.comparing(Item::getPrice)
                    .thenComparing(Item::getName)
                    .thenComparing(Item::getType)
                    .compare(this, o);
        }
    }
}