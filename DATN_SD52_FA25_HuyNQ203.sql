use master
GO

DROP DATABASE IF EXISTS DATN_SD52_FA25_HuyNQ203;
GO

CREATE DATABASE DATN_SD52_FA25_HuyNQ203
GO
USE DATN_SD52_FA25_HuyNQ203
GO

CREATE TABLE employee (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    code NVARCHAR(100) NOT NULL ,
    name NVARCHAR(255) NULL,
    phone NVARCHAR(50) NULL,
    email NVARCHAR(255) NULL,
    password NVARCHAR(255) NULL,
    role NVARCHAR(255) NULL,
    avatar NVARCHAR(255) NULL,
    birthday DATE NULL,
    gender NVARCHAR(50) NULL,
    note NVARCHAR(MAX) NULL,
    status NVARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at DATE NULL
);
GO

CREATE TABLE customer (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    code NVARCHAR(100) NOT NULL ,
    name NVARCHAR(255) NULL,
    phone NVARCHAR(50) NULL,
    email NVARCHAR(255) NULL,
    password NVARCHAR(255) NULL,
    avatar NVARCHAR(255) NULL,
    birthday DATE NULL,
    gender NVARCHAR(50) NULL,
    status NVARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME NULL,
    created_by BIGINT NULL,
    updated_at DATETIME NULL,
    updated_by BIGINT NULL,
);
GO

--Tạo bảng Xã Phương
CREATE TABLE province (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL
);
GO
--Tạo bảng Quận huyện
CREATE TABLE district (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NULL
);
GO
--Tạo bảng Tỉnh Thành phố
CREATE TABLE ward (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NULL
);
GO
--Tạo bảng Địa chỉ
CREATE TABLE [address] (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    customer_id BIGINT NULL,
    employee_id BIGINT NULL,
    province_id BIGINT NOT NULL,
    district_id BIGINT NOT NULL,
    ward_id BIGINT NOT NULL,
    is_default BIT NOT NULL DEFAULT 0,
    
	CONSTRAINT FK_address_province FOREIGN KEY (province_id)
        REFERENCES province(id),

    CONSTRAINT FK_address_district FOREIGN KEY (district_id)
        REFERENCES district(id),

    CONSTRAINT FK_address_ward FOREIGN KEY (ward_id)
        REFERENCES ward(id)
);

--Tạo thuộc tính sản phẩm
--Thương hiệu
CREATE TABLE brand (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    code NVARCHAR(100) NOT NULL ,
    name NVARCHAR(255) NOT NULL ,
    created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    is_deleted BIT NOT NULL DEFAULT 0
);
GO
--Loại giày
CREATE TABLE sport_type (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    code NVARCHAR(100) NOT NULL ,
    name NVARCHAR(255) NOT NULL ,
    created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    is_deleted BIT NOT NULL DEFAULT 0
);
GO
--Xuất xứ
CREATE TABLE origin (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    code NVARCHAR(100) NOT NULL ,
    name NVARCHAR(255) NOT NULL ,
    created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    is_deleted BIT NOT NULL DEFAULT 0
);
GO
--Kiểu dáng
CREATE TABLE collar (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    code NVARCHAR(100) NOT NULL ,
    name NVARCHAR(255) NOT NULL ,
    created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    is_deleted BIT NOT NULL DEFAULT 0
);
GO
--Chất liệu
CREATE TABLE material (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    code NVARCHAR(100) NOT NULL ,
    name NVARCHAR(255) NOT NULL ,
    created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    is_deleted BIT NOT NULL DEFAULT 0
);
GO
GO
--Đệm giữa
CREATE TABLE mid_sole (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    code NVARCHAR(100) NOT NULL ,
    name NVARCHAR(255) NOT NULL ,
    created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    is_deleted BIT NOT NULL DEFAULT 0
);
GO

--Tạo sản phẩm
CREATE TABLE product (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    code NVARCHAR(255) ,
    name NVARCHAR(200) NOT NULL,    
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    id_brand BIGINT NOT NULL,
);

