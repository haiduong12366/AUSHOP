create database AUSHOP
Go
use AUSHOP
Go

create table AppRole
(
	roleId int identity(1,1) PRIMARY KEY,
	ten varchar(50) 
);
Go

INSERT [dbo].[Approle]  VALUES ( N'ROLE_ADMIN');
INSERT [dbo].[Approle]  VALUES ( N'ROLE_USER');

create table KhachHang
(
	maKhachHang int identity(1, 1) PRIMARY KEY,
	email varchar(50),
	hoTen nvarchar(50),
	sdt varchar(10),
	diachi nvarchar(100),
	hinhanhKH varchar(1000),
	passwd varchar(32),
	tongChiTieu float,
	ngayDangKy datetime,
	last_login datetime,
	gioiTinh bit,
	is_admin bit
);
Go

create table UserRole
(
	id int identity(1, 1) PRIMARY KEY,
	roleId int,
	maKhachHang int,
	Foreign Key (maKhachHang) references KhachHang (maKhachHang),
	Foreign Key (roleId) references Approle (roleId)
);
Go


create table LoaiSanPham(
	maLoaiSP int identity(1, 1) Primary Key,
	tenLoaiSP nvarchar(100)
);
Go

create table NhaCungCap(
	maNhaCC int identity(1, 1) Primary Key,
	tenNhaCC nvarchar(100),
	emailNhaCC varchar(100),
	sdtNhaCC varchar(10),
	diaChiNhaCC nvarchar(100)
);
Go

create table SanPham(
	maSP int identity(1, 1) Primary Key,
	maLoaiSP int,
	maNhaCC int,
	tenSP nvarchar (100),
	moTa nvarchar (4000),
	ngaynhaphang date,
	donGia float,
	discount float,
	hinhAnh varchar(1000),
	slTonKho int,
	tinhTrang bit,
	Foreign Key (maLoaiSP) references LoaiSanPham (maLoaiSP),
	Foreign Key (maNhaCC) references NhaCungCap (maNhaCC),
);
Go

create table ThanhToan(
	maPT int identity(1, 1) Primary Key,
	tenPT nvarchar(100),
);
Go

create table DonHang(
	maDH int identity(1, 1) Primary Key,
	maKhachHang int,
	maPT int,
	ngayDatHang datetime,
	diaChiGiaoHang nvarchar(200),
	tinhTrang nvarchar (20),
	tongTien float,
	FOREIGN KEY (maKhachHang) REFERENCES KhachHang(maKhachHang),
	FOREIGN KEY (maPT) REFERENCES ThanhToan(maPT)
);
Go
create table ChiTietDonHang(
	maChiTietDH int identity(1,1) Primary Key,
	maDH int,
	maSP int,
	donGia float,
	soLuong int,
	tong float,
	Foreign Key (maDH) references DonHang(maDH),
	Foreign Key (maSP) references SanPham(maSP)
);
Go

create table DaXem(
	id int identity(1,1) Primary Key,
	maKhachHang int,
	maSP int,
	Foreign Key (maKhachHang) references KhachHang (maKhachHang),
	Foreign Key (maSP) references SanPham (maSP)
);
Go