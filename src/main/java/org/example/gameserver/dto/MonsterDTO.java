package org.example.gameserver.dto;

import lombok. *;
import org.example.gameserver.game.Monster;

@Getter
@Setter
public class MonsterDTO {

    public String name;
    public int level;
    public int hp;
    public int maxHp;
    public int attackDamage;
    public int defence;
    public int agility;
    public int debuffcount;

    public MonsterDTO(Monster m) {
        this.name = m.getName();
        this.level = m.getLevel();
        this.hp = m.getHp();
        this.maxHp = m.getMaxHp();
        this.attackDamage = m.getAttackDamage();
        this.defence = m.getDefence();
        this.agility = m.getAgility();
        this.debuffcount = m.getDebuffcount();
    }
}
