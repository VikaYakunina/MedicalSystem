package ru.yakunina.filmrest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yakunina.filmrest.domain.MedIssue;
import ru.yakunina.filmrest.domain.MedRequest;
import ru.yakunina.filmrest.domain.MedStorage;
import ru.yakunina.filmrest.domain.MedType;

import java.util.List;

public interface MedissueRepo extends JpaRepository<MedIssue, Long> {

    // нахождение выдачи по лекарству
    @Query("SELECT mi FROM MedIssue mi where mi.medstorage  = :medStorage")
    List<MedIssue> FindByMedstorage(@Param("medStorage") MedStorage medStorage);

    // нахождение выдачи по id лекарства
    @Query("SELECT mi FROM MedIssue mi join mi.medstorage ms where ms.id = :medstorageId")
    List<MedIssue> FindByMedstorageId(@Param("medstorageId") Long medstorageId);

    // нахождение выдачи по типу лекарства
    @Query("SELECT mi FROM MedIssue mi where mi.medstorage.medtype  = :medtype")
    List<MedIssue> FindByMedType(@Param("medtype") MedType medtype);

    // нахождение выдачи по названию лекарства
    @Query("SELECT mi FROM MedIssue mi where mi.medstorage.name  = :name")
    List<MedIssue> FindByMedName(@Param("name") String name);

    // нахождение выдачи по названию и типу лекарства
    @Query("SELECT mi FROM MedIssue mi where mi.medstorage.name  = :name and mi.medstorage.medtype  = :medtype")
    List<MedIssue> FindByMedNameAndMedType(@Param("name") String name, @Param("medtype") MedType medtype);
}
