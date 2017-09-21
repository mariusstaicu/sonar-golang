package fr.univartois.sonargo.gotest;

import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

public class FunctionFinder {
    private static final Logger LOGGER = Loggers.get(FunctionFinder.class);

    private HashMap<String, String> result = new HashMap<>();
    private String baseDir;
    private Stream<Path> paths;

    public FunctionFinder(SensorContext context) throws IOException {
        this.baseDir = context.fileSystem().baseDir().getPath();
        paths = Files.walk(Paths.get(baseDir)).filter(p -> p.toFile().getName().endsWith("_test.go"));
    }

    public HashMap<String, String> searchFunction() {

        paths.forEach((p) -> {
            LOGGER.info("search test function in " + p.toFile().getAbsolutePath());
            searchFunctionInFile(p);
        });
        LOGGER.debug(result.toString());
        return result;
    }

    private void searchFunctionInFile(Path p) {

        try (Stream<String> stream = Files.lines(p)) {

            stream
                    .filter(line -> line.contains("(t *testing.T)"))
                    .forEach((line) -> {
                        String nameFunction = getName(line);
                        if (nameFunction == null)
                            return;
                        result.put(nameFunction, p.toFile().getAbsolutePath());
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName(String line) {
        int indexTesting = line.indexOf("Test");

	if (indexTesting == -1) {
	    LOGGER.warn("This function name is not correct: " + s);
	    return null;
	}
	int indexParen = line.indexOf("(", indexTesting);
	if (indexParen == -1) {
	    LOGGER.warn("This function name is not correct: " + s);
	    return null;
	}
	int indexCloseParen = line.indexOf(")");
	if (indexCloseParen < indexParen) {
	    indexTesting = line.indexOf("Test", indexCloseParen);
	}

        return line.substring(indexTesting, indexParen);
    }

}
