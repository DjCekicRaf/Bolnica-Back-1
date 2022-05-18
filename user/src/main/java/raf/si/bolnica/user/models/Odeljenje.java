package raf.si.bolnica.user.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
public class Odeljenje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long odeljenjeId;

    @Column(nullable = false)
    private long poslovniBrojOdeljenja;

    @Column(nullable = false)
    private String naziv;

    @ManyToOne
    private ZdravstvenaUstanova bolnica;

    @OneToMany(mappedBy = "odeljenje")
    @JsonIgnore
    private List<User> zaposleni;

    @Column(nullable = true)
    private boolean obrisan = false;

    public Odeljenje(){

    }

    public long getOdeljenjeId() {
        return odeljenjeId;
    }

    public void setOdeljenjeId(long odeljenjeId) {
        this.odeljenjeId = odeljenjeId;
    }

    public long getPoslovniBrojOdeljenja() {
        return poslovniBrojOdeljenja;
    }

    public void setPoslovniBrojOdeljenja(long poslovniBrojOdeljenja) {
        this.poslovniBrojOdeljenja = poslovniBrojOdeljenja;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public ZdravstvenaUstanova getBolnica() {
        return bolnica;
    }

    public void setBolnica(ZdravstvenaUstanova bolnica) {
        this.bolnica = bolnica;
    }

    public List<User> getZaposleni() {
        return zaposleni;
    }

    public void setZaposleni(List<User> zaposleni) {
        this.zaposleni = zaposleni;
    }

    public boolean isObrisan() {
        return obrisan;
    }

    public void setObrisan(boolean obrisan) {
        this.obrisan = obrisan;
    }
}
