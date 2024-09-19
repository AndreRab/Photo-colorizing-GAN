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
    private static final String UPLOAD_DIR = "uploads/";

    public String handleFileUpload(MultipartFile file, HttpSession session) {
        if (file.isEmpty()) {
            logger.info("File was empty");
            return "redirect:/upload";
        }

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                logger.info("Create upload directory");
                Files.createDirectories(uploadPath);
            }

            String userId = (String) session.getAttribute("userId");
            if (userId == null) {
                userId = UUID.randomUUID().toString();
                session.setAttribute("userId", userId);
            }

            Path userDir = Paths.get("uploads", userId);
            if (!Files.exists(userDir)) {
                logger.info("Create directory new session");
                Files.createDirectories(userDir);
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(userDir + "/" + file.getOriginalFilename());
            Files.write(path, bytes);
            logger.info("File was saved on server");
            return sendPhotoToPython(path);

        } catch (IOException e) {
            return "redirect:/upload";
        }
    }

    public String sendPhotoToPython(Path path){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        logger.info("Headers were set");

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        FileSystemResource image = new FileSystemResource(path.toFile());
        body.add("file", image);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        logger.info("Request was created");

        ResponseEntity<String> response = restTemplate.exchange(
                pythonServerUrl,
                HttpMethod.POST,
                request,
                String.class
        );

        logger.info("Got response from python: " + response.getBody());

        if(response.getStatusCode() == HttpStatus.OK){
            return "redirect:/colorized";
        }
        else{
            return "redirect:/";
        }
    }

    public void receiveImageFromPython(MultipartFile file){
        try {
            if (file.isEmpty()) {
                logger.info("Didn't get file");
            }
            byte[] bytes = file.getBytes();
            logger.info("Received image of size: " + bytes.length);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String index(){
        return "uploadImage";
    }
}
