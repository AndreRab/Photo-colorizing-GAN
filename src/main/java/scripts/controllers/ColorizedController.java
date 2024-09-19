package scripts.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Controller;
import scripts.services.DataService;

@Controller
public class ColorizedController {
    private final DataService dataService;

    public ColorizedController(DataService dataService) {
        this.dataService = dataService;
    }

    @PostMapping("/colorized")
    public void receiveImageFromPython(@RequestParam("file") MultipartFile file) {
        dataService.receiveImageFromPython(file);
    }

    @GetMapping("/colorized")
    public String index(){
        return "colorized";
    }
}
