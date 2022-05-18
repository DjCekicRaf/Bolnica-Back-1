package raf.si.bolnica.user.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import raf.si.bolnica.user.models.Role;
import raf.si.bolnica.user.models.User;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static raf.si.bolnica.user.constants.Constants.JWT_KEY;

@Component
public class JwtTokenProvider {

    @Autowired
    JwtProperties jwtProperties;

    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes());
    }

    public String createToken(String username, User user) {
        return Jwts.builder()
                .setSubject(username)
                .claim("name", user.getName())
                .claim("surname", user.getSurname())
                .claim("title", user.getTitula())
                .claim("profession", user.getZanimanje())
                .claim("LBZ", user.getLbz().toString())
                .claim("PBO", user.getOdeljenje().getPoslovniBrojOdeljenja())
                .claim("department", user.getOdeljenje().getNaziv())
                .claim("PBB", user.getOdeljenje().getBolnica().getPoslovniBrojBolnice())
                .claim("hospital", user.getOdeljenje().getBolnica().getNaziv())
                .claim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.joining(",")))
                .setIssuer(JWT_KEY)
                .setExpiration(calculateExpirationDate())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private Date calculateExpirationDate() {
        Date now = new Date();
        return new Date(now.getTime() + jwtProperties.getValidityInMilliseconds());
    }

}
