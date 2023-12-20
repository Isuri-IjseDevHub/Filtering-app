package lk.ijse.dep11.app;

import java.sql.*;

/**
 * A class for testing data clearance by deleting duplicate records from the 'customer' table.
 */
public class TestClearance3 {

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
            // Create a statement for executing SQL queries to find duplicate contact numbers
            Statement stmDuplicates = connection.createStatement();

            // Create a prepared statement to retrieve IDs of records with duplicate contact numbers
            PreparedStatement stmRetrieve = connection.prepareStatement("SELECT id FROM customer WHERE contact=?", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

            // Execute a SQL query to find contact numbers with duplicates
            ResultSet rstDuplicates = stmDuplicates.executeQuery("SELECT contact, COUNT(contact) AS count FROM customer GROUP BY contact HAVING count > 1");

            // Iterate through each duplicate contact number
            while (rstDuplicates.next()) {
                String contact = rstDuplicates.getString("contact");
                int count = rstDuplicates.getInt("count");
                System.out.printf("%s - %d \n", contact, count);

                // Set the contact number parameter for the prepared statement
                stmRetrieve.setString(1, contact);

                // Execute a SQL query to retrieve IDs of records with the duplicate contact number
                ResultSet rstIds = stmRetrieve.executeQuery();

                // Move to the first result (there might be multiple IDs for the same contact)
                rstIds.next();

                // Iterate through each ID and delete the corresponding record
                while (rstIds.next()) {
                    int id = rstIds.getInt("id");
                    rstIds.deleteRow();
                    System.out.printf("%s : %d Deleted\n", contact, id);
                }
            }
        }
    }
}
