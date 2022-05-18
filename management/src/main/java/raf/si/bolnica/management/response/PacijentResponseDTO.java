package raf.si.bolnica.management.response;

import lombok.Getter;
import raf.si.bolnica.management.requests.PacijentCRUDDTO;
import raf.si.bolnica.management.entities.Pacijent;

import java.util.UUID;

@Getter
public class PacijentResponseDTO extends PacijentCRUDDTO {

    private UUID lbp;

    public PacijentResponseDTO(Pacijent pacijent) {
        lbp = pacijent.getLbp();
        adresa = pacijent.getAdresa();
        bracniStatus = pacijent.getBracniStatus();
        brojDece = pacijent.getBrojDece();
        datumRodjenja = pacijent.getDatumRodjenja();
        email = pacijent.getEmail();
        datumVremeSmrti = pacijent.getDatumVremeSmrti();
        ime = pacijent.getIme();
        imeRoditelja = pacijent.getImeRoditelja();
        prezime = pacijent.getPrezime();
        imeStaratelj = pacijent.getImeStaratelj();
        jmbg = pacijent.getJmbg();
        jmbgStaratelj = pacijent.getJmbgStaratelj();
        kontaktTelefon = pacijent.getKontaktTelefon();
        zemljaStanovanja = pacijent.getZemljaStanovanja();
        zemljaDrzavljanstva = pacijent.getZemljaDrzavljanstva();
        zanimanje = pacijent.getZanimanje();
        stepenStrucneSpreme = pacijent.getStepenStrucneSpreme();
        porodicniStatus = pacijent.getPorodicniStatus();
        pol = pacijent.getPol();
        mestoStanovanja = pacijent.getMestoStanovanja();
        mestoRodjenja = pacijent.getMestoRodjenja();
    }
}
