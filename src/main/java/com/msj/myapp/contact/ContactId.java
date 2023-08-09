package com.msj.myapp.contact;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ContactId implements Serializable {
    private long ownerId;
    private String email;

//    전체 필드 매개변수 생성자
//    equals, hashCode()




}
