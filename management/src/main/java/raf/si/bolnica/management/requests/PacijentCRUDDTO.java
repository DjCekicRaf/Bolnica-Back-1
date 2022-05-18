package raf.si.bolnica.management.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.management.entities.enums.*;

import java.sql.Date;
import java.sql.Timestamp;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacijentCRUDDTO {

    protected String jmbg;

    protected String ime;

    protected String imeRoditelja;

    protected String prezime;

    protected Pol pol;

    protected Date datumRodjenja;

    protected Timestamp datumVremeSmrti;

    protected String mestoRodjenja;

    protected CountryCode zemljaDrzavljanstva;

    protected String adresa;

    protected String mestoStanovanja;

    protected CountryCode zemljaStanovanja;

    protected String kontaktTelefon;

    protected String email;

    protected String jmbgStaratelj;

    protected String imeStaratelj;

    protected PorodicniStatus porodicniStatus;

    protected BracniStatus bracniStatus;

    protected Integer brojDece;

    protected StrucnaSprema stepenStrucneSpreme;

    protected String zanimanje;

}
