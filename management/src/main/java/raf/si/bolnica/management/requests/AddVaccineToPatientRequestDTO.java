package raf.si.bolnica.management.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddVaccineToPatientRequestDTO {

    private String lbp;

    private String naziv;

    private Date datumVakcinacije;

}
