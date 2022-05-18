package raf.si.bolnica.management.response;

import lombok.Getter;
import lombok.Setter;
import raf.si.bolnica.management.entities.Pregled;

import java.sql.Date;

@Getter
public class PregledResponseDTO {

    private Date datumPregleda;

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

    private Boolean obrisan;

    public PregledResponseDTO(Pregled p) {
        datumPregleda = p.getDatumPregleda();
        indikatorPoverljivosti = p.getIndikatorPoverljivosti();
        glavneTegobe = p.getGlavneTegobe();
        sadasnjaBolest = p.getSadasnjaBolest();
        licnaAnamneza = p.getLicnaAnamneza();
        porodicnaAnamneza = p.getPorodicnaAnamneza();
        misljenjePacijenta = p.getMisljenjePacijenta();
        objektivniNalaz = p.getObjektivniNalaz();
        dijagnoza = p.getDijagnoza();
        predlozenaTerapija = p.getPredlozenaTerapija();
        savet = p.getSavet();
        obrisan = p.getObrisan();
    }
}
