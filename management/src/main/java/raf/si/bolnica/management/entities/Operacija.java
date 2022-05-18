package raf.si.bolnica.management.entities;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Operacija {

    @Id
    @Column(name = "operacija_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long operacijaId;

    @Column(nullable = false)
    private Date datumOperacije;

    @Column(nullable = false)
    private String opis;

    private Boolean obrisan = false;

    // FKs
    @ManyToOne
    @JoinColumn(name = "zdravstveni_karton_id", nullable = false)
    private ZdravstveniKarton zdravstveniKarton;

    @Column(nullable = false)
    private long odeljenjeId;

    public long getOperacijaId() {
        return operacijaId;
    }

    public void setOperacijaId(long operacijaId) {
        this.operacijaId = operacijaId;
    }

    public long getOdeljenjeId() {
        return odeljenjeId;
    }

    public void setOdeljenjeId(long odeljenjeId) {
        this.odeljenjeId = odeljenjeId;
    }

    public Date getDatumOperacije() {
        return datumOperacije;
    }

    public void setDatumOperacije(Date datumOperacije) {
        this.datumOperacije = datumOperacije;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
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
}
