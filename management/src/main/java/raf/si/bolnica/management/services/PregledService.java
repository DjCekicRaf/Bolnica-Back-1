package raf.si.bolnica.management.services;

import raf.si.bolnica.management.entities.Pregled;
import raf.si.bolnica.management.requests.CreatePregledReportRequestDTO;

public interface PregledService {

    Pregled savePregled(Pregled pregled);
}
