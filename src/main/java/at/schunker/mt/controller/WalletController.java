package at.schunker.mt.controller;

import at.schunker.mt.dao.CredentialsDAO;
import at.schunker.mt.dao.DBWalletDAO;
import at.schunker.mt.dto.GenericResponse;
import at.schunker.mt.dto.User;
import at.schunker.mt.dto.UserPrivateKey;
import at.schunker.mt.exception.BadRequestException;
import at.schunker.mt.exception.InternalServerErrorException;
import at.schunker.mt.exception.NotFoundException;
import at.schunker.mt.exception.WalletBadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class WalletController {
    private static final Logger log = LoggerFactory.getLogger(WalletController.class);
    private final CredentialsDAO walletDAO;
    /*
    @Autowired
    private DiscoveryClient discoveryClient; */

    public WalletController(DBWalletDAO walletDAO) {
        this.walletDAO = walletDAO;
    }

    /*
    @RequestMapping("/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    } */

    @RequestMapping(value = "/wallet", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createWallet(HttpServletResponse servletResponse, @RequestBody UserPrivateKey userPrivateKey) {
        if (userPrivateKey.getPrivateKey() == null || userPrivateKey.getPassword() == null) {
            throw new WalletBadRequestException();
        }

        Credentials newCredentials = Credentials.create(userPrivateKey.getPrivateKey());
        Credentials credentials = this.walletDAO.loadCredentials(newCredentials.getAddress(), userPrivateKey.getPassword());

        if (newCredentials.equals(credentials)) {
            GenericResponse response = new GenericResponse(HttpStatus.CONFLICT.value(), "wallet already saved");
            return new ResponseEntity(response, HttpStatus.CONFLICT);
        }

        try {
            this.walletDAO.createWallet(newCredentials, userPrivateKey.getPassword());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }

        GenericResponse response = new GenericResponse(HttpStatus.OK.value(), "Credentials saved");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/wallet", method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteWallet(HttpServletResponse servletResponse, @RequestBody User user) {
        if (user.getAddress() == null || user.getPassword() == null) {
            throw new BadRequestException();
        }

        Credentials credentials = this.walletDAO.loadCredentials(user.getAddress(), user.getPassword());
        if (credentials == null) {
            throw new NotFoundException();
        }

        boolean deletionResult = this.walletDAO.deleteCredentials(user.getAddress(), user.getPassword());
        if (deletionResult == false) {
            throw new InternalServerErrorException();
        }

        GenericResponse response = new GenericResponse(HttpStatus.OK.value(), "Wallet deleted");
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
