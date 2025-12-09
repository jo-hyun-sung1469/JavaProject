package org.example.gameserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data//자동으로 get - set 설정
@AllArgsConstructor//자동으로 생성자 설정
public class AttackResponse {
    private int damageToMonster;
    private int healToPlayer;
    private boolean attackAvoidToPlayer;
    private boolean attackAvoidToMonster;
    private int barrierToPlayer;
    private int monsterHp;

    private boolean isMonsterDead;
    private boolean isPlayerDead;

    private int damageToPlayer;
    private int playerHp;

    private PlayerStateResponse player;  // 플레이어 전체 상태
    private MonsterDTO monster;          // 몬스터 전체 상태
}
