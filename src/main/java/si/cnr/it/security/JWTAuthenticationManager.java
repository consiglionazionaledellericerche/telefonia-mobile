package si.cnr.it.security;

import it.cnr.si.service.AceService;
import it.cnr.si.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTAuthenticationManager implements AuthenticationManager {

    @Autowired
    private AuthService authService;

    @Autowired
    private AceService aceService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String principal = (String) authentication.getPrincipal();
        String credentials = (String) authentication.getCredentials();
        try {
            // login ACE
            authService.getToken(principal, credentials);
//todo: da vedere problema con login su test
            List<GrantedAuthority> authorities =
                    aceService.ruoliAttivi(principal).stream()
                            .filter(ruolo -> ruolo.getContesti().stream()
                                    .anyMatch(r -> r.getSigla().equals("telefonia-app")))
                            .map(a -> new SimpleGrantedAuthority(a.getSigla()))
                            .collect(Collectors.toList());

            authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));

            // TODO creare ruoli amministrativi in ACE
            if(principal.equals("valerio.diego"))
                authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN));

            /**Da controllare bene*/
//            String sede_user = aceService.getPersonaByUsername(principal).getSede().getDenominazione(); //sede di username
//            String sede_cdsuoUser = aceService.getPersonaByUsername(principal).getSede().getCdsuo(); //sede_cds di username
//            String cds = sede_cdsuoUser.substring(0,3); //passo solo i primi tre caratteri quindi cds
//
//            if (cds.equals("000"))
//                sede_user = "SEDE CENTRALE";
//
//            authorities.add(new SimpleGrantedAuthority(sede_user));
            /**  Fine    */


            User utente = new User(principal, credentials, authorities);

            return new UsernamePasswordAuthenticationToken(utente, authentication, authorities);
        } catch(Exception e) {
            throw new BadCredentialsException("authentication failed for user: " + principal);
        }
    }

}

