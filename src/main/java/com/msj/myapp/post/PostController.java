package com.msj.myapp.post;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


//GET /Posts
//게시글 목록이 JSON으로 나오게
@RequestMapping(value = "/posts")
@RestController
////http://localhost:8080/posts
public class PostController {
//    동시성을 위한 자료구조
//    HashMap -> ConcurrentHashMap
//    Integer -> AtomicInteger
//    Map<Long,Post> postList = new ConcurrentHashMap<>();
    AtomicLong num = new AtomicLong(0);

    //PostRepository로부터 의존성 주입받음
    @Autowired
    PostRepository repo;
//--------------------------------------------------
//전체 테이블 목록 조회
    @GetMapping
    public List<Post> getPostList() {
        // 증가시키고 값 가져오기
//        postList.put(no, Post.builder()
//                .no(no)
//                .creatorName("명승정")
//                .title("HTML 학습 기록")
//                .content("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Repudiandae maiores sunt ab beatae provident? Eius non accusantium vitae dolor ipsa!")
//                .createdTime(new Date().getTime())
//                .build());
//
//        no = num.incrementAndGet();
//        postList.put(no, Post.builder()
//                .no(no)
//                .creatorName("이소석")
//                .title("자바 스크립트")
//                .content("자바스크립트는 ‘웹페이지에 생동감을 불어넣기 위해’ 만들어진 프로그래밍 언어입니다.\n" +
//                        "자바스크립트로 작성한 프로그램을 스크립트(script) 라고 부릅니다")
//                .createdTime(new Date().getTime())
//                .build());


//        var list = new ArrayList<>(postList.values());
        // 람다식(lambda expression)
        // 식이 1개인 함수식;
        // 매개변수 영역과 함수 본체를 화살표로 구분함
        // 함수 본체의 수식 값이 반환 값
//        list.sort((a,b)-> (int)(b.getNo() - a.getNo()));
    List<Post> list = repo.findAll(Sort.by("no").descending());
        return list;
    }
    
//전체 데이터 조회
@GetMapping(value = "/paging")
public Page<Post> getPostPaging(@RequestParam int page,@RequestParam int size){
    Sort sort = Sort.by("no").descending();
    PageRequest pageRequest = PageRequest.of(page,size,sort);
    return repo.findAll(pageRequest);
}

//No가 포함된 목록 조회
@GetMapping(value = "/paging/searchByTittle")
public Page<Post> getPostPagingSearchTittle(@RequestParam int page, @RequestParam int size, String title){
        Sort sort = Sort.by("no").descending();

    PageRequest pageRequest = PageRequest.of(page, size, sort);
    System.out.println(title);
//    title 포함하는 애 모두 출력
    return repo.findByTitleContaining(title,pageRequest);
}
//타이틀과 번호 검색
@GetMapping(value = "/paging/search")
public Page<Post> getPostPagingSearch(@RequestParam int page,@RequestParam int size,String query){
    Sort sort = Sort.by("no").descending();
    PageRequest pageRequest = PageRequest.of(page, size, sort);
//    작성자 타이틀 출력
    System.out.println(query); ///
    System.out.println(query); ///
   return repo.findByTitleContainsOrCreatorNameContains(query,query,pageRequest);
}


    //---------------------------------------------------------
//    title, content만 받고
//    no ,dateTime,creatorName은 기존에 있던애랑 같이
    

    // title, content 필수 속성
    // creatorName: 서버에서 가짜데이터로 넣음
    @PostMapping
    public ResponseEntity<Map<String, Object>> addPost(@RequestBody Post post) {
//          1. 입력값 검증(title,content)
//              -> 입력값 오류(빈값) 있으면 400에러 띄움
            if (post.getTitle() == null || post.getTitle().isEmpty()){
            Map<String,Object> res = new HashMap<>();
            res.put("data", null);
            res.put("Message", "잘못된 입력입니다");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }
            if (post.getContent() == null || post.getContent().isEmpty()){
                Map<String,Object> res = new HashMap<>();
                res.put("data", null);
                res.put("Message","컨텐트 내용을 입력해주세요");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }

//          2.채번: 번호를 딴다(1 .. 2, 3....)
            long no = num.incrementAndGet();
            post.setNo(no);

//          3. 번호(no),시간값(createdTIme) 게시자이름(creatorName) 설정  (set필드명(....)) 요청 객체에 설정
            post.setCreatedTime(new Date().getTime());
            post.setCreatorName("작성자");
//          4. 맵에 추가 (서버에서 생성된 값을 설정)
//        의존성 주입받은 JpaRepository메서드


            Post savedPost = repo.save(post);
                  Map<String, Object> res = new HashMap<>();
//                 응답객체
// (postList.get) map num.key랑 일치하는 값을 전부 가져옴  map.get은 key에 해당하는 속성 전부 get함
                  res.put("data",savedPost);
                  res.put("message", "created");
//          5. 생성된 객체를 맵에서 찾아서 반환


//          생성된 객체를 맵에서 찾아서 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @DeleteMapping(value = "/{no}")
    public ResponseEntity removePost(@PathVariable("no")Long no){
        System.out.println(no);
        if (no == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        repo.deleteById(no);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
@PutMapping(value = "/{no}")
    public ResponseEntity modifyPost(@PathVariable Long no,@RequestBody PostModifyRepository post){
        //1.키 값으로 조회
        Optional<Post> findPost = repo.findById(no);
        //2.객체가 없다면 404에러 반환
        if (!findPost.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //3.객체의 title 정보가 있다면 업데이트
    Post ModifyPost = findPost.get();
        if (ModifyPost.getTitle() != null && !ModifyPost.getTitle().isEmpty()){
            ModifyPost.setTitle(post.getTitle());
        }

        if (ModifyPost.getContent() != null && !ModifyPost.getContent().isEmpty()){
            ModifyPost.setContent(post.getContent());
        }
//      데이터베이스에 저장
        repo.save(ModifyPost);

        return ResponseEntity.status(HttpStatus.OK).build();
}
}