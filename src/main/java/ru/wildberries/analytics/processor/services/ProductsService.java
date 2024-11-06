package ru.wildberries.analytics.processor.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.wildberries.analytics.processor.dto.PriceStateDTO;
import ru.wildberries.analytics.processor.dto.ProductDTO;
import ru.wildberries.analytics.processor.dto.ProductSizeDTO;
import ru.wildberries.analytics.processor.models.PriceState;
import ru.wildberries.analytics.processor.models.Product;
import ru.wildberries.analytics.processor.models.ProductSize;
import ru.wildberries.analytics.processor.repositories.ProductsRepository;

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
            Product product = convertToProduct(productDTO);
            productsRepository.save(product);
        } catch (Exception ignored) {
        }
    }

    private Product convertToProduct(ProductDTO productDTO) throws Exception {
        Product product = new Product();
        try {
            ModelMapper modelMapper = new ModelMapper();
            product = modelMapper.map(productDTO, Product.class);

            List<ProductSize> sizes = getProductSizes(productDTO);
            product.setSizes(sizes);

            List<PriceState> priceHistoryList = getPriceHistory(productDTO);
            product.setPriceHistory(priceHistoryList);
        } catch (Exception e) {
            throw new Exception();
        }

        return product;
    }

    private List<PriceState> getPriceHistory(ProductDTO productDTO) throws Exception {
        List<PriceStateDTO> priceHistoryList = productDTO.getPriceHistory();
        List<PriceState> resultList = new ArrayList<>();
        try {
            for (PriceStateDTO priceStateDTO : priceHistoryList) {
                PriceState priceState = new PriceState();
                priceState.setTime(priceStateDTO.getTime());
                priceState.setPrice(priceStateDTO.getCurrency().getPrice());
                resultList.add(priceState);
            }
            if (!productDTO.getSizes().isEmpty()) {
                PriceState currentPriceState = getCurrentPriceState(productDTO);
                resultList.add(currentPriceState);
            }
        } catch (Exception e) {
            throw new Exception();
        }

        return resultList;
    }

    private PriceState getCurrentPriceState(ProductDTO productDTO) throws Exception {
        PriceState currentPriceState = new PriceState();
        try {
            int sizesLength = productDTO.getSizes().size();
            long currentTimeSeconds = (System.currentTimeMillis() / 100000) * 100;
            currentPriceState.setTime(String.valueOf(currentTimeSeconds));
            currentPriceState.setPrice(productDTO.getSizes().get(sizesLength - 1).getPrice().getProduct());
        } catch (Exception e) {
            throw new Exception();
        }

        return currentPriceState;
    }

    private List<ProductSize> getProductSizes(ProductDTO productDTO) throws Exception {
        List<ProductSize> sizes = new ArrayList<>();
        try {
            for (ProductSizeDTO productSizeDTO : productDTO.getSizes()) {
                ProductSize productSize = convertToProductSize(productSizeDTO);
                productSize.setBasicPrice(productSizeDTO.getPrice().getBasic());
                productSize.setDiscountPrice(productSizeDTO.getPrice().getProduct());
                sizes.add(productSize);
            }
        } catch (Exception e) {
            throw new Exception();
        }

        return sizes;
    }

    private ProductSize convertToProductSize(ProductSizeDTO productSizeDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(productSizeDTO, ProductSize.class);
    }
}
