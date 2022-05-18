package raf.si.bolnica.management.requests;

import raf.si.bolnica.management.entities.Pacijent;

public class PacijentCRUDRequestDTO extends PacijentCRUDDTO {

    public void updatePacijentWithData(Pacijent pacijent) {
        pacijent.setAdresa(this.getAdresa());
        pacijent.setBracniStatus(this.getBracniStatus());
        pacijent.setBrojDece(this.getBrojDece());
        pacijent.setDatumRodjenja(this.getDatumRodjenja());
        pacijent.setEmail(this.getEmail());
        pacijent.setDatumVremeSmrti(this.getDatumVremeSmrti());
        pacijent.setIme(this.getIme());
        pacijent.setImeRoditelja(this.getImeRoditelja());
        pacijent.setPrezime(this.getPrezime());
        pacijent.setImeStaratelj(this.getImeStaratelj());
        pacijent.setJmbg(this.getJmbg());
        pacijent.setJmbgStaratelj(this.getJmbgStaratelj());
        pacijent.setKontaktTelefon(this.getKontaktTelefon());
        pacijent.setZemljaStanovanja(this.getZemljaStanovanja());
        pacijent.setZemljaDrzavljanstva(this.getZemljaDrzavljanstva());
        pacijent.setZanimanje(this.getZanimanje());
        pacijent.setStepenStrucneSpreme(this.getStepenStrucneSpreme());
        pacijent.setPorodicniStatus(this.getPorodicniStatus());
        pacijent.setPol(this.getPol());
        pacijent.setMestoStanovanja(this.getMestoStanovanja());
        pacijent.setMestoRodjenja(this.getMestoRodjenja());
    }
}
