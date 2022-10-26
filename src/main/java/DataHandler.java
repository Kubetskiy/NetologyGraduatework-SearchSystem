import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.*;
import java.util.*;

/**
 Служебный класс. Работа с файлами, первичная инициализация структур данных
 */
public class DataHandler {
    private static final String STOP_WORDS_FILE = "stop-ru.txt";

    /**
     Загружает список стоп-слов (может по другому это называть?) из файла
     */
    public Set<String> getStopWords() throws IOException {
        var words = new TreeSet<String>();
        var file = new File(STOP_WORDS_FILE);
        try (var in = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {

            String word;
            while ((word = in.readLine()) != null) {
                words.add(word);
            }
        }
        return words;
    }

    /**
     Получаем папку для анализа (File), отдаем сформированную структуру данных

     @param pdfDir Папка расположения файлов для обработки
     @return Структура данных DocumentName -> Класс(страница, МАПа распределения слов)
     */
    public Map<String, Set<IndexedPage>> pdfDataHandler(File pdfDir) throws IOException {
        // TODO Уточнить, если передана не папка с файлами
        // TODO Также уточнить, считается ли по умолчанию, что все файлы в папке PDF
        // Основная структура данных
        Map<String, Set<IndexedPage>> indexedData = new HashMap<>();
        // Странички одной книги
        Set<IndexedPage> pagesFromDoc;
        // Распределение слов на странице
        Map<String, Integer> wordDistribution;
        // Переменные для удобства восприятия кода
        PdfDocument doc;
        PdfPage page;
        String text;
        String fileName;
        String[] words;
        // По всем файлам папки
        for (File f : Objects.requireNonNull(pdfDir.listFiles())) {
            fileName = f.getName();
            pagesFromDoc = new HashSet<>();
            // Вносим запись о файле
            indexedData.put(fileName, pagesFromDoc);
            // По всем страницам документа
            doc = new PdfDocument(new PdfReader(f));
            for (int i = 0; i < doc.getNumberOfPages(); i++) {
                page = doc.getPage(i + 1);
                text = PdfTextExtractor.getTextFromPage(page);
                words = text.split("\\P{IsAlphabetic}+");
                // Генерим распределение
                wordDistribution = new HashMap<>();
                for (String word : words) {
                    wordDistribution.put(word, wordDistribution.getOrDefault(word, 0) + 1);
                }
                // Добавляем объект IndexedPage(страница, распределение) к сету файла
                indexedData.get(fileName).add(new IndexedPage(i, wordDistribution));
            }
        }
        return indexedData;
    }
}
