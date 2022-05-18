package raf.si.bolnica.management.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;
import raf.si.bolnica.management.entities.enums.PrispecePacijenta;
import raf.si.bolnica.management.entities.enums.StatusPregleda;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class ZakazaniPregled {

    @Id
    @Column(name = "zakazani_pregled_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long zakazaniPregledId;

    @Column(nullable = false)
    private Timestamp datumIVremePregleda;

    @Enumerated(EnumType.STRING)
    private StatusPregleda statusPregleda = StatusPregleda.ZAKAZANO;

    @Enumerated(EnumType.STRING)
    private PrispecePacijenta prispecePacijenta = PrispecePacijenta.NIJE_DOSAO;

    private String Napomena;

    //FKs
    @Column(nullable = false)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID lbzLekara;

    @Column(nullable = false)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID lbzSestre;

    @ManyToOne
    @JoinColumn(name = "pacijent_id")
    private Pacijent pacijent;

    public UUID getLbzLekara() {
        return lbzLekara;
    }

    public UUID getLbzSestre() {
        return lbzSestre;
    }

    public long getZakazaniPregledId() {
        return zakazaniPregledId;
    }

    public Pacijent getPacijent() {
        return pacijent;
    }

    public PrispecePacijenta getPrispecePacijenta() {
        return prispecePacijenta;
    }

    public StatusPregleda getStatusPregleda() {
        return statusPregleda;
    }

    public String getNapomena() {
        return Napomena;
    }

    public Timestamp getDatumIVremePregleda() {
        return datumIVremePregleda;
    }

    public void setDatumIVremePregleda(Timestamp datumIVremePregleda) {
        this.datumIVremePregleda = datumIVremePregleda;
    }

    public void setLbzLekara(UUID lbzLekara) {
        this.lbzLekara = lbzLekara;
    }

    public void setLbzSestre(UUID lbzSestre) {
        this.lbzSestre = lbzSestre;
    }

    public void setNapomena(String napomena) {
        Napomena = napomena;
    }

    public void setPacijent(Pacijent pacijent) {
        this.pacijent = pacijent;
    }

    public void setPrispecePacijenta(PrispecePacijenta prispecePacijenta) {
        this.prispecePacijenta = prispecePacijenta;
    }

    public void setZakazaniPregledId(long zakazaniPregledId) {
        this.zakazaniPregledId = zakazaniPregledId;
    }

    public void setStatusPregleda(StatusPregleda statusPregleda) {
        this.statusPregleda = statusPregleda;
    }
    
}
