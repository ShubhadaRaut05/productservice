package com.shubhada.productservice.controllers;

import com.shubhada.productservice.dtos.*;
import com.shubhada.productservice.exceptions.NotFoundException;
import com.shubhada.productservice.models.Category;
import com.shubhada.productservice.models.Product;
import com.shubhada.productservice.repositories.ProductRepository;
import com.shubhada.productservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    //inversion of control
    private final ProductService productService;
    private ProductRepository productRepository;
    public ProductController(ProductService productService,ProductRepository productRepository){

        this.productService=productService;
        this.productRepository=productRepository;
    }
    public List<ProductResponseDTO> convertToProductResponseDTO(List<Product> products){
        List<ProductResponseDTO> newProducts=new ArrayList<>();
        for(Product pr:products){
            ProductResponseDTO product=new ProductResponseDTO();
            product.setId(pr.getId());
            product.setTitle(pr.getTitle());
            product.setPrice(pr.getPrice());
            product.setCategory(pr.getCategory().getName());
            product.setDescription(pr.getDescription());
            product.setImage(pr.getImageUrl());
            newProducts.add(product);
        }
        return newProducts;
    }
    public ProductResponseDTO convertToProductDTO(Optional<Product> products){
        ProductResponseDTO product=new ProductResponseDTO();
        product.setId(products.get().getId());
        product.setTitle(products.get().getTitle());
        product.setPrice(products.get().getPrice());
        product.setCategory(products.get().getCategory().getName());
        product.setDescription(products.get().getDescription());
        product.setImage(products.get().getImageUrl());
        return product;
    }
    @GetMapping("")
    public List<ProductResponseDTO> getAllProducts(){
        List<Product> products=new ArrayList<>();
        try {
            products= productService.getAllProducts();

        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        //return "Getting All Products";
        return convertToProductResponseDTO(products);
    }
    @GetMapping("/{productId}") //GetSingleProductResponseDTO
    public ResponseEntity<ProductResponseDTO> getSingleProduct(@PathVariable("productId") Long productId) throws NotFoundException {
        /*GetSingleProductResponseDTO responseDTO=new GetSingleProductResponseDTO();
        responseDTO.setProduct(
                productService.getSingleProduct(productId)
        );
         return responseDTO;*/
        MultiValueMap<String,String> headers=new LinkedMultiValueMap<>();
        headers.add(

                "auth-token","noaccess4youheyhey"

        );

      /*  try {
            Optional<Product> productOptional  = productService.getSingleProduct(productId);
        }catch(Exception e){
            throw new NotFoundException("No product with product Id: "+productId);
        }*/
       /* if(productOptional.isEmpty()){
            throw new NotFoundException("No product with product Id: "+productId);
        }*/
      ResponseEntity<ProductResponseDTO> response=new ResponseEntity(
              convertToProductDTO(Optional.ofNullable(productService.getSingleProduct(productId).orElseThrow(() ->
                      new NotFoundException("No product with product Id: " + productId)))),
              headers,
              HttpStatus.OK
      );

        return response;
    }
    @PostMapping("")
    public ResponseEntity<ProductResponseDTO> addNewProduct(@RequestBody AddNewProductRequestDTO product){
        Product products=new Product();
        //products.setId(product.getId());
        products.setCategory(new Category());
        products.getCategory().setName(product.getCategory());
        products.setTitle(product.getTitle());
        products.setPrice(product.getPrice());
        products.setDescription(product.getDescription());
        products.setImageUrl(product.getImage());

        ProductResponseDTO newProduct= convertToProductDTO(Optional.ofNullable(productService.addNewProduct(products)));
       /* Product newProduct=new Product();
        newProduct.setDescription(product.getDescription());
        newProduct.setImageUrl(product.getImage());
        newProduct.setTitle(product.getTitle());
        newProduct.setPrice(product.getPrice());
       newProduct=productRepository.save(newProduct);*/
       ResponseEntity<ProductResponseDTO> response=new ResponseEntity<>(newProduct,HttpStatus.OK);
        return response;
      //  return "Adding New Product with "+productDTO;
    }
    //assignment take requestBody

    @PatchMapping("/{productId}")
    public Product updateProduct(@PathVariable("productId") Long productId,
                                 @RequestBody ProductDTO productDTO){
        //return "Updating a Product with id: "+productId +" and with data: "+productDTO;
        //convert productDTO object into Product object
        Product product=new Product();
        product.setId(productDTO.getId());
        product.setCategory(new Category());
     //   product.getCategory().setName(productDTO.getCategory());
        product.setTitle(productDTO.getTitle());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        return productService.updateProduct(productId,product);
    }
    @PutMapping("/{productId}")
    public Product replaceProduct(@PathVariable("productId") Long productId,
                                  @RequestBody ProductDTO productDTO){
        Product product=new Product();
        product.setId(productDTO.getId());
        product.setCategory(new Category());
      //  product.getCategory().setName(productDTO.getCategory());
        product.setTitle(productDTO.getTitle());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
      return productService.replaceProduct(productId,product);
    }
    @DeleteMapping("/{productId}")
    public Optional<Product> deleteProduct(@PathVariable("productId") Long productId) throws NotFoundException {

        return Optional.ofNullable(productService.deleteProduct(productId).orElseThrow(() -> new NotFoundException("Product not found with id: " + productId)));
        //return "hello";
       /* return "Deleting a Product with id: "+productId;*/
    }
    /*@ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(Exception exception){
     ErrorResponseDTO errorResponseDTO=new ErrorResponseDTO();
     errorResponseDTO.setErrorMessage(exception.getMessage());
     return new ResponseEntity<>(errorResponseDTO,HttpStatus.NOT_FOUND);
    }*/
}
