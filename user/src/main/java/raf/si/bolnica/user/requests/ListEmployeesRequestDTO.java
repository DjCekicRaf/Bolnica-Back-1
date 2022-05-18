package raf.si.bolnica.user.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListEmployeesRequestDTO {

    private String name;

    private String surname;

    private Long department;

    private Long hospital;

    private Boolean obrisan;
}
