package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.model.Credentials;
import server.model.CredentialsDTO;
import server.service.IServiceCommon;
import server.service.restHelping.LogInRequest;

@RestController
@RequestMapping("/api/common")
public class CommonController {
    @Autowired
    private IServiceCommon serviceCommon;
    @PostMapping("/login")
    public CredentialsDTO login(@RequestBody LogInRequest logInRequest){
        return serviceCommon.login(logInRequest.getUsername(), logInRequest.getPassword());

    }
    @PostMapping("/logout")
    public void logout(@RequestBody Credentials credentials){
        serviceCommon.logout(credentials);
    }
}
