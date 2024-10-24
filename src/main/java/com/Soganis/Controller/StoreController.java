package com.Soganis.Controller;

import com.Soganis.Entity.Store;
import com.Soganis.Entity.User;
import com.Soganis.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "https://www.soganiuniforms.shop")
@RestController
@RequestMapping("/api/store")
public class StoreController {

    @Autowired
    private UserService userService;

    @PostMapping("/createStore")
    public String addStore(@RequestBody Store store)
    {
        String status=userService.createStore(store);
        return  status;
    }

    @GetMapping("/validate")
    public String validateStoreId(@RequestParam("storeId") String storeId)
    {
        String status=userService.validateStoreId(storeId);
        return status;
    }

    @PostMapping("/getAllStores")
    public ResponseEntity<List<Store>> getStores() {
        List<Store> stores = userService.getStore();

        if (stores.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(stores, HttpStatus.OK);
        }
    }

    @GetMapping("/validate/userId")
    public String validateUserId(@RequestParam("userId") String userId)
    {
        String status=userService.validateUserId(userId);
        return status;
    }


    @GetMapping("/getAllUser")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = userService.getAllUser();

        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }


    @PostMapping("/createUser")
    public  String createUser(@RequestBody User user)
    {
        String status=userService.createUser(user);
        return  status;
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> userValidate(@RequestBody User user) {
        User validateUser = userService.validateUser(user);

        if (validateUser != null) {
            return new ResponseEntity<>(validateUser, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }
}





