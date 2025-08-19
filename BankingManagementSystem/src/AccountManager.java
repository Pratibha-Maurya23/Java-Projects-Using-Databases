import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class AccountManager {
    private Connection connection;
    private Scanner scanner;
    public AccountManager(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }
    public  void debit_money(long account_no) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Security Pin: ");
        String security_Pin = scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if(account_no != 0){
                PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where account_no = ? and security_pin = ?");
                preparedStatement.setLong(1,account_no);
                preparedStatement.setString(2,security_Pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    double current_amount = resultSet.getDouble("balance");
                    if(amount <= current_amount){
                        String debit_query = "update accounts set balance = balance-? where account_no = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,account_no);
                        int rowsAffected = preparedStatement1.executeUpdate();
                        if(rowsAffected > 0){
                            System.out.println("Rs. "+amount+"debited Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }else {
                            System.out.println("Transition Failed! ");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else {
                        System.out.println("Insufficient Balance!  ");
                    }
                }
                else{
                    System.out.println("Invalid Pin! ");
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public  void credit_money(long account_no) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Security Pin: ");
        String security_Pin = scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if(account_no != 0){
                PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where account_no = ? and security_pin = ?");
                preparedStatement.setLong(1,account_no);
                preparedStatement.setString(2,security_Pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()) {
                    String credit_query = "update accounts set balance = balance + ? where account_no = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1, amount);
                    preparedStatement1.setLong(2, account_no);
                    int rowsAffected = preparedStatement1.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Rs. " + amount + "Credited Successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transition Failed! ");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }
                else{
                    System.out.println("Invalid Pin! ");
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public  void tranfer_money(long sender_account_no) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter Reciever Account Number: ");
        long reciever_account_no = scanner.nextLong();
        System.out.println("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Security Pin: ");
        String security_Pin = scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if(sender_account_no != 0 && reciever_account_no != 0){
                PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where account_no = ? and security_pin = ?");
                preparedStatement.setLong(1,sender_account_no);
                preparedStatement.setString(2,security_Pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    double current_amount = resultSet.getDouble("balance");
                    if(amount <= current_amount){

                        String debit_query = "update accounts set balance = balance-? where account_no = ?";
                        String credit_query = "update accounts set balance = balance+? where account_no = ?";

                        PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);
                        PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);

                        debitPreparedStatement.setDouble(1,amount);
                        debitPreparedStatement.setLong(2,sender_account_no);

                        creditPreparedStatement.setDouble(1,amount);
                        creditPreparedStatement.setDouble(2,reciever_account_no);

                        int rowsAffected1 = creditPreparedStatement.executeUpdate();
                        int rowsAffected2 = debitPreparedStatement.executeUpdate();
                        if(rowsAffected1 > 0 && rowsAffected2 > 0){
                            System.out.println("Rs. "+amount+"Transfer Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }else {
                            System.out.println("Transition Failed! ");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else {
                        System.out.println("Insufficient Balance!  ");
                    }
                }
                else{
                    System.out.println("Invalid Pin! ");
                }
            }else{
                System.out.println("Invalid Account Number");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public  void getBalace(long account_no){
        scanner.nextLine();
        System.out.println("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("select balance from accounts where account_no = ? and security_pin = ?");
            preparedStatement.setLong(1,account_no);
            preparedStatement.setString(2,security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance = resultSet.getDouble("balance");
                System.out.println("Balance: "+balance);
            }else{
                System.out.println("Invalid Security Pin !");
            }
        }catch ( SQLException e){
            e.printStackTrace();
        }
    }
}
