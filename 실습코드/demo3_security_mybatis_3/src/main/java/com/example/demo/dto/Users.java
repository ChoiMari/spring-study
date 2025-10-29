package com.example.demo.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Users {
    private  int userNo;
    private String userId;
    private String userPw;
    private String name;
    private String email;
    private Date regDate;
    private Date updDate;

    //권한 목록
    List<UserAuth> authList;  //여러개의 권한을 담기 위해서 

    private int enabled;  //1인 경우 활성화된 사용자 ...

}