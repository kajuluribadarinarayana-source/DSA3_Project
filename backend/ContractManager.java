import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ContractManager {

    private static final Path CONTRACT_FOLDER =
            Path.of("../data/contracts");

    public static List<String> getContracts()
            throws IOException {

        List<String> contracts =
                new ArrayList<>();

        if (!Files.exists(CONTRACT_FOLDER)) {
            return contracts;
        }

        try (var files =
                     Files.list(CONTRACT_FOLDER)) {

            files
                    .filter(Files::isRegularFile)
                    .forEach(file ->
                            contracts.add(
                                    file.getFileName()
                                            .toString()
                            ));
        }

        return contracts;
    }

    public static String readVersion(
            String fileName)
            throws IOException {

        Path path =
                Path.of(
                        "../data/versions",
                        fileName
                );

        return Files.readString(path);
    }
}