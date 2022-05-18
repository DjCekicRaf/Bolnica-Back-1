package raf.si.bolnica.management.response;

import lombok.Getter;
import lombok.Setter;
import raf.si.bolnica.management.entities.IstorijaBolesti;
import raf.si.bolnica.management.entities.enums.RezultatLecenja;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Date;

@Getter
public class IstorijaBolestiResponseDTO {

    private String dijagnoza;

    private Boolean indikatorPoverljivosti;

    private Date datumPocetkaZdravstvenogProblema;

    private Date datumZavrsetkaZdravstvenogProblema;

    private RezultatLecenja rezultatLecenja;

    private String opisTekucegStanja;

    private Date podatakValidanOd;

    private Date podatakValidanDo;

    private Boolean podaciValidni;

    private Boolean obrisan;

    public IstorijaBolestiResponseDTO(IstorijaBolesti i) {
        dijagnoza = i.getDijagnoza();
        indikatorPoverljivosti = i.getIndikatorPoverljivosti();
        datumPocetkaZdravstvenogProblema = i.getDatumPocetkaZdravstvenogProblema();
        datumZavrsetkaZdravstvenogProblema = i.getDatumZavrsetkaZdravstvenogProblema();
        rezultatLecenja = i.getRezultatLecenja();
        opisTekucegStanja = i.getOpisTekucegStanja();
        podatakValidanDo = i.getPodatakValidanDo();
        podatakValidanOd = i.getPodatakValidanOd();
        podaciValidni = i.getPodaciValidni();
        obrisan = i.getObrisan();
    }
}
