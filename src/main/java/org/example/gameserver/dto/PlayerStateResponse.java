package org.example.gameserver.dto;

import org.example.gameserver.game.Player;

public class PlayerStateResponse {

    public String name;
    public int hp;
    public int maxHp;

    public int intelligence;
    public int maxIntelligence;

    public int level;
    public int attackDamage;
    public int defence;
    public int agility;
    public int basicattack;
    public int barrier;

    public int[] skillCooldown;
    public int[] buffCount;

    public PlayerStateResponse(Player p) {
        this.name = p.getName();
        this.hp = p.getHp();
        this.maxHp = p.getMaxHp();
        this.intelligence = p.getIntelligence();
        this.maxIntelligence = p.getMaxintelligence();
        this.level = p.getLevel();
        this.attackDamage = p.getAttackDamage();
        this.defence = p.getDefence();
        this.agility = p.getAgility();
        this.basicattack = p.getBasicattack();
        this.barrier = p.getBarrier();
        this.skillCooldown = p.getSkillCooltime();
        this.buffCount = p.getBuffcount();
    }
}
