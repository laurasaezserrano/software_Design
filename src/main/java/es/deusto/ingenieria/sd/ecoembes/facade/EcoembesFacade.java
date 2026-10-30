package es.deusto.ingenieria.sd.ecoembes.facade;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.ingenieria.sd.ecoembes.dto.LoginRequestDTO;
import es.deusto.ingenieria.sd.ecoembes.dto.LoginResponseDTO;
import es.deusto.ingenieria.sd.ecoembes.service.EcoembesAppService;

@RestController
@RequestMapping("/api")
public class EcoembesFacade {

    private final EcoembesAppService appService;

    public EcoembesFacade(EcoembesAppService appService) {
        this.appService = appService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO request) {

        return ResponseEntity.ok(appService.login(request));
    }
}