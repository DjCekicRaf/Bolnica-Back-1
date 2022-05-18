package raf.si.bolnica.gateway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import raf.si.bolnica.gateway.constants.MicroserviceConstants;
import raf.si.bolnica.gateway.responses.UserResponseDTO;

import java.util.Optional;

import static raf.si.bolnica.gateway.constants.MicroserviceConstants.BASE_API;
import static raf.si.bolnica.gateway.constants.MicroserviceConstants.USER_MICROSERVICE;

@FeignClient(name = USER_MICROSERVICE)
@Service
@RequestMapping(value = BASE_API)
public interface UserInterface {

    @RequestMapping(value = MicroserviceConstants.UserMicroServiceConstants.FETCH_USER_BY_USERNAME)
    Optional<UserResponseDTO> fetchUserByUsername(@RequestParam String username);
}