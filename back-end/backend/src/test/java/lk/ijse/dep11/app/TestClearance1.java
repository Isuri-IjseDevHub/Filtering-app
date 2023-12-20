package lk.ijse.dep11.app;

import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * A class for testing and inserting sample data into the 'customer' table for clearance testing.
 */
public class TestClearance1 {

    /**
     * The main method of the program.
     *
     * @param args Command-line arguments (not used in this example).
     * @throws Exception If an exception occurs during execution.
     */
    public static void main(String[] args) throws Exception {
        // Load the MySQL JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Establish a connection to the MySQL database
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep11_filtering_app", "root", "mysql")) {
            // Prepare a SQL statement for inserting data into the 'customer' table
            PreparedStatement stm = connection.prepareStatement("INSERT INTO customer (first_name, last_name, contact, country) VALUES (?, ?, ?, ?)");

            // Create an instance of the Faker library for generating fake data
            Faker faker = new Faker();

            // Insert 700 records into the 'customer' table with fake data
            for (int i = 0; i < 700; i++) {
                stm.setString(1, faker.name().firstName());
                stm.setString(2, faker.name().lastName());
                stm.setString(3, faker.regexify("0\\d{2}-\\d{7}")); // Generate a fake contact number
                stm.setString(4, faker.country().name());
                stm.executeUpdate();
            }

            // Print a message indicating that records have been added
            System.out.println("Records added!");
        }
    }
}
