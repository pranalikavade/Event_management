package Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class CollegeEventTracker {
    private static final String URL = "jdbc:postgresql://localhost:5433/mydb1";
    private static final String USER = "users";
    private static final String PASSWORD = "panya_30";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static void insertParticipants(Collection<Participant> participants, String query) throws SQLException {
        try (Connection con = getConnection()) {
            PreparedStatement pstm = con.prepareStatement(query);
            for (Participant p : participants) {
                pstm.setString(1, p.getName());
                pstm.setString(2, p.getStudentClass());
                pstm.setInt(3, p.getAge());
                pstm.setString(4, p.getCollege());
                pstm.setString(5, p.getEvent());
                pstm.setInt(6, p.getFee());
                pstm.setString(7, p.getRoom());
                pstm.addBatch();
            }
            int[] rowInserted = pstm.executeBatch();
            System.out.println(rowInserted.length + " participants registered successfully.");
        }
    }

    private static void viewParticipants() {
        StringBuilder output = new StringBuilder();
        String query = "SELECT * FROM participants";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                output.append("Name: ").append(rs.getString("name")).append("\n");
                output.append("Class: ").append(rs.getString("class")).append("\n");
                output.append("Age: ").append(rs.getInt("age")).append("\n");
                output.append("College: ").append(rs.getString("college")).append("\n");
                output.append("Event: ").append(rs.getString("event_name")).append("\n");
                output.append("Fee: ₹").append(rs.getInt("fee")).append("\n");
                output.append("Room: ").append(rs.getString("room_number")).append("\n");
                output.append("------------------------------\n");
            }
        } catch (Exception e) {
            output.append("❌ Error: ").append(e.getMessage());
        }
        System.out.println(output);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("🎓 Welcome to College Event Tracker!");

        System.out.print("Enter your Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your Class: ");
        String studentClass = scanner.nextLine();
        System.out.print("Enter your Age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter your College Name: ");
        String college = scanner.nextLine();

        EventInfo events = new EventInfo();
        System.out.println("Choose an Event:");
        System.out.println(events.getEventList());
        System.out.print("Enter choice number: ");
        int eventIndex = Integer.parseInt(scanner.nextLine()) - 1;

        String selectedEvent = events.getEvent(eventIndex);
        int fee = events.getFee(eventIndex);
        String room = events.getRoom(eventIndex);

        System.out.println("📌 Eve