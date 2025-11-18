CREATE DATABASE DATN_SD52_FA25_HuyNQ203
GO
USE DATN_SD52_FA25_HuyNQ203

--Tạo bảng Xã Phương
create table Xa_phuong(
	Id int not null primary key,
	Ten varchar null
)

--Tạo bảng Quận huyện
create table Quan_huyen(
	Id int not null primary key,
	Ten varchar null
)

--Tạo bảng Tỉnh Thành phố
create table Tinh_tp(
	Id int not null primary key,
	Ten varchar null
)

--Tạo bảng Địa chỉ
create table Dia_chi(
	Id int not null primary key,
	Dia_chi_mac_dinh nvarchar null,
	id_xa int,
	id_quan int,
	id_tinh int,
	Foreign key (id_xa) references Xa_phuong (Id),
	Foreign key (id_quan) references Quan_huyen (Id),
	Foreign key (id_tinh) references Tinh_tp (Id)
)

-- Tạo bảng Khách hàng
create table Khach_hang(
	Id int not null primary key,
	Ho_ten nvarchar not null,
	SDT int null,
	Email nvarchar null,
	Mat_khau nvarchar null,
	Gioi_tinh bit,
	Ngay_sinh datetime null,
	Trang_thai int,
	Ngay_tao_tk datetime null,
	Tao_boi nvarchar null,
	Ngay_cap_nhat_tk datetime null,
	Cap_nhat_boi nvarchar null,
	id_dia_chi int,
	Foreign key (id_dia_chi) references Dia_chi(Id)  
)

-- Tạo bảng Nhân viên
create table Nhan_vien(
	Id int not null primary key,
	Ma int not null,
	Ho_ten nvarchar not null,
	SDT int null,
	Email nvarchar null,
	Mat_khau nvarchar null,
	Gioi_tinh bit,
	Avatar nvarchar null,
	Ngay_sinh datetime null,
	Trang_thai int,
	Ngay_vao_lam datetime null,
	Ghi_chu nvarchar null,
	id_dia_chi int,
	Foreign key (id_dia_chi) references Dia_chi(Id) 
)

-- Tạo bảng Ảnh
create table Anh(
	Id int not null primary key,
	asset_id int,
	cdn_url nvarchar,
	id_sp_bien_the int,
	Foreign key (id_sp_bien_the) references SP_bien_the(Id)
)

--Tạo bảng Thương hiệu
create table Thuong_hieu(
	Id int not null primary key,
	Ma int null,
	Ten_thuong_hieu nvarchar not null,
	Ngay_tao datetime null
) 

--Tạo bảng Loại giày
create table Loai_giay(
	Id int not null primary key,
	Ma int null,
	Ten_loai nvarchar not null,
	Ngay_tao datetime null
)

--Tạo bảng Xuất xứ
create table Xuat_xu(
	Id int not null primary key,
	Ma int null,
	Ten_xuat_xu nvarchar not null,
	Ngay_tao datetime null
)

--Tạo bảng Kiểu dáng
create table Kieu_dang(
	Id int not null primary key,
	Ma int null,
	Ten_kieu_dang nvarchar not null,
	Ngay_tao datetime null
)

--Tạo bảng Đệm giữa
create table Dem_giua(
	Id int not null primary key,
	Ma int null,
	Ten_dem_giua nvarchar not null,
	Ngay_tao datetime null
)

--Tạo bảng Chất liệu
create table Chat_lieu(
	Id int not null primary key,
	Ma int null,
	Ten_chat_lieu nvarchar not null,
	Ngay_tao datetime null
)

--Tạo bảng Kích thước
create table Kich_thuoc(
	Id int not null primary key,
	Ma int null,
	Ten_kich_thuoc nvarchar not null,
	Ngay_tao datetime null
)

--Tạo bảng Màu sắc
create table Mau_sac(
	Id int not null primary key,
	Ma int null,
	Ten_mau_sac nvarchar not null,
	Ngay_tao datetime null
)

-- Tạo bảng Sản phẩm
create table San_pham(
	Id int not null primary key,
	Ma int not null,
	Ten_sp nvarchar not null,
	Mo_ta nvarchar null,
	Trang_thai int null,
	Ngay_tao datetime null,
	id_thuong_hieu int,
	id_loai int,
	id_xuat_xu int,
	id_dem_giua int,
	id_chat_lieu int,
	Foreign key (id_thuong_hieu) references Thuong_hieu(Id),
	Foreign key (id_loai) references Loai_giay(Id),
	Foreign key (id_xuat_xu) references Xuat_xu(Id),
	Foreign key (id_dem_giua) references Dem_giua(Id),
	Foreign key (id_chat_lieu) references Chat_lieu(Id)
)

-- Tạo bảng Sản phẩm biến thể
create table SP_bien_the(
	Id int not null primary key,
	Ma int not null,
	QRCode varchar null,
	So_luong int null,
	Gia decimal null,
	Trang_thai int null,
	Ngay_tao datetime null,
	id_sp int,
	id_mau_sac int,
	id_kich_thuoc int,
	id_dot_giam_gia int,
	Foreign key (id_sp) references San_pham(Id),
	Foreign key (id_mau_sac) references Mau_sac(Id),
	Foreign key (id_kich_thuoc) references Kich_thuoc(Id),
	Foreign key (id_dot_giam_gia) references Dot_giam_gia(Id)
)

-- Tạo bảng Đợt giảm giá
create table Dot_giam_gia(
	Id int not null primary key,
	Ma int not null,
	Ten_dot_giam_gia nvarchar not null,
	Phan_tram_giam int null,
	Ngay_bat_dau datetime null,
	Ngay_ket_thuc datetime null,
	Trang_thai int null,
	Ngay_tao datetime null,
	Ngay_sua datetime null
)

-- Tạo bảng Chi tiết đợt giam giá
create table Dot_giam_gia_ct(
	Id int not null primary key,
	id_sp_bien_the int,
	id_dot_giam_gia int,
	Foreign key (id_sp_bien_the) references SP_bien_the(Id),
	Foreign key (id_dot_giam_gia) references Dot_giam_gia(Id)
)

-- Tạo bảng 