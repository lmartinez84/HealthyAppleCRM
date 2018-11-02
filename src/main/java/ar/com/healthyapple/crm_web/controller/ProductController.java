package ar.com.healthyapple.crm_web.controller;

import ar.com.healthyapple.crm_web.dto.Product.ProductDto;
import ar.com.healthyapple.crm_web.dto.Product.ProductProfileDto;
import ar.com.healthyapple.crm_web.dto.Product.ProductWithoutProfileDto;
import ar.com.healthyapple.crm_web.exceptions.AlreadyExistException;
import ar.com.healthyapple.crm_web.exceptions.NotFoundException;
import ar.com.healthyapple.crm_web.exceptions.PageDoesNotExistException;
import ar.com.healthyapple.crm_web.model.Product.Product;
import ar.com.healthyapple.crm_web.service.Client.ClientService;
import ar.com.healthyapple.crm_web.service.Product.ProductProfileService;
import ar.com.healthyapple.crm_web.service.Product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Uris.PRODUCTS)
public class ProductController {

    private ProductService productService;

    private EntityDtoConverter entityDtoConverter;

    private ProductProfileService productProfileService;

    private ClientService clientService;

    @Autowired
    public ProductController(ProductService productService, EntityDtoConverter entityDtoConverter, ProductProfileService productProfileService, ClientService clientService) {
        this.productService = productService;
        this.entityDtoConverter = entityDtoConverter;
        this.productProfileService = productProfileService;
        this.clientService = clientService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createProduct(@RequestBody ProductDto productDto) throws AlreadyExistException {
        Product product = productService.create(entityDtoConverter.convertToEntity(productDto, Product.class));
        return entityDtoConverter.convertToDto(product, ProductDto.class);
    }

    @GetMapping(Uris.ID)
    @ResponseStatus(HttpStatus.OK)
    public ProductDto readProduct(@PathVariable String id) throws NotFoundException {

        Product product = productService.read(Long.parseLong(id));
        ProductProfileDto productProfileDto = entityDtoConverter.convertToDto(productProfileService.readByName(product.getProductType().getName()), ProductProfileDto.class);
        ProductDto productDto = entityDtoConverter.convertToDto(product, ProductDto.class);
        productDto.setProfile(productProfileDto);
        return productDto;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto updateProduct(@RequestBody ProductDto productDto) throws NotFoundException {
        Product product = productService.update(entityDtoConverter.convertToEntity(productDto, Product.class));
        return entityDtoConverter.convertToDto(product, ProductDto.class);
    }

    @DeleteMapping(Uris.ID)
    @ResponseStatus(HttpStatus.OK)
    public void deleteProductById(@PathVariable Long id, @RequestParam Long client_id) throws NotFoundException {
        clientService.deleteClientProductById(client_id, id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductWithoutProfileDto> findAllProducts(Pageable pageable) throws PageDoesNotExistException {

        Page<Product> productPage = this.productService.findAll(pageable);

        Page<ProductWithoutProfileDto> productDtoPage = productPage.map(product -> entityDtoConverter.convertToThinProductDto(product, ProductWithoutProfileDto.class));

        return productDtoPage;
    }


    @GetMapping(Uris.NAMES + Uris.CLIENTS + Uris.ID)
    @ResponseStatus(HttpStatus.OK)
    public Map<Long, String> findProductsIdAndDescriptionByClientId(@PathVariable Long id) throws NotFoundException {
        return this.clientService.findProductsIdAndDescriptionByClientId(id);
    }


    @GetMapping(Uris.CLIENTS + Uris.ID)
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> findProductsByClientId(@PathVariable Long id) throws NotFoundException {
        return this.clientService.findProductsByClientId(id).stream()
                .map(product -> entityDtoConverter.convertToDto(product, ProductDto.class))
                .collect(Collectors.toList());
    }


}
