package raf.si.bolnica.management.services;

import raf.si.bolnica.management.entities.Pacijent;
import raf.si.bolnica.management.entities.Pregled;
import raf.si.bolnica.management.entities.ZakazaniPregled;
import raf.si.bolnica.management.requests.CreateScheduledAppointmentRequestDTO;
import raf.si.bolnica.management.requests.UpdateAppointmentStatusDTO;
import raf.si.bolnica.management.requests.UpdateArrivalStatusDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;


public interface ScheduledAppointmentService {

        ZakazaniPregled saveAppointment(ZakazaniPregled appointment);
        List<ZakazaniPregled> getAppointmentByLBZ(UUID lbz) ;
        List<ZakazaniPregled> getAppointmentByLBZAndDate(UUID lbz, Timestamp date);
        ZakazaniPregled fetchById(long id);
}