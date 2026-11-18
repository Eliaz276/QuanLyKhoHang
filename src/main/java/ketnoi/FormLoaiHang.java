
package ketnoi;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import ketnoi.KetNoi;

public class FormLoaiHang extends JFrame {

    JTextField txtMaLoai = new JTextField();
    JTextField txtTenLoai = new JTextField();
    JTextField txtMoTa = new JTextField();
    JTextField txtTimKiem = new JTextField();

    DefaultTableModel model;
    JTable table;

    public FormLoaiHang() {
        setTitle("QUAN LY LOAI HANG");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel pnNhap = new JPanel(new GridLayout(3, 2, 10, 10));
        pnNhap.setBorder(BorderFactory.createTitledBorder("Thong tin loai hang"));
        pnNhap.add(new JLabel("Ma loai:"));
        pnNhap.add(txtMaLoai);
        pnNhap.add(new JLabel("Ten loai:"));
        pnNhap.add(txtTenLoai);
        pnNhap.add(new JLabel("Mo ta:"));
        pnNhap.add(txtMoTa);
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
        model.setColumnIdentifiers(
            new String[]{"Ma loai", "Ten loai", "Mo ta"}
        );
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
                    txtMaLoai.setText(model.getValueAt(dong, 0).toString());
                    txtTenLoai.setText(model.getValueAt(dong, 1).toString());
                    txtMoTa.setText(model.getValueAt(dong, 2) == null
                            ? "" : model.getValueAt(dong, 2).toString());
                }
            }
        });
        hienThi("");
    }
    void hienThi(String tuKhoa) {
        model.setRowCount(0);
        String sql = "SELECT * FROM LOAIHANG "
                   + "WHERE MaLoai LIKE ? OR TenLoai LIKE ? OR MoTa LIKE ?";
        try (Connection conn = KetNoi.ketNoi()) {
            if (conn == null) return;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                String key = "%" + tuKhoa + "%";
                ps.setString(1, key);
                ps.setString(2, key);
                ps.setString(3, key);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("MaLoai"),
                        rs.getString("TenLoai"),
                        rs.getString("MoTa")
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    void them() {
        String sql = "INSERT INTO LOAIHANG(MaLoai, TenLoai, MoTa) VALUES(?,?,?)";
        try (Connection conn = KetNoi.ketNoi()) {
            if (conn == null) return;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, txtMaLoai.getText().trim());
                ps.setString(2, txtTenLoai.getText().trim());
                ps.setString(3, txtMoTa.getText().trim());
                if (txtMaLoai.getText().trim().isEmpty()
                        || txtTenLoai.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui long nhap ma va ten loai!");
                    return;
                }
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Them thanh cong!");
                lamMoi();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loi: " + e.getMessage());
        }
    }
    void sua() {
        String sql = "UPDATE LOAIHANG SET TenLoai=?, MoTa=? WHERE MaLoai=?";
        try (Connection conn = KetNoi.ketNoi()) {
            if (conn == null) return;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, txtTenLoai.getText().trim());
                ps.setString(2, txtMoTa.getText().trim());
                ps.setString(3, txtMaLoai.getText().trim());
                int kq = ps.executeUpdate();
                if (kq > 0) {
                    JOptionPane.showMessageDialog(this, "Sua thanh cong!");
                } else {
                    JOptionPane.showMessageDialog(this, "Khong tim thay loai hang!");
                }
                lamMoi();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loi: " + e.getMessage());
        }
    }
    void xoa() {
        String ma = txtMaLoai.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hay chon loai hang can xoa!");
            return;
        }
        int chon = JOptionPane.showConfirmDialog(
            this, "Ban co chac muon xoa?", "Xac nhan",
            JOptionPane.YES_NO_OPTION
        );
        if (chon != JOptionPane.YES_OPTION) return;
        String sql = "DELETE FROM LOAIHANG WHERE MaLoai=?";
        try (Connection conn = KetNoi.ketNoi()) {
            if (conn == null) return;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, ma);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Da xoa loai hang!");
                lamMoi();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this, "Khong the xoa! Co the loai hang dang duoc su dung."
            );
        }
    }
    void lamMoi() {
        txtMaLoai.setText("");
        txtTenLoai.setText("");
        txtMoTa.setText("");
        txtTimKiem.setText("");
        table.clearSelection();
        hienThi("");
    }
    public static void main(String[] args) {
        new FormLoaiHang().setVisible(true);
    }
}
