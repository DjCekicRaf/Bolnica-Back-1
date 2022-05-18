package raf.si.bolnica.management.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import raf.si.bolnica.management.entities.enums.KrvnaGrupa;
import raf.si.bolnica.management.entities.enums.RhFaktor;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
public class ZdravstveniKarton {

    @Id
    @Column(name = "zdravstveni_karton_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long zdravstveniKartonId;

    @Column(nullable = false)
    private Date datumRegistracije;

    @Enumerated(EnumType.STRING)
    private KrvnaGrupa krvnaGrupa;

    @Enumerated(EnumType.STRING)
    private RhFaktor rhFaktor;

    private Boolean obrisan = false;

    @OneToMany(mappedBy = "zdravstveniKarton")
    @JsonIgnore
    private Set<Operacija> operacije;

    @OneToMany(mappedBy = "zdravstveniKarton")
    @JsonIgnore
    private Set<Pregled> pregledi;

    @OneToMany(mappedBy = "zdravstveniKarton", cascade = CascadeType.ALL)
    @JsonIgnore
    Set<AlergenZdravstveniKarton> alergenZdravstveniKarton;

    // FKs
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "pacijent")
    private Pacijent pacijent;

    public long getZdravstveniKartonId() {
        return zdravstveniKartonId;
    }

    public void setZdravstveniKartonId(long zdravstveniKartonId) {
        this.zdravstveniKartonId = zdravstveniKartonId;
    }

    public Pacijent getPacijent() {
        return pacijent;
    }

    public void setPacijent(Pacijent pacijent) {
        this.pacijent = pacijent;
    }

    public Date getDatumRegistracije() {
        return datumRegistracije;
    }

    public void setDatumRegistracije(Date datumRegistracije) {
        this.datumRegistracije = datumRegistracije;
    }

    public KrvnaGrupa getKrvnaGrupa() {
        return krvnaGrupa;
    }

    public void setKrvnaGrupa(KrvnaGrupa krvnaGrupa) {
        this.krvnaGrupa = krvnaGrupa;
    }

    public RhFaktor getRhFaktor() {
        return rhFaktor;
    }

    public void setRhFaktor(RhFaktor rhFaktor) {
        this.rhFaktor = rhFaktor;
    }

    public Boolean getObrisan() {
        return obrisan;
    }

    public void setObrisan(Boolean obrisan) {
        this.obrisan = obrisan;
    }

    public Set<Operacija> getOperacije() {
        return operacije;
    }

    public void setOperacije(Set<Operacija> operacije) {
        this.operacije = operacije;
    }

    public void setPregledi(Set<Pregled> pregledi) {
        this.pregledi = pregledi;
    }

    public Set<Pregled> getPregledi() {
        return pregledi;
    }
}
