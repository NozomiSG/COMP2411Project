import java.sql.SQLException;

public abstract class Account {
    private String ID;
    private String Password;
    Account(String ID,String Password){
        this.setID(ID);
        this.setPassword(Password);
    }
    public abstract boolean checkLogin() throws SQLException;
    public String getID(){return ID;}
    public String getPassword(){return Password;}
    public void setID(String ID){this.ID=ID;}
    public void setPassword(String Password){this.Password = Password;}
}
