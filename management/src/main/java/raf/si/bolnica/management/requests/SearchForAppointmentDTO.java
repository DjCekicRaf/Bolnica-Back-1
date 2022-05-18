package raf.si.bolnica.management.requests;

import java.sql.Timestamp;
import java.util.UUID;

public class SearchForAppointmentDTO {

    private String lbz;

    private Timestamp date;

    public String getLbz() {
        return lbz;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setLbz(String lbz) {
        this.lbz = lbz;
    }
}
