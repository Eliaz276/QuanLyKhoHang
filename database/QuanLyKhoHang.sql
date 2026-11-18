IF DB_ID(N'QuanLyKhoHang') IS NULL
BEGIN
    CREATE DATABASE QuanLyKhoHang;
END;
GO

USE QuanLyKhoHang;
GO

IF OBJECT_ID(N'dbo.LOAIHANG', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.LOAIHANG
    (
        MaLoai VARCHAR(10) NOT NULL,
        TenLoai NVARCHAR(100) NOT NULL,
        MoTa NVARCHAR(255) NULL,
        CONSTRAINT PK_LOAIHANG PRIMARY KEY (MaLoai),
        CONSTRAINT UQ_LOAIHANG_TenLoai UNIQUE (TenLoai)
    );
END;
GO

IF OBJECT_ID(N'dbo.NHACUNGCAP', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.NHACUNGCAP
    (
        MaNCC VARCHAR(10) NOT NULL,
        TenNCC NVARCHAR(150) NOT NULL,
        SoDienThoai VARCHAR(15) NULL,
        DiaChi NVARCHAR(200) NULL,
        Email VARCHAR(100) NULL,
        CONSTRAINT PK_NHACUNGCAP PRIMARY KEY (MaNCC)
    );
END;
GO

IF OBJECT_ID(N'dbo.HANGHOA', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.HANGHOA
    (
        MaHang VARCHAR(10) NOT NULL,
        TenHang NVARCHAR(150) NOT NULL,
        MaLoai VARCHAR(10) NOT NULL,
        MaNCC VARCHAR(10) NOT NULL,
        SoLuong INT NOT NULL CONSTRAINT DF_HANGHOA_SoLuong DEFAULT (0),
        DonGia DECIMAL(18,2) NOT NULL CONSTRAINT DF_HANGHOA_DonGia DEFAULT (0),
        DonViTinh NVARCHAR(30) NULL,
        NgayNhap DATE NOT NULL CONSTRAINT DF_HANGHOA_NgayNhap DEFAULT (CONVERT(DATE, GETDATE())),
        CONSTRAINT PK_HANGHOA PRIMARY KEY (MaHang),
        CONSTRAINT FK_HANGHOA_LOAIHANG FOREIGN KEY (MaLoai)
            REFERENCES dbo.LOAIHANG(MaLoai),
        CONSTRAINT FK_HANGHOA_NHACUNGCAP FOREIGN KEY (MaNCC)
            REFERENCES dbo.NHACUNGCAP(MaNCC),
        CONSTRAINT CK_HANGHOA_SoLuong CHECK (SoLuong >= 0),
        CONSTRAINT CK_HANGHOA_DonGia CHECK (DonGia >= 0)
    );
END;
GO

IF OBJECT_ID(N'dbo.TAIKHOAN', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.TAIKHOAN
    (
        MaTK INT IDENTITY(1,1) PRIMARY KEY,
        TenDangNhap NVARCHAR(50) NOT NULL UNIQUE,
        MatKhauHash VARBINARY(32) NOT NULL,
        HoTen NVARCHAR(100),
        VaiTro NVARCHAR(50),
        TrangThai BIT DEFAULT 1
    );
END;

IF NOT EXISTS (
    SELECT 1 FROM dbo.TAIKHOAN
    WHERE TenDangNhap = N'admin'
)
BEGIN
    INSERT INTO dbo.TAIKHOAN
    (TenDangNhap, MatKhauHash, HoTen, VaiTro, TrangThai)
    VALUES
    (
        N'admin',
        HASHBYTES('SHA2_256', CONVERT(NVARCHAR(100), N'123456')),
        N'Quản trị viên',
        N'Admin',
        1
    );
END;

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'IX_HANGHOA_TenHang' AND object_id = OBJECT_ID(N'dbo.HANGHOA'))
    CREATE INDEX IX_HANGHOA_TenHang ON dbo.HANGHOA(TenHang);
GO

IF NOT EXISTS (SELECT 1 FROM dbo.LOAIHANG WHERE MaLoai = 'LH01')
    INSERT INTO dbo.LOAIHANG(MaLoai, TenLoai, MoTa)
    VALUES ('LH01', N'Đồ điện tử', N'Thiết bị điện tử và phụ kiện');

IF NOT EXISTS (SELECT 1 FROM dbo.LOAIHANG WHERE MaLoai = 'LH02')
    INSERT INTO dbo.LOAIHANG(MaLoai, TenLoai, MoTa)
    VALUES ('LH02', N'Văn phòng phẩm', N'Dụng cụ học tập và văn phòng');

IF NOT EXISTS (SELECT 1 FROM dbo.LOAIHANG WHERE MaLoai = 'LH03')
    INSERT INTO dbo.LOAIHANG(MaLoai, TenLoai, MoTa)
    VALUES ('LH03', N'Đồ gia dụng', N'Sản phẩm sử dụng trong gia đình');
GO

IF NOT EXISTS (SELECT 1 FROM dbo.NHACUNGCAP WHERE MaNCC = 'NCC01')
    INSERT INTO dbo.NHACUNGCAP(MaNCC, TenNCC, SoDienThoai, DiaChi, Email)
    VALUES ('NCC01', N'Công ty Minh Phát', '0901234567', N'Hà Nội', 'minhphat@example.com');

IF NOT EXISTS (SELECT 1 FROM dbo.NHACUNGCAP WHERE MaNCC = 'NCC02')
    INSERT INTO dbo.NHACUNGCAP(MaNCC, TenNCC, SoDienThoai, DiaChi, Email)
    VALUES ('NCC02', N'Cửa hàng Thành Công', '0912345678', N'Hà Nội', 'thanhcong@example.com');
GO

IF NOT EXISTS (SELECT 1 FROM dbo.HANGHOA WHERE MaHang = 'HH01')
    INSERT INTO dbo.HANGHOA(MaHang, TenHang, MaLoai, MaNCC, SoLuong, DonGia, DonViTinh, NgayNhap)
    VALUES ('HH01', N'Chuột máy tính', 'LH01', 'NCC01', 25, 120000, N'Cái', CONVERT(DATE, GETDATE()));

IF NOT EXISTS (SELECT 1 FROM dbo.HANGHOA WHERE MaHang = 'HH02')
    INSERT INTO dbo.HANGHOA(MaHang, TenHang, MaLoai, MaNCC, SoLuong, DonGia, DonViTinh, NgayNhap)
    VALUES ('HH02', N'Vở ghi chép', 'LH02', 'NCC02', 100, 12000, N'Quyển', CONVERT(DATE, GETDATE()));
GO

SELECT * FROM dbo.LOAIHANG;
SELECT * FROM dbo.NHACUNGCAP;
SELECT H.MaHang, H.TenHang, L.TenLoai, N.TenNCC,
       H.SoLuong, H.DonGia, H.DonViTinh, H.NgayNhap
FROM dbo.HANGHOA H
INNER JOIN dbo.LOAIHANG L ON H.MaLoai = L.MaLoai
INNER JOIN dbo.NHACUNGCAP N ON H.MaNCC = N.MaNCC;
GO