--Tạo thuộc tính sản phẩm biến thể
-- Màu sắc
CREATE TABLE color (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    code NVARCHAR(255) NOT NULL ,
    name NVARCHAR(255) NOT NULL ,
    created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    is_deleted BIT NOT NULL DEFAULT 0,
    hex_code NVARCHAR(100) NOT NULL
);
-- Giới tính
CREATE TABLE gender (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    code NVARCHAR(255) NOT NULL ,
    name NVARCHAR(255) NOT NULL ,
    created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    is_deleted BIT NOT NULL DEFAULT 0
);
-- kích cỡ
CREATE TABLE size (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    code NVARCHAR(255) NOT NULL ,
    name NVARCHAR(255) NOT NULL ,
    created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    is_deleted BIT NOT NULL DEFAULT 0
);

CREATE TABLE product_variant (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    code NVARCHAR(255),
    qr_code NVARCHAR(255) NULL,
    quantity INT NULL,
    price BIGINT NULL,
	description NVARCHAR(MAX) NULL,
    status NVARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    product_id BIGINT NOT NULL,
    id_color BIGINT NOT NULL,
    
    CONSTRAINT FK_product_variant_product FOREIGN KEY (product_id)
        REFERENCES product(id),
    CONSTRAINT FK_product_variant_color FOREIGN KEY (id_color)
        REFERENCES color(id)
);

CREATE TABLE image (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    variant_id BIGINT NOT NULL,           -- FK tới ProductVariant
    public_id NVARCHAR(255) NOT NULL UNIQUE,  -- ID ảnh duy nhất
    cdn_url NVARCHAR(500) NOT NULL,      -- URL ảnh
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_image_variant FOREIGN KEY (variant_id)
        REFERENCES product_variant(id)
);
GO

 -- Insert nhân viên mẫu
INSERT INTO employee (code, name, phone, email, password, role , gender, status, created_at) 
VALUES 
('NV000001', N'Nguyễn Văn Admin', '0901234567', 'admin@gmail.com', '123456', 'ADMIN', 'MALE', 'ACTIVE', GETDATE()),
('NV000002', N'Trần Thị B', '0902345678', 'staff@gmail.com', '123456', 'STAFF', 'FEMALE', 'ACTIVE', GETDATE()),
('NV000003', N'Lê Văn C', '0903456789', 'staff2@gmail.com', '123456', 'STAFF', 'MALE', 'ACTIVE', GETDATE());

-- Insert khách hàng mẫu
INSERT INTO customer (code, name, phone, email, password, gender, birthday, status, created_at) 
VALUES 
('KH000001', N'Hoàng Văn D', '0904567890', 'customer1@gmail.com', '123456', 'MALE', '1990-01-15', 'ACTIVE', GETDATE()),
('KH000002', N'Phạm Thị E', '0905678901', 'customer2@gmail.com', '123456', 'FEMALE', '1992-05-20', 'ACTIVE', GETDATE()),
('KH000003', N'Đặng Văn F', '0906789012', 'customer3@gmail.com', '123456', 'MALE', '1988-09-10', 'ACTIVE', GETDATE()),
('KH000004', N'Vũ Thị G', '0907890123', 'customer4@gmail.com', '123456', 'FEMALE', '1995-12-25', 'INACTIVE', GETDATE()),
('KH000005', N'Mai Văn H', '0908901234', 'customer5@gmail.com', '123456', 'OTHER', '1993-03-18', 'ACTIVE', GETDATE());
GO


INSERT INTO brand (code, name, created_at, is_deleted) VALUES
('BR001',  'Sunrise Co.',                     '2024-01-15 09:12:00', 0),
('BR002',  'GreenLeaf Products',              '2024-02-03 14:25:00', 0),
('BR003',  'BlueWave Electronics',            '2024-02-20 11:05:00', 0),
('BR004',  'NorthStar Outfitters',            '2024-03-10 16:40:00', 0),
('BR005',  'UrbanNest',                       '2024-03-21 08:30:00', 0),
('BR006',  'MetroStyle',                      '2024-04-02 13:15:00', 0),
('BR007',  'SilverLine Tools',                '2024-04-18 10:00:00', 0),
('BR008',  'PeakPerformance',                 '2024-05-05 09:50:00', 0),
('BR009',  'Harbor Home',                     '2024-05-22 15:05:00', 0),
('BR010',  'Velvet & Co',                     '2024-06-01 12:20:00', 0),
('BR011',  'Apex Motors',                     '2024-06-14 17:10:00', 0),
('BR012',  'Nimbus Tech',                     '2024-07-02 08:45:00', 0),
('BR013',  'Crescent Cosmetics',              '2024-07-19 14:00:00', 0)
GO

