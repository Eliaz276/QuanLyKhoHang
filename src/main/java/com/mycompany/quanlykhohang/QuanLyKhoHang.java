
package com.mycompany.quanlykhohang;
import ketnoi.FormDangNhap;
import javax.swing.SwingUtilities;
public class QuanLyKhoHang {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FormDangNhap().setVisible(true);
        });
    }
}
