package raf.si.bolnica.user.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.user.models.User;

import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDataResponseDTO {

    private String name;

    private String surname;

    private Date dob;

    private String gender;

    private String jmbg;

    private String address;

    private UUID lbz;

    private String city;

    private String contact;

    private String email;

    private String title;

    private String profession;

    private long department;

    private String username;

    public UserDataResponseDTO (User user) {
        contact = user.getKontaktTelefon();
        address = user.getAdresaStanovanja();
        city = user.getMestoStanovanja();
        department = user.getOdeljenje().getOdeljenjeId();
        dob = user.getDatumRodjenja();
        gender = user.getPol();
        jmbg = user.getJmbg();
        lbz = user.getLbz();
        profession = user.getZanimanje();
        email = user.getEmail();
        title = user.getTitula();
        name = user.getName();
        surname = user.getSurname();
        username = user.getKorisnickoIme();
    }
}
