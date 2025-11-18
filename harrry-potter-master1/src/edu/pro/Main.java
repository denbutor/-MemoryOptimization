package edu.pro;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final String FILE_PATH = "harrry-potter-master1/src/edu/pro/txt/harry.txt";
    private static final int TOP_WORDS_COUNT = 30;

    public static void main(String[] args) {

        System.out.println("Starting file processing (memory optimization)...");
        LocalDateTime start = LocalDateTime.now();

        Map<String, Long> wordFrequencies = null;

        try (Stream<String> lines = Files.lines(Paths.get(FILE_PATH))) {

            wordFrequencies = lines
                    .map(line -> line.replaceAll("[^A-Za-z ]", " ").toLowerCase(Locale.ROOT))
                    .flatMap(line -> Arrays.stream(line.split(" +")))
                    .filter(word -> !word.isEmpty())
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1); // Exit if file is not found
        }

        System.out.println("Counting finished. Sorting top-" + TOP_WORDS_COUNT + " words...");

        if (wordFrequencies != null) {
            wordFrequencies.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(TOP_WORDS_COUNT)
                    .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
        }

        LocalDateTime finish = LocalDateTime.now();

        System.out.println("------");

        long durationMillis = ChronoUnit.MILLIS.between(start, finish);
        System.out.println("Execution time (milliseconds): " + durationMillis);
    }
}