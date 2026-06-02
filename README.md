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
thêm dữ liệu(chạy 1 lần đi để nó tự tạo bảng xong mới add đc dữ liệu):
```sql
USE [fruitshop2]
GO

-- 1. BẢNG CATEGORIES (DANH MỤC)
SET IDENTITY_INSERT [dbo].[categories] ON 

INSERT [dbo].[categories] ([id], [description], [name]) VALUES (1, N'Trái cây tươi sạch', N'Trái cây')
INSERT [dbo].[categories] ([id], [description], [name]) VALUES (2, N'Rau củ hữu cơ', N'Rau củ')
INSERT [dbo].[categories] ([id], [description], [name]) VALUES (3, N'Nước ép và trà', N'Đồ uống')

SET IDENTITY_INSERT [dbo].[categories] OFF
GO

-- 2. BẢNG PRODUCTS (SẢN PHẨM)
SET IDENTITY_INSERT [dbo].[products] ON 

INSERT [dbo].[products] ([id], [description], [image], [name], [price], [stock], [category_id]) VALUES (1, N'Táo hữu cơ', N'tao-do.jpg', N'Táo đỏ', CAST(25000.00 AS Numeric(38, 2)), 97, 1)
INSERT [dbo].[products] ([id], [description], [image], [name], [price], [stock], [category_id]) VALUES (2, N'Cam tươi Việt Nam', N'cam-vang.jpg', N'Cam vàng', CAST(22000.00 AS Numeric(38, 2)), 78, 1)
INSERT [dbo].[products] ([id], [description], [image], [name], [price], [stock], [category_id]) VALUES (3, N'Cà rốt sạch', N'ca-rot.jpg', N'Cà rốt', CAST(15000.00 AS Numeric(38, 2)), 119, 2)
INSERT [dbo].[products] ([id], [description], [image], [name], [price], [stock], [category_id]) VALUES (4, N'Nước ép cam tươi', N'nuoc-ep-cam.jpg', N'Nước ép cam', CAST(30000.00 AS Numeric(38, 2)), 50, 3)
INSERT [dbo].[products] ([id], [description], [image], [name], [price], [stock], [category_id]) VALUES (5, N'ngon', N'', N'nho', CAST(1.00 AS Numeric(38, 2)), 20, 1)

SET IDENTITY_INSERT [dbo].[products] OFF
GO

-- 3. BẢNG CUSTOMERS (KHÁCH HÀNG)
SET IDENTITY_INSERT [dbo].[customers] ON 

INSERT [dbo].[customers] ([id], [active], [address], [email], [name], [phone]) VALUES (1, 1, N'Hà Nội', N'nguyenvana@example.com', N'Nguyễn Văn A', N'0901234567')
INSERT [dbo].[customers] ([id], [active], [address], [email], [name], [phone]) VALUES (2, 1, N'Hồ Chí Minh', N'tranthib@example.com', N'Trần Thị B', N'0912345678')
INSERT [dbo].[customers] ([id], [active], [address], [email], [name], [phone]) VALUES (3, 1, N'cccc', N'phongha0948@gmail.com', N'phong', N'9048533142')
INSERT [dbo].[customers] ([id], [active], [address], [email], [name], [phone]) VALUES (4, 1, N'fffff', N'phongha094d8@gmail.com', N'phong', N'9048533142')
INSERT [dbo].[customers] ([id], [active], [address], [email], [name], [phone]) VALUES (5, 1, N'ddddd', N'phongha04d8@gmail.com', N'phong', N'9048533142')
INSERT [dbo].[customers] ([id], [active], [address], [email], [name], [phone]) VALUES (6, 1, N'Bắc Giang', N'dwadwadawdad@gmail.com', N'Hà Văn Phong', N'0948533142')
INSERT [dbo].[customers] ([id], [active], [address], [email], [name], [phone]) VALUES (7, 1, N'dddd', N'dwadwawdad@gmail.com', N'Hà Văn Phong', N'0948533142')

SET IDENTITY_INSERT [dbo].[customers] OFF
GO

-- 4. BẢNG SHOP_ORDERS (ĐƠN HÀNG)
SET IDENTITY_INSERT [dbo].[shop_orders] ON 

INSERT [dbo].[shop_orders] ([id], [created_at], [status], [total_amount], [customer_id]) VALUES (1, CAST(N'2026-05-29T12:31:57.9633330' AS DateTime2), N'NEW', CAST(79000.00 AS Numeric(38, 2)), 1)
INSERT [dbo].[shop_orders] ([id], [created_at], [status], [total_amount], [customer_id]) VALUES (2, CAST(N'2026-06-01T13:47:29.2579440' AS DateTime2), N'NEW', CAST(72000.00 AS Numeric(38, 2)), 7)
INSERT [dbo].[shop_orders] ([id], [created_at], [status], [total_amount], [customer_id]) VALUES (3, CAST(N'2026-06-01T18:32:53.1735130' AS DateTime2), N'NEW', CAST(62000.00 AS Numeric(38, 2)), 7)

SET IDENTITY_INSERT [dbo].[shop_orders] OFF
GO

-- 5. BẢNG ORDER_ITEMS (CHI TIẾT ĐƠN HÀNG)
SET IDENTITY_INSERT [dbo].[order_items] ON 

INSERT [dbo].[order_items] ([id], [quantity], [unit_price], [order_id], [product_id]) VALUES (1, 2, CAST(25000.00 AS Numeric(38, 2)), 1, 1)
INSERT [dbo].[order_items] ([id], [quantity], [unit_price], [order_id], [product_id]) VALUES (2, 1, CAST(30000.00 AS Numeric(38, 2)), 1, 4)
INSERT [dbo].[order_items] ([id], [quantity], [unit_price], [order_id], [product_id]) VALUES (3, 2, CAST(25000.00 AS Numeric(38, 2)), 2, 1)
INSERT [dbo].[order_items] ([id], [quantity], [unit_price], [order_id], [product_id]) VALUES (4, 1, CAST(22000.00 AS Numeric(38, 2)), 2, 2)
INSERT [dbo].[order_items] ([id], [quantity], [unit_price], [order_id], [product_id]) VALUES (5, 1, CAST(25000.00 AS Numeric(38, 2)), 3, 1)
INSERT [dbo].[order_items] ([id], [quantity], [unit_price], [order_id], [product_id]) VALUES (6, 1, CAST(22000.00 AS Numeric(38, 2)), 3, 2)
INSERT [dbo].[order_items] ([id], [quantity], [unit_price], [order_id], [product_id]) VALUES (7, 1, CAST(15000.00 AS Numeric(38, 2)), 3, 3)

SET IDENTITY_INSERT [dbo].[order_items] OFF
GO

-- 6. BẢNG USER_ACCOUNTS (TÀI KHOẢN NGƯỜI DÙNG)
SET IDENTITY_INSERT [dbo].[user_accounts] ON 

INSERT [dbo].[user_accounts] ([id], [active], [email], [full_name], [password], [role], [username]) VALUES (1, 1, N'admin@fruitshop.local', N'Quản trị viên', N'$2a$10$dinjLbTWV1BBvntrHiQA7OC0j0QCSi3XaPB8/Dk9Tyf6ha7Pkfl42', N'ROLE_ADMIN', N'admin')
INSERT [dbo].[user_accounts] ([id], [active], [email], [full_name], [password], [role], [username]) VALUES (3, 1, N'manager@fruitshop.com', N'Quản lý', N'manager123', N'ROLE_MANAGER', N'manager')
INSERT [dbo].[user_accounts] ([id], [active], [email], [full_name], [password], [role], [username]) VALUES (6, 1, N'dwadwawdad@gmail.com', N'Hà Văn Phong', N'$2a$10$VYof6XEDkUQ4pwDeTYTzz.KhfdK5FuuEb9L2.zTcPsl4dk5l20HCO', N'ROLE_CUSTOMER', N'phong')

SET IDENTITY_INSERT [dbo].[user_accounts] OFF
GO
```
sau đó chạy lại dự án.

---

## 8. Repository

GitHub:

https://github.com/HaVanPhong1/WebHoaQua
