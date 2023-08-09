package com.msj.myapp.myCoach;


import com.msj.myapp.auth.AuthService;
import com.msj.myapp.auth.entity.LoginRepository;
import com.msj.myapp.auth.entity.ProfileRepository;
import com.msj.myapp.auth.util.HashUtil;
import com.msj.myapp.myCoach.entity.ProgramRepository;
import com.msj.myapp.myCoach.request.signupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class userController {

    @Autowired
    private LoginRepository repo;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private UserService service;
    @Autowired
    private HashUtil hash;
    @PostMapping (value = "/signup")
    public ResponseEntity signup (@RequestBody signupRequest req){
        System.out.println(req);
        long programId = service.createIdentity(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
