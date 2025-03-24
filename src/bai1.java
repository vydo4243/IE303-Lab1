/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */

/**
 *
 * @author Admin
 */
import java.util.Random;
import java.util.Scanner;
public class bai1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhap r: ");
        double r = scanner.nextDouble(); // Bán kính của hình tròn
        int numPoints = 1000000; // Số lượng điểm ngẫu nhiên

        double approxArea = approximateCircleArea(r, numPoints);
        System.out.printf("Dien tich xap xi cua hinh tron ban kinh %.2f la: %.4f\n", r, approxArea);
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
