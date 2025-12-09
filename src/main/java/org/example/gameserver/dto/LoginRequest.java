package org.example.gameserver.dto;

// Spring Server - LoginRequest.java
public class LoginRequest {
    public String username; // 유니티에서 보낸 JSON의 "username" 필드와 일치해야 함
    public String password;
}