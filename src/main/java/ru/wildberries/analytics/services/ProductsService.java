package ru.wildberries.analytics.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.wildberries.analytics.dto.PriceStateDTO;
import ru.wildberries.analytics.dto.ProductDTO;
import ru.wildberries.analytics.dto.ProductSizeDTO;
import ru.wildberries.analytics.models.PriceState;
import ru.wildberries.analytics.models.Product;
import ru.wildberries.analytics.models.ProductSize;
import ru.wildberries.analytics.repositories.ProductsRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductsService {

    private final ProductsRepository productsRepository;

    @Autowired
    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Transactional
    public void process(String productJson) {
        ObjectMapper mapper = new ObjectMapper();
        ProductDTO productDTO;
        try {
            productDTO = mapper.readValue(productJson, ProductDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Product product = convertToProduct(productDTO);
        productsRepository.save(product);
    }

    private Product convertToProduct(ProductDTO productDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Product product = modelMapper.map(productDTO, Product.class);

        List<ProductSize> sizes = getProductSizes(productDTO);
        product.setSizes(sizes);

        List<PriceState> priceHistoryList = getPriceHistory(productDTO);
        product.setPriceHistory(priceHistoryList);

        return product;
    }

    private List<PriceState> getPriceHistory(ProductDTO productDTO) {
        List<PriceStateDTO> priceHistoryList = productDTO.getPriceHistory();
        List<PriceState> resultList = new ArrayList<>();
        for (PriceStateDTO priceStateDTO : priceHistoryList) {
            PriceState priceState = new PriceState();
            priceState.setTime(priceStateDTO.getTime());
            priceState.setPrice(priceStateDTO.getCurrency().getPrice());
            resultList.add(priceState);
        }
        PriceState currentPriceState = getCurrentPriceState(productDTO);
        resultList.add(currentPriceState);

        return resultList;
    }

    private PriceState getCurrentPriceState(ProductDTO productDTO) {
        PriceState currentPriceState = new PriceState();
        int sizesLength = productDTO.getSizes().size();
        long currentTimeSeconds = (System.currentTimeMillis() / 100000) * 100;
        currentPriceState.setTime(String.valueOf(currentTimeSeconds));
        currentPriceState.setPrice(productDTO.getSizes().get(sizesLength - 1).getPrice().getProduct());

        return currentPriceState;
    }

    private List<ProductSize> getProductSizes(ProductDTO productDTO) {
        List<ProductSize> sizes = new ArrayList<>();
        for (ProductSizeDTO productSizeDTO : productDTO.getSizes()) {
            ProductSize productSize = convertToProductSize(productSizeDTO);
            productSize.setBasicPrice(productSizeDTO.getPrice().getBasic());
            productSize.setDiscountPrice(productSizeDTO.getPrice().getProduct());
            sizes.add(productSize);
        }
        return sizes;
    }

    private ProductSize convertToProductSize(ProductSizeDTO productSizeDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(productSizeDTO, ProductSize.class);
    }
}
