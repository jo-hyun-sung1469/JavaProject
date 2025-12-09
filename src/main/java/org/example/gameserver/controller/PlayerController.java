package org.example.gameserver.controller;

import org.example.gameserver.dto.AuthResponse;
import org.example.gameserver.dto.CooldownRequest;
import org.example.gameserver.dto.LoginRequest;
import org.example.gameserver.dto.PlayerStateResponse;
import org.example.gameserver.entity.PlayerEntity;
import org.example.gameserver.game.Player;
import org.example.gameserver.service.PlayerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/player")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // 로그인 -> AuthResponse 반환 (유니티 용)
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {
        PlayerEntity e = playerService.login(req.username, req.password);

        AuthResponse res = new AuthResponse();
        res.playerId = e.getId();
        res.username = e.getUsername();

        res.level = e.getLevel();
        res.statistics = e.getStatistics();

        res.hp = e.getHp();
        res.maxHp = e.getMaxHp();

        res.attackDamage = e.getAttackDamage();
        res.basicattack = e.getBasicattack();
        res.defence = e.getDefence();
        res.agility = e.getAgility();

        res.intelligence = e.getIntelligence();
        res.maxIntelligence = e.getMaxIntelligence();
        res.skillCooldown = playerService.loadCooldown(e);
        res.barrier = e.getBarrier();
        res.buffCount = playerService.loadBuffCount(e);

        return res;
    }

    // ID로 플레이어 불러오기 (관리/디버그)
    @GetMapping("/{id}")
    public PlayerEntity getById(@PathVariable Long id) {
        return playerService.getById(id);
    }

    // 전투 종료 후 서버에 저장 (유니티에서 보냄)
    @PostMapping("/save")
    public String saveAfterBattle(@RequestBody AuthResponse req) {
        PlayerEntity entity = playerService.getById(req.playerId);
        Player gamePlayer = playerService.convertToGame(entity);

        // apply incoming changes from client (only allowed fields)
        gamePlayer.level = req.level;
        gamePlayer.statistics = req.statistics;
        gamePlayer.hp = req.maxHp;
        gamePlayer.maxHp = req.maxHp;
        gamePlayer.attackDamage = req.attackDamage;
        gamePlayer.basicattack = req.basicattack;
        gamePlayer.defence = req.defence;
        gamePlayer.agility = req.agility;
        gamePlayer.intelligence = req.intelligence;
        gamePlayer.maxintelligence = req.maxIntelligence;
        gamePlayer.barrier = req.barrier;
        // you could update more fields here carefully

        // save to DB
        playerService.updateEntity(gamePlayer, entity);
        return "OK";
    }
    //player의 모든 스탯 전송
    @GetMapping("/state")
    public PlayerStateResponse getState(@RequestParam long playerId) {
        Player p = playerService.getGamePlayer(playerId); // ← Game Player 가져오기;
        return new PlayerStateResponse(p);
    }

    // 쿨타임 저장 전용
    @PostMapping("/saveCooldown")
    public String saveCooldown(@RequestBody CooldownRequest req) {
        PlayerEntity e = playerService.getById(req.playerId);
        playerService.saveCooldown(e, req.cooldown);
        return "OK";
    }
}
