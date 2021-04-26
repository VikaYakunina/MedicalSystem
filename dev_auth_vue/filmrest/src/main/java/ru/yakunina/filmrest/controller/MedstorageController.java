package ru.yakunina.filmrest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yakunina.filmrest.domain.MedIssue;
import ru.yakunina.filmrest.domain.MedRequest;
import ru.yakunina.filmrest.domain.MedStorage;
import ru.yakunina.filmrest.domain.MedType;
import ru.yakunina.filmrest.repo.MedissueRepo;
import ru.yakunina.filmrest.repo.MedrequestRepo;
import ru.yakunina.filmrest.repo.MedstorageRepo;
import ru.yakunina.filmrest.repo.MedtypeRepo;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("medstorage")
public class
MedstorageController {
    private final MedstorageRepo medstorageRepo;
    private MedtypeRepo medtypeRepo;
    private MedissueRepo medissueRepo;
    private MedrequestRepo medrequestRepo;

    @Autowired
    public MedstorageController(MedstorageRepo medstorageRepo,
                                MedtypeRepo medtypeRepo,
                                MedissueRepo medissueRepo,
                                MedrequestRepo medrequestRepo){
        this.medstorageRepo = medstorageRepo;
        this.medtypeRepo = medtypeRepo;
        this.medissueRepo = medissueRepo;
        this.medrequestRepo = medrequestRepo;
    }

    @GetMapping  // получение всех лекарств
    public List<MedStorage> list(@RequestParam(name="name", required=false, defaultValue="") String name,
                                 @RequestParam(name="medtypeid",required=false, defaultValue="") Long medtypeid){

        if (!name.equals("") && medtypeid==null){    // если задан параметр name
            return medstorageRepo.FindByName(name);       // находим лекарство по названию
        }
        if (name.equals("") && medtypeid != null ){    // если задан параметр medtypeid
            MedType medType = medtypeRepo.findById(medtypeid).orElse(null);
            return medstorageRepo.FindByMedtypeId(medType);       // находим лекарство типу
        }
        if (!name.equals("") && medtypeid != null){    // если задан параметр name и medtypeid
            MedType medType = medtypeRepo.findById(medtypeid).orElse(null);
            return medstorageRepo.FindByNameAndMedtypeId(name, medType); // находим лекарство названию и типу
        }
        return medstorageRepo.findAll();
    }

    @GetMapping("{id}")
    public MedStorage getOne(@PathVariable("id") MedStorage medStorage){
        return medStorage;
    }

    @PostMapping
    public MedStorage create(@RequestBody MedStorage medStorage) throws JsonProcessingException {
        //ObjectMapper mapper = new ObjectMapper();
        //String jsonString = mapper.writeValueAsString(medStorage);
        //System.out.println(jsonString);
        MedType medType = medtypeRepo.findById(medStorage.getMedtype().getId()).orElse(null);
        if(medType == null){
            medType = new MedType();
        }
        medStorage.setMedtype(medType);
        return medstorageRepo.save(medStorage);
    }

    @PutMapping("{id}")
    public MedStorage update(@PathVariable("id") MedStorage medStorageFromDb,
                                @RequestBody MedStorage medStorage ){
        BeanUtils.copyProperties(medStorage, medStorageFromDb,  "id");
        return medstorageRepo.save(medStorageFromDb);
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") MedStorage medStorage){
        List<MedIssue> findMedIssue = medissueRepo.FindByMedstorage(medStorage);
        List<MedRequest> findMedRequest = medrequestRepo.FindByMedstorage(medStorage);
        if (findMedIssue.size()>0 || findMedRequest.size()>0){   // есть выданные лекарства или заявки
            Map<String, String> map = new HashMap<>();
            map.put("error", "Это лекарство нельзя удалить!");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }else {
            medstorageRepo.delete(medStorage);
            Map<String, String> map = new HashMap<>();
            map.put("error", "");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }
}
