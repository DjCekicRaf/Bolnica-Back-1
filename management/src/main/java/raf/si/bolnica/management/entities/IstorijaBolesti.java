package raf.si.bolnica.management.entities;

import raf.si.bolnica.management.entities.enums.RezultatLecenja;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class IstorijaBolesti {

    @Id
    @Column(name = "bolest_pacijenta_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bolestPacijentaId;

    @Column(nullable = false)
    private String dijagnoza;

    private Boolean indikatorPoverljivosti = false;

    @Column(nullable = false)
    private Date datumPocetkaZdravstvenogProblema;

    private Date datumZavrsetkaZdravstvenogProblema;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RezultatLecenja rezultatLecenja;

    @Column(nullable = false)
    private String opisTekucegStanja;

    @Column(nullable = false)
    private Date podatakValidanOd;

    @Column(nullable = false)
    private Date podatakValidanDo;

    @Column(nullable = false)
    private Boolean podaciValidni;

    private Boolean obrisan = false;

    // FKs
    @ManyToOne
    @JoinColumn(name = "zdravstveni_karton_id")
    private ZdravstveniKarton zdravstveniKarton;

    public long getBolestPacijentaId() {
        return bolestPacijentaId;
    }

    public void setBolestPacijentaId(long bolestPacijentaId) {
        this.bolestPacijentaId = bolestPacijentaId;
    }

    public String getDijagnoza() {
        return dijagnoza;
    }

    public void setDijagnoza(String dijagnoza) {
        this.dijagnoza = dijagnoza;
    }

    public Boolean getIndikatorPoverljivosti() {
        return indikatorPoverljivosti;
    }

    public void setIndikatorPoverljivosti(Boolean indikatorPoverljivosti) {
        this.indikatorPoverljivosti = indikatorPoverljivosti;
    }

    public Date getDatumPocetkaZdravstvenogProblema() {
        return datumPocetkaZdravstvenogProblema;
    }

    public void setDatumPocetkaZdravstvenogProblema(Date datumPocetkaZdravstvenogProblema) {
        this.datumPocetkaZdravstvenogProblema = datumPocetkaZdravstvenogProblema;
    }

    public Date getDatumZavrsetkaZdravstvenogProblema() {
        return datumZavrsetkaZdravstvenogProblema;
    }

    public void setDatumZavrsetkaZdravstvenogProblema(Date datumZavrsetkaZdravstvenogProblema) {
        this.datumZavrsetkaZdravstvenogProblema = datumZavrsetkaZdravstvenogProblema;
    }

    public RezultatLecenja getRezultatLecenja() {
        return rezultatLecenja;
    }

    public void setRezultatLecenja(RezultatLecenja rezultatLecenja) {
        this.rezultatLecenja = rezultatLecenja;
    }

    public String getOpisTekucegStanja() {
        return opisTekucegStanja;
    }

    public void setOpisTekucegStanja(String opisTekucegStanja) {
        this.opisTekucegStanja = opisTekucegStanja;
    }

    public Date getPodatakValidanOd() {
        return podatakValidanOd;
    }

    public void setPodatakValidanOd(Date podatakValidanOd) {
        this.podatakValidanOd = podatakValidanOd;
    }

    public Date getPodatakValidanDo() {
        return podatakValidanDo;
    }

    public void setPodatakValidanDo(Date podatakValidanDo) {
        this.podatakValidanDo = podatakValidanDo;
    }

    public Boolean getPodaciValidni() {
        return podaciValidni;
    }

    public void setPodaciValidni(Boolean podaciValidni) {
        this.podaciValidni = podaciValidni;
    }

    public Boolean getObrisan() {
        return obrisan;
    }

    public void setObrisan(Boolean obrisan) {
        this.obrisan = obrisan;
    }
}
