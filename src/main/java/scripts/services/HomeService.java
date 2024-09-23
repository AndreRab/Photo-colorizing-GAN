package scripts.services;

import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;

@Service
public class HomeService {
    private static final Logger logger = LogManager.getLogger(HomeService.class);
    private static final String UPLOAD_DIR = "uploads";

    public String index(HttpSession session){
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            userId = UUID.randomUUID().toString();
            session.setAttribute("userId", userId);
            logger.info(userId + " was created");
        }

        Path userDir = Paths.get(UPLOAD_DIR, userId);
        if (Files.exists(userDir)) {
            try {
                deleteDirectory(userDir);
                logger.info(userId + " directory was deleted");
            }
            catch (IOException exception){
                logger.info(exception);
            }
        }
        return "index";
    }

    public static void deleteDirectory(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
