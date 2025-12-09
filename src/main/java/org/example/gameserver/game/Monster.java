package org.example.gameserver.game;

import lombok.Getter;

@Getter
public class Monster extends Unit{
    public int debuffcount;
    public Monster(String name,int hp, int maxhp, int attackDamage, int Defence, int agility, int level, int debuffcount) {
        super(name, hp, maxhp, attackDamage, Defence, agility, level);
        this.debuffcount = debuffcount;
    }

    public String getName(){ return name; }
    public int getHp() {return hp;}
    public int getMaxHp() { return maxHp; }
    public int getAttackDamage() { return attackDamage; }
    public int getLevel() { return level; }
    public int getDefence() { return defence; }
    public int getAgility() { return agility; }


    public void performAttack(Player target) {
        target.hp -= attackDamage * 2;
    }

    @Override
    public void turnEnd(){
        if(debuffcount > 0){
            hp -= 25 * level;//원래는 player의 레벨에 따라 값이 달라져야 하지만
            debuffcount--;
        }
    }

    public void takeDamage(int damage,Unit unit) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }

//        System.out.println(this.name + "이(가) 피해를 입었습니다. 남은 HP: " + this.hp);
    }

    public int giveDamage(Player target) {
        return attackDamage - target.defence;
    }

    @Override
    public void levelUp() {
        maxHp += 250;
        hp = maxHp;
        defence += 20;
        agility += 1;
        attackDamage += 60;
        debuffcount = 0;//디버프 초기화
        level += 1;
    }
}
