package za.co.facebrick.user.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.facebrick.user.data.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
