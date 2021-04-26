package ru.yakunina.filmrest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yakunina.filmrest.domain.MedType;

import java.util.List;

public interface MedtypeRepo extends JpaRepository<MedType, Long> {

    @Query("SELECT m FROM MedType  m where m.typename= :typename")
    List<MedType> FindByName(@Param("typename") String typename);

    @Query("SELECT m FROM MedType  m where m.id= :id")
    List<MedType> FindByTypeId(@Param("id") Long id);
}
