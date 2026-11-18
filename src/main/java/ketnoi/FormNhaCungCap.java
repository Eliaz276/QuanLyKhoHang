
package ketnoi;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FormNhaCungCap extends JFrame {
    JTextField txtMaNCC = new JTextField();
    JTextField txtTenNCC = new JTextField();
    JTextField txtSDT = new JTextField();
    JTextField txtDiaChi = new JTextField();
    JTextField txtEmail = new JTextField();
    JTextField txtTimKiem = new JTextField();
    DefaultTableModel model;
    JTable table;
    public FormNhaCungCap() {
        setTitle("QUAN LY NHA CUNG CAP");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel pnNhap = new JPanel(new GridLayout(5, 2, 10, 10));
        pnNhap.setBorder(BorderFactory.createTitledBorder("Thong tin nha cung cap"));
        pnNhap.add(new JLabel("Ma nha cung cap:"));
        pnNhap.add(txtMaNCC);
        pnNhap.add(new JLabel("Ten nha cung cap:"));
        pnNhap.add(txtTenNCC);
        pnNhap.add(new JLabel("So dien thoai:"));
        pnNhap.add(txtSDT);
        pnNhap.add(new JLabel("Dia chi:"));
        pnNhap.add(txtDiaChi);
        pnNhap.add(new JLabel("Email:"));
        pnNhap.add(txtEmail);
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
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
            "Ma NCC", "Ten NCC", "So dien thoai", "Dia chi", "Email"
        });
        table = new JTable(model);
        add(pnTren, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());
        btnMoi.addActionListener(e -> lamMoi());
        btnTim.addActionListener(e -> hienThi(txtTimKiem.getText()));
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int dong = table.getSelectedRow();
                if (dong >= 0) {
                    txtMaNCC.setText(model.getValueAt(dong, 0).toString());
                    txtTenNCC.setText(model.getValueAt(dong, 1).toString());
                    txtSDT.setText(model.getValueAt(dong, 2) == null ? "" : model.getValueAt(dong, 2).toString());
                    txtDiaChi.setText(model.getValueAt(dong, 3) == null ? "" : model.getValueAt(dong, 3).toString());
                    txtEmail.setText(model.getValueAt(dong, 4) == null ? "" : model.getValueAt(dong, 4).toString());
                }
            }
        });
        hienThi("");
    }
    void hienThi(String tuKhoa) {
        model.setRowCount(0);
        String sql = "SELECT * FROM NHACUNGCAP "
                   + "WHERE MaNCC LIKE ? OR TenNCC LIKE ? "
                   + "OR SoDienThoai LIKE ? OR DiaChi LIKE ? OR Email LIKE ?";
        try (Connection conn = KetNoi.ketNoi()) {
            if (conn == null) return;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                String key = "%" + tuKhoa.trim() + "%";
                for (int i = 1; i <= 5; i++) {
                    ps.setString(i, key);
                }
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("MaNCC"),
                        rs.getString("TenNCC"),
                        rs.getString("SoDienThoai"),
                        rs.getString("DiaChi"),
                        rs.getString("Email")
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loi: " + e.getMessage());
        }
    }
    void them() {
        if (txtMaNCC.getText().trim().isEmpty()
                || txtTenNCC.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui long nhap ma va ten nha cung cap!");
            return;
        }
        String sql = "INSERT INTO NHACUNGCAP "
                   + "(MaNCC, TenNCC, SoDienThoai, DiaChi, Email) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = KetNoi.ketNoi()) {
            if (conn == null) return;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, txtMaNCC.getText().trim());
                ps.setString(2, txtTenNCC.getText().trim());
                ps.setString(3, txtSDT.getText().trim());
                ps.setString(4, txtDiaChi.getText().trim());
                ps.setString(5, txtEmail.getText().trim());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Them nha cung cap thanh cong!");
                lamMoi();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loi: " + e.getMessage());
        }
    }
    void sua() {
        String sql = "UPDATE NHACUNGCAP SET TenNCC=?, SoDienThoai=?, "
                   + "DiaChi=?, Email=? WHERE MaNCC=?";
        try (Connection conn = KetNoi.ketNoi()) {
            if (conn == null) return;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, txtTenNCC.getText().trim());
                ps.setString(2, txtSDT.getText().trim());
                ps.setString(3, txtDiaChi.getText().trim());
                ps.setString(4, txtEmail.getText().trim());
                ps.setString(5, txtMaNCC.getText().trim());
                int kq = ps.executeUpdate();
                if (kq > 0) {
                    JOptionPane.showMessageDialog(this, "Sua thanh cong!");
                } else {
                    JOptionPane.showMessageDialog(this, "Khong tim thay nha cung cap!");
                }
                lamMoi();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loi: " + e.getMessage());
        }
    }
    void xoa() {
        String ma = txtMaNCC.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hay chon nha cung cap can xoa!");
            return;
        }
        int chon = JOptionPane.showConfirmDialog(
            this, "Ban co chac muon xoa?", "Xac nhan",
            JOptionPane.YES_NO_OPTION
        );
        if (chon != JOptionPane.YES_OPTION) return;
        String sql = "DELETE FROM NHACUNGCAP WHERE MaNCC=?";
        try (Connection conn = KetNoi.ketNoi()) {
            if (conn == null) return;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, ma);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Da xoa nha cung cap!");
                lamMoi();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this, "Khong the xoa! Nha cung cap co the dang duoc su dung."
            );
        }
    }
    void lamMoi() {
        txtMaNCC.setText("");
        txtTenNCC.setText("");
        txtSDT.setText("");
        txtDiaChi.setText("");
        txtEmail.setText("");
        txtTimKiem.setText("");
        table.clearSelection();
        hienThi("");
    }
    public static void main(String[] args) {
        new FormNhaCungCap().setVisible(true);
    }
}
