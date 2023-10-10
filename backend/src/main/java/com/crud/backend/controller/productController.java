package com.crud.backend.controller;

import com.crud.backend.exception.UserNotFoundException;
import com.crud.backend.model.Product;
import com.crud.backend.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@CrossOrigin("http://localhost:3000/")
public class productController {
    @Autowired
    private ProductRepository prodRepository;

    @PostMapping("/products")
    public ResponseEntity<?> newProduct(@RequestBody Product newProduct) {
        Product savedUser = prodRepository.save(newProduct);
        return ResponseEntity.ok("Success");
    }
//        Product newProduct(@RequestBody Product newProduct){
//        return prodRepository.save(newProduct);
//    }


    @GetMapping("/products")
    List<Product> getAllProducts(){
        return  prodRepository.findAll();
    }

    @GetMapping("/products/{id}")
    Product getProductsById(@PathVariable Long id){
        return prodRepository.findById(id)
                .orElseThrow(()->new  UserNotFoundException(id));
    }

    @DeleteMapping("/products/{id}")
    String deleteUser(@PathVariable Long id){
        if(!prodRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }
        prodRepository.deleteById(id);
        return "User With id "  +id+" is Deleted.";
    }
}