INSERT INTO collar (code, name, created_at, is_deleted) VALUES
('COL001', 'Low-cut',               '2024-01-10 09:00:00', 0),
('COL002', 'Mid-cut',               '2024-01-25 10:15:00', 0),
('COL003', 'High-cut',              '2024-02-05 11:30:00', 0),
('COL004', 'Slip-on',               '2024-02-20 14:45:00', 0),
('COL005', 'Ankle Boot',            '2024-03-03 08:20:00', 0),
('COL006', 'Chelsea Boot',          '2024-03-17 09:50:00', 0),
('COL007', 'Sock-style',            '2024-04-02 10:40:00', 0),
('COL008', 'Lace-up',               '2024-04-18 13:30:00', 0),
('COL009', 'Velcro Strap',          '2024-05-01 15:05:00', 0),
('COL010', 'Hook & Loop',           '2024-05-15 09:10:00', 0),
('COL011', 'Zipper Collar',         '2024-06-02 11:00:00', 0),
('COL012', 'Elastic Collar',        '2024-06-20 16:30:00', 0),
('COL013', 'Fold-over',             '2024-07-05 08:45:00', 0),
('COL014', 'Padded Collar',         '2024-07-22 10:10:00', 0),
('COL015', 'Knitted Collar',        '2024-08-01 14:00:00', 0);
GO

-- ----------------------------
-- 2. Bảng sport_type
-- ----------------------------
INSERT INTO sport_type (code, name, created_at, is_deleted)
VALUES
('ST001', 'Running', GETDATE(), 0),
('ST002', 'Basketball', GETDATE(), 0),
('ST003', 'Casual', GETDATE(), 0);

-- ----------------------------
-- 3. Bảng origin
-- ----------------------------
INSERT INTO origin (code, name, created_at, is_deleted)
VALUES
('OR001', 'Vietnam', GETDATE(), 0),
('OR002', 'USA', GETDATE(), 0),
('OR003', 'Germany', GETDATE(), 0);

-- ----------------------------
-- 4. Bảng material
-- ----------------------------
INSERT INTO material (code, name, created_at, is_deleted)
VALUES
('MAT001', 'Leather', GETDATE(), 0),
('MAT002', 'Mesh', GETDATE(), 0),
('MAT003', 'Synthetic', GETDATE(), 0);

-- ----------------------------
-- 5. Bảng mid_sole
-- ----------------------------
INSERT INTO mid_sole (code, name, created_at, is_deleted)
VALUES
('MS001', 'EVA', GETDATE(), 0),
('MS002', 'PU', GETDATE(), 0);



-- ----------------------------
-- 7. Bảng size
-- ----------------------------
INSERT INTO size (code, name, created_at, is_deleted)
VALUES
('S001', '36', GETDATE(), 0),
('S002', '37', GETDATE(), 0),
('S003', '38', GETDATE(), 0),
('S004', '39', GETDATE(), 0),
('S005', '40', GETDATE(), 0),
('S006', '41', GETDATE(), 0);
GO

