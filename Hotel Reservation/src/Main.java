import java.sql.DriverManager;
import  java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String username = "root";
    private static final String password = "shri@1919%";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded sucessfully");
        }catch (ClassNotFoundException e){
            System.out.println("Driver Error: "+e.getMessage());
        }
        try{
            Connection con = DriverManager.getConnection(url,username,password);
            while(true){
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM ");
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservation");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();
                switch(choice){
                    case 1:
                        reserveRoom(con, sc);
                        break;
                    case 2:
                        viewReservations(con);
                        break;
                    case 3:
                        getRoomNumber(con, sc);
                        break;
                    case 4:
                        updateReservation(con, sc);
                        break;
                    case 5:
                        deleteReservation(con, sc);
                        break;
                    case 0:
                        exit();
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again. ");
                }
            }

        }catch (SQLException e){
            System.out.println("Connection Error: "+e.getMessage());
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }


    }
    private static void reserveRoom(Connection con, Scanner sc){
        try{
            System.out.print("Enter guest name: ");
            String guestName = sc.next();
            sc.nextLine();
            System.out.print("Enter room number: ");
            int roomNumber = sc.nextInt();
            System.out.print("Enter contact Number: ");
            String contactNumber = sc.next();
            sc.nextLine();
            String sql = "INSERT INTO reservations (guest_name, room_no, contact_no)"+"VALUES ('" + guestName + "',"+roomNumber+",'"+ contactNumber+"');";
            try(Statement stmt = con.createStatement()){
                int affectedRows = stmt.executeUpdate(sql);
                if(affectedRows > 0){
                    System.out.println("Reservation Sucessfully !!! ");
                }else {
                    System.out.println("Reservation Failed. ");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void viewReservations(Connection con)  {
        String sql = "SELECT reservation_id, guest_name, room_no, contact_no, reservation_date FROM reservations;";
        try (Statement stmt = con.createStatement()){
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Current Reservation: ");
            System.out.println("+----------------+-------------+---------------+--------------------+----------------------+");
            System.out.println("| Reservation Id | Guest       | Room Number   | Conatact Number    | Reservation Date     |");
            System.out.println("+----------------+-------------+---------------+--------------------+----------------------+");

            while (rs.next()){
                int reservationId = rs.getInt("reservation_id");
                String guestName = rs.getString("guest_name");
                int roomNumber = rs.getInt("room_no");
                String contactNo = rs.getString("contact_no");
                String reservationDate = rs.getString("reservation_date");
                System.out.printf("| %-14d | %-15.15s | %-11d | %-16.16s | %-19.19s |\n",
                        reservationId,
                        guestName != null ? guestName : "",
                        roomNumber,
                        contactNo != null ? contactNo : "",
                        reservationDate != null ? reservationDate : "");
            }
            System.out.println("+----------------+-------------+---------------+--------------------+----------------------+");
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    private static void getRoomNumber(Connection con, Scanner sc) {
        try{
            System.out.print("Enter the reservation Id: ");
            int reservationId = sc.nextInt();
            System.out.print("Enter the guest name: ");
            String guestName = sc.next();
            sc.nextLine();
            String sql = "SELECT room_no FROM reservations  WHERE reservation_id = " + reservationId + " AND guest_name = '" + guestName + "'";
            try (Statement stmt = con.createStatement();
                 ResultSet  rs = stmt.executeQuery(sql)){
                if(rs.next()){
                    int roomNumber = rs.getInt("room_no");
                    System.out.println("Room number for reservation Id "+reservationId+" and guest "+guestName+"is :"+roomNumber);
                }else {
                    System.out.println("Reservation is not found for the given ID and guest name.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void updateReservation(Connection con, Scanner sc) {
        System.out.print("Enter the reservation Id: ");
        int reservationId = sc.nextInt();
        sc.nextLine();
        if(!reservationExists(con, reservationId)){
            System.out.println("Reservation not found for the given ID. ");
            return;
        }
        System.out.print("Enter new guest name: ");
        String newGuestName = sc.nextLine();
        System.out.print("Enter new room number: ");
        int newRoomNumber = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter new contact number: ");
        String newContactNumber = sc.nextLine();

        String sql  = "UPDATE reservations SET guest_name = '"+newGuestName+"',"+
                "room_no = "+newRoomNumber+","+
                "contact_no = '"+newContactNumber+"' "+
                "WHERE reservation_id = " +reservationId;
        try(Statement stmt = con.createStatement()){
            int affectedRows = stmt.executeUpdate(sql);
            if(affectedRows > 0){
                System.out.println("Reservation update Sucessfully !!! ");
            }else {
                System.out.println("Reservation update Failed. ");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void deleteReservation(Connection con, Scanner sc) {
        System.out.print("Enter the reservation Id: ");
        int reservatioId = sc.nextInt();
        sc.nextLine();
        if (!reservationExists(con, reservatioId)) {
            System.out.println("Reservation not found for the given ID. ");
            return;
        }
        String sql = "DELETE FROM reservations WHERE reservation_id = " + reservatioId;
        try (Statement stmt = con.createStatement()) {
            int affectedRows = stmt.executeUpdate(sql);
            if (affectedRows > 0) {
                System.out.println("Reservation delete Sucessfully !!! ");
            } else {
                System.out.println("Reservation delete Failed. ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static boolean reservationExists(Connection con,int reservationId){
        try{
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = "+reservationId;
            try (Statement stmt = con.createStatement();
                 ResultSet  rs = stmt.executeQuery(sql)){
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void exit() throws  InterruptedException {
        System.out.print("Exiting Syatem");
        int i = 5;
        while(i!= 0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
    }
}