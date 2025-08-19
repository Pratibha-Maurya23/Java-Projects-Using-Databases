import java.sql.*;
import java.util.Scanner;

public class BankingApp {
    private static  final String url = "jdbc:mysql://localhost:3306/banking_system?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String username = "root";
    private static final String password = "shri@1919%";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            User user = new User(connection,scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            String  email;
            long account_no;

            while (true){
                System.out.println(" ****** WELCOME TO BANKING SYSTEM ***** ");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Enter your choice: ");
                int choice1 = scanner.nextInt();
                switch (choice1){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if(email!= null){
                            System.out.println();
                            System.out.println("User Logged In! ");
                            if(!accounts.account_exist(email)){
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exist");
                                if(scanner.nextInt() == 1){
                                    account_no = accounts.open_Account(email);
                                    System.out.println("Account Created Successfully ");
                                    System.out.println("Your account number is: "+ account_no);
                                }else {
                                    break;
                                }

                            }
                            account_no = accounts.getAccount_no(email);
                            int choice2 = 0;
                            while(choice2 != 5){
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Tranfer Money");
                                System.out.println("4. View Balance");
                                System.out.println("5. Log out");
                                System.out.println("Enter your choice: ");
                                choice2 = scanner.nextInt();
                                switch (choice2){
                                    case 1:
                                        accountManager.debit_money(account_no);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_no);
                                        break;
                                    case 3:
                                        accountManager.tranfer_money(account_no);
                                        break;
                                    case 4:
                                        accountManager.getBalace(account_no);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter valid choice!..");
                                        break;
                                }
                            }
                        }else {
                            System.out.println("Incorrect Email or Password");
                        }
                        break;
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("Existing System! ");
                        return;
                    default:
                        System.out.println("Enter valid choice ");
                        break;
                }
            }
        }catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close(); // Close scanner to prevent resource leak
        }

    }
}
