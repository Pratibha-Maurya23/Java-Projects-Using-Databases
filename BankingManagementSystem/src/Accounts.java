import java.sql.*;
import java.util.Scanner;


public class Accounts {
    private Connection connection;
    private Scanner scanner;
    public Accounts(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public long open_Account(String email){
        if(!account_exist(email)){
           String query = "insert into accounts(account_no, full_name, email, balance, security_pin) values (?, ?, ?, ?, ?)";
           scanner.nextLine();
            System.out.println("Full name: ");
            String full_name = scanner.nextLine();
            System.out.println("Enter Initial Amount: ");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Enter Security Pin: ");
            String security_pin = scanner.nextLine();

            try {
                long account_no = generateAccountNumber();
               PreparedStatement preparedStatement = connection.prepareStatement(query);
               preparedStatement.setLong(1,account_no);
               preparedStatement.setString(2, full_name);
               preparedStatement.setString(3, email);
               preparedStatement.setDouble(4,balance);
               preparedStatement.setString(5,security_pin);

               int rowsAffected = preparedStatement.executeUpdate();
               if(rowsAffected > 0){
                   return account_no;
               }else {
                   throw  new RuntimeException("Account Creation Failed! ");
               }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        throw new IllegalStateException("Account Already Exists for this email");
    }

    public long getAccount_no(String email){
        String query = "select account_no from accounts where email = ?";

        try{
          PreparedStatement preparedStatement = connection.prepareStatement(query);
          preparedStatement.setString(1,email);
          ResultSet resultSet = preparedStatement.executeQuery();
          if(resultSet.next()){
              return resultSet.getLong("account_no");
          }
        }catch (SQLException e){
            e.printStackTrace();
        }
        throw  new RuntimeException("Account Number Does't Exists ");
    }

    public long generateAccountNumber(){
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select account_no from accounts order by account_no desc limit 1");
            if(resultSet.next()){
                long last_account_number = resultSet.getLong("account_no");
                return last_account_number+1;
            }else {
                return 1000100;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 1000100;
    }

    public boolean account_exist(String email){
        String query = "select account_no from accounts where email = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return true;
            }else {
                return false;
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return  false;
    }
}
