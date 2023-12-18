package lk.ijse.dep11.app.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTO implements Serializable {
    private Integer id;
    private String firstName;
    private String lastName;
    private String contact;
    private String country;
}