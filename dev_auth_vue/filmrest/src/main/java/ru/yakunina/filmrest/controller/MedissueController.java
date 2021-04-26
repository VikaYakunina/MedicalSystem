package ru.yakunina.filmrest.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yakunina.filmrest.domain.MedIssue;
import ru.yakunina.filmrest.domain.MedRequest;
import ru.yakunina.filmrest.domain.MedStorage;
import ru.yakunina.filmrest.domain.MedType;
import ru.yakunina.filmrest.repo.MedissueRepo;
import ru.yakunina.filmrest.repo.MedstorageRepo;
import ru.yakunina.filmrest.repo.MedtypeRepo;

import java.util.List;

@RestController
@RequestMapping("medissue")
public class MedissueController {
    private MedstorageRepo medstorageRepo;
    private MedtypeRepo medtypeRepo;
    private final MedissueRepo medissueRepo;

    @Autowired
    public MedissueController(MedstorageRepo medstorageRepo,
                                MedtypeRepo medtypeRepo,
                                MedissueRepo medissueRepo){
        this.medstorageRepo = medstorageRepo;
        this.medtypeRepo = medtypeRepo;
        this.medissueRepo = medissueRepo;
    }

    @GetMapping
    public List<MedIssue> list(@RequestParam(name="medstorageId", required=false, defaultValue="") Long medstorageId,
                               @RequestParam(name="name", required=false, defaultValue="") String name,
                               @RequestParam(name="medtypeid", required=false, defaultValue="") Long medtypeid) {
        List<MedIssue> resMedIssue;
        if (medstorageId!=null) {               // если указано лекарство
            resMedIssue = medissueRepo.FindByMedstorageId(medstorageId);  // находим выдачу  по лекарству
        }else {

            if (!name.equals("") && medtypeid==null){    // если задан параметр name
                return medissueRepo.FindByMedName(name);       // находим выдачу по названию лекарства
            }
            if (name.equals("") && medtypeid != null ){    // если задан параметр medtypeid
                MedType medType = medtypeRepo.findById(medtypeid).orElse(null);
                return medissueRepo.FindByMedType(medType);       // находим выдачу по типу лекарства
            }
            if (!name.equals("") && medtypeid != null){    // если задан параметр name и medtypeid
                MedType medType = medtypeRepo.findById(medtypeid).orElse(null);
                return medissueRepo.FindByMedNameAndMedType(name, medType); // находим выдачу по типу и названию лекарства
            }

            resMedIssue = medissueRepo.findAll();  // получаем все выдачи
        }
        return resMedIssue;
    }

    @GetMapping("{id}")
    public MedIssue getOne(@PathVariable("id") MedIssue medIssue){
        MedIssue resMedIssue = medIssue;
        return resMedIssue;
    }

    @PostMapping
    public MedIssue create(@RequestBody MedIssue medIssue){
        MedStorage medStorage = medstorageRepo.findById(medIssue.getMedstorage().getId()).orElse(null);
        if(medStorage == null){
            medStorage = new MedStorage();
        }else {
            MedType medType = medtypeRepo.findById(medStorage.getMedtype().getId()).orElse(null);
            if (medType == null) {
                medType = new MedType();
            }
            medStorage.setMedtype(medType);
            //-- нужно уменьшить количество на складе
            if (medStorage.getCnt()>=medIssue.getCnt()) {
                int cnt = medStorage.getCnt() - medIssue.getCnt();
                medStorage.setCnt(cnt);
                medstorageRepo.save(medStorage); // сохраняем изменения
            }
            //----------------------------
        }
        medIssue.setMedstorage(medStorage);
        return medissueRepo.save(medIssue);
    }

    @PutMapping("{id}")
    public MedIssue update(@PathVariable("id") MedIssue medIssueFromDb,
                             @RequestBody MedIssue medIssue){
        BeanUtils.copyProperties(medIssue, medIssueFromDb,  "id");
        return medissueRepo.save(medIssueFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") MedIssue medIssue){
        medissueRepo.delete(medIssue);
    }
}
