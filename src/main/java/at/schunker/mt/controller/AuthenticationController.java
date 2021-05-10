package at.schunker.mt.controller;

import at.schunker.mt.dao.CredentialsDAO;
import at.schunker.mt.dao.DBWalletDAO;
import at.schunker.mt.dto.GenericResponse;
import at.schunker.mt.dto.JwtTokenResponse;
import at.schunker.mt.dto.User;
import at.schunker.mt.exception.AuthenticationForbiddenException;
import at.schunker.mt.service.JwtAuthenticationException;
import at.schunker.mt.service.JwtAuthenticationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.Credentials;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final CredentialsDAO walletDAO;
    private JwtAuthenticationService authenticationService;
    /*
    @Autowired
    private DiscoveryClient discoveryClient; */
    @Value("${jwt.header}")
    private String authorizationHeaderName;

    public AuthenticationController(DBWalletDAO walletDAO, JwtAuthenticationService authenticationService){
        this.walletDAO = walletDAO;
        this.authenticationService = authenticationService;
    }
    /*
    @RequestMapping("/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    } */

    @RequestMapping(value = "/auth", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity authenticateUser(HttpServletResponse response, @RequestBody User user){
        log.info("authenticateUser: " + user.toString());

        if (user.getAddress() == null || user.getPassword() == null) {
            throw new AuthenticationForbiddenException();
        }

        /*
        EmailValidator emailValidator = EmailValidator.getInstance();

        if (!emailValidator.isValid(user.getEmail())) {
            throw new AuthenticationForbiddenException();
        } */

        /*
        Optional<User> optUser = this.repository.findOneByEmail(user.getEmail());
        if (!optUser.isPresent()) {
            throw new AuthenticationForbiddenException();
        }

        User savedUser = optUser.get();
        log.info("user: " + savedUser.toString());
        */
        String token = null;

        try {
            token = this.authenticationService.generateJwtToken(user.getAddress(), user.getPassword());
        } catch (JwtAuthenticationException ex) {
            throw new AuthenticationForbiddenException(user);
        }

        log.info("token: {}", token);
        JwtTokenResponse tokenResponse = new JwtTokenResponse(token);

        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity logoutUser(HttpServletRequest request, HttpServletResponse response) {
        log.info("logoutUser");

        String authorizationHeader = request.getHeader(this.authorizationHeaderName);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            GenericResponse responseBody = new GenericResponse(HttpStatus.BAD_REQUEST.value(), "Token not found");
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }

        String authToken = authorizationHeader.substring(7);

        if (authToken == null) {
            GenericResponse responseBody = new GenericResponse(HttpStatus.BAD_REQUEST.value(), "Token not found");
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }

        //TODO: save token in blacklist

        GenericResponse responseBody = new GenericResponse(HttpStatus.OK.value(), "Logout successful");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

}
