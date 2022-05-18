package raf.si.bolnica.user.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeRequestDTO {
    private String name;

    private String surname;

    private Date dob;

    private String gender;

    private String jmbg;

    private UUID lbz;

    private String address;

    private String city;

    private String contact;

    private String title;

    private String profession;

    private long department;

    private String oldPassword;

    private String newPassword;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
