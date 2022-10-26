import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private final Set<String> stopWords;
    private final DataHandler dataPreparationTool;
    private final Map<String, Set<IndexedPage>> allIndexedDocuments;

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        this.dataPreparationTool = new DataHandler();
        stopWords = dataPreparationTool.getStopWords();
        allIndexedDocuments = dataPreparationTool.pdfDataHandler(pdfsDir);
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> response = new ArrayList<>();
        var documents = allIndexedDocuments.keySet();
        for (String pdfName : documents) {
            var pages = allIndexedDocuments.get(pdfName);
            for (IndexedPage page : pages) {
                if (page.getWordDistribution().containsKey(word)) {
                    response.add(new PageEntry(pdfName, page.getPage()+1, page.getWordDistribution().get(word)));
                }
            }
        }
        Collections.sort(response);
        return response;
    }
}
