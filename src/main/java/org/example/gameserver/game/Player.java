package org.example.gameserver.game;

public class Player extends Unit {
    public int statistics = 0;
    public int intelligence;
    public int maxintelligence;
    public int barrier;
    public int[] skillCooltime = new int[12];
    public int[] buffcount = {0,0};
    private boolean attackUp = false;
    public int basicattack;

    public Player(String name,int hp, int maxhp, int attackDamage, int basicattack, int defence,int intelligence,
                  int maxIntelligence, int agility, int[] skillCooltime, int statistics, int level, int barrier, int[] buffcount) {
        super(name, hp, maxhp, attackDamage, defence, agility, level);
        this.intelligence = intelligence;
        this.maxintelligence = maxIntelligence;
        this.basicattack = basicattack;
        this.statistics = statistics;

        // 버그 수정: 인덱스 기반 반복문 사용
        for(int i = 0; i < this.skillCooltime.length; i++){
            this.skillCooltime[i] = skillCooltime[i];
        }

        for(int i = 0; i < this.buffcount.length; i++){
            this.buffcount[i] = buffcount[i];
        }
        this.barrier = barrier;
    }

    // ----------getter&setter---------------------------]
    public String getName(){ return name; }
    public int getHp() {return hp;}
    public int getMaxHp() { return maxHp; }
    public int getAttackDamage() { return attackDamage; }
    public int getLevel() { return level; }
    public int getDefence() { return defence; }
    public int getIntelligence() { return intelligence; }
    public int getMaxintelligence() { return maxintelligence; }
    public int getAgility() { return agility; }
    public int getStatistics() { return statistics; }
    public int getBarrier() { return barrier; }
    public int getBasicattack() { return basicattack; }
    public boolean getAttackUp() { return attackUp; }

    // ★ skillCooltime getter 추가
    public int[] getSkillCooltime() { return skillCooltime; }
    public int[] getBuffcount() { return buffcount; }
    // ----------getter&setter 끝---------------------------

    public int giveDamage(Unit target, Player player) {//일반 공격
        if(intelligence <= maxintelligence - 50)
            player.intelligence += 50;
        else
            player.intelligence = maxintelligence;
        return attackDamage - target.defence;
    }

    public int hpRecovery(Player player) {
        if(intelligence <= maxintelligence - 50)
            player.intelligence += 50;
        else
            player.intelligence = player.maxintelligence;
        return 50 * player.level;
    }

    public void takeDamage(int damage, Player player){//회피 필요
        int barrierDamage = damage;
        damage -= barrier;
        this.hp -= damage;
        this.barrier -= barrierDamage;
        if(barrier < 0)
            barrier = 0;

        if (this.hp < 0)
            this.hp = 0;

//        System.out.println(this.name + "이(가) 피해를 입었습니다. 남은 HP: " + this.hp);
    }

    // ----------게임 스킬 목록-------------------------
    public int doubleDamage() {
        if(skillCooltime[0] <= 0){
            if(intelligence >= 50) {
                skillCooltime[0] = 3;
                intelligence -= 50;
                return attackDamage * 2;
            }
        }
        return 0;
    }

    public int ignoreDefense(Monster target) {
        if(skillCooltime[1] <= 0){
            if(intelligence >= 50) {
                skillCooltime[1] = 3;
                intelligence -= 50;
                return attackDamage;
            }
        }
        return 0;
    }

    public int increaseDefense(int damage, Player player){
        if(skillCooltime[2] <= 0) {
            if (intelligence >= 50) {
                skillCooltime[2] = 3;
                intelligence -= 50;
                return damage - (int) (player.defence * 0.25);
            }
        }
        return 0;
    }

    public void debuffCreate(Monster target) {
        if(skillCooltime[3] <= 0) {
            if (intelligence >= 75) {
                target.debuffcount = 3;
                skillCooltime[3] = 5;
                intelligence -= 75;
            }
        }
    }

