package lab01_bai3;

import java.util.*;

public class lab01_bai3{
    static class Point implements Comparable<Point> {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Point p) {
            if (this.y == p.y) return this.x - p.x;
            return this.y - p.y;
        }

        @Override
        public String toString() {
            return x + " " + y;
        }
    }

    // Tính tích có hướng của vector PQ và QR
    static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0; // Thẳng hàng
        return (val > 0) ? 1 : 2; // 1: Quẹo phải, 2: Quẹo trái
    }

    // Tính khoảng cách bình phương giữa 2 điểm
    static int distSq(Point p1, Point p2) {
        return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
    }

    // Tìm Convex Hull bằng thuật toán Graham Scan
    public static List<Point> convexHull(Point points[]) {
        int n = points.length;
        if (n < 3) return null; // Không đủ điểm để tạo Convex Hull

        // Tìm điểm có tọa độ y nhỏ nhất (nếu bằng thì x nhỏ nhất)
        int minY = 0;
        for (int i = 1; i < n; i++) {
            if (points[i].compareTo(points[minY]) < 0) {
                minY = i;
            }
        }

        // Đưa điểm P0 ra đầu mảng
        Point temp = points[0];
        points[0] = points[minY];
        points[minY] = temp;
        Point p0 = points[0];

        // Sắp xếp các điểm còn lại theo góc cực trị với P0
        Arrays.sort(points, 1, n, (p1, p2) -> {
            int o = orientation(p0, p1, p2);
            if (o == 0) {
                return (distSq(p0, p2) >= distSq(p0, p1)) ? -1 : 1;
            }
            return (o == 2) ? -1 : 1;
        });

        // Sử dụng Stack để lưu các điểm trên Convex Hull
        Stack<Point> stack = new Stack<>();
        stack.push(points[0]);
        stack.push(points[1]);
        stack.push(points[2]);

        // Duyệt qua các điểm còn lại
        for (int i = 3; i < n; i++) {
            while (stack.size() > 1 && 
                   orientation(nextToTop(stack), stack.peek(), points[i]) != 2) {
                stack.pop();
            }
            stack.push(points[i]);
        }

        // Chuyển các điểm trong stack thành danh sách
        List<Point> hull = new ArrayList<>(stack);
        Collections.reverse(hull); // Đảo ngược danh sách để lấy điểm đúng thứ tự
        return hull;
    }

    private static Point nextToTop(Stack<Point> stack) {
        Point top = stack.pop();
        Point nextTop = stack.peek();
        stack.push(top);
        return nextTop;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nhap so luong tram canh bao va toa do tuong ung cho tung tram:");
        int n = scanner.nextInt(); // Số lượng trạm phát sóng
        Point points[] = new Point[n];

        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            points[i] = new Point(x, y);
        }

        List<Point> hull = convexHull(points);

        if (hull != null) {
            System.out.println("\nToa do cac tram duoc su dung lam tram canh bao:");
            for (Point p : hull) {
                System.out.println(p);
            }
        } else {
            System.out.println("Khong du diem de tao Convex Hull.");
        }

        scanner.close();
    }
}
