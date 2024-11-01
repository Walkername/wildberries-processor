package ru.wildberries.analytics.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wildberries.analytics.dto.ProductDTO;
import ru.wildberries.analytics.services.ProductsService;

@RestController
@RequestMapping("/process")
public class ProcessorController {

    private final ProductsService productsService;

    @Autowired
    public ProcessorController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> process(
            @RequestBody String productJson
    ) {
        productsService.process(productJson);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
