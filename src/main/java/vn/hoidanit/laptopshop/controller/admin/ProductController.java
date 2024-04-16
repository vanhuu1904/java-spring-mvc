package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UploadService;

@Controller
public class ProductController {

    private UploadService uploadService;
    private ProductService productService;

    public ProductController(UploadService uploadService, ProductService productService) {
        this.uploadService = uploadService;
        this.productService = productService;
    }

    @GetMapping("/admin/product")
    public String getProduct(Model model) {
        List<Product> products = this.productService.fetchProducts();
        model.addAttribute("products", products);
        return "admin/product/show";
    }

    @GetMapping("/admin/product/{id}")
    public String getMethodName(@PathVariable long id, Model model) {
        System.out.println(">>>check id" + id);
        Product product = this.productService.getProductById(id).get();
        model.addAttribute("product", product);
        model.addAttribute("id", id);
        return "/admin/product/detail";
    }
    

    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String  createProductPage(Model model,@ModelAttribute("newProduct") @Valid Product product, BindingResult newProductBindingResult, @RequestParam("hoidanitFile") MultipartFile file){

            List<FieldError> errors = newProductBindingResult.getFieldErrors();
            for (FieldError error : errors) {
                System.out.println(">>>>" + error.getField() + " - " + error.getDefaultMessage());
            }

            // validate
            if (newProductBindingResult.hasErrors()) {
                return "admin/product/create";
            }

            String image = this.uploadService.handleSaveUploadFile(file, "product");
            product.setImage(image);
            this.productService.createProduct(product);

        return "redirect:/admin/product";
    }

    @RequestMapping("/admin/product/update/{id}") // GET
    public String getUpdateProduct(Model model, @PathVariable long id) {
        Product currentProduct = this.productService.getProductById(id).get();
        model.addAttribute("newProduct", currentProduct);
        return "admin/product/update";
    }
    @PostMapping("/admin/product/update")
    public String  updateProductPage(Model model,@ModelAttribute("newProduct") @Valid Product product, BindingResult newProductBindingResult, @RequestParam("hoidanitFile") MultipartFile file){

            List<FieldError> errors = newProductBindingResult.getFieldErrors();
            for (FieldError error : errors) {
                System.out.println(">>>>" + error.getField() + " - " + error.getDefaultMessage());
            }

            // validate
            if (newProductBindingResult.hasErrors()) {
                return "admin/product/update";
            }
            String image = this.uploadService.handleSaveUploadFile(file, "product");
            Product updateProduct = this.productService.getProductById(product.getId()).get();
            if(updateProduct != null){
                updateProduct.setName(product.getName());
                updateProduct.setDetailDesc(product.getDetailDesc());
                updateProduct.setShortDesc(product.getShortDesc());
                updateProduct.setQuantity(product.getQuantity());
                updateProduct.setPrice(product.getPrice());
                updateProduct.setSold(product.getSold());
                // updateProduct.setImage(product.getImage());
                this.productService.createProduct(updateProduct);
            }

        return "redirect:/admin/product";
    }
    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        // product product = new product();
        // product.setId(id);
        model.addAttribute("newProduct", new Product());
        return "admin/product/delete";
    }

    @PostMapping("/admin/product/delete")
    public String deleteProduct(Model model, @ModelAttribute("newProduct") Product product) {
        this.productService.deleteProduct(product.getId());
        return "redirect:/admin/product";
    }
}
