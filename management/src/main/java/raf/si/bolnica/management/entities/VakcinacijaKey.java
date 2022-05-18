package raf.si.bolnica.management.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VakcinacijaKey implements Serializable {

    @Column(name = "vakcina_id")
    private long vakcinaId;

    @Column(name = "zdravstveni_karton_id")
    private long zdravstveniKartonId;


    public long getVakcinaId() {
        return vakcinaId;
    }

    public void setVakcinaId(long vakcinaId) {
        this.vakcinaId = vakcinaId;
    }

    public long getZdravstveniKartonId() {
        return zdravstveniKartonId;
    }

    public void setZdravstveniKartonId(long zdravstveniKartonId) {
        this.zdravstveniKartonId = zdravstveniKartonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VakcinacijaKey that = (VakcinacijaKey) o;
        return vakcinaId == that.vakcinaId && zdravstveniKartonId == that.zdravstveniKartonId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vakcinaId, zdravstveniKartonId);
    }
}
