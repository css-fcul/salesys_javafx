package pt.ul.fc.css.salesys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pt.ul.fc.css.salesys.mapper.RestDtoMapper;
import pt.ul.fc.css.salesys.services.ProductService;
import pt.ul.fc.css.salesys.services.SaleService;

@Controller
public class WebController {

    private final ProductService productService;
    private final SaleService saleService;
    private final RestDtoMapper restDtoMapper;

    public WebController(ProductService productService,
                         SaleService saleService,
                         RestDtoMapper restDtoMapper) {
        this.productService = productService;
        this.saleService = saleService;
        this.restDtoMapper = restDtoMapper;
    }

    // ----------------- Customers -----------------
    @GetMapping("/web/customers")
    public String customers(Model model) {
        //TODO
        return "";
    }

    @GetMapping("/web/customers/new")
    public String newCustomer() {
        //TODO
        return "";
    }

    @PostMapping("/web/customers")
    public String createCustomer(@RequestParam("vatNumber") String vatNumber,
                                 @RequestParam("designation") String designation,
                                 @RequestParam(value = "phone", required = false) String phone,
                                 RedirectAttributes ra,
                                 Model model) {
        //TODO
        return "";
    }

    @GetMapping("/web/customers/{vat}/sale")
    public String customerSale(@PathVariable("vat") String vat, RedirectAttributes ra) {
        //TODO
        return "";
    }

    // ----------------- Sales -----------------
    @GetMapping("/web/sales")
    public String sales(Model model) {
        var sales = restDtoMapper.mapToSaleResponseDtos(saleService.getAllSales());
        model.addAttribute("sales", sales);
        return "sales"; 
    }

    @GetMapping("/web/sales/new")
    public String newSale() {
        return "sale-form";
    }

    @PostMapping("/web/sales")
    public String createSale(@RequestParam("vat") String vat,
                             RedirectAttributes ra,
                             Model model) {
        try {
            saleService.createSale(vat);
            ra.addFlashAttribute("message", "Sale created for VAT " + vat + ".");
            return "redirect:/web/sales";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "sale-form";
        }
    }

    @GetMapping("/web/sales/{saleId}")
    public String saleDetails(@PathVariable("saleId") long saleId, Model model, RedirectAttributes ra) {
        return saleService.getSaleById(saleId)
                .map(sale -> {
                    var saleDto = restDtoMapper.mapToSaleResponseDto(sale);
                    var products = restDtoMapper.mapToProductResponseDtos(productService.getAllProducts());
                    
                    model.addAttribute("sale", saleDto);
                    model.addAttribute("products", products);
                    model.addAttribute("saleOpen", sale.getStatus() == pt.ul.fc.css.salesys.entities.Sale.SaleStatus.OPEN);
                    return "sale-details";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("error", "Sale not found.");
                    return "redirect:/web/sales";
                });
    }

    @PostMapping("/web/sales/{saleId}/product")
    public String addProductToSale(@PathVariable("saleId") long saleId,
                                   @RequestParam("productCode") int productCode,
                                   @RequestParam("quantity") int quantity,
                                   RedirectAttributes ra) {
        try {
            saleService.addProductToSale(saleId, productCode, quantity);
            ra.addFlashAttribute("message", "Product added to sale.");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/web/sales/" + saleId;
    }

    @PostMapping("/web/sales/{saleId}/close")
    public String closeSale(@PathVariable("saleId") long saleId, RedirectAttributes ra) {
        try {
            saleService.closeSale(saleId);
            ra.addFlashAttribute("message", "Sale closed.");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/web/sales/" + saleId;
    }
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("appName", "SaleSys");
        return "index";
    }

    @GetMapping("/web/products")
    public String products(Model model) {
        var products = restDtoMapper.mapToProductResponseDtos(productService.getAllProducts());
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/web/products/new")
    public String newProduct(Model model) {
        return "product-form";
    }

    @PostMapping("/web/products")
    public String createProduct(@RequestParam("code") int code,
                                @RequestParam("description") String description,
                                @RequestParam("faceValue") double faceValue,
                                @RequestParam("stockQuantity") int stockQuantity,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        try {
            productService.addProduct(code, description, faceValue, stockQuantity);
            redirectAttributes.addFlashAttribute("message", "Product created successfully.");
            return "redirect:/web/products";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "product-form";
        }
    }
}
