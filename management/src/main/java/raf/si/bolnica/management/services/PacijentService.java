package raf.si.bolnica.management.services;

import raf.si.bolnica.management.entities.Pacijent;

import java.util.UUID;

public interface PacijentService {

    Pacijent fetchPacijentByLbp(UUID lbp);

    Pacijent savePacijent(Pacijent pacijent);

    Pacijent fetchPacijentById(Long id);

    void deleteById(Long id);
}