    public void buffCreate_hpRegeneration(Player player){
        if(skillCooltime[4] <= 0) {
            player.buffcount[0] = 3;
            player.skillCooltime[4] = 5;
            player.intelligence += 50;
        }
    }

    public void buffCreate_intelligenceRegeneration(Player player){
        if(skillCooltime[5] <= 0) {
            player.buffcount[1] = 3;
            player.skillCooltime[5] = 5;
            player.intelligence += 50;
        }
    }

    public int barrierCreate(){
        if(skillCooltime[6] <= 0) {
            if (intelligence >= 100) {
                skillCooltime[6] = 4;
                intelligence -= 100;
                return 75 * level;
            }
        }
        return 0;
    }

    public int hpRecovery_enhanced(Player player){
        if(skillCooltime[7] <= 0) {
            if (player.hp > player.maxHp) {
                player.hp = player.maxHp;
            }
            skillCooltime[7] = 4;
            return 150 * level;
        }
        return 0;
    }

    public void attackPowerUp(Player player){//일반 공격도 같이
        if(skillCooltime[8] <= 0) {
            if (intelligence >= 100) {
                player.attackDamage = (int) (attackDamage * 1.25);
                attackUp = true;
                skillCooltime[8] = 4;
                intelligence -= 100;
            }
        }
    }

    public int tradeStrike(Player player, Monster target){
        if(skillCooltime[9] <= 0) {
            if (intelligence >= 200) {
                player.hp -= 50;
                skillCooltime[9] = 5;
                intelligence -= 200;
                return player.attackDamage * (3 + player.level - 1);
            }
        }
        return 0;
    }

    public int Overdrive(Player player, Monster target){
        if(skillCooltime[10] <= 0) {
        int usingintell = player.intelligence;
        player.intelligence = usingintell / 10;

        skillCooltime[10] = 7;
        if(usingintell >= 550)
            return (player.attackDamage + usingintell) * 50;
        else
            return (player.attackDamage + usingintell) * 3;
        }
        return 0;
    }

    public int defensiveReadiness(Player player){
        if(skillCooltime[11] <= 0) {
            if (player.intelligence >= 50) {
                player.hp -= 50;
                player.intelligence -= 50;
                skillCooltime[11] = 5;
                return 125 * level;
            }
        }
        return 0;
    }
    // ----------게임 스킬 목록-------------------------

    @Override
    public void turnEnd(){
        if(buffcount[0] > 0){

            if(hp <= maxHp)
                hp += 50 * level;

            if(hp > maxHp)
                hp = maxHp;
        }

        if(buffcount[1] > 0){
            if(intelligence <= maxintelligence)  // 버그 수정: maxHp -> intelligence
                intelligence += 50 * level;

            if(intelligence > maxintelligence)
                intelligence = maxintelligence;
        }

        // 버그 수정: 인덱스 기반 반복문 사용
        for(int i = 0; i < skillCooltime.length; i++){
            if(skillCooltime[i] > 0){
                skillCooltime[i]--;
            }
        }

        for(int i = 0; i < buffcount.length; i++){
            if(buffcount[i] > 0){
                buffcount[i]--;
            }
        }

        if(attackUp){
            attackDamage = basicattack;
            attackUp = false;
        }
    }

    @Override
    public void levelUp() {
        statistics += 5;
        maxHp += 200;
        hp = maxHp;
        defence += 15;
        agility += 1;
        maxintelligence += 50;
        basicattack += 40;
        attackDamage = basicattack;
        level += 1;
        buffcount[0] = 0;
        buffcount[1] = 0;
        barrier = 0;
    }

    public void statisticsUp(int ability) {
        statistics--;
        if(ability == 1){
            hp += 200;
            maxHp += 200;
        }
        else if(ability == 2){
            attackDamage += 40;
        }
        else if(ability == 3){
            defence += 15;
        }
        else if(ability == 4){
            intelligence += 75;
            maxintelligence += 75;
        }
        else if(ability == 5){
            agility += 5;
        }
    }


}