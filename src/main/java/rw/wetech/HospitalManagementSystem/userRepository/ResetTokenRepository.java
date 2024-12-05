package rw.wetech.HospitalManagementSystem.userRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rw.wetech.HospitalManagementSystem.model.ResetToken;
import rw.wetech.HospitalManagementSystem.model.User;

import java.util.Optional;

public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {
    void deleteByToken(String token);
    Optional<ResetToken> findByUser(User user);
    Optional<ResetToken> findByToken(String token);
}