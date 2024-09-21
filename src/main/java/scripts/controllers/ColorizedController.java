package scripts.controllers;


import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scripts.services.ColorizedService;
import scripts.services.DataService;

@Controller
public class ColorizedController {
    private final DataService dataService;
    private final ColorizedService colorizedService;

    public ColorizedController(DataService dataService, ColorizedService colorizedService) {
        this.dataService = dataService;
        this.colorizedService = colorizedService;
    }

    @PostMapping("/colorized")
    public String receiveImageFromPython(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId
    ) {
        dataService.receiveImageFromPython(file, userId);
        return "redirect:/colorized";
    }


    @GetMapping("/colorized")
    public String index(Model model, HttpSession session){
        return colorizedService.index(model, session);
    }
}
