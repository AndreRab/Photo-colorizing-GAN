package scripts.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import scripts.services.DataService;

@Controller
public class UploadImageController {
    private final DataService dataService;

    public UploadImageController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/upload")
    public String index(){
        return this.dataService.index();
    }

    @PostMapping("/upload/send")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, HttpSession session) {
        return dataService.handleFileUpload(file, session);
    }
}
