package ru.yakunina.filmrest.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yakunina.filmrest.domain.MedStorage;
import ru.yakunina.filmrest.domain.MedType;
import ru.yakunina.filmrest.repo.MedstorageRepo;
import ru.yakunina.filmrest.repo.MedtypeRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("medtype")
public class MedtypeController {
    private final MedtypeRepo medtypeRepo;
    private MedstorageRepo medstorageRepo;

    @Autowired
    public MedtypeController(MedtypeRepo medtypeRepo,
                             MedstorageRepo medstorageRepo) {
        this.medtypeRepo = medtypeRepo;
        this.medstorageRepo = medstorageRepo;
    }

    @GetMapping
    public List<MedType> list(@RequestParam(name="typename", required=false, defaultValue="") String typename){
        if (!typename.equals("")){                    // если задан параметр typename
            return medtypeRepo.FindByName(typename); // находим по названию типа лекарства
        }
        return medtypeRepo.findAll();
    }

    @GetMapping("{id}")
    public MedType getOne(@PathVariable("id") MedType medType){
        return medType;
    }

    @PostMapping
    public MedType create(@RequestBody MedType medType){
        return medtypeRepo.save(medType);
    }

    @PutMapping("{id}")
    public MedType update(@PathVariable("id") MedType medTypeFromDb,
                             @RequestBody MedType medType){
        BeanUtils.copyProperties(medType, medTypeFromDb,  "id");
        return medtypeRepo.save(medTypeFromDb);
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") MedType medType){
        List<MedStorage> findCinemasession = medstorageRepo.FindByMedtypeId(medType);
        if (findCinemasession.size()>0){   // лекарства с таким типом есть
            Map<String, String> map = new HashMap<>();
            map.put("error", "Этот тип лекарства нельзя удалить!");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }else {
            medtypeRepo.delete(medType);
            Map<String, String> map = new HashMap<>();
            map.put("error", "");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }
}
