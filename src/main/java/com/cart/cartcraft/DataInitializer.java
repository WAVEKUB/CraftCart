package com.cart.cartcraft;

import com.cart.cartcraft.enums.OrderStatus;
import com.cart.cartcraft.model.*;
import com.cart.cartcraft.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.rowset.serial.SerialBlob;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

/**
 * DataInitializer seeds the database with realistic mock data on startup.
 * All insert operations are idempotent — data is only created if it doesn't
 * already exist, so restarting the application will never duplicate records.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final RoleRepository     roleRepository;
    private final UserRepository     userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository  productRepository;
    private final ImageRepository    imageRepository;
    private final CartRepository     cartRepository;
    private final OrderRepository    orderRepository;
    private final PasswordEncoder    passwordEncoder;

    // Base URL pattern for image downloads (matches ImageController endpoint)
    private static final String DOWNLOAD_URL_PREFIX = "/api/v1/images/image/download/";

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedRoles();
        seedUsers();
        seedCategories();
        seedProducts();
        seedCartsAndOrders();
        System.out.println("✅  Mock data seeding complete.");
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  ROLES
    // ──────────────────────────────────────────────────────────────────────────
    private void seedRoles() {
        createRoleIfAbsent("ROLE_ADMIN");
        createRoleIfAbsent("ROLE_USER");
    }

    private void createRoleIfAbsent(String name) {
        if (!roleRepository.existsByName(name)) {
            roleRepository.save(new Role(name));
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  USERS
    // ──────────────────────────────────────────────────────────────────────────
    private void seedUsers() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        Role userRole  = roleRepository.findByName("ROLE_USER");

        createUserIfAbsent("admin@craftcart.com", "Admin", "CraftCart",
                "Admin@1234", Set.of(adminRole, userRole));

        createUserIfAbsent("alice@example.com",   "Alice",   "Johnson",  "Alice@1234",  Set.of(userRole));
        createUserIfAbsent("bob@example.com",     "Bob",     "Williams", "Bob@1234",    Set.of(userRole));
        createUserIfAbsent("carol@example.com",   "Carol",   "Smith",    "Carol@1234",  Set.of(userRole));
        createUserIfAbsent("david@example.com",   "David",   "Brown",    "David@1234",  Set.of(userRole));
        createUserIfAbsent("emma@example.com",    "Emma",    "Davis",    "Emma@1234",   Set.of(userRole));
    }

    private void createUserIfAbsent(String email, String firstName, String lastName,
                                    String rawPassword, Set<Role> roles) {
        if (!userRepository.existsByEmail(email)) {
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setRoles(roles);
            userRepository.save(user);
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  CATEGORIES
    // ──────────────────────────────────────────────────────────────────────────
    private void seedCategories() {
        List.of("Electronics", "Clothing", "Home & Kitchen",
                "Books", "Sports & Outdoors", "Beauty & Personal Care",
                "Toys & Games", "Automotive")
                .forEach(name -> {
                    if (!categoryRepository.existsByName(name)) {
                        categoryRepository.save(new Category(name));
                    }
                });
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  PRODUCTS  (with images)
    // ──────────────────────────────────────────────────────────────────────────
    private void seedProducts() {
        Category electronics = categoryRepository.findByName("Electronics");
        Category clothing    = categoryRepository.findByName("Clothing");
        Category homeKitchen = categoryRepository.findByName("Home & Kitchen");
        Category books       = categoryRepository.findByName("Books");
        Category sports      = categoryRepository.findByName("Sports & Outdoors");
        Category beauty      = categoryRepository.findByName("Beauty & Personal Care");
        Category toys        = categoryRepository.findByName("Toys & Games");
        Category automotive  = categoryRepository.findByName("Automotive");

        // ── Electronics ───────────────────────────────────────────────────────
        createProductIfAbsent(
                "Wireless Noise-Cancelling Headphones", "Sony",
                new BigDecimal("349.99"), 45,
                "Premium over-ear headphones with 30-hour battery life, multipoint Bluetooth, and industry-leading noise cancellation.",
                electronics,
                "headphones.jpg",
                "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&q=80");

        createProductIfAbsent(
                "4K Smart LED TV 55\"", "Samsung",
                new BigDecimal("799.99"), 20,
                "55-inch 4K UHD Smart TV with HDR10+, built-in Alexa, and crystal-clear picture quality.",
                electronics,
                "smart-tv.jpg",
                "https://images.unsplash.com/photo-1593784991095-a205069470b6?w=600&q=80");

        createProductIfAbsent(
                "Mechanical Gaming Keyboard", "Corsair",
                new BigDecimal("129.99"), 80,
                "Full-size mechanical keyboard with Cherry MX Red switches, per-key RGB backlighting, and aluminium frame.",
                electronics,
                "keyboard.jpg",
                "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&q=80");

        createProductIfAbsent(
                "Portable Bluetooth Speaker", "JBL",
                new BigDecimal("99.99"), 120,
                "Waterproof portable speaker with 12-hour playtime, deep bass, and built-in power bank.",
                electronics,
                "speaker.jpg",
                "https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=600&q=80");

        createProductIfAbsent(
                "True Wireless Earbuds", "Apple",
                new BigDecimal("199.99"), 200,
                "AirPods Pro with Active Noise Cancellation, Transparency mode, and MagSafe charging case.",
                electronics,
                "earbuds.jpg",
                "https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=600&q=80");

        createProductIfAbsent(
                "Smartwatch Series 9", "Apple",
                new BigDecimal("449.99"), 60,
                "GPS + Cellular smartwatch with health monitoring, crash detection, and all-day battery life.",
                electronics,
                "smartwatch.jpg",
                "https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=600&q=80");

        // ── Clothing ──────────────────────────────────────────────────────────
        createProductIfAbsent(
                "Classic Fit Oxford Shirt", "Ralph Lauren",
                new BigDecimal("89.99"), 150,
                "100% cotton Oxford shirt with button-down collar, available in multiple colours.",
                clothing,
                "oxford-shirt.jpg",
                "https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=600&q=80");

        createProductIfAbsent(
                "Slim-Fit Denim Jeans", "Levi's",
                new BigDecimal("59.99"), 200,
                "511 slim-fit jeans in stretch denim for all-day comfort, classic 5-pocket styling.",
                clothing,
                "jeans.jpg",
                "https://images.unsplash.com/photo-1542272604-787c3835535d?w=600&q=80");

        createProductIfAbsent(
                "Women's Running Jacket", "Nike",
                new BigDecimal("79.99"), 90,
                "Lightweight, water-resistant running jacket with reflective details for low-light visibility.",
                clothing,
                "running-jacket.jpg",
                "https://images.unsplash.com/photo-1539109136881-3be0616acf4b?w=600&q=80");

        createProductIfAbsent(
                "Premium Hoodie", "Adidas",
                new BigDecimal("64.99"), 110,
                "Soft-fleece pullover hoodie with kangaroo pocket and adjustable drawcord hood.",
                clothing,
                "hoodie.jpg",
                "https://images.unsplash.com/photo-1620799140408-edc6dcb6d633?w=600&q=80");

        // ── Home & Kitchen ────────────────────────────────────────────────────
        createProductIfAbsent(
                "Stainless Steel Cookware Set", "Cuisinart",
                new BigDecimal("279.99"), 35,
                "12-piece stainless steel cookware set with impact-bonded bases for even heating.",
                homeKitchen,
                "cookware.jpg",
                "https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=600&q=80");

        createProductIfAbsent(
                "Espresso Machine", "De'Longhi",
                new BigDecimal("449.99"), 25,
                "Automatic espresso machine with built-in milk frother, 15-bar pressure, and 1.5 L tank.",
                homeKitchen,
                "espresso-machine.jpg",
                "https://images.unsplash.com/photo-1510591509098-f4fdc6d0ff04?w=600&q=80");

        createProductIfAbsent(
                "Robot Vacuum Cleaner", "iRobot",
                new BigDecimal("399.99"), 40,
                "Roomba i3 with smart mapping, auto-adjust suction, and compatible with Alexa & Google Home.",
                homeKitchen,
                "robot-vacuum.jpg",
                "https://images.unsplash.com/photo-1558317374-067fb5f30001?w=600&q=80");

        createProductIfAbsent(
                "Bamboo Cutting Board Set", "OXO",
                new BigDecimal("39.99"), 300,
                "3-piece bamboo cutting board set with juice groove and non-slip feet.",
                homeKitchen,
                "cutting-board.jpg",
                "https://images.unsplash.com/photo-1588165171080-c89acfa5ee83?w=600&q=80");

        // ── Books ─────────────────────────────────────────────────────────────
        createProductIfAbsent(
                "Clean Code", "Robert C. Martin",
                new BigDecimal("34.99"), 500,
                "A handbook of agile software craftsmanship. Essential reading for every developer.",
                books,
                "clean-code.jpg",
                "https://images.unsplash.com/photo-1532012197267-da84d127e765?w=600&q=80");

        createProductIfAbsent(
                "The Pragmatic Programmer", "David Thomas & Andrew Hunt",
                new BigDecimal("44.99"), 400,
                "20th anniversary edition — your journey to mastery in software development.",
                books,
                "pragmatic-programmer.jpg",
                "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&q=80");

        createProductIfAbsent(
                "Designing Data-Intensive Applications", "Martin Kleppmann",
                new BigDecimal("54.99"), 350,
                "The big ideas behind reliable, scalable, and maintainable systems.",
                books,
                "ddia-book.jpg",
                "https://images.unsplash.com/photo-1553729459-efe14ef6055d?w=600&q=80");

        // ── Sports & Outdoors ─────────────────────────────────────────────────
        createProductIfAbsent(
                "Yoga Mat Premium", "Manduka",
                new BigDecimal("89.99"), 180,
                "PRO 6mm thick yoga mat with lifetime guarantee, non-slip texture, and superior cushioning.",
                sports,
                "yoga-mat.jpg",
                "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=600&q=80");

        createProductIfAbsent(
                "Mountain Bike Helmet", "Giro",
                new BigDecimal("119.99"), 75,
                "MIPS-equipped mountain bike helmet with 22 vents and BOA fit system.",
                sports,
                "bike-helmet.jpg",
                "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=600&q=80");

        createProductIfAbsent(
                "Resistance Bands Set", "Fit Simplify",
                new BigDecimal("24.99"), 500,
                "Set of 5 resistance bands with carrying case, suitable for all fitness levels.",
                sports,
                "resistance-bands.jpg",
                "https://images.unsplash.com/photo-1517963879433-6ad2b056d712?w=600&q=80");

        // ── Beauty & Personal Care ────────────────────────────────────────────
        createProductIfAbsent(
                "Hydrating Face Serum", "The Ordinary",
                new BigDecimal("14.99"), 600,
                "Hyaluronic Acid 2% + B5 serum for intense hydration and plump skin.",
                beauty,
                "face-serum.jpg",
                "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600&q=80");

        createProductIfAbsent(
                "Electric Toothbrush", "Oral-B",
                new BigDecimal("79.99"), 220,
                "Oral-B Pro 1000 with pressure sensor, 2-minute timer, and CrossAction brush head.",
                beauty,
                "toothbrush.jpg",
                "https://images.unsplash.com/photo-1607613009820-a29f7bb81c04?w=600&q=80");

        // ── Toys & Games ──────────────────────────────────────────────────────
        createProductIfAbsent(
                "LEGO Technic Race Car", "LEGO",
                new BigDecimal("149.99"), 100,
                "1,580-piece Technic race car with realistic details, working steering, and openable hood.",
                toys,
                "lego-car.jpg",
                "https://images.unsplash.com/photo-1587654780291-39c9404d746b?w=600&q=80");

        createProductIfAbsent(
                "Monopoly Classic", "Hasbro",
                new BigDecimal("29.99"), 250,
                "The classic property trading board game for ages 8 and up, 2-8 players.",
                toys,
                "monopoly.jpg",
                "https://images.unsplash.com/photo-1611371805429-8b5c1b2c34ba?w=600&q=80");

        // ── Automotive ────────────────────────────────────────────────────────
        createProductIfAbsent(
                "Dash Cam 4K", "Vantrue",
                new BigDecimal("169.99"), 85,
                "4K UHD front dash cam with night vision, 170° wide angle, and built-in GPS.",
                automotive,
                "dash-cam.jpg",
                "https://images.unsplash.com/photo-1449965408869-eaa3f722e40d?w=600&q=80");

        createProductIfAbsent(
                "Car Phone Holder", "iOttie",
                new BigDecimal("39.99"), 400,
                "Easy One Touch 5 universal car mount with telescopic arm and dashboard/windshield mounting.",
                automotive,
                "phone-holder.jpg",
                "https://images.unsplash.com/photo-1511919884226-fd3cad34687c?w=600&q=80");
    }

    /**
     * Creates a product (if absent) and attaches a single image to it.
     * The image binary is fetched from the given URL and stored as a Blob.
     * If the image download fails, the product is still saved without an image
     * so seeding doesn't break the entire startup.
     */
    private void createProductIfAbsent(String name, String brand, BigDecimal price,
                                       int quantity, String description,
                                       Category category,
                                       String fileName, String imageUrl) {
        if (productRepository.existsByNameAndBrand(name, brand)) {
            return; // already seeded
        }

        Product product = productRepository.save(
                new Product(name, brand, price, quantity, description, category));

        attachImage(product, fileName, imageUrl);
    }

    /**
     * Downloads image bytes from {@code imageUrl} and saves an Image entity
     * linked to the given product. Sets the proper download URL so the
     * ImageController can serve it.
     */
    private void attachImage(Product product, String fileName, String imageUrl) {
        try {
            byte[] bytes = fetchBytes(imageUrl);
            if (bytes == null || bytes.length == 0) return;

            Image image = new Image();
            image.setFileName(fileName);
            image.setFileType("image/jpeg");
            image.setImage(new SerialBlob(bytes));
            image.setProduct(product);
            image.setDownloadUrl(DOWNLOAD_URL_PREFIX + "0"); // placeholder before we know the ID

            Image saved = imageRepository.save(image);
            // Update with the real ID now that it's persisted
            saved.setDownloadUrl(DOWNLOAD_URL_PREFIX + saved.getId());
            imageRepository.save(saved);

            System.out.printf("  🖼  Image saved for: %s (%s)%n", product.getName(), fileName);

        } catch (SQLException e) {
            System.err.printf("  ⚠  Failed to save image blob for '%s': %s%n",
                    product.getName(), e.getMessage());
        }
    }

    /**
     * Reads all bytes from a URL. Returns null on any network/IO error
     * so a single failed download doesn't abort the entire seeder.
     */
    private byte[] fetchBytes(String url) {
        try (InputStream in = URI.create(url).toURL().openStream()) {
            return in.readAllBytes();
        } catch (Exception e) {
            System.err.printf("  ⚠  Could not fetch image from %s: %s%n", url, e.getMessage());
            return null;
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  CARTS & ORDERS
    // ──────────────────────────────────────────────────────────────────────────
    private void seedCartsAndOrders() {
        User alice = userRepository.findByEmail("alice@example.com");
        User bob   = userRepository.findByEmail("bob@example.com");
        User carol = userRepository.findByEmail("carol@example.com");
        User david = userRepository.findByEmail("david@example.com");

        List<Product> headphones = productRepository.findByBrandAndName("Sony",          "Wireless Noise-Cancelling Headphones");
        List<Product> jeans      = productRepository.findByBrandAndName("Levi's",        "Slim-Fit Denim Jeans");
        List<Product> espresso   = productRepository.findByBrandAndName("De'Longhi",     "Espresso Machine");
        List<Product> yogaMat    = productRepository.findByBrandAndName("Manduka",       "Yoga Mat Premium");
        List<Product> serum      = productRepository.findByBrandAndName("The Ordinary",  "Hydrating Face Serum");
        List<Product> keyboard   = productRepository.findByBrandAndName("Corsair",       "Mechanical Gaming Keyboard");

        // Alice: active cart with 2 items
        if (alice != null && cartRepository.findByUserId(alice.getId()) == null) {
            Cart aliceCart = new Cart();
            aliceCart.setUser(alice);
            if (!headphones.isEmpty()) aliceCart.getItems().add(buildCartItem(headphones.get(0), 1, aliceCart));
            if (!jeans.isEmpty())      aliceCart.getItems().add(buildCartItem(jeans.get(0),      2, aliceCart));
            recalcCart(aliceCart);
            cartRepository.save(aliceCart);
        }

        // Bob: active cart with 1 item
        if (bob != null && cartRepository.findByUserId(bob.getId()) == null) {
            Cart bobCart = new Cart();
            bobCart.setUser(bob);
            if (!keyboard.isEmpty()) bobCart.getItems().add(buildCartItem(keyboard.get(0), 1, bobCart));
            recalcCart(bobCart);
            cartRepository.save(bobCart);
        }

        // Alice: past orders
        if (alice != null && orderRepository.findByUserId(alice.getId()).isEmpty()) {
            if (!espresso.isEmpty()) createOrder(alice, LocalDate.now().minusDays(30), OrderStatus.DELIVERED, espresso.get(0), 1);
            if (!serum.isEmpty())    createOrder(alice, LocalDate.now().minusDays(10), OrderStatus.SHIPPED,   serum.get(0),    3);
        }

        // Carol: past order
        if (carol != null && orderRepository.findByUserId(carol.getId()).isEmpty()) {
            if (!yogaMat.isEmpty()) createOrder(carol, LocalDate.now().minusDays(5), OrderStatus.PROCESSING, yogaMat.get(0), 1);
        }

        // David: pending order
        if (david != null && orderRepository.findByUserId(david.getId()).isEmpty()) {
            if (!jeans.isEmpty()) createOrder(david, LocalDate.now(), OrderStatus.PENDING, jeans.get(0), 2);
        }
    }

    private CartItem buildCartItem(Product product, int quantity, Cart cart) {
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setUnitPrice(product.getPrice());
        item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        item.setCart(cart);
        return item;
    }

    private void recalcCart(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(total);
    }

    private void createOrder(User user, LocalDate date, OrderStatus status,
                              Product product, int quantity) {
        BigDecimal itemPrice = product.getPrice();
        BigDecimal total     = itemPrice.multiply(BigDecimal.valueOf(quantity));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(date);
        order.setOrderStatus(status);
        order.setOrderTotalAmount(total);

        OrderItem orderItem = new OrderItem(order, product, quantity, itemPrice);
        order.getOrderItems().add(orderItem);

        orderRepository.save(order);
    }
}
