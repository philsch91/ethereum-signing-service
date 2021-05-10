package at.schunker.mt.service;

import at.schunker.mt.dao.DBWalletDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.web3j.crypto.Credentials;

@Service
public class JwtAuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationService.class);
    private DBWalletDAO walletDAO;
    private JwtTokenAuthorizationService tokenService;
    //private PasswordEncoder passwordEncoder;

    public JwtAuthenticationService(DBWalletDAO walletDAO, JwtTokenAuthorizationService tokenService){
        this.walletDAO = walletDAO;
        this.tokenService = tokenService;
        //this.passwordEncoder = passwordEncoder;
    }

    public String generateJwtToken(String address, String password) throws JwtAuthenticationException {
        log.info("generateJwtToken");
        log.info(password);
        /*
        Optional<User> optionalUser = this.userRepository.findOneByEmail(email);

        if (!optionalUser.isPresent()) {
            throw new JwtAuthenticationException("incorrect user");
        }

        User user = optionalUser.get();
        log.info("JwtAuthenticationService.user: " + user.toString());

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.info("incorrect password");
            throw new JwtAuthenticationException("incorrect password");
        }
        */

        //TODO: handle exceptions
        Credentials credentials = this.walletDAO.loadCredentials(password, address);

        if (credentials == null) {
            log.info("incorrect password");
            throw new JwtAuthenticationException("incorrect password");
        }

        String token = this.tokenService.generateToken(address);
        log.info("JwtAuthenticationService.token: " + token);

        return token;
    }
}
