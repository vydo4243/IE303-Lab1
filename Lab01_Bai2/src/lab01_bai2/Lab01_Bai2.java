/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lab01_bai2;

/**
 *
 * @author Admin
 */

import java.util.Random;
public class Lab01_Bai2 {
    public static void main(String[] args) {
        double r = 1; // Bán kính của hình tròn
        int numPoints = 1000000; // Số lượng điểm ngẫu nhiên

        double approxArea = approximateCircleArea(r, numPoints);
        double pi=approxArea/(r*r);
        
        System.out.printf("Xap xi gia tri cua pi la:  %.6f\n",pi);
    }
    public static double approximateCircleArea(double r, int numPoints) {
        Random random = new Random();
        int pointsInCircle = 0;

        for (int i = 0; i < numPoints; i++) {
            // Tạo điểm ngẫu nhiên trong hình vuông [-r, r] x [-r, r]
            double x = random.nextDouble() * 2 * r - r;
            double y = random.nextDouble() * 2 * r - r;

            // Kiểm tra điểm có nằm trong hình tròn không
            if (x * x + y * y <= r * r) {
                pointsInCircle++;
            }
        }

        // Diện tích hình vuông ngoại tiếp hình tròn
        double areaSquare = (2 * r) * (2 * r);

        // Diện tích hình tròn xấp xỉ
        return ((double) pointsInCircle / numPoints) * areaSquare;
    }
}
