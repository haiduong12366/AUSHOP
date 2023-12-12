package AUSHOP.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import AUSHOP.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import AUSHOP.Model.HangTonKhoModel;
import AUSHOP.entity.SanPham;



@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {

	@Query("select new AUSHOP.Model.HangTonKhoModel(o.maLoaiSP, sum(o.donGia), sum(o.slTonKho)) from SanPham o group by o.maLoaiSP order by sum(o.slTonKho) desc")
	List<HangTonKhoModel> getTonKhoByLoaiSanPham();

	@Query(value = "select loai_san_pham.ten_loaisp , sum(chi_tiet_don_hang.don_gia) as 'Tổng tiền', COUNT(san_pham.sl_ton_kho) as 'Số lượng' \r\n"
			+ "from chi_tiet_don_hang \r\n" + "join don_hang on don_hang.madh = chi_tiet_don_hang.madh\r\n"
			+ "join san_pham on chi_tiet_don_hang.masp = san_pham.masp \r\n"
			+ "join loai_san_pham on loai_san_pham.ma_loaisp = san_pham.ma_loaisp\r\n" + "where don_hang.tinh_trang = '2'\r\n"
			+ "group by loai_san_pham.ten_loaisp\r\n" + "order by COUNT(san_pham.sl_ton_kho) desc", nativeQuery = true)
	List<Object[]> getLoaiSanPhamBanChay();

	@Query(value = "select  san_pham.tensp , sum(chi_tiet_don_hang.don_gia) as 'Tổng tiền', COUNT(san_pham.sl_ton_kho) as 'Số lượng' from chi_tiet_don_hang \r\n"
			+ "join don_hang on don_hang.madh = chi_tiet_don_hang.madh\r\n"
			+ "join san_pham on chi_tiet_don_hang.masp = san_pham.masp \r\n" + "where don_hang.tinh_trang = '2'\r\n"
			+ "group by san_pham.tensp\r\n" + "order by COUNT(san_pham.sl_ton_kho) desc", nativeQuery = true)
	List<Object[]> getSanPhamBanChay();

	@Query(value = "select don_hang.ma_khach_hang, khach_hang.ho_ten as 'Ten', COUNT(don_hang.madh) as 'Tong so don', sum(don_hang.tong_tien) as 'Tong tien'\r\n"
			+ "from don_hang\r\n"
			+ "join khach_hang on khach_hang.ma_khach_hang = don_hang.ma_khach_hang\r\n"
			+ "where don_hang.tinh_trang = '2'\r\n"
			+ "group by don_hang.ma_khach_hang,khach_hang.ho_ten \r\n"
			+ "order by COUNT(don_hang.madh) desc", nativeQuery = true)
	List<Object[]> getNguoiMuaNhieuNhat();

	@Query(value = "select ngay_dat_hang as 'Time' ,count(madh) as 'so luong',sum(tong_tien) as 'tong tien' from don_hang where tinh_trang = '2'\r\n"
			+ "group by ngay_dat_hang\r\n"
			+ "order by ngay_dat_hang desc", nativeQuery = true)
	List<Object[]> getThongKeTheoNgay();


	@Query(value = "select cast(year(ngay_dat_hang) as varchar) + '-' +cast(month(ngay_dat_hang) as varchar) month, \r\n"
			+ "count(madh) as 'count', sum(tong_tien) as 'sum' from don_hang where tinh_trang = '2'\r\n"
			+ "group by month(ngay_dat_hang), year(ngay_dat_hang)\r\n"
			+ "order by month desc", nativeQuery = true)
	List<Object[]> getThongKeTheoThang();

	@Query(value = "select year(ngay_dat_hang) as 'year',count(madh) as 'count', sum(tong_tien) as 'sum' from don_hang where tinh_trang = '2'\r\n"
			+ "group by year(ngay_dat_hang)\r\n"
			+ "order by year(ngay_dat_hang) desc", nativeQuery = true)
	List<Object[]> getThongKeTheoNam();

	@Query(value = "select * from san_pham where is_delete = 0 and tensp like %?%", nativeQuery = true)
	Page<SanPham> findBytenSPContaining(String tenSP, Pageable pageable);

	@Query(value = "select * from san_pham where ma_nhacc = ? and is_delete = 0", nativeQuery = true)
	Page<SanPham> findSanPhamByMaNhaCCContaining(Long brand, Pageable pageable);

	@Query(value = "select * from san_pham where tensp like %?% AND ma_loaisp = ? and is_delete = 0", nativeQuery = true)
	Page<SanPham> findByTenSPAndMaLoaiSPContaining(String tenSP, Long maloaisp, Pageable pageable);

	@Query(value = "select * from san_pham where tensp like %?% and ma_nhacc = ? and ma_loaisp = ? and is_delete = 0", nativeQuery = true)
	Page<SanPham> findSanPhamByTenSPAndMaNhaCCAndMaLoaiSPContaining(String tenSP, Long ma_nhacc, Long ma_loaisp, Pageable pageable);

	@Query(value = "select * from san_pham where tensp like %?% and ma_nhacc = ? and is_delete = 0", nativeQuery = true)
	Page<SanPham> findSanPhamByTenSPAndMaNhaCCContaining(String tenSP, Long ma_nhacc, Pageable pageable);

	@Query(value = "select * from san_pham where ma_loaisp = ? and is_delete = 0", nativeQuery = true)
	Page<SanPham> findAllProductByCategoryId(int id, Pageable pageable);

	@Query(value = "select san_pham.masp, san_pham.tensp, san_pham.hinh_anh, san_pham.don_gia, san_pham.discount, san_pham.sl_ton_kho , sum(chi_tiet_don_hang.don_gia) as 'Tổng tiền', COUNT(san_pham.sl_ton_kho) as 'Số lượng' from chi_tiet_don_hang \r\n"
			+ "join don_hang on don_hang.madh = chi_tiet_don_hang.madh\r\n"
			+ "join san_pham on chi_tiet_don_hang.masp = san_pham.masp \r\n" + "where don_hang.tinh_trang = '2'\r\n"
			+ "group by san_pham.tensp\r\n" + "order by COUNT(san_pham.sl_ton_kho) desc", nativeQuery = true)
	List<SanPham> ListSanPhamBanChay();


	@Query(value = "SELECT * FROM san_pham WHERE ma_loaisp = (SELECT ma_loaisp FROM san_pham WHERE masp = ?)", nativeQuery = true)
	Page<SanPham> ListSanPhamCungLoai(int masp, Pageable pageable);

	@Query(value = "SELECT * FROM san_pham WHERE ma_nhacc = (SELECT ma_nhacc FROM san_pham WHERE masp = ?)", nativeQuery = true)
	Page<SanPham> ListSanPhamCungNhaCC(int masp, Pageable pageable);

	@Query(value = "SELECT loai_san_pham.ten_loaisp FROM loai_san_pham JOIN san_pham ON loai_san_pham.ma_loaisp = san_pham.ma_loaisp WHERE masp = ?", nativeQuery = true)
	String getTenLoaiSP(int masp);

	@Query(value = "SELECT nha_cung_cap.ten_nhacc FROM san_pham JOIN nha_cung_cap ON nha_cung_cap.ma_nhacc = san_pham.ma_nhacc WHERE san_pham.masp = ?", nativeQuery = true)
	String getTenNhaCC(int masp);

	@Query(value = "SELECT * FROM san_pham ORDER BY masp OFFSET ? ROWS FETCH NEXT 4 ROWS ONLY", nativeQuery = true)
	List<SanPham> getNext4(int offset);

	@Query(value = "select top 8 * from san_pham where is_delete = 0 order by masp desc", nativeQuery = true)
	List<SanPham> get8Last();

	@Query(value = "select top 4 * from san_pham where is_delete = 0", nativeQuery = true)
	List<SanPham> gettop4();

	@Query(value = "SELECT san_pham.masp, san_pham.tensp, san_pham.hinh_anh, san_pham.don_gia, san_pham.discount, san_pham.sl_ton_kho\r\n"
			+ "FROM chi_tiet_don_hang  \r\n"
			+ "JOIN san_pham  ON chi_tiet_don_hang.masp = san_pham.masp \r\n"
			+ "GROUP BY san_pham.masp, san_pham.tensp, san_pham.hinh_anh, san_pham.don_gia, san_pham.discount, san_pham.sl_ton_kho\r\n"
			+ "ORDER BY COUNT(chi_tiet_don_hang.so_luong) DESC", nativeQuery = true)
	List<Object[]> getTopProducts(Pageable pageable);


	@Query(value = "select san_pham.tensp , sum(chi_tiet_don_hang.don_gia) as 'Tổng tiền', COUNT(san_pham.sl_ton_kho) as 'Số lượng' from chi_tiet_don_hang \r\n"
			+ "join don_hang on don_hang.madh = chi_tiet_don_hang.madh\r\n"
			+ "join san_pham on chi_tiet_don_hang.masp = san_pham.masp \r\n" + "where don_hang.tinh_trang = '2'\r\n"
			+ "AND san_pham.ma_nhacc = ?1\r\n" 
			+ "group by san_pham.tensp\r\n" + "order by COUNT(san_pham.sl_ton_kho) desc", nativeQuery = true)
	List<Object[]> getLoaiSanPhamBanChayTheoNhaCC(int maNhaCC);


	@Modifying
	@Transactional
	@Query(value = "UPDATE san_pham SET discount = ?,don_gia = ?,hinh_anh = ?,mo_ta =? ,sl_ton_kho = ?,tensp = ?,tinh_trang = ?,ma_loaisp = ?,ma_nhacc = ? WHERE masp = ?  ", nativeQuery = true)
	void update(double discount, double donGia, String hinhAnh, String moTa, int slTonKho,
			String tenSP, boolean tinhTrang, int maLoaiSP, int maNhaCC, int maSP);

	

	@Query(value = "SELECT DISTINCT san_pham.masp, san_pham.discount, san_pham.don_gia, san_pham.hinh_anh, san_pham.mo_ta, san_pham.ngaynhaphang, san_pham.sl_ton_kho, san_pham.tensp, san_pham.tinh_trang, san_pham.ma_loaisp, san_pham.ma_nhacc FROM san_pham JOIN da_xem ON da_xem.masp = san_pham.masp WHERE ma_khach_hang = ?", nativeQuery = true)
	List<SanPham> findSanPhamById(int id);

	@Modifying
	@Transactional
	@Query(value = "update san_pham set is_delete = 1 where masp = ?", nativeQuery = true)
	void isDelete(Integer id);

	@Query(value = "select * from san_pham where is_delete = 0", nativeQuery = true)
	Page<SanPham> findWithIsDelete(Pageable pageable);

}
