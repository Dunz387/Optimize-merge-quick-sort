import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CutoffExperiment {

    static final int Cutoff_min = 1;
    static final int Cutoff_max = 50;
    static final int Cutoff_step = 1;
    static final int warm_up = 3;
    static final int measure = 5;

    public static void main(String[] args) throws Exception {
        File dataDir = new File("data");
        File[] dataFiles = dataDir.listFiles((dir, name) -> name.endsWith(".txt"));
        if (dataFiles == null || dataFiles.length == 0) {
            System.err.println("Không tìm thấy file dữ liệu trong thư mục data/");
            return;
        }
        Arrays.sort(dataFiles, Comparator.comparing(File::getName));

        Files.createDirectories(Paths.get("results"));
        PrintWriter csv = new PrintWriter(new FileWriter("results/benchmark.csv"));
        csv.println("algorithm,data_type,data_size,cutoff,run,time_ns");

        int totalCutoffs = (Cutoff_max - Cutoff_min) / Cutoff_step + 1;
        int totalConfigs = dataFiles.length * 2 * totalCutoffs;
        int configsDone = 0;

        System.out.println("Cutoff: " + Cutoff_min + " -> " + Cutoff_max);
        System.out.println("Data files: " + dataFiles.length);
        System.out.println();

        for (File dataFile : dataFiles) {
            String fileName = dataFile.getName().replace(".txt", "");
            String[] parts = fileName.split("_");
            String dataType = parts[0];
            int dataSize = Integer.parseInt(parts[1]);
            Integer[] original = readData(dataFile);

            System.out.println("── " + fileName + " ──");

            // Merge Sort
            for (int cutoff = Cutoff_min; cutoff <= Cutoff_max; cutoff += Cutoff_step) {
                for (int w = 0; w < warm_up; w++) {
                    Sorting_Algorithms.merge_sort(original.clone(), cutoff);
                }
                for (int run = 1; run <= measure; run++) {
                    Integer[] copy = original.clone();
                    long start = System.nanoTime();
                    Sorting_Algorithms.merge_sort(copy, cutoff);
                    long elapsed = System.nanoTime() - start;
                    csv.println("merge_sort," + dataType + "," + dataSize + "," + cutoff + "," + run + "," + elapsed);
                }
                configsDone++;
            }
            System.out.println("  merge_sort done");

            // Quick Sort
            for (int cutoff = Cutoff_min; cutoff <= Cutoff_max; cutoff += Cutoff_step) {
                for (int w = 0; w < warm_up; w++) {
                    Sorting_Algorithms.quick_sort(original.clone(), cutoff);
                }
                for (int run = 1; run <= measure; run++) {
                    Integer[] copy = original.clone();
                    long start = System.nanoTime();
                    Sorting_Algorithms.quick_sort(copy, cutoff);
                    long elapsed = System.nanoTime() - start;
                    csv.println("quick_sort," + dataType + "," + dataSize + "," + cutoff + "," + run + "," + elapsed);
                }
                configsDone++;
            }
            System.out.println("  quick_sort done");
            System.out.printf("  [%d/%d  %.0f%%]\n\n", configsDone, totalConfigs, 100.0 * configsDone / totalConfigs);
            csv.flush();
        }
  
        csv.close();
        System.out.println("Kết quả trong results/benchmark.csv");
    }

    static Integer[] readData(File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        Integer[] data = new Integer[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            data[i] = Integer.parseInt(lines.get(i).trim());
        }
        return data;
    }
}
