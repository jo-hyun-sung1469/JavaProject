package org.example.gameserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String password;
    @Column(unique = true, updatable = false)
    private String username;

    private int level;
    private int hp;
    protected int maxHp;
    protected int Defence;
    protected int agility;
    protected int attackDamage;
    private int statistics;
    private int Intelligence;
    private int maxIntelligence;
    private int basicattack;
    @Lob
    private String skillCooldownJson; //배열을 JSON 문자열로 저장
    @Lob
    private String buffCountJson;
    private int barrier;

    public PlayerEntity(String username, String password) {
        this.username = username;
        this.password = password;
        this.level = 1;
        this.maxHp = 300;
        this.hp = maxHp;
        this.Defence = 30;
        this.agility = 100;
        this.attackDamage = 75;
        this.basicattack = attackDamage;
        this.statistics = 0;
        this.maxIntelligence = 300;
        this.Intelligence = maxIntelligence;
        this.skillCooldownJson = "[0,0,0,0,0,0,0,0,0,0,0,0]";
        this.buffCountJson = "[0,0]";
        this.barrier = 0;
    }

    public PlayerEntity() {}


}
