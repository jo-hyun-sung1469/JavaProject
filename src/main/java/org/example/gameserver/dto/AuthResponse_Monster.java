package org.example.gameserver.dto;

import org.example.gameserver.entity.MonsterEntity;

public class AuthResponse_Monster {
    public Long id;
    public Long playerId;

    public String name;
    public int hp;
    public int maxHp;
    public int defence;
    public int agility;
    public int attackDamage;
    public int level;
    public int debuffCount;

    public AuthResponse_Monster(MonsterEntity existing) {
        this.id = existing.getId();
        this.playerId = existing.getPlayerId();
        this.name = existing.getName();
        this.hp = existing.getHp();
        this.maxHp = existing.getMaxHp();
        this.defence = existing.getDefence();
        this.agility = existing.getAgility();
        this.attackDamage = existing.getAttackDamage();
        this.level = existing.getLevel();
        this.debuffCount = existing.getDebuffCount();
    }
    public AuthResponse_Monster() {

    }
}
