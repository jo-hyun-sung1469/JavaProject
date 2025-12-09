package org.example.gameserver.controller;

import org.example.gameserver.dto.AuthResponse;
import org.example.gameserver.dto.AuthResponse_Monster;
import org.example.gameserver.dto.MonsterDTO;
import org.example.gameserver.dto.PlayerStateResponse;
import org.example.gameserver.entity.MonsterEntity;
import org.example.gameserver.entity.PlayerEntity;
import org.example.gameserver.game.Monster;
import org.example.gameserver.game.Player;
import org.example.gameserver.service.MonsterService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/monster")
public class MonsterController {
    private final MonsterService monsterService;

    public MonsterController(MonsterService monsterService) {
        this.monsterService = monsterService;
    }

    @PostMapping("/create")
    public AuthResponse_Monster createMonster(@RequestParam long playerId) {

        MonsterEntity existing = monsterService.getByPlayerId(playerId);

        if (existing != null) {
            return new AuthResponse_Monster(existing);
        }
        MonsterEntity m = monsterService.createMonsterForPlayer(playerId);
        AuthResponse_Monster res = new AuthResponse_Monster();
        res.id = m.getId();
        res.playerId = m.getPlayerId();
        res.name = m.getName();
        res.hp = m.getHp();
        res.maxHp = m.getMaxHp();
        res.defence = m.getDefence();
        res.agility = m.getAgility();
        res.attackDamage = m.getAttackDamage();
        res.level = m.getLevel();
        res.debuffCount = m.getDebuffCount();

        return res;
    }

    @GetMapping("/{id}")
    public MonsterEntity getById(@PathVariable Long id) {
        return monsterService.findById(id);
    }

    @PostMapping("/save")
    public String saveAfterBattle(@RequestBody AuthResponse_Monster req) {
        MonsterEntity monster = monsterService.findById(req.id);
        Monster gameMonster = monsterService.convertToMonster(monster);

        gameMonster.level = req.level;
        gameMonster.hp = req.maxHp;
        gameMonster.maxHp = req.maxHp;
        gameMonster.attackDamage = req.attackDamage;
        gameMonster.defence = req.defence;
        gameMonster.agility = req.agility;
        gameMonster.debuffcount = req.debuffCount;

        monsterService.updateEntity(gameMonster,monster);
        return "monster saved";
    }
    @GetMapping("/state")
    public MonsterDTO getState(@RequestParam long monsterId) {
        Monster p = monsterService.getMonster(monsterId); // ← Game Player 가져오기;
        return new MonsterDTO(p);
    }
}
