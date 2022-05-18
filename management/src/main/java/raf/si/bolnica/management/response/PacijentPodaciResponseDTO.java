package raf.si.bolnica.management.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.management.entities.Alergen;
import raf.si.bolnica.management.entities.Vakcina;
import raf.si.bolnica.management.entities.enums.KrvnaGrupa;
import raf.si.bolnica.management.entities.enums.RhFaktor;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class PacijentPodaciResponseDTO {

    private KrvnaGrupa krvnaGrupa;

    private RhFaktor rhFaktor;

    Set<Vakcina> vakcine;

    Set<Alergen> alergeni;


}
