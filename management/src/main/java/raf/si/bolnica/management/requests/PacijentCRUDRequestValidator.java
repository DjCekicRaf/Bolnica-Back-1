package raf.si.bolnica.management.requests;

public class PacijentCRUDRequestValidator {

    private PacijentCRUDRequestValidator() {
    }

    public static String checkValid(PacijentCRUDRequestDTO request) {
        if (request.getJmbg() == null) return "JMBG je obavezno polje!";
        if (request.getIme() == null) return "Ime je obavezno polje!";
        if (request.getImeRoditelja() == null) return "Ime roditelja je obavezno polje!";
        if (request.getPrezime() == null) return "Prezime je obavezno polje!";
        if (request.getPol() == null) return "Pol je obavezno polje!";
        if (request.getDatumRodjenja() == null) return "Datum rodjenja je obavezno polje!";
        if (request.getMestoRodjenja() == null) return "Mesto rodjenja je obavezno polje!";
        if (request.getZemljaDrzavljanstva() == null) return "Zemlja drzavljanstva je obavezno polje!";
        if (request.getZemljaStanovanja() == null) return "Zemlja stanovanja je obavezno polje!";
        return "ok";
    }
}
