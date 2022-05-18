package raf.si.bolnica.management.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlergenZdravstveniKarton {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "alergen_id")
    private Alergen alergen;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "zdravstveni_karton_id")
    private ZdravstveniKarton zdravstveniKarton;

    @Column(nullable = false)
    private Boolean obrisan;

    public Long getId() {
        return id;
    }

    public Alergen getAlergen() {
        return alergen;
    }

    public ZdravstveniKarton getZdravstveniKarton() {
        return zdravstveniKarton;
    }

    public Boolean getObrisan() {
        return obrisan;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAlergen(Alergen alergen) {
        this.alergen = alergen;
    }

    public void setZdravstveniKarton(ZdravstveniKarton zdravstveniKarton) {
        this.zdravstveniKarton = zdravstveniKarton;
    }

    public void setObrisan(Boolean obrisan) {
        this.obrisan = obrisan;
    }
}
