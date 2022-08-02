package com.usu.satu.controller;

import com.usu.satu.exeption.AcceptedException;
import com.usu.satu.helper.ResponseBody;
import com.usu.satu.model.User;
import com.usu.satu.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @GetMapping("/all")
    public ResponseEntity getAllUsers(){
        try {
            List<User> data = userService.getUsers();
            if(data != null){
                return new ResponseBody().found("success get users", data);
            } else {
                return new ResponseBody().notFound("no user data", null);
            }
        }
        catch (Exception e){
            logger.error("getAllUsers : "+e.getMessage());
            return new ResponseBody().failed("failed get user", e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity createUser(String userId){
        try {
            User data = userService.addUser(userId);
            if(data != null){
                return new ResponseBody().found("success create user", data);
            } else {
                return new ResponseBody().notFound("no user data", null);
            }
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage(), null);
        } catch (Exception e){
            logger.error("createUser : "+e.getMessage());
            return new ResponseBody().failed("failed create user", e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity removeUser(String userId){
        try {
            User data = userService.deleteUser(userId);
            if(data != null){
                return new ResponseBody().found("success create user", data);
            } else {
                return new ResponseBody().notFound("no user data", null);
            }
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage(), null);
        } catch (Exception e){
            logger.error("createUser : "+e.getMessage());
            return new ResponseBody().failed("failed create user", e.getMessage());
        }
    }
}
