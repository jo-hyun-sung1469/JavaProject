package org.example.gameserver.controller;

import org.example.gameserver.dto.AttackResponse;
import org.example.gameserver.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/battle")
public class BattleController {

    @Autowired
    private PlayerService playerService;

    // 기본 공격
    @PostMapping("/attack")
    public AttackResponse attack(
            @RequestParam long playerId,
            @RequestParam long monsterId
    ) {
        return playerService.useSkill(playerId, 12, monsterId); // 0번 스킬 = 기본 공격
    }

    @PostMapping("/recovery")
    public AttackResponse recovery(
            @RequestParam long playerId,
            @RequestParam long monsterId
    )
        {
        return playerService.useSkill(playerId, 13, monsterId);
        }

    // 스킬 공격
    @PostMapping("/skill")
    public AttackResponse useSkill(
            @RequestParam long playerId,
            @RequestParam long monsterId,
            @RequestParam int skillId
    ) {
        return playerService.useSkill(playerId, skillId, monsterId);
    }
}
