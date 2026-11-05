package es.deusto.ingenieria.sd.ecoembes.facade;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.deusto.ingenieria.sd.ecoembes.dto.LoginRequestDTO;
import es.deusto.ingenieria.sd.ecoembes.service.AuthService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/auth")
@RestController
@Tag(name = "Authentication", description = "Login and logout operations for Ecoembes employees")
public class EcoembesAuthController {

    private final AuthService authService;

    public EcoembesAuthController(AuthService authService) {
        this.authService = authService;
    }

    //POST login
    @Operation(
		summary = "Login", 
		description = "Authenticates a user and returns a token if the credentials are valid.",
		responses = {
			@ApiResponse(responseCode = "200", description = "Successfully authenticated"),
			@ApiResponse(responseCode = "401", description = "Unauthorized - invalid credentials")
		})
    
    @PostMapping("/login")
    public ResponseEntity<String> login(
    	@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Employee credentials (email and password)", required = true)
             @Valid @RequestBody LoginRequestDTO credentials) {
    	
        String token = authService.login(credentials.getEmail(), credentials.getPassword());

        if (token != null) {
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //POST logout
    @Operation(
    	summary = "Logout",
    	description = "Logs out a user by invalidating the provided token.",
    	responses = {
						@ApiResponse(responseCode = "204", description = "Successfully logged out"),
						@ApiResponse(responseCode = "401", description = "Unauthorized - invalid token")
					})
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
    		@Parameter(name="token", description = "Authentication token of the employee", required = true)
    		@RequestParam String token) {
    	
        boolean loggedOut = authService.logout(token);

        if (loggedOut) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}