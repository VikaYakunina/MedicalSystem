package ru.yakunina.filmrest.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yakunina.filmrest.domain.MedRequest;
import ru.yakunina.filmrest.domain.MedStorage;
import ru.yakunina.filmrest.domain.MedType;
import ru.yakunina.filmrest.repo.MedrequestRepo;
import ru.yakunina.filmrest.repo.MedstorageRepo;
import ru.yakunina.filmrest.repo.MedtypeRepo;

import java.util.List;

@RestController
@RequestMapping("medrequest")
public class MedrequestController {
    private MedstorageRepo medstorageRepo;
    private MedtypeRepo medtypeRepo;
    private final MedrequestRepo medrequestRepo;

    @Autowired
    public MedrequestController(MedstorageRepo medstorageRepo,
                                MedtypeRepo medtypeRepo,
                                MedrequestRepo medrequestRepo){
        this.medstorageRepo = medstorageRepo;
        this.medtypeRepo = medtypeRepo;
        this.medrequestRepo = medrequestRepo;
    }

    @GetMapping
    public List<MedRequest> list(@RequestParam(name="medstorageId", required=false, defaultValue="") Long medstorageId,
                                 @RequestParam(name="name", required=false, defaultValue="") String name,
                                 @RequestParam(name="medtypeid", required=false, defaultValue="") Long medtypeid) {
        List<MedRequest> resMedRequest;
        if (medstorageId!=null) {               // если указано лекарство
            resMedRequest = medrequestRepo.FindByMedstorageId(medstorageId);  // находим заявки  по лекарству
        }else {

            if (!name.equals("") && medtypeid==null){    // если задан параметр name
                return medrequestRepo.FindByMedName(name);       // находим заявки по названию лекарства
            }
            if (name.equals("") && medtypeid != null ){    // если задан параметр medtypeid
                MedType medType = medtypeRepo.findById(medtypeid).orElse(null);
                return medrequestRepo.FindByMedType(medType);       // находим заявки по типу лекарства
            }
            if (!name.equals("") && medtypeid != null){    // если задан параметр name и medtypeid
                MedType medType = medtypeRepo.findById(medtypeid).orElse(null);
                return medrequestRepo.FindByMedNameAndMedType(name, medType); // находим заявки по типу и названию лекарства
            }

            resMedRequest = medrequestRepo.findAll();  // получаем все заявки
        }
        return resMedRequest;
    }

    @GetMapping("{id}")
    public MedRequest getOne(@PathVariable("id") MedRequest medRequest){
        MedRequest resMedRequest = medRequest;
        return resMedRequest;
    }

    @PostMapping
    public MedRequest create(@RequestBody MedRequest medRequest){
        MedStorage medStorage = medstorageRepo.findById(medRequest.getMedstorage().getId()).orElse(null);
        if(medStorage == null){
            medStorage = new MedStorage();
        }else {
            MedType medType = medtypeRepo.findById(medStorage.getMedtype().getId()).orElse(null);
            if (medType == null) {
                medType = new MedType();
            }
            medStorage.setMedtype(medType);
        }
        medRequest.setMedstorage(medStorage);
        return medrequestRepo.save(medRequest);
    }

    @PutMapping("{id}")
    public MedRequest update(@PathVariable("id") MedRequest medRequestFromDb,
                             @RequestBody MedRequest medRequest){
        BeanUtils.copyProperties(medRequest, medRequestFromDb,  "id");
        return medrequestRepo.save(medRequestFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") MedRequest medRequest){
        medrequestRepo.delete(medRequest);
    }
}
