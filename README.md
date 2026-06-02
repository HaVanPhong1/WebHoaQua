# WebHoaQua - Hướng dẫn cài đặt và chạy dự án

## 1. Clone dự án

```bash
git clone https://github.com/HaVanPhong1/WebHoaQua.git
```

Hoặc tải file ZIP từ GitHub và giải nén.

---

## 2. Yêu cầu môi trường

Trước khi chạy dự án cần cài đặt:

* Java JDK (khuyến nghị phiên bản theo dự án)
* Maven
* SQL Server
* IDE: IntelliJ IDEA, Eclipse hoặc VS Code

---

## 3. Tạo cơ sở dữ liệu

Dự án sử dụng SQL Server.

### Bước 1: Tạo Database

Tạo database mới với tên:

```sql
CREATE DATABASE fruitshop2;
```

### Bước 2: Tạo tài khoản SQL Server

Nếu chưa có tài khoản, tạo:

```sql
CREATE LOGIN fruitshop_user
WITH PASSWORD = 'FruitShop@2026';

USE fruitshop2;

CREATE USER fruitshop_user FOR LOGIN fruitshop_user;

ALTER ROLE db_owner ADD MEMBER fruitshop_user;
```

Hoặc sử dụng tài khoản SQL Server của riêng bạn.

---

## 4. Cấu hình kết nối Database

Mở file:

```text
src/main/resources/application.properties
```

Tìm dòng:

```properties
spring.datasource.url=jdbc:sqlserver://LAPTOP-GM414B44:1433;databaseName=fruitshop2
spring.datasource.username=fruitshop_user
spring.datasource.password=FruitShop@2026
```

Nếu SQL Server được cài trên chính máy đang chạy dự án thì sửa thành:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=fruitshop2
spring.datasource.username=fruitshop_user
spring.datasource.password=FruitShop@2026
```

Hoặc thay `localhost` bằng tên máy chủ SQL Server của bạn.

---

## 5. Chạy dự án

Trong thư mục dự án:

```bash
mvn clean install
mvn spring-boot:run
```

Hoặc chạy trực tiếp từ IntelliJ IDEA.

---

## 6. Tạo bảng tự động

Dự án đang sử dụng:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Khi chạy lần đầu:

* Hibernate sẽ tự tạo các bảng còn thiếu.
* Không cần tự tạo từng bảng bằng tay.

Lưu ý: Database `fruitshop2` vẫn phải tồn tại trước khi chạy ứng dụng.

---

## 7. Một số lỗi thường gặp

### Không kết nối được SQL Server

Kiểm tra:

* SQL Server đã khởi động chưa.
* Cổng 1433 đã được bật chưa.
* SQL Server Authentication đã được bật chưa.
* Tài khoản và mật khẩu trong `application.properties` có đúng không.

### Database không tồn tại

Tạo database:

```sql
CREATE DATABASE fruitshop2;
```

sau đó chạy lại dự án.

---

## 8. Repository

GitHub:

https://github.com/HaVanPhong1/WebHoaQua
