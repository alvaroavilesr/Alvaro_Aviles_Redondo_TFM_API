package es.upm.tfm.domain.services;

import es.upm.tfm.adapters.mysqldb.dto.JwtRequestDTO;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.JwtResponse;
import es.upm.tfm.domain.persistence_ports.JwtPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtPersistence jwtPersistence;

    @Autowired
    @Lazy
    public JwtService(JwtPersistence jwtPersistence) {
        this.jwtPersistence = jwtPersistence;
    }

    public JwtResponse createJwtToken(JwtRequestDTO jwtRequestDTO) throws UserNotFoundException, Exception {
        return this.jwtPersistence.createJwtToken(jwtRequestDTO);
    }
}
