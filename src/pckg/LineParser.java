package pckg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

// Интерфейс для чтения файлов
interface FileService {
    List<String> readLines(String filePath) throws IOException;
}

// Интерфейс для анализа строк
interface LineAnalysisService {
    void analyzeLines(List<String> lines);
}

// Реализация FileService
class FileServiceImpl implements FileService {
    @Override
    public List<String> readLines(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }
}

// Реализация LineAnalysisService
class LineAnalysisServiceImpl implements LineAnalysisService {
    @Override
    public void analyzeLines(List<String> lines) {
        // 1. Подсчет длины каждой строки
        System.out.println("Длины строк:");
        IntStream.range(0, lines.size())
                .parallel()
                .forEach(i -> {
                    String line = lines.get(i).trim();
                    if (!line.isEmpty()) {
                        System.out.println("Строка " + (i + 1) + " - длина: " + line.length());
                    }
                });

        // 2. Подсчет количества символов в каждой строк
        System.out.println("\nКоличество символов в строках:");
        IntStream.range(0, lines.size())
                .parallel()
                .forEach(i -> {
                    String line = lines.get(i).trim();
                    if (!line.isEmpty()) {
                        long charCount = line.chars().count();
                        System.out.println("Строка " + (i + 1) + " - символов: " + charCount);
                    }
                });

        // 3. Нахождение строки с максимальным количеством уникальных символов
        Optional<Integer> maxUniqueIndex = IntStream.range(0, lines.size())
                .parallel()
                .filter(i -> !lines.get(i).trim().isEmpty())
                .boxed()
                .max(Comparator.comparingInt(i -> (int) lines.get(i).trim().chars().distinct().count()));
        maxUniqueIndex.ifPresent(index -> {
            System.out.println("\nСтрока с наибольшим количеством уникальных символов:");
            System.out.println("Строка " + (index + 1) + " - \"" + lines.get(index).trim() + "\"");
        });

        // 4. Подсчет уникальных символов для каждой строки
        System.out.println("\nУникальные символы в строках:");
        IntStream.range(0, lines.size())
                .parallel()
                .forEach(i -> {
                    String line = lines.get(i).trim();
                    if (!line.isEmpty()) {
                        long uniqueCharCount = line.chars().distinct().count();
                        System.out.println("Строка " + (i + 1) + " - уникальных символов: " + uniqueCharCount);
                    }
                });

        // 5. Поиск самого длинного слова в каждой строке
        System.out.println("\nСамое длинное слово в каждой строке:");
        IntStream.range(0, lines.size())
                .parallel()
                .forEach(i -> {
                    String line = lines.get(i).trim();
                    if (!line.isEmpty()) {
                        Optional<String> longestWord = Arrays.stream(line.split("\\W+"))
                                .max(Comparator.comparingInt(String::length));
                        longestWord.ifPresent(word ->
                                System.out.println("Строка " + (i + 1) + " - самое длинное слово: \"" + word + "\""));
                    }
                });

        // 6. Поиск самого длинного слова во всем тексте
        Optional<String> longestWordInText = lines.stream()
                .parallel()
                .flatMap(line -> Arrays.stream(line.trim().split("\\W+")))
                .filter(word -> !word.isEmpty())
                .max(Comparator.comparingInt(String::length));
        longestWordInText.ifPresent(word ->
                System.out.println("\nСамое длинное слово во всем тексте: \"" + word + "\""));
    }
}

// Главный класс с внедрением зависимостей
public class LineParser {
    public static void main(String[] args) {
        // Создаем экземпляры сервисов
        FileService fileService = new FileServiceImpl();
        LineAnalysisService lineAnalysisService = new LineAnalysisServiceImpl();

        // Путь к текстовому файлу
        String filePath = "src/pckg/book.txt";

        try {
            // Читаем содержимое файла
            List<String> lines = fileService.readLines(filePath);
            // Анализируем строки
            lineAnalysisService.analyzeLines(lines);
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }
}
