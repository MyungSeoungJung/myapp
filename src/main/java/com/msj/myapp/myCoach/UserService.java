package com.msj.myapp.myCoach;

import com.msj.myapp.auth.util.HashUtil;
import com.msj.myapp.myCoach.entity.User;
import com.msj.myapp.myCoach.entity.Program;
import com.msj.myapp.myCoach.entity.ProgramRepository;
import com.msj.myapp.myCoach.entity.userRepository;
import com.msj.myapp.myCoach.request.signupRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private userRepository repo;
    private ProgramRepository profileRepo;

    @Autowired
    HashUtil hash;

    @Autowired
    //데이터 베이스와 접촉하기 위해? 생성자는 객체를 초기화해주기 위해서인데 이해가 안감
    public UserService(userRepository repo, ProgramRepository profileRepo){
        this.repo = repo;
        this.profileRepo = profileRepo;
    }

    @Transactional
//    HTML에서 받은 양식을 고대로 가져 옴
    public long createIdentity(signupRequest req){
        User toSaveUser = User.builder()
                .name(req.getName())  //html에서 입력 받은 signupRequest 객체의 필드를 get
                .sex(req.getSex())
                .age(req.getAge())
                .phone(req.getPhone())
                .height(req.getHeight())
                .weight(req.getWeight())
                .userChoiceLevel(req.getUserChoiceLevel())
                .activity(req.getActivity())
                .userChoiceGoal(req.getUserChoiceGoal())
                .secret(hash.createHash(req.getPassword()))
                .build();
        System.out.println(req);
        User savedUser = repo.save(toSaveUser);

        Program toSaveProgram =
                Program.builder()
                        .programTitle(req.getProgramTitle())
                        .build();
        String programName = profileRepo.save(toSaveProgram).getProgramTitle();
        savedUser.setProgramName(programName);  //user필드에 programName을 할당함
        repo.save(savedUser);
        return savedUser.getId();
    }
}

