import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BooleanSearchEngine implements SearchEngine {
    private final Set<String> stopWords;
    private final Map<String, Set<IndexedPage>> allIndexedDocuments;

    /**
     Загружаем стоп-слова и проиндексированные в единую структуру файлы pdf
     */
    public BooleanSearchEngine(File pdfsDir) throws IOException {
        DataHandler dataPreparationTool = new DataHandler();
        stopWords = dataPreparationTool.getStopWords();
        allIndexedDocuments = dataPreparationTool.pdfDataHandler(pdfsDir);
    }

    /**
     @param searchString Список поиска (может содержать стоп-слова)
     @return Список, отсортированный суммарному количеству переданных слов на странице
     */
    @Override
    public List<PageEntry> search(String searchString) {
        // Разбираем строку поиска, чистим от стоп-слов
        String[] searchWords = searchString.split("\\P{IsAlphabetic}+");
        List<String> words = Arrays.stream(searchWords)
                .filter(word -> !stopWords.contains(word))
                .collect(Collectors.toList());
        // Сюда будем результаты поиска складывать
        List<PageEntry> response = new ArrayList<>();
        // По названиям документов
        var documents = allIndexedDocuments.keySet();
        for (String pdfName : documents) {
            // Страницы одного документа
            var pages = allIndexedDocuments.get(pdfName);
            for (IndexedPage page : pages) {
                // На каждой старнице для всех слов из поиска
                int count = 0;
                for (String word : words) {
                    if (page.getWordDistribution().containsKey(word)) {
                        count += page.getWordDistribution().get(word);
                    }
                }
                // Добавим страницу к результатам если счетчик не нулевой
                if (count > 0) {
                    response.add(new PageEntry(pdfName, page.getPage() + 1, count));
                }
            }
        }
        Collections.sort(response);
        return response;
    }
}
