import java.util.Map;

/**
 Результаты анализа страницы PDF документа с заполненной МАПой распределения слов
 */
public class IndexedPage {
    private final int page;
    private final Map<String, Integer> wordDistribution;

    public int getPage() {
        return page;
    }

    public Map<String, Integer> getWordDistribution() {
        return wordDistribution;
    }

    public IndexedPage(int page, Map<String, Integer> wordDistribution) {
        this.page = page;
        this.wordDistribution = wordDistribution;
    }
}