INSERT INTO color (code, name, created_at, is_deleted, hex_code) VALUES
('CL001', 'Red',         '2024-01-10 09:12:00', 0, '#FF0000'),
('CL002', 'Green',       '2024-01-15 14:25:00', 0, '#00FF00'),
('CL003', 'Blue',        '2024-01-20 11:05:00', 0, '#0000FF'),
('CL004', 'Yellow',      '2024-02-10 16:40:00', 0, '#FFFF00'),
('CL005', 'Orange',      '2024-02-21 08:30:00', 0, '#FFA500'),
('CL006', 'Purple',      '2024-03-02 13:15:00', 0, '#800080'),
('CL007', 'Pink',        '2024-03-18 10:00:00', 0, '#FFC0CB'),
('CL008', 'Brown',       '2024-04-05 09:50:00', 0, '#A52A2A'),
('CL009', 'Gray',        '2024-04-22 15:05:00', 0, '#808080'),
('CL010', 'Black',       '2024-05-01 12:20:00', 0, '#000000'),
('CL011', 'White',       '2024-05-14 17:10:00', 0, '#FFFFFF'),
('CL012', 'Cyan',        '2024-06-02 08:45:00', 0, '#00FFFF');
GO
-- ----------------------------
-- 6. Bảng gender (cho sản phẩm)
-- ----------------------------
INSERT INTO gender (code, name, created_at, is_deleted)
VALUES
('G001', 'Male', GETDATE(), 0),
('G002', 'Female', GETDATE(), 0),
('G003', 'Unisex', GETDATE(), 0);

-- ----------------------------
-- 1. Bảng product (sản phẩm)
-- ----------------------------
INSERT INTO product (code, name, status, created_at, id_brand)
VALUES
('PRD001', 'Sneaker Sunrise', 'ACTIVE', GETDATE(), 1),
('PRD002', 'GreenLeaf Running Shoes', 'ACTIVE', GETDATE(), 2),
('PRD003', 'BlueWave Headphones', 'ACTIVE', GETDATE(), 3),
('PRD004', 'NorthStar Jacket', 'ACTIVE', GETDATE(), 4),
('PRD005', 'UrbanNest Chair', 'ACTIVE', GETDATE(), 5);

-- Chèn dữ liệu biến thể sản phẩm
INSERT INTO product_variant (code, product_id, id_color, quantity, price, description)
VALUES
('VAR001', 1, 1, 10, 500000, 'Sneaker Sunrise Red'),
('VAR002', 1, 2, 15, 520000, 'Sneaker Sunrise Green'),
('VAR003', 2, 3, 20, 480000, 'GreenLeaf Blue'),
('VAR004', 2, 4, 12, 490000, 'GreenLeaf Yellow'),
('VAR005', 3, 5, 8, 1500000, 'BlueWave Orange'),
('VAR006', 4, 6, 5, 1200000, 'NorthStar Purple');


-- ----------------------------
-- 9. Bảng image (ảnh biến thể)
-- ----------------------------
INSERT INTO image (variant_id, public_id, cdn_url)
VALUES
(1, 'ASSET001', 'https://cdn.example.com/images/variant1_img1.jpg'),
(1, 'ASSET002', 'https://cdn.example.com/images/variant1_img2.jpg'),
(2, 'ASSET003', 'https://cdn.example.com/images/variant2_img1.jpg'),
(2, 'ASSET004', 'https://cdn.example.com/images/variant2_img2.jpg'),
(3, 'ASSET005', 'https://cdn.example.com/images/variant3_img1.jpg'),
(4, 'ASSET006', 'https://cdn.example.com/images/variant4_img1.jpg'),
(5, 'ASSET007', 'https://cdn.example.com/images/variant5_img1.jpg'),
(6, 'ASSET008', 'https://cdn.example.com/images/variant6_img1.jpg');



select * from employee
select * from brand
select * from color
select * from product
select * from product_variant

delete  from brand 
where  brand.name = ' Meridian Sportswear'


-- 1. Tìm tên constraint
SELECT name 
FROM sys.default_constraints 
WHERE parent_object_id = OBJECT_ID('customer')
  AND parent_column_id = (
      SELECT column_id 
      FROM sys.columns 
      WHERE object_id = OBJECT_ID('customer')
      AND name = 'status'
  );

-- 2. Drop constraint
ALTER TABLE customer DROP CONSTRAINT DF__customer__status__276EDEB3;

-- 3. Alter column
ALTER TABLE customer ALTER COLUMN status VARCHAR(255);
