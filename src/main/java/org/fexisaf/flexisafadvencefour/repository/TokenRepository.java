package org.fexisaf.flexisafadvencefour.repository;

import org.fexisaf.flexisafadvencefour.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
//
//    @Query(value = """
//SELECT * FROM token join users on token.user_id = users.id
//where users.id = ?1 and token.revoked = false;
//""",
//            nativeQuery = true
//    )
@Query(value = """
            SELECT t FROM TokenEntity t JOIN t.user u 
            WHERE u.id = ?1 AND t.revoked = false
            """)
    List<TokenEntity> findAllById(Long id);

    Optional<TokenEntity> findByToken(String token);
}
