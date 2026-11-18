
package ketnoi;

import java.sql.*;
import javax.swing.*;
import java.awt.*;

public class FormDangNhap extends JFrame {

    JTextField txtTenDangNhap = new JTextField();
    JPasswordField txtMatKhau = new JPasswordField();

    public FormDangNhap() {
        setTitle("DANG NHAP HE THONG");
        setSize(400, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel pnChinh = new JPanel(new BorderLayout(10, 15));
        pnChinh.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        JLabel lblTieuDe = new JLabel(
            "DANG NHAP",
            SwingConstants.CENTER
        );
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        JPanel pnNhap = new JPanel(new GridLayout(2, 2, 10, 15));
        pnNhap.add(new JLabel("Ten dang nhap:"));
        pnNhap.add(txtTenDangNhap);
        pnNhap.add(new JLabel("Mat khau:"));
        pnNhap.add(txtMatKhau);
        JButton btnDangNhap = new JButton("Dang nhap");
        JButton btnThoat = new JButton("Thoat");
        JPanel pnNut = new JPanel();
        pnNut.add(btnDangNhap);
        pnNut.add(btnThoat);
        pnChinh.add(lblTieuDe, BorderLayout.NORTH);
        pnChinh.add(pnNhap, BorderLayout.CENTER);
        pnChinh.add(pnNut, BorderLayout.SOUTH);
        add(pnChinh);
        btnDangNhap.addActionListener(e -> dangNhap());
        txtMatKhau.addActionListener(e -> dangNhap());
        btnThoat.addActionListener(e -> {
            System.exit(0);
        });
        getRootPane().setDefaultButton(btnDangNhap);
    }
    void dangNhap() {
        String ten = txtTenDangNhap.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword());
        if (ten.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Vui long nhap tai khoan va mat khau!"
            );
            return;
        }
        String sql = "SELECT * FROM TAIKHOAN "
                   + "WHERE TenDangNhap = ? "
                   + "AND MatKhauHash = HASHBYTES('SHA2_256', "
                   + "CONVERT(NVARCHAR(100), ?)) "
                   + "AND TrangThai = 1";
        try (Connection conn = KetNoi.ketNoi()) {
            if (conn == null) return;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, ten);
                ps.setString(2, matKhau);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(
                            this, "Dang nhap thanh cong!"
                        );
                        dispose();
                        new FormChinh().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(
                            this, "Sai ten dang nhap hoac mat khau!"
                        );
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this, "Loi dang nhap: " + e.getMessage()
            );
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FormDangNhap().setVisible(true);
        });
    }
}
