package raf.si.bolnica.management.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.management.entities.enums.KrvnaGrupa;
import raf.si.bolnica.management.entities.enums.RhFaktor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMedicalRecordBloodTypeRhFactorRequestDTO {

    private String lbp;

    private KrvnaGrupa krvnaGrupa;

    private RhFaktor rhFaktor;

}
