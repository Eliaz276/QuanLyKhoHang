
package ketnoi;

import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FormHangHoa extends JFrame {

    JTextField txtMaHang = new JTextField();
    JTextField txtTenHang = new JTextField();
    JTextField txtSoLuong = new JTextField();
    JTextField txtDonGia = new JTextField();
    JTextField txtDonViTinh = new JTextField();
    JTextField txtNgayNhap = new JTextField(LocalDate.now().toString());
    JTextField txtTimKiem = new JTextField();

    JComboBox<String> cboLoai = new JComboBox<>();
    JComboBox<String> cboNCC = new JComboBox<>();

    DefaultTableModel model;
    JTable table;

    public FormHangHoa() {
        setTitle("QUAN LY HANG HOA");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel pnNhap = new JPanel(new GridLayout(4, 4, 10, 10));
        pnNhap.setBorder(BorderFactory.createTitledBorder("Thong tin hang hoa"));
        pnNhap.add(new JLabel("Ma hang:"));
        pnNhap.add(txtMaHang);
        pnNhap.add(new JLabel("Ten hang:"));
        pnNhap.add(txtTenHang);
        pnNhap.add(new JLabel("Loai hang:"));
        pnNhap.add(cboLoai);
        pnNhap.add(new JLabel("Nha cung cap:"));
        pnNhap.add(cboNCC);
        pnNhap.add(new JLabel("So luong:"));
        pnNhap.add(txtSoLuong);
        pnNhap.add(new JLabel("Don gia:"));
        pnNhap.add(txtDonGia);
        pnNhap.add(new JLabel("Don vi tinh:"));
        pnNhap.add(txtDonViTinh);
        pnNhap.add(new JLabel("Ngay nhap (yyyy-MM-dd):"));
        pnNhap.add(txtNgayNhap);
        JButton btnThem = new JButton("Them");
        JButton btnSua = new JButton("Sua");
        JButton btnXoa = new JButton("Xoa");
        JButton btnMoi = new JButton("Lam moi");
        JButton btnTim = new JButton("Tim kiem");
        JPanel pnNut = new JPanel();
        pnNut.add(btnThem);
        pnNut.add(btnSua);
        pnNut.add(btnXoa);
        pnNut.add(btnMoi);
        JPanel pnTim = new JPanel(new BorderLayout(5, 5));
        pnTim.add(txtTimKiem, BorderLayout.CENTER);
        pnTim.add(btnTim, BorderLayout.EAST);
        JPanel pnTren = new JPanel(new BorderLayout(5, 5));
        pnTren.add(pnNhap, BorderLayout.NORTH);
        pnTren.add(pnNut, BorderLayout.CENTER);
        pnTren.add(pnTim, BorderLayout.SOUTH);
        model = new DefaultTableModel(
            new String[]{
                "Ma hang", "Ten hang", "Ma loai", "Ten loai",
                "Ma NCC", "Ten NCC", "So luong", "Don gia",
                "Don vi tinh", "Ngay nhap"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        add(pnTren, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        taiComboBox();
        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());
        btnMoi.addActionListener(e -> lamMoi());
        btnTim.addActionListener(e -> hienThi(txtTimKiem.getText()));
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int dong = table.getSelectedRow();
                if (dong >= 0) {
                    txtMaHang.setText(model.getValueAt(dong, 0).toString());
                    txtTenHang.setText(model.getValueAt(dong, 1).toString());
                    chonCombo(cboLoai, model.getValueAt(dong, 2).toString());
                    chonCombo(cboNCC, model.getValueAt(dong, 4).toString());
                    txtSoLuong.setText(model.getValueAt(dong, 6).toString());
                    txtDonGia.setText(model.getValueAt(dong, 7).toString());
                    txtDonViTinh.setText(
                        model.getValueAt(dong, 8) == null
                        ? "" : model.getValueAt(dong, 8).toString()
                    );
                    txtNgayNhap.setText(model.getValueAt(dong, 9).toString());
                }
            }
        });
        hienThi("");
    }
    void taiComboBox() {
        try (Connection conn = KetNoi.ketNoi()) {
            if (conn == null) return;
            String sqlLoai = "SELECT MaLoai, TenLoai FROM LOAIHANG ORDER BY MaLoai";
            try (PreparedStatement ps = conn.prepareStatement(sqlLoai);
                 ResultSet rs = ps.executeQuery()) {
                cboLoai.removeAllItems();
                while (rs.next()) {
                    cboLoai.addItem(
                        rs.getString("MaLoai") + " - " + rs.getString("TenLoai")
                    );
                }
            }
            String sqlNCC = "SELECT MaNCC, TenNCC FROM NHACUNGCAP ORDER BY MaNCC";
            try (PreparedStatement ps = conn.prepareStatement(sqlNCC);
                 ResultSet rs = ps.executeQuery()) {
                cboNCC.removeAllItems();
                while (rs.next()) {
                    cboNCC.addItem(
                        rs.getString("MaNCC") + " - " + rs.getString("TenNCC")
                    );
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loi tai danh sach: " + e.getMessage());
        }
    }
    String layMa(JComboBox<String> cbo) {
        Object chon = cbo.getSelectedItem();
        if (chon == null) {
            return "";
        }
        return chon.toString().split(" - ", 2)[0];
    }
    void chonCombo(JComboBox<String> cbo, String ma) {
        for (int i = 0; i < cbo.getItemCount(); i++) {
            if (cbo.getItemAt(i).startsWith(ma + " - ")) {
                cbo.setSelectedIndex(i);
                return;
            }
        }
    }
    boolean kiemTra() {
        if (txtMaHang.getText().trim().isEmpty()
                || txtTenHang.getText().trim().isEmpty()
                || cboLoai.getSelectedItem() == null
                || cboNCC.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui long nhap day du thong tin!");
            return false;
        }
        try {
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            BigDecimal donGia = new BigDecimal(txtDonGia.getText().trim());
            Date.valueOf(txtNgayNhap.getText().trim());
            if (soLuong < 0 || donGia.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "So luong va don gia khong duoc am!");
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this, "So luong, don gia hoac ngay nhap khong hop le!"
            );
            return false;
        }
        return true;
    }
    void hienThi(String tuKhoa) {
        model.setRowCount(0);
        String sql = "SELECT H.MaHang, H.TenHang, H.MaLoai, L.TenLoai, "
                   + "H.MaNCC, N.TenNCC, H.SoLuong, H.DonGia, "
                   + "H.DonViTinh, H.NgayNhap "
                   + "FROM HANGHOA H "
                   + "INNER JOIN LOAIHANG L ON H.MaLoai = L.MaLoai "
                   + "INNER JOIN NHACUNGCAP N ON H.MaNCC = N.MaNCC "
                   + "WHERE H.MaHang LIKE ? OR H.TenHang LIKE ? "
                   + "OR L.TenLoai LIKE ? OR N.TenNCC LIKE ?";
        try (Connection conn = KetNoi.ketNoi()) {
            if (conn == null) return;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                String key = "%" + tuKhoa.trim() + "%";
                for (int i = 1; i <= 4; i++) {
                    ps.setString(i, key);
                }
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("MaHang"),
                        rs.getString("TenHang"),
                        rs.getString("MaLoai"),
                        rs.getString("TenLoai"),
                        rs.getString("MaNCC"),
                        rs.getString("TenNCC"),
                        rs.getInt("SoLuong"),
                        rs.getBigDecimal("DonGia").toPlainString(),
                        rs.getString("DonViTinh"),
                        rs.getDate("NgayNhap").toString()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loi: " + e.getMessage());
        }
    }
    void them() {
        if (!kiemTra()) return;
        String sql = "INSERT INTO HANGHOA "
                   + "(MaHang, TenHang, MaLoai, MaNCC, SoLuong, DonGia, DonViTinh, NgayNhap) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = KetNoi.ketNoi()) {
            if (conn == null) return;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, txtMaHang.getText().trim());
                ps.setString(2, txtTenHang.getText().trim());
                ps.setString(3, layMa(cboLoai));
                ps.setString(4, layMa(cboNCC));
                ps.setInt(5, Integer.parseInt(txtSoLuong.getText().trim()));
                ps.setBigDecimal(6, new BigDecimal(txtDonGia.getText().trim()));
                ps.setString(7, txtDonViTinh.getText().trim());
                ps.setDate(8, Date.valueOf(txtNgayNhap.getText().trim()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Them hang hoa thanh cong!");
                lamMoi();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loi them hang hoa: " + e.getMessage());
        }
    }
    void sua() {
        if (!kiemTra()) return;
        String sql = "UPDATE HANGHOA SET TenHang=?, MaLoai=?, MaNCC=?, "
                   + "SoLuong=?, DonGia=?, DonViTinh=?, NgayNhap=? WHERE MaHang=?";
        try (Connection conn = KetNoi.ketNoi()) {
            if (conn == null) return;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, txtTenHang.getText().trim());
                ps.setString(2, layMa(cboLoai));
                ps.setString(3, layMa(cboNCC));
                ps.setInt(4, Integer.parseInt(txtSoLuong.getText().trim()));
                ps.setBigDecimal(5, new BigDecimal(txtDonGia.getText().trim()));
                ps.setString(6, txtDonViTinh.getText().trim());
                ps.setDate(7, Date.valueOf(txtNgayNhap.getText().trim()));
                ps.setString(8, txtMaHang.getText().trim());
                int kq = ps.executeUpdate();
                if (kq > 0) {
                    JOptionPane.showMessageDialog(this, "Sua hang hoa thanh cong!");
                } else {
                    JOptionPane.showMessageDialog(this, "Khong tim thay hang hoa!");
                }
                lamMoi();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loi sua hang hoa: " + e.getMessage());
        }
    }
    void xoa() {
        String ma = txtMaHang.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hay chon hang hoa can xoa!");
            return;
        }
        int chon = JOptionPane.showConfirmDialog(
            this,
            "Ban co chac muon xoa hang hoa nay?",
            "Xac nhan",
            JOptionPane.YES_NO_OPTION
        );
        if (chon != JOptionPane.YES_OPTION) return;
        String sql = "DELETE FROM HANGHOA WHERE MaHang=?";
        try (Connection conn = KetNoi.ketNoi()) {
            if (conn == null) return;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, ma);
                int kq = ps.executeUpdate();
                if (kq > 0) {
                    JOptionPane.showMessageDialog(this, "Xoa hang hoa thanh cong!");
                } else {
                    JOptionPane.showMessageDialog(this, "Khong tim thay hang hoa!");
                }
                lamMoi();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loi xoa hang hoa: " + e.getMessage());
        }
    }
    void lamMoi() {
        txtMaHang.setText("");
        txtTenHang.setText("");
        txtSoLuong.setText("");
        txtDonGia.setText("");
        txtDonViTinh.setText("");
        txtNgayNhap.setText(LocalDate.now().toString());
        txtTimKiem.setText("");
        if (cboLoai.getItemCount() > 0) {
            cboLoai.setSelectedIndex(0);
        }
        if (cboNCC.getItemCount() > 0) {
            cboNCC.setSelectedIndex(0);
        }
        table.clearSelection();
        hienThi("");
    }
    public static void main(String[] args) {
        new FormHangHoa().setVisible(true);
    }
}
