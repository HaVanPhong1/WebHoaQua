package com.example.demo.config;
import com.example.demo.entity.AccountRole;
import com.example.demo.entity.UserAccount;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    public ApplicationRunner initializer(
            UserAccountRepository accountRepository, 
            CategoryRepository categoryRepository, 
            ProductRepository productRepository, 
            PasswordEncoder passwordEncoder, 
            DataSource dataSource) {
        return args -> {
            // Drop any SQL Server check constraint on the 'role' column of 'user_accounts' table 
            // that prevents inserting 'ROLE_CUSTOMER' (since check constraint was generated from old enum values)
            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {
                String sql = "DECLARE @ConstraintName NVARCHAR(255);\n" +
                        "SELECT @ConstraintName = dc.name\n" +
                        "FROM sys.check_constraints dc\n" +
                        "JOIN sys.columns c ON dc.parent_object_id = c.object_id AND dc.parent_column_id = c.column_id\n" +
                        "WHERE OBJECT_NAME(dc.parent_object_id) = 'user_accounts' AND c.name = 'role';\n" +
                        "\n" +
                        "IF @ConstraintName IS NOT NULL\n" +
                        "BEGIN\n" +
                        "    DECLARE @SQL NVARCHAR(MAX) = 'ALTER TABLE user_accounts DROP CONSTRAINT ' + QUOTENAME(@ConstraintName);\n" +
                        "    EXEC sp_executesql @SQL;\n" +
                        "END";
                 stmt.execute(sql);
            } catch (Exception e) {
                System.err.println("Could not drop check constraint on user_accounts(role): " + e.getMessage());
            }

            if (accountRepository.count() == 0) {
                UserAccount admin = new UserAccount();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setFullName("Quản trị viên");
                admin.setEmail("admin@fruitshop.local");
                admin.setRole(AccountRole.ROLE_ADMIN);
                admin.setActive(true);
                accountRepository.save(admin);
            }

            if (categoryRepository.count() == 0 && productRepository.count() == 0) {
                Category noiDia = new Category();
                noiDia.setName("Trái cây nội địa");
                noiDia.setDescription("Các loại trái cây sạch từ nhà vườn Việt Nam");
                noiDia = categoryRepository.save(noiDia);

                Category nhapKhau = new Category();
                nhapKhau.setName("Trái cây nhập khẩu");
                nhapKhau.setDescription("Trái cây tươi ngon nhập khẩu chính ngạch từ nước ngoài");
                nhapKhau = categoryRepository.save(nhapKhau);

                // Add products
                Product orange = new Product();
                orange.setName("Cam sành miền Tây");
                orange.setDescription("Cam sành tươi ngon, mọng nước, nhiều vitamin C.");
                orange.setImage("/images/orange.png");
                orange.setPrice(new BigDecimal("35000"));
                orange.setStock(100);
                orange.setCategory(noiDia);
                productRepository.save(orange);

                Product apple = new Product();
                apple.setName("Táo Fuji Nhật Bản");
                apple.setDescription("Táo Fuji giòn ngọt, thơm ngon nhập khẩu trực tiếp từ Nhật.");
                apple.setImage("/images/apple.png");
                apple.setPrice(new BigDecimal("85000"));
                apple.setStock(50);
                apple.setCategory(nhapKhau);
                productRepository.save(apple);

                Product banana = new Product();
                banana.setName("Chuối sứ miền Tây");
                banana.setDescription("Chuối sứ chín tự nhiên, thơm ngon ngọt lịm.");
                banana.setImage("/images/banana.png");
                banana.setPrice(new BigDecimal("25000"));
                banana.setStock(120);
                banana.setCategory(noiDia);
                productRepository.save(banana);

                Product watermelon = new Product();
                watermelon.setName("Dưa hấu Long An");
                watermelon.setDescription("Dưa hấu đỏ ngọt, giòn mát thanh nhiệt cơ thể.");
                watermelon.setImage("/images/watermelon.png");
                watermelon.setPrice(new BigDecimal("18000"));
                watermelon.setStock(80);
                watermelon.setCategory(noiDia);
                productRepository.save(watermelon);
            }
        };
    }
}
