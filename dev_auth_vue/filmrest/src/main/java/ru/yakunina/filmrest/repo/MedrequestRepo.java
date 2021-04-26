package ru.yakunina.filmrest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yakunina.filmrest.domain.MedIssue;
import ru.yakunina.filmrest.domain.MedRequest;
import ru.yakunina.filmrest.domain.MedStorage;
import ru.yakunina.filmrest.domain.MedType;

import java.util.List;

public interface MedrequestRepo extends JpaRepository<MedRequest, Long> {

    // нахождение заявки по лекарству
    @Query("SELECT mr FROM MedRequest mr where mr.medstorage  = :medStorage")
    List<MedRequest> FindByMedstorage(@Param("medStorage") MedStorage medStorage);

    // нахождение заявки по id лекарства
    @Query("SELECT mr FROM MedRequest mr join mr.medstorage ms where ms.id = :medstorageId")
    List<MedRequest> FindByMedstorageId(@Param("medstorageId") Long medstorageId);

    // нахождение заявки по типу лекарства
    @Query("SELECT mr FROM MedRequest mr where mr.medstorage.medtype  = :medtype")
    List<MedRequest> FindByMedType(@Param("medtype") MedType medtype);

    // нахождение заявки по названию лекарства
    @Query("SELECT mr FROM MedRequest mr where mr.medstorage.name  = :name")
    List<MedRequest> FindByMedName(@Param("name") String name);

    // нахождение заявки по названию и типу лекарства
    @Query("SELECT mr FROM MedRequest mr where mr.medstorage.name  = :name and mr.medstorage.medtype  = :medtype")
    List<MedRequest> FindByMedNameAndMedType(@Param("name") String name, @Param("medtype") MedType medtype);
}
