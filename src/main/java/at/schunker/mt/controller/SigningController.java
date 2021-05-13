package at.schunker.mt.controller;

import at.schunker.mt.dao.CredentialsDAO;
import at.schunker.mt.dao.DBWalletDAO;
import at.schunker.mt.dto.User;
import at.schunker.mt.service.JwtAuthenticatedProfile;
import at.schunker.mt.service.Web3SigningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.RawTransaction;

import javax.servlet.http.HttpServletResponse;

@RestController
public class SigningController extends BaseRestController {

    private static final Logger log = LoggerFactory.getLogger(SigningController.class);
    private final Web3SigningService signingService;

    public SigningController(Web3SigningService signingService) {
        this.signingService = signingService;
    }

    @RequestMapping(value = "/sign", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sign(HttpServletResponse response, @RequestBody RawTransaction transaction){
        JwtAuthenticatedProfile authenticatedProfile = super.getAuthentication();
        String address = authenticatedProfile.getName();

        try {
            this.signingService.signTransaction(address, transaction);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }
}
