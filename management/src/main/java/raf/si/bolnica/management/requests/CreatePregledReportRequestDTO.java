package raf.si.bolnica.management.requests;

import lombok.Data;
import raf.si.bolnica.management.entities.enums.RezultatLecenja;
import java.util.UUID;

@Data
public class CreatePregledReportRequestDTO {

    private String lbp;

    private String lbz;

    private Boolean indikatorPoverljivosti;

    private String glavneTegobe;

    private String sadasnjaBolest;

    private String licnaAnamneza;

    private String porodicnaAnamneza;

    private String misljenjePacijenta;

    private String objektivniNalaz;

    private String dijagnoza;

    private String predlozenaTerapija;

    private String savet;

    private RezultatLecenja rezultatLecenja;

    private String opisTekucegStanja;
}
