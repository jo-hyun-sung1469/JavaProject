package org.example.gameserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.gameserver.dto.AttackResponse;
import org.example.gameserver.dto.MonsterDTO;
import org.example.gameserver.dto.PlayerStateResponse;
import org.example.gameserver.entity.MonsterEntity;
import org.example.gameserver.entity.PlayerEntity;
import org.example.gameserver.game.Monster;
import org.example.gameserver.repository.PlayerRepository;
import org.example.gameserver.game.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private MonsterService monsterService;

    @Autowired
    private ObjectMapper mapper;

    // 회원가입 + 몬스터 자동 생성
    public PlayerEntity register(String username, String password) {
        if (playerRepo.findByUsername(username).isPresent()) {
            throw new RuntimeException("이미 존재하는 계정입니다!");
        }

        PlayerEntity newPlayer = new PlayerEntity(username, password);
        PlayerEntity saved = playerRepo.save(newPlayer);

        // 플레이어가 생성되면 몬스터도 자동 생성
        monsterService.createMonsterForPlayer(saved.getId());


        return saved;
    }

    public boolean exists(String username) {
        return playerRepo.findByUsername(username).isPresent();
    }

    public PlayerEntity login(String username, String password) {
        PlayerEntity p = playerRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("계정 없음"));

        if (!p.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호 틀림");
        }

        return p;
    }

    public PlayerEntity getById(Long id) {
        return playerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("player not found"));
    }

    // Entity → Game Player 변환
    public Player convertToGame(PlayerEntity e) {
        Player p = new Player(
                e.getUsername(),
                e.getHp(),
                e.getMaxHp(),
                e.getAttackDamage(),
                e.getBasicattack(),
                e.getDefence(),
                e.getIntelligence(),
                e.getMaxIntelligence(),
                e.getAgility(),
                loadCooldown(e),
                e.getStatistics(),
                e.getLevel(),
                e.getBarrier(),
                loadBuffCount(e)
        );
        return p;
    }

    // Game Player → Entity 업데이트
    public void updateEntity(Player gamePlayer, PlayerEntity entity) {
        entity.setHp(gamePlayer.getHp());
        entity.setMaxHp(gamePlayer.getMaxHp());
        entity.setAttackDamage(gamePlayer.getBasicattack());
        entity.setBasicattack(gamePlayer.getBasicattack());
        entity.setLevel(gamePlayer.getLevel());
        entity.setDefence(gamePlayer.getDefence());
        entity.setIntelligence(gamePlayer.getIntelligence());
        entity.setMaxIntelligence(gamePlayer.getMaxintelligence());
        entity.setAgility(gamePlayer.getAgility());
        entity.setStatistics(gamePlayer.getStatistics());

        saveCooldown(entity, gamePlayer.getSkillCooltime());
        saveBuffCount(entity, gamePlayer.getBuffcount());

        playerRepo.save(entity);
    }

    // 쿨타임 json 저장
    public void saveCooldown(PlayerEntity e, int[] cooldowns) {
        try {
            String json = mapper.writeValueAsString(cooldowns);
            e.setSkillCooldownJson(json);
            playerRepo.save(e);
        } catch (Exception ex) {
            throw new RuntimeException("JSON 변환 실패: " + ex.getMessage());
        }
    }

    // 쿨타임 로딩
    public int[] loadCooldown(PlayerEntity e) {
        try {
            if (e.getSkillCooldownJson() == null) return new int[12];
            return mapper.readValue(e.getSkillCooldownJson(), int[].class);
        } catch (Exception ex) {
            throw new RuntimeException("JSON 파싱 실패: " + ex.getMessage());
        }
    }

    public void saveBuffCount(PlayerEntity e, int[] cooldowns) {
        try {
            String json = mapper.writeValueAsString(cooldowns);
            e.setBuffCountJson(json);
            playerRepo.save(e);
        } catch (Exception ex) {
            throw new RuntimeException("JSON 변환 실패: " + ex.getMessage());
        }
    }

    // 쿨타임 로딩
    public int[] loadBuffCount(PlayerEntity e) {
        try {
            if (e.getBuffCountJson() == null) return new int[2];
            return mapper.readValue(e.getSkillCooldownJson(), int[].class);
        } catch (Exception ex) {
            throw new RuntimeException("JSON 파싱 실패: " + ex.getMessage());
        }
    }

    public Player getGamePlayer(long id) {
        PlayerEntity entity = getById(id);
        return convertToGame(entity);
    }

    public AttackResponse useSkill(long playerId, int skillId, Long monsterId) {

        PlayerEntity playerEntity = getById(playerId);
        Player player = convertToGame(playerEntity);

        MonsterEntity monsterEntity = null;
        Monster target = null;

        if (monsterId != null) {
            monsterEntity = monsterService.findById(monsterId);
            target = monsterService.convertToMonster(monsterEntity); // 전투용 객체 생성
        }

        // 몬스터 선공 데미지 계산
        int damageFromMonster = (target != null) ? target.giveDamage(player) : 0;


        // ============================
        // 2) 스킬 실행
        // ============================
        int barrierToPlayer = 0;
        int damageFromPlayer = 0;
        int healFromPlayer = 0;
        boolean attackAvoidToPlayer;
        boolean attackAvoidToMonster;
        boolean playerDead;
        boolean monsterDead;
        switch(skillId) {

            case 0: // doubleDamage()
                damageFromPlayer = player.doubleDamage();
                System.out.println("실행됨");
                break;

            case 1: // ignoreDefense()
                if (target != null)
                    damageFromPlayer = player.ignoreDefense(target);
                System.out.println("실행됨");
                break;

            case 2: // increaseDefense()
                damageFromMonster = player.increaseDefense(damageFromMonster, player);
                System.out.println("실행됨");
                break;

            case 3:
                if (target != null)
                    player.debuffCreate(target);
                System.out.println("실행됨");
                break;

            case 4:
                player.buffCreate_hpRegeneration(player);
                System.out.println("실행됨");
                break;

            case 5:
                player.buffCreate_intelligenceRegeneration(player);
                System.out.println("실행됨");
                break;

            case 6:
                barrierToPlayer = player.barrierCreate();
                System.out.println("실행됨");
                break;

            case 7:
                healFromPlayer = player.hpRecovery_enhanced(player);
                System.out.println("실행됨");
                break;

            case 8:
                player.attackPowerUp(player);
                if (target != null) {
                    damageFromPlayer = player.giveDamage(target, player);
                }
                System.out.println("실행됨");
                break;

            case 9:
                if (target != null)
                    damageFromPlayer = player.tradeStrike(player, target);
                System.out.println("실행됨");
                break;

            case 10:
                damageFromPlayer = player.Overdrive(player, target);
                System.out.println("실행됨");
                break;

            case 11:
                barrierToPlayer = player.defensiveReadiness(player);
                System.out.println("실행됨");
                break;

            case 12:
                damageFromPlayer = player.giveDamage(Objects.requireNonNull(target), player);
                System.out.println("실행됨");
                break;

            case 13:
                healFromPlayer = player.hpRecovery(player);
                System.out.println("실행됨");
                break;
            default:
                throw new IllegalArgumentException("Invalid skillId");
        }
        attackAvoidToPlayer = player.attackEvasion(player);
        attackAvoidToMonster = Objects.requireNonNull(target).attackEvasion(target);
        // ============================
        // 3) 턴 종료 처리
        // ============================
        if(!attackAvoidToMonster)
            Objects.requireNonNull(target).takeDamage(damageFromPlayer, target);
        player.barrier += barrierToPlayer;
        if(player.maxHp-healFromPlayer >= player.hp)
            player.hp += healFromPlayer;
        else
            player.hp = player.maxHp;

        monsterDead = target.getHp() <= 0;
        playerDead = player.getHp() <= 0;

        if (target.getHp() > 0) {
            // 몬스터 생존 → 플레이어 피격 처리
            if(!attackAvoidToPlayer)
                player.takeDamage(damageFromMonster, player);
            player.turnEnd();
            target.turnEnd();
            }
        else {
            // 몬스터 사망 → 레벨업
            player.levelUp();
            target.levelUp();
        }


        // ============================
        // 4) PlayerEntity 저장
        // ============================
        updateEntity(player, playerEntity); // DB 저장 포함됨


        // ============================
        // 5) MonsterEntity 저장
        // ============================
        monsterService.updateEntity(target, monsterEntity);


        // ============================
        // 6) 클라이언트로 응답 반환
        // ============================
        PlayerStateResponse playerDTO = new PlayerStateResponse(player);
        MonsterDTO monsterDTO = new MonsterDTO(Objects.requireNonNull(target));

        System.out.println(damageFromPlayer);
        System.out.println(damageFromMonster);
        System.out.println(attackAvoidToMonster);
        System.out.println(healFromPlayer);
        System.out.println(monsterDead);
        System.out.println(playerDead);
        System.out.println(attackAvoidToPlayer);
        System.out.println(barrierToPlayer);
        System.out.println();
        return new AttackResponse(
                damageFromPlayer,
                healFromPlayer,
                attackAvoidToPlayer,
                attackAvoidToMonster,
                barrierToPlayer,
                target.getHp(),
                monsterDead,
                playerDead,
                damageFromMonster,
                player.getHp(),
                playerDTO,
                monsterDTO
        );
    }

}