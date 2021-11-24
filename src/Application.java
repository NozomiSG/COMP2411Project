import oracle.jdbc.driver.OracleConnection;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {

    public static User user = new User("", "");
    public static Administrator admin = new Administrator("", "");

    public static void main(String[] args) throws SQLException {
        homepage();
    }


    public static void registerAccount()throws SQLException {
        String username, password = "", password_1, phoneNumber = "";
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
            System.out.print("Please enter your username (Enter ~ to quit): ");
            try {
                username = scanner.nextLine();
                if (username.equals("~"))
                    return;
                if (username.length() > 15)
                    System.out.println("Your username should be less than 15 characters. Please try again!");
                else if (nam.contains(username))
                    System.out.println("This username has been used. Please try again!");
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
        flag = true;
        while (flag) {
            flag = false;
            System.out.print("Please enter your telephone number (Enter ~ to quit): ");
            try {
                phoneNumber = scanner.nextLine();
                if (phoneNumber.equals("~"))
                    return;
                if (phoneNumber.length() > 13)
                    System.out.println("Your phone number should be less than 13 digits. Please try again!");
                else if (tel.contains(phoneNumber)) {
                    System.out.println("This phone number has been used. Please try again!");
                    flag = true;
                }

                else {
                    if (isNumeric(phoneNumber))
                        break;
                    else {
                        System.out.println("Your number should be all digit. Please try again");
                        flag = true;
                    }
                }
            } catch (Exception e) {
                System.out.println("Your enter is wrong, please try again!");
                flag = true;
                scanner.next();
            }
        }
        conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");

        stmt = conn.createStatement();
        rset = stmt.executeQuery("select count(*) from userinf");
        rset.next();
        int id = rset.getInt(1) + 1;
        rset = stmt.executeQuery("insert into userinf values(" + id +", '" + phoneNumber + "', '" + password + "', '" + username + "')");
        stmt.executeQuery("COMMIT");
        conn.close();
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        return str.chars().allMatch(Character::isDigit);
    }

    public static void loginAccount() throws SQLException {
        String info, password;
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter your username/telephone number(Enter ~ to quit): ");
        info = scanner.nextLine();
        user.setInfo(info);
        if (info.equals("~")) return;
        System.out.print("Please enter your password(Enter ~ to quit): ");
        password = scanner.nextLine();
        if (password.equals("~")) return;
        user.setPassword(password);

        if (user.checkLogin()) {
            System.out.println("Login successfully!\n\n\n");
            userHomePage();
        }
        else {
            System.out.println("The username or password is incorrect. Please try again");
            loginAccount();
        }
    }

    public static void loginAdmin() throws SQLException {
        String account,password,operation="";
        boolean flag=false;
        Administrator ad=new Administrator(null,null);
        Scanner scanner=new Scanner(System.in);
        System.out.println("\n=====================================");
        while (!operation.equals("q")){
            System.out.println("Please enter your account: ");
        account=scanner.nextLine();
            ad.setID(account);
            System.out.println("Please enter your password: ");
            password=scanner.nextLine();
            ad.setPassword(password);
            if(ad.checkLogin()) {
                flag=true;
                break;
            }
            System.out.println("Wrong account/password! Please try again or enter 'q' to quit: ");
            operation=scanner.nextLine();
        }
        if (flag){
            System.out.println("===================================================");
            System.out.println("Welcome, administrator "+ad.getID()+"!");
            System.out.println("Change Order state                     >>> Enter(1)");
            System.out.println("Add a new place                        >>> Enter(2)");
            System.out.println("Change your password                   >>> Enter(3)");
            System.out.println("Logout                                 >>> Enter(4)");
            System.out.println("Please enter your command: ");
            operation=scanner.nextLine();
        }
        while (true){
            try {
                switch (operation){
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                }
            } catch (Exception e) {
                System.out.println("Your enter is wrong, please try again!");
                scanner.next();
            }
        }
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

    public static void userHomePage() throws SQLException {
        System.out.println("Hello " + user.getName());
        System.out.println("\n==========================================");
        System.out.println("Check personal Information     >>> Enter(1)");
        System.out.println("Establish new orders           >>> Enter(2)");
        System.out.println("Check delivery status          >>> Enter(3)");
        System.out.println("Change password                >>> Enter(4)");
        System.out.println("Logout                         >>> Enter(0)");
        System.out.println("===========================================\n");
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
//            case 1 ->
//            case 2 ->
//            case 3 ->
            case 4 -> changePassword(user);
            case 0 -> {
                System.out.println("Bye\n\n\n\n\n\n\n");
                return;
            }
        }
        userHomePage();
    }

    public static void changePassword(Account ac) throws SQLException {
        String pass1, pass2;
        Scanner scanner = new Scanner(System.in);
        boolean flag = true;
        while (flag) {
            System.out.print("Please enter your previous password(Enter ~ to quit): ");
            pass1 = scanner.nextLine();
            if (pass1.equals("~")) return;
            user.setPassword(pass1);
            System.out.print("Please enter your new password(Enter ~ to quit): ");
            pass2 = scanner.nextLine();
            if (pass2.equals("~")) break;
            flag = ac.changePassword(pass1, pass2);
        }

    }


    public static void homepage() throws SQLException {
        System.out.println("Welcome use deliverApp!");
        System.out.println("\n=====================================");
        System.out.println("Register an account      >>> Enter(1)");
        System.out.println("Login your account       >>> Enter(2)");
        System.out.println("Check delivery status    >>> Enter(3)");
        System.out.println("Login admin account      >>> Enter(4)");
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
            case 1 -> registerAccount();
            case 2 -> loginAccount();
            case 3 -> checkDelivery();
            case 4 -> loginAdmin();
            case 0 -> System.out.println("Bye");
        }
        homepage();
    }
}

