package ru.yakunina.filmrest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yakunina.filmrest.domain.Usr;

public interface UsrRepo extends JpaRepository<Usr, Long> {

    // проверка при логине
    @Query("SELECT u FROM Usr u where u.username= :username and u.password=:password")
    Usr FindByUsernameAndPassword(@Param("username") String username,
                                  @Param("password") String password);
    // проверка при добавлении
    @Query("SELECT u FROM Usr u where u.username= :username")
    Usr FindByUsername(@Param("username") String username);

}
