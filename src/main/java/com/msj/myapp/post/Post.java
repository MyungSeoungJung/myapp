package com.msj.myapp.post;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor //전체 필드 초기화 생성자 => 생성자의 매개변수 순서와 클래스의 필드 순서가 정확히 일치해야 해당 애너테이션으로 생성된 생성자를 사용할 수 있습니다.
@NoArgsConstructor  // 빈 생성자  => 서버에서는 매개변수의 순서를 몰라서 일단 빈생성자를 먼저 만든다음 @AllArgsConstructor로 자동으로 필드값 set해줌
//서버에서는 매개변수의 순서를 몰라서 NoArgsConstructor빈 생성자를 먼저 만든다음 AllArgsConstructor으로 필드로 set을 함?
@Builder
@Entity
public class Post {
    @Id
//    database의 auto-increment를 사용함
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long no;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
//    작성자 이름
    private String creatorName;
    private long createdTime;
//    게시글 작성자 id
    private long creatorId;

    @Column(columnDefinition = "LONGTEXT")
    private String image;

}
