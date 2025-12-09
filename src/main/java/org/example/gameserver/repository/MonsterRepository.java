package org.example.gameserver.repository;

import org.example.gameserver.entity.MonsterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonsterRepository extends JpaRepository<MonsterEntity, Long> {
    MonsterEntity findByPlayerId(Long playerId);
}
