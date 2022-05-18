package raf.si.bolnica.management.entities;

import javax.persistence.*;

@Entity
public class Vakcina {

    @Id
    @Column(name = "vakcina_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long vakcinaId;

    @Column(nullable = false)
    private String naziv;

    @Column(nullable = false)
    private String tip;

    @Column(nullable = false)
    private String opis;

    @Column(nullable = false)
    private String proizvodjac;

    public long getVakcinaId() {
        return vakcinaId;
    }

    public void setVakcinaId(long vakcinaId) {
        this.vakcinaId = vakcinaId;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getProizvodjac() {
        return proizvodjac;
    }

    public void setProizvodjac(String proizvodjac) {
        this.proizvodjac = proizvodjac;
    }
}
