package at.schunker.mt.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FilePasswordDAO implements PasswordDAO {
    private static final Logger logger = LoggerFactory.getLogger(FilePasswordDAO.class);
    @Value("${passwordDirectoryPath}")
    private String directoryPath;
    private PasswordEncoder passwordEncoder;

    public FilePasswordDAO() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String getDirectoryPath() {
        return this.directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        // TODO: check path
        Path passwordFilePath = Paths.get(directoryPath);
        //passwordFilePath.
        this.directoryPath = directoryPath;
    }

    public PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean savePassword(String address, String password) {
        String fileName = this.directoryPath + "/" + address + ".pwf";
        File passwordFile = new File(fileName);

        logger.info("passwordFile.getName(): " + passwordFile.getName());
        logger.info("passwordFile.getPath(): " + passwordFile.getAbsolutePath());

        if (passwordFile.exists()) {
            return false;
        }

        Path passwordFilePath = Paths.get(fileName);
        //String encodedPassword = this.passwordEncoder.encode(password);

        try {
            Files.write(passwordFilePath, password.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            //ex.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public String getPassword(String address) {
        String fileName = this.directoryPath + "/" + address + ".pwf";
        File passwordFile = new File(fileName);

        if (!passwordFile.exists()) {
            return null;
        }

        Path passwordFilePath = Paths.get(fileName);
        String password = null;
        try {
            //password = Files.readString(passwordFilePath);    // Java 13
            byte[] passwordBytes = Files.readAllBytes(passwordFilePath);
            password = new String(passwordBytes, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            return null;
        }

        return password;
    }
}
