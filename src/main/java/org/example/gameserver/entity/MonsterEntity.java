package org.example.gameserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class MonsterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long playerId;

    private String name;
    private int hp;
    private int maxHp;
    protected int Defence;
    protected int agility;
    protected int attackDamage;
    protected int level;
    private int debuffCount;

    public MonsterEntity() {
        name = "Monster";
        hp = 500;
        maxHp = 500;
        Defence = 45;
        agility = 90;
        attackDamage = 50;
        level = 1;
        debuffCount = 0;
    }


}
