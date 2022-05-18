package raf.si.bolnica.user.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
public class ZdravstvenaUstanova {
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long zdravstvenaUstanovaId;

    @Column(nullable = false, unique = true)
    private long poslovniBrojBolnice;

    @Column(nullable = false)
    private String naziv;

    @Column(nullable = false)
    private String skracenNaziv;

    @Column(nullable = false)
    private String mesto;

    @Column(nullable = false)
    private String adresa;

    @Column(nullable = false)
    private String delatnost;

    @OneToMany(mappedBy = "bolnica")
    @JsonIgnore
    private List<Odeljenje> odeljenja;

    @Column(nullable = true)
    private boolean obrisan = false;

    @Column(nullable = false)
    private Date datumOsnivanja;

    public ZdravstvenaUstanova(){

    }

    public long getZdravstvenaUstanovaId() {
        return zdravstvenaUstanovaId;
    }

    public void setZdravstvenaUstanovaId(long zdravstvenaUstanovaId) {
        this.zdravstvenaUstanovaId = zdravstvenaUstanovaId;
    }

    public long getPoslovniBrojBolnice() {
        return poslovniBrojBolnice;
    }

    public void setPoslovniBrojBolnice(long poslovniBrojBolnice) {
        this.poslovniBrojBolnice = poslovniBrojBolnice;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getSkracenNaziv() {
        return skracenNaziv;
    }

    public void setSkracenNaziv(String skracenNaziv) {
        this.skracenNaziv = skracenNaziv;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getDelatnost() {
        return delatnost;
    }

    public void setDelatnost(String delatnost) {
        this.delatnost = delatnost;
    }

    public List<Odeljenje> getOdeljenja() {
        return odeljenja;
    }

    public void setOdeljenja(List<Odeljenje> odeljenja) {
        this.odeljenja = odeljenja;
    }

    public boolean isObrisan() {
        return obrisan;
    }

    public void setObrisan(boolean obrisan) {
        this.obrisan = obrisan;
    }

    public Date getDatumOsnivanja() {
        return datumOsnivanja;
    }

    public void setDatumOsnivanja(Date datumOsnivanja) {
        this.datumOsnivanja = datumOsnivanja;
    }
}
