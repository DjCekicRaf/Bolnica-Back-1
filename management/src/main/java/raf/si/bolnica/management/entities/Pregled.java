package raf.si.bolnica.management.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Entity
public class Pregled {

    @Id
    @Column(name = "pregled_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pregledId;

    @Column(nullable = false)
    private Date datumPregleda;

    private Boolean indikatorPoverljivosti = false;

    private String glavneTegobe;

    private String sadasnjaBolest;

    private String licnaAnamneza;

    private String porodicnaAnamneza;

    private String misljenjePacijenta;

    @Column(nullable = false)
    private String objektivniNalaz;

    private String dijagnoza;

    private String predlozenaTerapija;

    private String savet;

    private Boolean obrisan = false;

    // FKs
    @ManyToOne
    @JoinColumn(name = "zdravstveni_karton_id")
    private ZdravstveniKarton zdravstveniKarton;

    @Column(nullable = false)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID lbz;

    public long getPregledId() {
        return pregledId;
    }

    public void setPregledId(long pregledId) {
        this.pregledId = pregledId;
    }

    public Date getDatumPregleda() {
        return datumPregleda;
    }

    public void setDatumPregleda(Date datumPregleda) {
        this.datumPregleda = datumPregleda;
    }

    public Boolean getIndikatorPoverljivosti() {
        return indikatorPoverljivosti;
    }

    public void setIndikatorPoverljivosti(Boolean indikatorPoverljivosti) {
        this.indikatorPoverljivosti = indikatorPoverljivosti;
    }

    public String getGlavneTegobe() {
        return glavneTegobe;
    }

    public void setGlavneTegobe(String glavneTegobe) {
        this.glavneTegobe = glavneTegobe;
    }

    public String getSadasnjaBolest() {
        return sadasnjaBolest;
    }

    public void setSadasnjaBolest(String sadasnjaBolest) {
        this.sadasnjaBolest = sadasnjaBolest;
    }

    public String getLicnaAnamneza() {
        return licnaAnamneza;
    }

    public void setLicnaAnamneza(String licnaAnamneza) {
        this.licnaAnamneza = licnaAnamneza;
    }

    public String getPorodicnaAnamneza() {
        return porodicnaAnamneza;
    }

    public void setPorodicnaAnamneza(String porodicnaAnamneza) {
        this.porodicnaAnamneza = porodicnaAnamneza;
    }

    public String getMisljenjePacijenta() {
        return misljenjePacijenta;
    }

    public void setMisljenjePacijenta(String misljenjePacijenta) {
        this.misljenjePacijenta = misljenjePacijenta;
    }

    public String getObjektivniNalaz() {
        return objektivniNalaz;
    }

    public void setObjektivniNalaz(String objektivniNalaz) {
        this.objektivniNalaz = objektivniNalaz;
    }

    public String getDijagnoza() {
        return dijagnoza;
    }

    public void setDijagnoza(String dijagnoza) {
        this.dijagnoza = dijagnoza;
    }

    public String getPredlozenaTerapija() {
        return predlozenaTerapija;
    }

    public void setPredlozenaTerapija(String predlozenaTerapija) {
        this.predlozenaTerapija = predlozenaTerapija;
    }

    public String getSavet() {
        return savet;
    }

    public void setSavet(String savet) {
        this.savet = savet;
    }

    public Boolean getObrisan() {
        return obrisan;
    }

    public void setObrisan(Boolean obrisan) {
        this.obrisan = obrisan;
    }

    public ZdravstveniKarton getZdravstveniKarton() {
        return zdravstveniKarton;
    }

    public void setZdravstveniKarton(ZdravstveniKarton zdravstveniKarton) {
        this.zdravstveniKarton = zdravstveniKarton;
    }

    public UUID getLbz() {
        return lbz;
    }

    public void setLbz(UUID lbz) {
        this.lbz = lbz;
    }
}
