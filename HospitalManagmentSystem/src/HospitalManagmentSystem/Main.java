package HospitalManagmentSystem;

import java.sql.*;
import java.util.Scanner;

public class Main {
    private static  final String url = "jdbc:mysql://localhost:3306/hospital?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String username = "root";
    private static final String password = "shri@1919%";
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        try(Connection connection = DriverManager.getConnection(url,username,password)){
            Patient patient = new Patient(connection,scanner);
            Doctor doctor = new Doctor(connection);
            while(true){
                System.out.println("***** HOSPITAL MANAGEMENT SYSTEM *****");
                System.out.println("1. Add Patient");
                System.out.println("2. view Patient");
                System.out.println("3. view Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                switch (choice){
                    case 1:
                        // Add Patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        // view patient
                        patient.viewPatient();
                        System.out.println();
                        break;
                    case 3:
                        // view doctor
                        doctor.viewDoctor();
                        System.out.println();
                        break;
                    case 4:
                        // book appointment
                        bookAppointment(patient,doctor,connection,scanner);
                        System.out.println();
                        break;
                    case 5:
                        // exist
                        System.out.println("Existing .....");
                        return;
                    default:
                        System.out.println("Enter valid choice!!!!");
                        break;
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public  static  void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner){
        System.out.print("Enter Patient id: ");
        int patientId = scanner.nextInt();
        System.out.print("Enter Doctor Id: ");
        int doctorId = scanner.nextInt();
        System.out.print("Enter appointment date yyyy-mm-dd: ");
        String appointmentDate = scanner.next();
        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if(checkDoctorAvaibility(doctorId, appointmentDate,connection)){
                String appointmentQuery = "insert into appointment(patient_id, doctor_id, appointment_date) values (?, ?, ?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,patientId);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3,appointmentDate);
                    int affectedRows = preparedStatement.executeUpdate();
                    if(affectedRows > 0){
                        System.out.println("Appointment Booked!!!");
                    }else{
                        System.out.println("Appointment Booking Failed!!!");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else {
                System.out.println("Doctor not available on this date...");
            }
        }else{
            System.out.println("Either doctor or patient doesn't exist!!!!");
        }
    }

    public static boolean checkDoctorAvaibility(int doctorId,String appointmentDate,Connection connection){
        String query = "SELECT COUNT(*) FROM appointment WHERE doctor_id = ? AND appointment_date = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count == 0){
                    return true;
                }else {
                    return false;
                }
            }
        }catch ( SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}