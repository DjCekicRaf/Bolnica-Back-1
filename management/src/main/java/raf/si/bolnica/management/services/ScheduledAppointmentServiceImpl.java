package raf.si.bolnica.management.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raf.si.bolnica.management.entities.Pacijent;
import raf.si.bolnica.management.entities.ZakazaniPregled;
import raf.si.bolnica.management.entities.enums.PrispecePacijenta;
import raf.si.bolnica.management.entities.enums.StatusPregleda;
import raf.si.bolnica.management.repositories.ScheduledAppointmentRepository;
import raf.si.bolnica.management.requests.CreateScheduledAppointmentRequestDTO;
import raf.si.bolnica.management.requests.UpdateAppointmentStatusDTO;
import raf.si.bolnica.management.requests.UpdateArrivalStatusDTO;
import raf.si.bolnica.management.services.ScheduledAppointmentService;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ScheduledAppointmentServiceImpl implements ScheduledAppointmentService {

    @Autowired
    private ScheduledAppointmentRepository scheduledAppointmentRepository;

    @Override
    public ZakazaniPregled saveAppointment(ZakazaniPregled appointment) {
        return scheduledAppointmentRepository.save(appointment);
    }

    @Override
    public List<ZakazaniPregled> getAppointmentByLBZ(UUID lbz) {
        return scheduledAppointmentRepository.findByLbzLekara(lbz);
    }

    @Override
    public ZakazaniPregled fetchById(long id) {
        return scheduledAppointmentRepository.getZakazaniPregledByZakazaniPregledId(id);
    }

    @Override
    public List<ZakazaniPregled> getAppointmentByLBZAndDate(UUID lbz, Timestamp date) {
        return scheduledAppointmentRepository.findByLbzLekaraAndAndDatumIVremePregleda(lbz, date);
    }

}