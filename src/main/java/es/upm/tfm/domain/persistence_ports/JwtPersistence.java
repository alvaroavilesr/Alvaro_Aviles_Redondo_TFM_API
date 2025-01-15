package es.upm.tfm.domain.persistence_ports;

import es.upm.tfm.adapters.mysqldb.dto.JwtRequestDTO;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.JwtResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtPersistence {

    JwtResponse createJwtToken(JwtRequestDTO jwtRequestDTO) throws Exception, UserNotFoundException;
}
