package scripts.services;

import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class DataService {
    private static final Logger logger = LogManager.getLogger(HomeService.class);
    private static final String pythonServerUrl = "http://python-app:5000/process";
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String UPLOAD_DIR = "uploads";

    public String handleFileUpload(MultipartFile file, HttpSession session) {
        if (file.isEmpty()) {
            logger.info("File was empty");
            return "redirect:/upload";
        }

        try {
            String userId = (String) session.getAttribute("userId");
            if (userId == null) {
                userId = UUID.randomUUID().toString();
                session.setAttribute("userId", userId);
                logger.info(userId + " was created");
            }

            Path userDir = Paths.get(UPLOAD_DIR, userId);
            if (!Files.exists(userDir)) {
                logger.info("Create directory for new session");
                Files.createDirectories(userDir);
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(userDir.toString(), file.getOriginalFilename());
            Files.write(path, bytes);
            logger.info("File was received on server");
            return sendPhotoToPython(path, userId);

        } catch (IOException e) {
            return "redirect:/upload";
        }
    }

    public String sendPhotoToPython(Path path, String userId){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        logger.info("Headers were set");

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        FileSystemResource image = new FileSystemResource(path.toFile());
        body.add("file", image);
        body.add("userId", userId);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        logger.info("Request was created");

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    pythonServerUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            logger.info("Got response from Python");

            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("Python status: OK");
                return "redirect:/colorized";
            } else {
                return "redirect:/";
            }
        } catch (Exception e) {
            logger.error("Error in sending request: " + e.getMessage(), e);
            return "redirect:";
        }
    }


    public void receiveImageFromPython(MultipartFile file, String userId) {
        try {
            if (file.isEmpty()) {
                logger.info("Didn't get file");
                return;
            }
            if(userId == null){
                logger.info("Didn't get userId");
                return;
            }
            Path path = Paths.get("uploads", userId).resolve("colorized.png");
            byte[] bytes = file.getBytes();
            Files.write(path, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String index(){
        return "uploadImage";
    }
}
