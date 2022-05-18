package raf.si.bolnica.management.requests;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
public class PreglediRequestDTO {

    private Date from;

    private Date to;

    private Date on;

}
