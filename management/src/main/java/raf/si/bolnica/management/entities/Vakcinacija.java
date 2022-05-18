package raf.si.bolnica.management.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vakcinacija {

    @EmbeddedId
    private VakcinacijaKey id;

    @ManyToOne
    @MapsId("vakcinaId")
    @JoinColumn(name = "vakcina_id")
    private Vakcina vakcina;

    @ManyToOne
    @MapsId("zdravstveniKartonId")
    @JoinColumn(name = "zdravstveni_karton_id")
    private ZdravstveniKarton zdravstveniKarton;

    @Column(nullable = false)
    private Date datumVakcinacije;

    @Column(nullable = false)
    private Boolean obrisan = false;


    public VakcinacijaKey getId() {
        return id;
    }

    public void setId(VakcinacijaKey id) {
        this.id = id;
    }

    public Vakcina getVakcina() {
        return vakcina;
    }

    public void setVakcina(Vakcina vakcina) {
        this.vakcina = vakcina;
    }

    public ZdravstveniKarton getZdravstveniKarton() {
        return zdravstveniKarton;
    }

    public void setZdravstveniKarton(ZdravstveniKarton zdravstveniKarton) {
        this.zdravstveniKarton = zdravstveniKarton;
    }

    public Date getDatumVakcinacije() {
        return datumVakcinacije;
    }

    public void setDatumVakcinacije(Date datumVakcinacije) {
        this.datumVakcinacije = datumVakcinacije;
    }

    public Boolean getObrisan() {
        return obrisan;
    }

    public void setObrisan(Boolean obrisan) {
        this.obrisan = obrisan;
    }
}
