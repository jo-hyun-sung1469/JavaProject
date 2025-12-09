package org.example.gameserver.service;

import lombok.RequiredArgsConstructor;
import org.example.gameserver.entity.MonsterEntity;
import org.example.gameserver.game.Monster;
import org.example.gameserver.repository.MonsterRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MonsterService {

    private final MonsterRepository monsterRepository;

    public MonsterEntity findById(Long id) {
        return monsterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monster not found"));
    }

    public MonsterEntity save(MonsterEntity monster) {
        return monsterRepository.save(monster);
    }

    public MonsterEntity create(String name, int level, int hp, int damage) {
        MonsterEntity m = new MonsterEntity();
        m.setName(name);
        m.setLevel(level);
        m.setHp(hp);
        m.setMaxHp(hp);
        m.setAttackDamage(damage);
        return monsterRepository.save(m);
    }
    // 플레이어용 몬스터 자동 생성
    public MonsterEntity createMonsterForPlayer(Long playerId) {

        MonsterEntity monster = new MonsterEntity();
        monster.setPlayerId(playerId);
        monster.setName("Monster - Lv." + monster.getLevel());
        monster.setLevel(monster.getLevel());
        monster.setHp(monster.getMaxHp());
        monster.setMaxHp(monster.getMaxHp());
        monster.setAttackDamage(monster.getAttackDamage());
        monster.setDefence(monster.getDefence());
        monster.setId(monster.getId());
        monster.setAgility(monster.getAgility());
        monster.setDebuffCount(monster.getDebuffCount());

        return monsterRepository.save(monster);
    }

    // Game Player → Entity 업데이트
    public void updateEntity(Monster monster, MonsterEntity entity) {
        entity.setHp(monster.getHp());
        entity.setMaxHp(monster.getMaxHp());
        entity.setAttackDamage(monster.getAttackDamage());
        entity.setLevel(monster.getLevel());
        entity.setDefence(monster.getDefence());
        entity.setAgility(monster.getAgility());
        entity.setDebuffCount(monster.getDebuffcount());

        monsterRepository.save(entity);
    }

    public MonsterEntity getByPlayerId(Long playerId) {
        return monsterRepository.findByPlayerId(playerId);
    }

    public Monster convertToMonster(MonsterEntity entity) {
        Monster monster = new Monster(
                entity.getName(),
                entity.getHp(),
                entity.getMaxHp(),
                entity.getAttackDamage(),
                entity.getDefence(),
                entity.getAgility(),
                entity.getLevel(),
                entity.getDebuffCount()
        );
        return monster;
    }

    public Monster getMonster(Long id) {
        MonsterEntity entity = monsterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monster not found"));

        return convertToMonster(entity);
    }
}
