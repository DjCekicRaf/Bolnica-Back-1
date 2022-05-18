package raf.si.bolnica.management.entities;


import javax.persistence.*;
import java.util.Set;

@Entity
public class Alergen {

    @Id
    @Column(name = "alergen_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long alergenId;

    @Column(nullable = false)
    private String naziv;

    @OneToMany(mappedBy = "alergen", cascade = CascadeType.ALL)
    private Set<AlergenZdravstveniKarton> alergenZdravstveniKarton;

    public Alergen(){

    }
    public Alergen(String naziv){
        this.naziv = naziv;
    }

    public long getAlergenId() {
        return alergenId;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setAlergenId(long alergenId) {
        this.alergenId = alergenId;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
}
