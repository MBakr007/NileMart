import controller.ProductController;
import dao.product.InMemoryProductDao;
import dao.product.ProductDao;
import service.ProductService;
import view.ProductViewer;

public class Main {

    void main() {

        ProductViewer viewer = new ProductViewer();
        ProductDao productDao = new InMemoryProductDao();
        ProductService productService = new ProductService(productDao);

        ProductController productController = new ProductController(viewer,  productService);

        productController.listAvailableProducts();
    }
}
