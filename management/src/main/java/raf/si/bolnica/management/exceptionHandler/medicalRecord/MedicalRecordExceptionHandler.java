package raf.si.bolnica.management.exceptionHandler.medicalRecord;

import org.springframework.stereotype.Component;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.exceptions.MissingRequestFieldsException;
import raf.si.bolnica.management.requests.UpdateMedicalRecordBloodTypeRhFactorRequestDTO;

import java.util.function.Consumer;

@Component
public class MedicalRecordExceptionHandler {

    public final Consumer<UpdateMedicalRecordBloodTypeRhFactorRequestDTO> validateFieldsForUpdatingBloodTypeRhFactor = (requestDTO) -> {
        if (requestDTO.getLbp() == null || requestDTO.getKrvnaGrupa() == null || requestDTO.getRhFaktor() == null)
            throw new MissingRequestFieldsException(Constants.MissingRequestFields.MESSAGE, Constants.MissingRequestFields.DEVELOPER_MESSAGE);
    };

}
