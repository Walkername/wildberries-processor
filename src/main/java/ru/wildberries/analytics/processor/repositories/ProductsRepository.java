package ru.wildberries.analytics.processor.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.wildberries.analytics.processor.models.Product;

@Repository
public interface ProductsRepository extends MongoRepository<Product, Integer> {

}
