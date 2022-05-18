package raf.si.bolnica.management.requests;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

public class CreateScheduledAppointmentRequestDTO {

    private Timestamp dateAndTimeOfAppointment;

    private String note;

    // Employee (doctor) that will be conducting examination
    private String lbz;

    private String lbp;

    public Timestamp getDateAndTimeOfAppointment() {
        return dateAndTimeOfAppointment;
    }

    public void setDateAndTimeOfAppointment(Timestamp dateAndTimeOfAppointment) {
        this.dateAndTimeOfAppointment = dateAndTimeOfAppointment;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLbp() {
        return lbp;
    }

    public void setLbp(String lbp) {
        this.lbp = lbp;
    }

    public String getLbz() {
        return lbz;
    }

    public void setLbz(String lbz) {
        this.lbz = lbz;
    }
}




