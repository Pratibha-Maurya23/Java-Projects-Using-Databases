import javax.sound.midi.Soundbank;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class User {
    private Connection connection;
    private Scanner scanner;
    public User(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register(){
        scanner.nextLine();
        System.out.print("Full Name: ");
        String full_name = scanner.nextLine();
        System.out.print("Email: ");
        String email =  scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if(user_exist(email)){
            System.out.println("User is already Registered !!!");
            return;
        }

        String registered_query = "insert into user(full_name, email, password) values (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(registered_query);
            preparedStatement.setString(1,full_name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);

            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0){
                System.out.println("Registration Successful !!!");
            }else{
                System.out.println("Registration Failed...");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String login(){
        scanner.nextLine();
        System.out.print("Email: ");
        String email =  scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        String login_query = "select * from user where email = ? and password = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return email;
            }else {
                return null;
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return  null;
    }

    public boolean user_exist(String email){
        String query = "select * from user where email = ?";

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
