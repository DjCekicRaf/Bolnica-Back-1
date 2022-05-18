package raf.si.bolnica.management.exceptionHandler.medicalRecord;

import org.springframework.stereotype.Component;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.exceptions.MissingRequestFieldsException;
import raf.si.bolnica.management.requests.AddAllergentToPatientRequestDTO;

import java.util.function.Consumer;

@Component
public class AllergenRecordExceptionHandler {

    public final Consumer<AddAllergentToPatientRequestDTO> validateFieldsForAddingAllergenToPatient = (requestDTO) -> {
        if (requestDTO.getNaziv().isBlank())
            throw new MissingRequestFieldsException(Constants.MissingRequestFields.MESSAGE, Constants.MissingRequestFields.DEVELOPER_MESSAGE);
    };

}
