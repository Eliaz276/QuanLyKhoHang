
package ketnoi;

import javax.swing.*;
import java.awt.*;

public class FormChinh extends JFrame {

    public FormChinh() {
        setTitle("PHAN MEM QUAN LY KHO HANG");
        setSize(650, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel pnChinh = new JPanel(new BorderLayout(15, 15));
        pnChinh.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        JLabel lblTieuDe = new JLabel(
            "QUAN LY KHO HANG",
            SwingConstants.CENTER
        );
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 26));
        JLabel lblMoTa = new JLabel(
            "He thong quan ly hang hoa, loai hang va nha cung cap",
            SwingConstants.CENTER
        );
        lblMoTa.setFont(new Font("Arial", Font.PLAIN, 14));
        JPanel pnTieuDe = new JPanel(new GridLayout(2, 1, 5, 5));
        pnTieuDe.add(lblTieuDe);
        pnTieuDe.add(lblMoTa);
        JButton btnLoaiHang = new JButton("QUAN LY LOAI HANG");
        JButton btnNhaCungCap = new JButton("QUAN LY NHA CUNG CAP");
        JButton btnHangHoa = new JButton("QUAN LY HANG HOA");
        JButton btnThoat = new JButton("THOAT");
        Font fontNut = new Font("Arial", Font.BOLD, 15);
        btnLoaiHang.setFont(fontNut);
        btnNhaCungCap.setFont(fontNut);
        btnHangHoa.setFont(fontNut);
        btnThoat.setFont(fontNut);
        JPanel pnNut = new JPanel(new GridLayout(4, 1, 15, 15));
        pnNut.add(btnLoaiHang);
        pnNut.add(btnNhaCungCap);
        pnNut.add(btnHangHoa);
        pnNut.add(btnThoat);
        JLabel lblCuoi = new JLabel(
            "DO AN JAVA - SQL SERVER 2025",
            SwingConstants.CENTER
        );
        pnChinh.add(pnTieuDe, BorderLayout.NORTH);
        pnChinh.add(pnNut, BorderLayout.CENTER);
        pnChinh.add(lblCuoi, BorderLayout.SOUTH);
        add(pnChinh);
        btnLoaiHang.addActionListener(e -> {
            new FormLoaiHang().setVisible(true);
        });
        btnNhaCungCap.addActionListener(e -> {
            new FormNhaCungCap().setVisible(true);
        });
        btnHangHoa.addActionListener(e -> {
            new FormHangHoa().setVisible(true);
        });
        btnThoat.addActionListener(e -> {
            int chon = JOptionPane.showConfirmDialog(
                this,
                "Ban co chac muon thoat?",
                "Xac nhan",
                JOptionPane.YES_NO_OPTION
            );
            if (chon == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FormChinh().setVisible(true);
        });
    }
}
