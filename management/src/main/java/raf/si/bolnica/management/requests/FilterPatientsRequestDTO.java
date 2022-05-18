package raf.si.bolnica.management.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FilterPatientsRequestDTO {
    private String jmbg;
    private UUID lbp;
    private String ime;
    private String prezime;
}
