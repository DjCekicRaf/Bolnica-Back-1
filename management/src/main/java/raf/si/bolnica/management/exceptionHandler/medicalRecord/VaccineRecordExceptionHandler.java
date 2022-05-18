package raf.si.bolnica.management.exceptionHandler.medicalRecord;

import org.springframework.stereotype.Component;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.exceptions.MissingRequestFieldsException;
import raf.si.bolnica.management.requests.AddVaccineToPatientRequestDTO;

import java.util.function.Consumer;

@Component
public class VaccineRecordExceptionHandler {

    public final Consumer<AddVaccineToPatientRequestDTO> validateFieldsForAddingVaccineToPatient = (requestDTO) -> {
        if (requestDTO.getLbp() == null || requestDTO.getNaziv() == null || requestDTO.getDatumVakcinacije() == null)
            throw new MissingRequestFieldsException(Constants.MissingRequestFields.MESSAGE, Constants.MissingRequestFields.DEVELOPER_MESSAGE);
    };

}
