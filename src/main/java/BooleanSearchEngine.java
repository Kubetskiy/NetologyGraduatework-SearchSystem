import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
/*
        for (Map.Entry<String, Set<IndexedPage>> page : allIndexedDocuments) {

        }
*/

        return Collections.emptyList();
    }
}
