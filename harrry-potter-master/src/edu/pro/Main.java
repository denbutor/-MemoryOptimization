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

    private static final String FILE_PATH = "harrry-potter-master/src/edu/pro/txt/harry.txt";
    private static final int TOP_WORDS_COUNT = 30;

    public static void main(String[] args) throws IOException {

        LocalDateTime start = LocalDateTime.now();

        try (Stream<String> lines = Files.lines(Paths.get(FILE_PATH))) {

            Map<String, Long> wordFrequencies = lines
                    .parallel()
                    .map(line -> line.replaceAll("[^A-Za-z ]", " ").toLowerCase(Locale.ROOT))
                    .flatMap(line -> Arrays.stream(line.split(" +")))
                    .filter(word -> !word.isEmpty())
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            wordFrequencies.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(TOP_WORDS_COUNT)
                    .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        LocalDateTime finish = LocalDateTime.now();
        System.out.println("------");
        System.out.println("Execution time (milliseconds): " + ChronoUnit.MILLIS.between(start, finish));
    }
}