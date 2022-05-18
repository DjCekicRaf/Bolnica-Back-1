package raf.si.bolnica.management.requests;

import org.springframework.http.HttpStatus;

public class PregledReportRequestValidator {

    private PregledReportRequestValidator() {}

    public static String validate(CreatePregledReportRequestDTO request) {
        if (request.getObjektivniNalaz() == null) return  "Objektivni nalaz je obavezno polje!";
        if (request.getLbp() == null) return  "LBP je obavezno polje!";
        if (request.getLbz() == null) return  "LBZ je obavezno polje!";
        return "OK";
    }
}
