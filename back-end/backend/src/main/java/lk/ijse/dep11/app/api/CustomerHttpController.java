package lk.ijse.dep11.app.api;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lk.ijse.dep11.app.to.CustomerTO;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PreDestroy;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/customers")
@Validated
public class CustomerHttpController {

    private final HikariDataSource pool;

    public CustomerHttpController(Environment env) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(env.getRequiredProperty("spring.datasource.url"));
        config.setUsername(env.getRequiredProperty("spring.datasource.username"));
        config.setPassword(env.getRequiredProperty("spring.datasource.password"));
        config.setDriverClassName(env.getRequiredProperty("spring.datasource.driver-class-name"));
        config.setMaximumPoolSize(env.getRequiredProperty("spring.datasource.hikari.maximum-pool-size", Integer.class));
        pool = new HikariDataSource(config);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String exceptionHandler(ConstraintViolationException exp) {
        ResponseStatusException resExp = new ResponseStatusException(HttpStatus.BAD_REQUEST,
                exp.getMessage());
        exp.initCause(resExp);
        throw resExp;

    }

    @PreDestroy
    private void destroy() {
        pool.close();
    }

    @GetMapping
    public List<CustomerTO> getAllCustomers(String q) {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM customer " +
                    "WHERE id LIKE ? OR first_name LIKE ? OR last_name LIKE ? OR " +
                    "contact LIKE ? OR country LIKE ?");
            if (q == null) q = "";
            for (int i = 1; i <= 5; i++) stm.setObject(i, "%" + q + "%");
            ResultSet rst = stm.executeQuery();
            return getCustomerList(rst);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(params = {"sort"})
    public List<CustomerTO> getAllSortedCustomers(String q,
                                                  @Pattern(regexp = "^(id|first_name|last_name|contact|country),(asc|desc)$",
                                                          message = "Invalid sorting parameter") String sort) {
        String[] splitText = sort.split(",");
        String colName = splitText[0];
        String order = splitText[1];
        List<String> colNames = List.of("id", "first_name", "last_name", "contact", "country");

        try (Connection connection = pool.getConnection()) {

            final int COLUMN_INDEX = colNames.indexOf(colName.intern());

            PreparedStatement stm = connection.prepareStatement("SELECT * FROM customer " +
                    "WHERE id LIKE ? OR first_name LIKE ? OR last_name LIKE ? OR " +
                    "contact LIKE ? OR country LIKE ? ORDER BY " + colNames.get(COLUMN_INDEX) + " " +
                    (order.equalsIgnoreCase("asc") ? "ASC" : "DESC"));
            if (q == null) q = "";
            for (int i = 1; i <= 5; i++) stm.setObject(i, "%" + q + "%");
            ResultSet rst = stm.executeQuery();
            return getCustomerList(rst);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(params = {"page", "size"})
    public List<CustomerTO> getAllPaginatedCustomers(String q,
                                                     @Positive(message = "Page can't be zero or negative") int page,
                                                     @Positive(message = "Size can't be zero or negative") int size) {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM customer " +
                    "WHERE id LIKE ? OR first_name LIKE ? OR last_name LIKE ? OR " +
                    "contact LIKE ? OR country LIKE ? LIMIT ? OFFSET ?");
            if (q == null) q = "";
            for (int i = 1; i <= 5; i++) stm.setObject(i, "%" + q + "%");
            stm.setInt(6, size);
            stm.setInt(7, (page - 1) * size);
            ResultSet rst = stm.executeQuery();
            return getCustomerList(rst);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(params = {"page", "size", "sort"})
    public List<CustomerTO> getAllPaginatedAndSortedCustomers(String q,
                                                              @Positive(message = "Page can't be zero or negative") int page,
                                                              @Positive(message = "Size can't be zero or negative") int size,
                                                              @Pattern(regexp = "^(id|first_name|last_name|contact|country),(asc|desc)$",
                                                                      message = "Invalid sorting parameter") String sort) {
        String[] splitText = sort.split(",");
        String colName = splitText[0];
        String order = splitText[1];
        List<String> colNames = List.of("id", "first_name", "last_name", "contact", "country");

        try (Connection connection = pool.getConnection()) {
            final int COLUMN_INDEX = colNames.indexOf(colName.intern());
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM customer " +
                    "WHERE id LIKE ? OR first_name LIKE ? OR last_name LIKE ? OR " +
                    "contact LIKE ? OR country LIKE ? ORDER BY " + colNames.get(COLUMN_INDEX) + " " +
                    (order.equalsIgnoreCase("asc") ? "ASC" : "DESC") +
                    " LIMIT ? OFFSET ?");
            if (q == null) q = "";
            for (int i = 1; i <= 5; i++) stm.setObject(i, "%" + q + "%");
            stm.setInt(6, size);
            stm.setInt(7, (page - 1) * size);
            ResultSet rst = stm.executeQuery();
            return getCustomerList(rst);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<CustomerTO> getCustomerList(ResultSet rst) throws SQLException {
        LinkedList<CustomerTO> customerList = new LinkedList<>();
        while (rst.next()) {
            int id = rst.getInt("id");
            String firstName = rst.getString("first_name");
            String lastName = rst.getString("last_name");
            String contact = rst.getString("contact");
            String country = rst.getString("country");
            customerList.add(new CustomerTO(id, firstName, lastName, contact, country));
        }
        return customerList;
    }

}