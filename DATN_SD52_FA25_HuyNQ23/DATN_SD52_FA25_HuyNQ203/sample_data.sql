-- Script tạo dữ liệu mẫu cho hệ thống

USE DATN_SD52_FA25_HuyNQ203;
GO

-- Insert nhân viên mẫu
-- Password: 123456
INSERT INTO employee (code, name, phone, email, password, role, gender, status, created_at) 
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

PRINT N'Đã thêm dữ liệu mẫu thành công!';
PRINT N'';
PRINT N'Thông tin đăng nhập:';
PRINT N'ADMIN: admin@gmail.com / 123456';
PRINT N'STAFF: staff@gmail.com / 123456';
