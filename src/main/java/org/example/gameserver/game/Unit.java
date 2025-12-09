package org.example.gameserver.game;

import jakarta.persistence.MappedSuperclass;

// Player와 Monster의 공통 기반 클래스
@MappedSuperclass

public abstract class Unit {

    public final String name;//이름
    public int hp;//체력 레벨업: 100증가, 스탯 추가시: 200
    public int maxHp;//최대 체력
    public int defence;//피해량 감소 레벨업: 15, 스탯 업: 35
    public int agility;//민첩(회피 확률, 레벨업시 0.1% 스탯 추가시 증가량 0.5%, 기본 5%, 적군 15%)
    public int attackDamage;// 공격 대미지 레벨업: 20증가, 스탯 추가시: 40
    public int level;// 레벨 이길시 1증가 스탯은 5씩

    public Unit(String name,int hp, int maxhp, int attackDamage, int defence, int agility, int level) {
        this.name = name;
        this.hp = hp;
        this.maxHp = maxhp;
        this.defence = defence;
        this.agility = agility;
        this.attackDamage = attackDamage;
        this.level = level;
    }




    public int giveDamage(Unit target) {
        return attackDamage - target.defence;
    }


//    public abstract void takeDamage(int damage, Player player);

    public abstract void turnEnd();


    public abstract void levelUp();


    public boolean attackEvasion(Unit target) {
        int random = (int)(Math.random() * 1000) + 1;
        if(random <= target.agility)
            return true;
        return false;
    }
}