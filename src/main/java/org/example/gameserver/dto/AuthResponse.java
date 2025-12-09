package org.example.gameserver.dto;

// Spring Server - AuthResponse.java

public class AuthResponse {
    public Long playerId;
    public String username;
    public String jwtToken; // 필수: 유니티가 저장해서 다음 통신에 사용


    public int level;
    public int statistics;

    public int hp;
    public int maxHp;

    public int attackDamage;
    public int basicattack;
    public int defence;
    public int agility;

    public int intelligence;
    public int maxIntelligence;

    public int barrier;
    public int[] skillCooldown;//따로 저장 함수 존재함
    public int[] buffCount;
}
