package raf.si.bolnica.management.services.vakcina;

import raf.si.bolnica.management.entities.Vakcina;

public interface VakcinaService {

    Vakcina findVakcinaByNaziv(String naziv);

    Vakcina save(Vakcina vakcina);

}
