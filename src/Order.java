import oracle.jdbc.driver.OracleConnection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Order {
    private final int orderID;
    private final String userID;
    private static ArrayList<Item> items = new ArrayList<>();

    Order(String userID) throws SQLException {
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("select MAX(order_id) from orderinf");
        rset.next();
        int id = rset.getInt(1) + 1;
        conn.close();
        this.orderID = id;
        this.userID = userID;
    }

    public int getID() {
        return orderID;
    }

    public void newOrder() throws SQLException {
        listObject();
        System.out.println("\n=====================================");
        System.out.println("Insert an object         >>> Enter(1)");
        System.out.println("Delete an object         >>> Enter(2)");
        System.out.println("Next step                >>> Enter(3)");
        System.out.println("Exit the program         >>> Enter(0)");
        System.out.println("=====================================\n");
        int scan;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Please enter your command: ");
            try {
                scan = scanner.nextInt();
                if (scan >= 0 && scan <= 3)
                    break;
                else
                    System.out.println("Your enter is wrong, please try again!");
            } catch (Exception e) {
                System.out.println("Your enter is wrong, please try again!");
                scanner.next();
            }
        }
        switch (scan) {
            case 1 -> creatObject();
            case 2 -> deleteObject();
            case 3 -> deliverOrder();
            default -> {return;}
        }
        newOrder();
    }

    public void creatObject() {
        String name, we;
        Double weight;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Please enter your object name(Enter ~ to quit): ");
            try {
                name = scanner.nextLine();
                if (name.equals("~"))
                    return;
                if (name.length() > 30)
                    System.out.println("Your username should be less than 30 characters. Please try again!");
                else
                    break;
            } catch (Exception e) {
                System.out.println("Your enter is wrong, please try again!");
                scanner.next();
            }
        }

        while (true) {
            System.out.print("Please enter your object weight(Enter ~ to quit): ");
            try {
                we = scanner.nextLine();
                if (we.equals("~"))
                    return;
                weight = Double.valueOf(we);
                break;
            } catch (Exception e) {
                System.out.println("Your enter is wrong, please try again!()");
                scanner.nextLine();
            }
        }
        items.add(new Item(orderID, name, weight));
    }

    public void listObject() throws SQLException {
        if (items.isEmpty()) {
            System.out.println("\nNo item");
            return;
        }

        System.out.println("\n============================");
        for (Item i : items) {
            System.out.println("--------------");
            System.out.println("Name: " + i.getName());
            System.out.println("Weight: " + i.getWeight());
            System.out.println("--------------");
        }

        System.out.println("============================\n");
    }

    public static void deleteObject() {
        String itemName;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Please enter your command: ");
            try {
                 itemName = scanner.nextLine();
                 for (int n=0; n<items.size(); n++) {
                     if (items.get(n).getName().equals(itemName)) {
                         items.remove(n);
                         System.out.println("Delete successful!");
                         return;
                     }
                 }
                System.out.println("No items named: "+itemName+"!");
                 return;
            } catch (Exception e) {
                System.out.println("Your enter is wrong, please try again!");
                scanner.next();
            }
        }
    }
    public void deliverOrder() throws SQLException {
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");
        Statement stmt = conn.createStatement();
        for (Item item : items)
            stmt.executeQuery("insert into object values(" + getID() + ", '" + item.getName() + "', " + item.getWeight() + ")");
        stmt.executeQuery("COMMIT");
        ResultSet rset = stmt.executeQuery("select phone_number from userinf where phone_number = " + userID);
        rset.next();
        int total = rset.getInt(1);
        double totalWeight = 0;
        for (Item item : items)
            totalWeight += item.getWeight();
        stmt.executeQuery("insert into orderinf values(" + userID + ", " +  + ", " + total + ", " + getID() + ", " + orderID + ", " + items.size() + ", " + totalWeight + ")");

    }

}
