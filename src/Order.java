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
            case 3 -> {
                deliverOrder();
                return;
            }
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

    public void listObject() {
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
            System.out.print("Please enter your object weight(Enter ~ to quit): ");
            try {
                 itemName = scanner.nextLine();
                 for (int n=0; n<items.size(); n++) {
                     if (itemName.equals("~")) return;
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

    public ArrayList<Integer> printPlace() throws SQLException {
        ArrayList<Integer> place = new ArrayList<>();
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("select * from place");
        System.out.println("Place:");
        while (rset.next()) {
            System.out.printf("Place id: %8d Place name: %25S\n", rset.getInt(1), rset.getString(2));
            place.add(rset.getInt(1));
        }
        conn.close();
        System.out.println("\n\n\n");
        return place;
    }

    public void deliverOrder() throws SQLException {
        String r_phone = "";
        String r_name = "";
        int a = 0, b = 0;
        String c, d;
        Scanner scanner = new Scanner(System.in);
        ArrayList<Integer> place = printPlace();
        boolean flag = true;

        // place id
        while (flag) {
            System.out.print("Select your sender place id: ");
            try {
                c = scanner.nextLine();
                a = Integer.parseInt(c);
                for (int n : place) {
                    if (a == n) {
                        flag = false;
                        break;
                    }
                }
                if (flag)
                    System.out.println("Your enter is wrong, please try again!");
            } catch (Exception e) {
                System.out.println("Your enter is wrong, please try again!");
                scanner.nextLine();
            }
        }

        // place id
        flag = true;
        while (flag) {
            System.out.print("Select your receiver place id: ");
            try {
                d = scanner.nextLine();
                b = Integer.parseInt(d);
                for (int n : place) {
                    if (n == b) {
                        flag = false;
                        break;
                    }
                }
                if (flag)
                    System.out.println("Your enter is wrong, please try again!");
            } catch (Exception e) {
                System.out.println("Your enter is wrong, please try again!");
                scanner.nextLine();
            }
        }

        // receiver's name
        flag = true;
        while (flag){
            try {
                flag = false;
                System.out.print("Please enter receiver's name: ");
                r_name = scanner.nextLine();
                if (r_name.equals("")) {
                    System.out.println("Your name cannot be empty, please try again!");
                    flag = true;
                }
            } catch (Exception e) {
                System.out.println("Your enter is wrong, please try again!");
                return;
            }
        }

        // phone num
        flag = true;
        while (flag) {
            flag = false;
            System.out.print("Please enter receiver's telephone number (Enter ~ to quit): ");
            try {
                r_phone = scanner.nextLine();
                if (r_phone.equals("~"))
                    return;
                if (r_phone.length() > 13) {
                    System.out.println("Your phone number should be less than 13 digits. Please try again!");
                    flag = true;
                }
                if (Application.isNumeric(r_phone))
                    break;
                else {
                    System.out.println("Your number should be all digit. Please try again");
                    flag = true;
                }

            } catch (Exception e) {
                System.out.println("Your enter is wrong, please try again!");
                scanner.next();
            }
        }

        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");
        Statement stmt = conn.createStatement();
        for (Item item : items)
            stmt.executeQuery("insert into object values(" + orderID + ", '" + item.getName() + "', " + item.getWeight() + ")");
        conn.close();
        double totalWeight = 0;
        for (Item item : items)
            totalWeight += item.getWeight();
        double price = countPrice(a, b ,totalWeight);
        conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");        stmt = conn.createStatement();
        stmt.executeQuery("insert into orderinf values('" + r_phone + "', " + orderID  + ", " +  items.size() + ", " + totalWeight + ", " + price + ", " + userID + ")");
        stmt.executeQuery("insert into order_state values(" + orderID +", "+ 0 + ")");
        stmt.executeQuery("insert into receiver values(" + orderID +", '"+ r_phone +"', '"+ r_name + "')");
        stmt.executeQuery("insert into orderPlace values(" + orderID +", "+ a +", "+ b + ")");
        stmt.executeQuery("COMMIT");
        System.out.println("Deliver successful!");
        conn.close();
    }

    public double countPrice(int a, int b, double weight) throws SQLException {
        System.out.println("Print place a: "+a);
        System.out.println("Print place b: "+b);
        double distance;
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("select distance from placeDistance where (place_a = "+a+" and place_b = "+b+") OR (place_a = "+b+" and place_b = "+a+")");
        rset.next();
        distance = rset.getInt(1);
        conn.close();
        // minimum delivery amount
        if (distance<100) distance = 100;
        if (weight<3) weight = 3;
        if (distance > 1000) return weight*distance*0.01 + 200;
        else if (distance >= 300) return weight*distance*0.02 + 90;
        else return weight*distance*0.05;

    }

}
