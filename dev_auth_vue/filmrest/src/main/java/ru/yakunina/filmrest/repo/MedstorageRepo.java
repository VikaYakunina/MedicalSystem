package ru.yakunina.filmrest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yakunina.filmrest.domain.MedStorage;
import ru.yakunina.filmrest.domain.MedType;

import java.util.List;

public interface MedstorageRepo extends JpaRepository<MedStorage, Long> {

    @Query("SELECT ms FROM MedStorage ms where ms.medtype = :medtype")
    List<MedStorage> FindByMedtypeId(@Param("medtype") MedType medtype);

    @Query("SELECT ms FROM MedStorage ms where ms.name= :name")
    List<MedStorage> FindByName(@Param("name") String name);

    @Query("SELECT ms FROM MedStorage ms where ms.name= :name and ms.medtype = :medtype")
    List<MedStorage> FindByNameAndMedtypeId(@Param("name") String name, @Param("medtype") MedType medtype);
}
