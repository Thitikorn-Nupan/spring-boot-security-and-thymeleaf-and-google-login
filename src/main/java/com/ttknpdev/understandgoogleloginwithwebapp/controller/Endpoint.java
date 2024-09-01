package com.ttknpdev.understandgoogleloginwithwebapp.controller;

import com.ttknpdev.understandgoogleloginwithwebapp.log.Logback;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


@Controller
@RequestMapping(value = "/api")
public class Endpoint {
    private Logback logback;

    public Endpoint() {
        logback = new Logback(Endpoint.class);
    }

    // External any mail  can login , Internal only gmail can login
    @GetMapping(value = "/user/profile")
    public String userProfilePage(OAuth2AuthenticationToken authentication, Model model) {
       logback.log.info("authentication.getDetails() {}",authentication.getDetails()); // WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=9DE9F68356711C7E76D228EEBC063152]
       logback.log.info("authentication.getPrincipal() {}",authentication.getPrincipal());
        /*
        "Name":[
           106851002008571169342
        ],
        "Granted Authorities":[
           [
              "OIDC_USER",
              "SCOPE_https":,
              "SCOPE_https":,
              "SCOPE_openid"
           ]
        ],
        "User Attributes":[
           {
              at_hash=QMY43Uut8yC8C9kLc--OGg,
              sub=106851002008571169342,
              "email_verified=true",
              "iss=https":,
              "given_name=ttk",
              nonce=Q-0fBc5aiKwe5VVD8THXdW8XdfDMPdPXzPINI1_Az44,
              "picture=https"://lh3.googleusercontent.com/a/ACg8ocIZUg8fFelSP1bKipasa1yaOBTgAy9_H1KVevcDhigFE-hvqD43=s96-c,
              "aud="[
                 819874519183-70041e2veh1amsplguc62ni2vk0b2c5u.apps.googleusercontent.com
              ],
              azp=819874519183-70041e2veh1amsplguc62ni2vk0b2c5u.apps.googleusercontent.com,
              "name=ttk np",
              "exp=2024-08-29T11":"00":04Z,
              "family_name=np",
              "iat=2024-08-29T10":"00":04Z,
              email=thitikorn.nupan36@gmail.com
           }
        ]
        */
        logback.log.info("authentication.getPrincipal() {}",authentication.getAuthorities());
        // [OIDC_USER, SCOPE_https://www.googleapis.com/auth/userinfo.email, SCOPE_https://www.googleapis.com/auth/userinfo.profile, SCOPE_openid]

        OAuth2User oAuth2User = authentication.getPrincipal();

        String exp = oAuth2User.getAttribute("exp").toString();
        String iat = oAuth2User.getAttribute("iat").toString();

        String expFormat = getDatetimeFormat(exp);
        String iatFormat = getDatetimeFormat(iat);

        model.addAttribute("username", oAuth2User.getAttribute("name"));
        model.addAttribute("email", oAuth2User.getAttribute("email"));
        model.addAttribute("pictureUrl", oAuth2User.getAttribute("picture"));
        model.addAttribute("expFormat", expFormat);
        model.addAttribute("iatFormat", iatFormat);

        return "user-profile";
    }

    @GetMapping(value = "/user")
    public ResponseEntity userInfo(Principal user) {
        return ResponseEntity.ok(user);
    }

    private String getDatetimeFormat(String datetime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // Parse the ISO 8601 string to an Instant object
        Instant instant = Instant.parse(datetime);
        // Convert the Instant to LocalDateTime in the desired time zone
        ZoneId zoneId = ZoneId.of("Asia/Bangkok"); // Replace with your desired time zone
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return localDateTime.format(formatter);
    }


    // permit all ***
    @GetMapping(value = "/login")
    public String userLoginPage() {
        return "user-login";
    }
    @GetMapping(value = "/hello-world")
    public ResponseEntity helloWorld() {
        return ResponseEntity.ok("Hello World");
    }


}
