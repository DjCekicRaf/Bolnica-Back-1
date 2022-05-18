package raf.si.bolnica.management.services.alergen;

import raf.si.bolnica.management.entities.Alergen;

public interface AlergenService {

    Alergen findAlergenByNaziv(String naziv);

    Alergen saveAlergen(Alergen alergen);

}
