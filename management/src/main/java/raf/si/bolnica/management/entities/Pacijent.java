package raf.si.bolnica.management.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;
import raf.si.bolnica.management.entities.enums.*;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class Pacijent {

    @Id
    @Column(name = "pacijent_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pacijentId;

    @Column(nullable = false, unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID lbp;

    @Column(nullable = false)
    private String ime;

    @Column(nullable = false)
    private String prezime;

    @Column(nullable = false)
    private Date datumRodjenja;

    @Column(nullable = false)
    private String imeRoditelja;

    @Column(nullable = false, unique = true)
    private String jmbg;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Pol pol;

    @Column(nullable = false)
    private String mestoRodjenja;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CountryCode zemljaDrzavljanstva;

    private Timestamp datumVremeSmrti;

    private String adresa;

    private String mestoStanovanja;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CountryCode zemljaStanovanja;

    private String kontaktTelefon;

    @Column(unique = true)
    private String email;

    private String jmbgStaratelj;

    private String imeStaratelj;

    @Enumerated(EnumType.STRING)
    private PorodicniStatus porodicniStatus;

    @Enumerated(EnumType.STRING)
    private BracniStatus bracniStatus;

    private Integer brojDece;

    @Enumerated(EnumType.STRING)
    private StrucnaSprema stepenStrucneSpreme;

    private String zanimanje;

    private Boolean obrisan = false;

    // FKs
    @OneToOne(mappedBy = "pacijent")
    @JsonIgnore
    private ZdravstveniKarton zdravstveniKarton;

    public long getPacijentId() {
        return pacijentId;
    }

    public void setPacijentId(long pacijentId) {
        this.pacijentId = pacijentId;
    }

    public UUID getLbp() {
        return lbp;
    }

    public void setLbp(UUID lbp) {
        this.lbp = lbp;
    }

    public ZdravstveniKarton getZdravstveniKarton() {
        return zdravstveniKarton;
    }

    public void setZdravstveniKarton(ZdravstveniKarton zdravstveniKarton) {
        this.zdravstveniKarton = zdravstveniKarton;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public Date getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(Date datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public String getImeRoditelja() {
        return imeRoditelja;
    }

    public void setImeRoditelja(String imeRoditelja) {
        this.imeRoditelja = imeRoditelja;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public Pol getPol() {
        return pol;
    }

    public void setPol(Pol pol) {
        this.pol = pol;
    }

    public String getMestoRodjenja() {
        return mestoRodjenja;
    }

    public void setMestoRodjenja(String mestoRodjenja) {
        this.mestoRodjenja = mestoRodjenja;
    }

    public CountryCode getZemljaDrzavljanstva() {
        return zemljaDrzavljanstva;
    }

    public void setZemljaDrzavljanstva(CountryCode zemljaDrzavljanstva) {
        this.zemljaDrzavljanstva = zemljaDrzavljanstva;
    }

    public Timestamp getDatumVremeSmrti() {
        return datumVremeSmrti;
    }

    public void setDatumVremeSmrti(Timestamp datumVremeSmrti) {
        this.datumVremeSmrti = datumVremeSmrti;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getMestoStanovanja() {
        return mestoStanovanja;
    }

    public void setMestoStanovanja(String mestoStanovanja) {
        this.mestoStanovanja = mestoStanovanja;
    }

    public CountryCode getZemljaStanovanja() {
        return zemljaStanovanja;
    }

    public void setZemljaStanovanja(CountryCode zemljaStanovanja) {
        this.zemljaStanovanja = zemljaStanovanja;
    }

    public String getKontaktTelefon() {
        return kontaktTelefon;
    }

    public void setKontaktTelefon(String kontaktTelefon) {
        this.kontaktTelefon = kontaktTelefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJmbgStaratelj() {
        return jmbgStaratelj;
    }

    public void setJmbgStaratelj(String jmbgStaratelj) {
        this.jmbgStaratelj = jmbgStaratelj;
    }

    public String getImeStaratelj() {
        return imeStaratelj;
    }

    public void setImeStaratelj(String imeStaratelj) {
        this.imeStaratelj = imeStaratelj;
    }

    public PorodicniStatus getPorodicniStatus() {
        return porodicniStatus;
    }

    public void setPorodicniStatus(PorodicniStatus porodicniStatus) {
        this.porodicniStatus = porodicniStatus;
    }

    public BracniStatus getBracniStatus() {
        return bracniStatus;
    }

    public void setBracniStatus(BracniStatus bracniStatus) {
        this.bracniStatus = bracniStatus;
    }

    public Integer getBrojDece() {
        return brojDece;
    }

    public void setBrojDece(Integer brojDece) {
        this.brojDece = brojDece;
    }

    public StrucnaSprema getStepenStrucneSpreme() {
        return stepenStrucneSpreme;
    }

    public void setStepenStrucneSpreme(StrucnaSprema stepenStrucneSpreme) {
        this.stepenStrucneSpreme = stepenStrucneSpreme;
    }

    public String getZanimanje() {
        return zanimanje;
    }

    public void setZanimanje(String zanimanje) {
        this.zanimanje = zanimanje;
    }

    public Boolean getObrisan() {
        return obrisan;
    }

    public void setObrisan(Boolean obrisan) {
        this.obrisan = obrisan;
    }
}
