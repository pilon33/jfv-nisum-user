package com.nisum.test.user.controller;

import com.nisum.test.user.dtos.JwtRequestDTO;
import com.nisum.test.user.dtos.JwtResponseDTO;
import com.nisum.test.user.security.JwtTokenUtil;
import com.nisum.test.user.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    private static final String USER_DISABLE = "Usuario deshabilitado";
    private static final String NO_OK_CREDENTIAL = "Credenciales Invalidas";



    @PostMapping(value = "/authenticate")
    public ResponseEntity<JwtResponseDTO> createAuthenticationToken(@RequestBody JwtRequestDTO authenticationRequest)  {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateTokenForExistingUser(userDetails);

        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    private void authenticate(String username, String password) throws DisabledException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new DisabledException(USER_DISABLE, e);
        } catch (BadCredentialsException e) {
            throw new DisabledException(NO_OK_CREDENTIAL, e);
        }
    }
}
