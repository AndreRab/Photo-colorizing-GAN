package scripts.services;

import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class ColorizedService {
    private static final Logger logger = LogManager.getLogger(HomeService.class);
    public String index(Model model, HttpSession session){
        model.addAttribute("userId", session.getAttribute("userId"));
        return "colorized";
    }
}
