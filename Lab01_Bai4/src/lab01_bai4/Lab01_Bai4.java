package lab01_bai4;

import java.util.*;
import java.io.*;
public class Lab01_Bai4 {

    private static final Map<String, Integer> vocab = new LinkedHashMap<>();   //Lưu trữ từ và id của từ
    private static final Map<String, Integer> corpus = new LinkedHashMap<>();   //Đếm số lần xuất hiện của từng từ.
    private static final Map<String, Integer> pairCorpus = new LinkedHashMap<>();   //Đếm số lần xuất hiện của cặp từ.
    private static double[] probs;  //Mảng lưu xác suất xuất hiện của từng từ.
    private static double[][] conditionalProbs; //Ma trận lưu xác suất có điều kiện giữa các từ.
    
    public static void readFile(String filePath) {
        List<String> lines = new ArrayList<>();
        //Đọc file theo từng dòng + trim + lowerCase
        try (Scanner fileScanner = new Scanner(new File(filePath))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim().toLowerCase();
                lines.add(line);
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Không tìm thấy file");
            return;
        }
        
        int wordId = 0;
        for (String line : lines) {
            //Tách từng từ
            String[] words = line.split("\\s+");
            for (String word : words) {
                //Nếu từ này đã có thì +1 vào số lần xuất hiện, còn nếu chưa có thì thêm vào với số lần xuất hiện là 1
                corpus.put(word, corpus.getOrDefault(word, 0) + 1);
                //Thêm từ và id nếu mới
                if (!vocab.containsKey(word)) {
                    vocab.put(word, wordId++);
                }
            }
            //Tương tự cho cặp từ
            for (int i = 0; i < words.length - 1; i++) {
                String pair = words[i] + "_" + words[i + 1];
                pairCorpus.put(pair, pairCorpus.getOrDefault(pair, 0) + 1);
            }
        }
    }
    public static void constructSingleProb() {  //tính xác suất xuất hiện của từng từ trong tập dữ liệu
        int totalWords = 0; //tính tổng số từ trong tập dữ liệu.
        for (Map.Entry<String, Integer> item: corpus.entrySet()) {
            totalWords += item.getValue();
        }
        probs = new double[vocab.size()];
        
        for (Map.Entry<String, Integer> item : corpus.entrySet()) {
            String word = item.getKey();
            int wordId = vocab.get(word);
            probs[wordId] = (double) item.getValue() / totalWords;  //tính xác suất của từ
        }
    }

    public static void constructConditionalProb() { //tính xác suất có điều kiện giữa hai từ 
        int totalPairsOfWords = 0; //tính tổng số cặp từ trong tập dữ liệu.
        for (Map.Entry<String, Integer> item: pairCorpus.entrySet()) {
            totalPairsOfWords += item.getValue();
        }
        conditionalProbs = new double[vocab.size()][vocab.size()];  //xác suất xuất hiện từ w_j sau w_i.
        
        for (Map.Entry<String, Integer> item : pairCorpus.entrySet()) {
            String[] words = item.getKey().split("_");
            if (words.length != 2) continue;    //cặp từ là gồm 2 từ :))
            
            int wordId1 = vocab.getOrDefault(words[0], -1); //lấy id từ thứ nhất
            int wordId2 = vocab.getOrDefault(words[1], -1); //lấy id từ thứ 2
            
            if (wordId1 != -1 && wordId2 != -1) {
                conditionalProbs[wordId1][wordId2] = (double) item.getValue() / totalPairsOfWords;  //tính xác suất
            }
        }
    }
    public static void train() {
        constructSingleProb();
        constructConditionalProb();
    }
    
    public static List<String> infer(String w0, int length) {
        List<String> words = new ArrayList<>();
        words.add(w0);        
        
        Integer w0Idx = vocab.get(w0);  //id từ mình vừa nhập
        if (w0Idx == null) {
            System.out.println("Từ này không có trong từ điển: " + w0);
            return words;
        }
        
        //double logProbs = -Math.log(probs[w0Idx]);
        
        for (int t = 1; t <= length; t++) {
            String bestWord = "";
            double maxProb = 0.0;
            int bestWordIdx = -1;
            
            for (Map.Entry<String, Integer> entry : vocab.entrySet()) {
                int wordIdx = entry.getValue();
                // Xác suất xuât hiện của cặp từ
                double prob = conditionalProbs[w0Idx][wordIdx];
                // Lấy từ mà cặp từ có xác suất cao nhất
                if (prob > maxProb) {
                    bestWord = entry.getKey();
                    maxProb = prob;
                    bestWordIdx = wordIdx;
                }
            }
            
            if (bestWordIdx == -1) break;
            
            words.add(bestWord);    //Thêm từ có xác suất cao nhất đó vào danh sách
            w0Idx = bestWordIdx;    //Tìm từ tiếp theo sau từ vừa tìm được
        }
        
        return words;
    }
    public static String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập từ khóa: ");
        String word = scanner.nextLine().trim().toLowerCase(); // Loại bỏ khoảng trắng và chuẩn hóa
        scanner.close();
        return word;
    }
    public static void main(String[] args) {
        String filePath = "src/UIT-ViOCD.txt";
        readFile(filePath);
        train();        
        List<String> predictedWords = infer(getUserInput(), 5);
        System.out.println(String.join(" ", predictedWords));
    }
}