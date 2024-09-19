package scripts.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import scripts.services.DataService;
import scripts.services.HomeService;

@Controller
public class HomeController {
    private final HomeService homeService;
    private final DataService dataService;

    public HomeController(HomeService homeService, DataService dataService) {
        this.homeService = homeService;
        this.dataService = dataService;
    }

    @GetMapping("/")
    public String index(){
        return this.homeService.index();
    }
}
