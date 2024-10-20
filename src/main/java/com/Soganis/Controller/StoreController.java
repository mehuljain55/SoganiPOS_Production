package com.Soganis.Controller;

import com.Soganis.Entity.Store;
import com.Soganis.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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




}
