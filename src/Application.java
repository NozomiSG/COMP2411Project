import oracle.jdbc.driver.OracleConnection;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) throws SQLException {
        homepage();
    }


    public static void registerAccount()throws SQLException {
        String adminName, password = "", password_1, phoneNumber = "";
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> nam = new ArrayList<>();
        ArrayList<String> tel = new ArrayList<>();
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("select u_name, phone_number from userinf");
        while (rset.next()) {
            nam.add(rset.getString(1));
            tel.add(rset.getString(2));
        }
        conn.close();
        while (true) {
            System.out.print("Please enter your adminName (Enter ~ to quit): ");
            try {
                adminName = scanner.nextLine();
                if (adminName.equals("~"))
                    return;
                if (adminName.length() > 15)
                    System.out.println("Your adminName should be less than 15 characters. Please try again!");
                else if (nam.contains(adminName))
                    System.out.println("This adminName has been used. Please try again!");
                else
                    break;
            } catch (Exception e) {
                System.out.println("Your enter is wrong, please try again!");
                scanner.next();
            }
        }
        boolean flag = true;
        while (flag) {
            System.out.print("Please enter your password(Enter ~ to quit): ");
            try {
                password = scanner.nextLine();
                if (password.equals("~"))
                    return;
                if (password.length() > 15)
                    System.out.println("Your password should be less than 15 characters. Please try again!");
                else {
                    while (true) {
                        System.out.print("Please enter your password again(Enter ~ to quit): ");
                        try {
                            password_1 = scanner.nextLine();
                            if (password_1.equals("~"))
                                return;
                            if (!password.equals(password_1))
                                System.out.println("The two passwords are different. Please try again!");
                            else
                                flag = false;
                            break;
                        } catch (Exception e) {
                            System.out.println("Your enter is wrong, please try again!");
                            scanner.next();
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Your enter is wrong, please try again!");
                scanner.next();
            }
        }

        while (true) {
            System.out.print("Please enter your telephone number (Enter ~ to quit): ");
            try {
                phoneNumber = scanner.nextLine();
                if (phoneNumber.equals("~"))
                    return;
                if (phoneNumber.length() > 13)
                    System.out.println("Your phone number should be less than 13 digits. Please try again!");
                else if (tel.contains(phoneNumber))
                    System.out.println("This phone number has been used. Please try again!");
                else {
                    if (isNumeric(phoneNumber))
                        break;
                    else
                        System.out.println("Your number should be all digit. Please try again");
                }
            } catch (Exception e) {
                System.out.println("Your enter is wrong, please try again!");
                scanner.next();
            }
        }
        conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");

        stmt = conn.createStatement();
        rset = stmt.executeQuery("select count(*) from userinf");
        rset.next();
        int id = rset.getInt(1) + 1;
        rset = stmt.executeQuery("insert into userinf values(" + id +", '" + phoneNumber + "', '" + password + "', '" + adminName + "')");
        System.out.println("\nCreat successfully!\n\n\n");
        conn.close();
    }



    public static boolean isNumeric(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        return str.chars().allMatch(Character::isDigit);
    }



    public static void loginAccount() throws SQLException {
        String adminName, password, user_id;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your adminName (Enter ~ to quit): ");

        adminName = scanner.nextLine();
        if (adminName.equals("~"))
            return;
        else {
            System.out.print("Please enter your password (Enter ~ to quit): ");
            password = scanner.nextLine();
            if (password.equals("~"))
                return;
        }
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("select u_name, password, user_id from userinf");
        while (rset.next()) {
            if (rset.getString(1).equals(adminName)) {
                if (rset.getString(2).equals(password) ) {
                    System.out.println("Login successfully!\n\n\n");
                    user_id = rset.getString(3);
                    conn.close();

                }
                else {
                    System.out.println("The user name or password is incorrect. Please try again");
                    break;
                }
            }
        }
        conn.close();
        System.out.println("The adminName or password is incorrect. Please try again");
        loginAccount();
    }
    public static void loginAdmin() throws SQLException {
    }
    public static void list_order(int OrderID) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection)DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms","20074794D","Peter0817..");
        Statement stmt = conn.createStatement();
        ArrayList<Integer> orderid_=new ArrayList<>();
        ResultSet rset = stmt.executeQuery("select order_id from orderinf");
        while (rset.next()){
            orderid_.add(rset.getInt(1));
        }
        if(!orderid_.contains(OrderID)) {
            System.out.println("There is no such orderID! Please check and try again");
            conn.close();
            checkDelivery();
        }
        String receiver,sender;
        rset=stmt.executeQuery("select u_name , r_name from userinf,receiver where userinf.user_id=(select user_id from orderinf where order_id="+ OrderID +") AND receiver.phone_number=(select r_phone from orderinf where order_id="+ OrderID +")");
        rset.next();
        sender=rset.getString(1);
        receiver=rset.getString(2);
        rset = stmt.executeQuery("select * from orderinf where order_id="+OrderID);
        rset.next();
        System.out.println("\n\n=====================================");
        System.out.println("The Order information: ");
        System.out.println("OrderID:                  "+OrderID);
        System.out.println("The sender's name:        "+sender);
        System.out.println("The receiver's name:      "+receiver);
        System.out.println("The number of the object: "+rset.getInt(4));
        System.out.println("Total weight:             "+rset.getDouble(5));
        System.out.println("Total price:              "+rset.getDouble(6)+"\n\n");
        conn.close();
        System.out.println("Please enter to continue...");
        scanner.nextLine();
    }
    public static void checkDelivery() throws SQLException {
        System.out.println("\n=====================================");
        Scanner scanner = new Scanner(System.in);
        int choice;
        while(true) {
            try {
                System.out.println("OrderID to check         >>> Enter(1) ");
                System.out.println("PhoneNumber to check     >>> Enter(2) ");
                choice = scanner.nextInt();
                if (choice > 2 || choice < 1) System.out.println("Your enter is wrong, please try again!");
                else break;
            } catch (Exception e) {
                System.out.println("Your enter is wrong, please try again!");
                scanner.next();
            }
        }
        if(choice==1){
            int OrderID;
            while(true) {
                try {
                    System.out.println("Please enter the OrderID: ");
                    OrderID = scanner.nextInt();
                    if (String.valueOf(OrderID).length()>8) System.out.println("The OrderID should be at most 8 numbers!");
                    else break;
                } catch (Exception e) {
                    System.out.println("Your enter is wrong, please try again!");
                    scanner.next();
                }
            }
            list_order(OrderID);
        }

        else{
            Scanner sc=new Scanner(System.in);
            String phoneNumber;
            while(true) {
                try {
                    System.out.println("Please enter your the phone umber: ");
                    phoneNumber = sc.nextLine();
                    if (String.valueOf(phoneNumber).length()>13) System.out.println("The phone umber should be at most 13 numbers!");
                    else break;
                } catch (Exception e) {
                    System.out.println("Your enter is wrong, please try again!");
                    sc.next();
                }
            }
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            OracleConnection conn = (OracleConnection)DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms","20074794D","Peter0817..");
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("select order_id from receiver where phone_number= '"+ phoneNumber +"'");
            while (rset.next()){
                list_order(rset.getInt(1));
            }
            rset = stmt.executeQuery("select order_id from orderinf where user_id=(select user_id from userinf where phone_number='"+phoneNumber+"')");
            while (rset.next()){
                list_order(rset.getInt(1));
            }
            conn.close();
        }

    }

    public static void homepage() throws SQLException {
        System.out.println("Welcome use deliverApp!");
        System.out.println("\n=====================================");
        System.out.println("Register an account      >>> Enter(1)");
        System.out.println("Login your account       >>> Enter(2)");
        System.out.println("Check delivery status    >>> Enter(3)");
        System.out.println("Login as admin           >>> Enter(4)");
        System.out.println("Exit the program         >>> Enter(0)");
        System.out.println("=====================================\n\n\n");
        int scan;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Please enter your command: ");
            try {
                scan = scanner.nextInt();
                if (scan >= 0 && scan <= 4)
                    break;
                else
                    System.out.println("Your enter is wrong, please try again!");
            }catch(Exception e) {
                System.out.println("Your enter is wrong, please try again!");
                scanner.next();
            }
        }
        switch (scan) {
            case 1 ->
                registerAccount();
            case 2 ->
                loginAccount();
            case 3 ->
                checkDelivery();
            case 4 ->
                loginAdmin();
            case 0 -> {
                System.out.println("Bye");
                return;
            }
            default ->
                System.out.println("Unknown command, please enter again!\n\n\n\n");
        }
        homepage();
    }
}

