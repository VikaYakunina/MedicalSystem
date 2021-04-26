package ru.yakunina.filmrest.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yakunina.filmrest.Constants;
import ru.yakunina.filmrest.domain.Usr;
import ru.yakunina.filmrest.domain.Views;
import ru.yakunina.filmrest.exceptions.AuthException;
import ru.yakunina.filmrest.repo.UsrRepo;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.jsonwebtoken.Jwts.builder;

@RestController
//@RequestMapping("usr")
public class UsrController {

    private final UsrRepo usrRepo;
    @Autowired
    public UsrController(UsrRepo usrRepo) {
        this.usrRepo = usrRepo;
    }

    //@GetMapping
    @GetMapping("usr")
    @JsonView(Views.IdNameUser.class)     // чтобы показывать только поля у Usr помеченные нотацией @JsonView(Views.IdNameUser.class) и @JsonView(Views.IdUser.class)
    public List<Usr> list(){
        return usrRepo.findAll();
    }

    //@GetMapping("{id}")
    @GetMapping("usr/{id}")
    @JsonView(Views.IdNameUser.class)   // чтобы показывать только поля у Usr помеченные нотацией @JsonView(Views.IdNameUser.class) и @JsonView(Views.IdUser.class)
    public Usr getOne(@PathVariable("id") Usr usr){
        return usr;
    }

    @PostMapping("/register")
    public ResponseEntity create(@RequestBody Usr usr) throws JsonProcessingException {
        String username =  usr.getUsername();
        if (username.length()<3){
            Map<String, String> map = new HashMap<>();
            //map.put("message", "Логин не должен быть менее 3 символов");
            //return new ResponseEntity<>(map, HttpStatus.OK);
            map.put("error", "Логин не должен быть менее 3 символов!");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        if (usr.getPassword().length()<3){
            Map<String, String> map = new HashMap<>();
            //map.put("message", "Пароль не должен быть менее 3 символов");
            //return new ResponseEntity<>(map, HttpStatus.OK);
            map.put("error", "Пароль не должен быть менее 3 символов!");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        Usr findUsr = usrRepo.FindByUsername(username);
        if (findUsr==null){          // если не нашли пользователя  с таким именем в БД
            // шифруем пароль
            String hashedPassword = BCrypt.hashpw(usr.getPassword(), BCrypt.gensalt(10));
            usr.setPassword(hashedPassword);
            usrRepo.save(usr);   // добавляем пользователя в базу
            //map.put("message", "Пользователь зарегистрирован");
            //return new ResponseEntity<>(map, HttpStatus.OK);
            return new ResponseEntity<>(generateJWTToken(usr), HttpStatus.OK); //отправляем токен
        }else{                     // если нашли пользователя в БД
            Map<String, String> map = new HashMap<>();
            //map.put("message", "Такой пользователь уже зарегистрирован");
            //return new ResponseEntity<>(map, HttpStatus.OK);
            map.put("error", "Такой пользователь уже зарегистрирован в системе!");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @PostMapping("/login")
    @JsonView(Views.IdNameUser.class)     // чтобы показывать только поля у Usr помеченные нотацией @JsonView(Views.IdNameUser.class) и @JsonView(Views.IdUser.class)
    public ResponseEntity<Map<String, String>> loginUsr(@RequestBody Usr usr) throws JsonProcessingException {
        String username =  usr.getUsername();
        Usr findUser = usrRepo.FindByUsername(username);
        // проверяем - нашли ли пользователя в БД или его пароль не совпадает с зашифрованным паролем из БД
        if ((findUser == null) || ( !BCrypt.checkpw(usr.getPassword(), findUser.getPassword())) ){
            // если не нашли пользователя в БД или пароль не совпадает с зашифрованным паролем из БД
            Map<String, String> map = new HashMap<>();
            //map.put("message", "Неверный логин или пароль");
            //return new ResponseEntity<>(map, HttpStatus.OK);
            map.put("error", "Неверный логин или пароль");     // если не нашли в БД пользователя, то возвращаем ошибку
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }else {              // если нашли пользователя в БД
            //Map<String, String> map = new HashMap<>();
            //map.put("message", "Успешный вход в систему");
            //return new ResponseEntity<>(map, HttpStatus.OK);
            return new ResponseEntity<>(generateJWTToken(findUser), HttpStatus.OK);  //отправляем токен
        }
        //return findUser;  // можно отправить пользователя который залогинился
    }

    //@PutMapping("{id}")
    @PutMapping("usr/{id}")
    public Usr update(@PathVariable("id") Usr usrFromDb,
                      @RequestBody Usr usr){
        BeanUtils.copyProperties(usr, usrFromDb,  "id");
        return usrRepo.save(usrFromDb);
    }

    //@DeleteMapping("{id}")
    @DeleteMapping("usr/{id}")
    public void delete(@PathVariable("id") Usr usr){
        usrRepo.delete(usr);
    }

    private Map<String, String> generateJWTToken(Usr usr) throws JsonProcessingException {
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("id", usr.getId())
                .claim("username", usr.getUsername())
                .claim("phonenumber", usr.getPhonenumber())
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        //-- еще отправляем usr -- без пароля пароля
        Usr myUsr = new Usr();
        myUsr.setId(usr.getId());
        myUsr.setUsername(usr.getUsername());
        myUsr.setPhonenumber(usr.getPhonenumber());
        myUsr.setPassword("***");
        //Creating the ObjectMapper object   -- jakson в pom
        ObjectMapper mapper = new ObjectMapper();
        //Converting the usr to JSONString
        String jsonString = mapper.writeValueAsString(myUsr);
        map.put("user", jsonString);
        return map;
    }

}

