package scripts.services;

import org.apache.logging.log4j.*;
import org.springframework.stereotype.Service;

@Service
public class HomeService {
    private static final Logger logger = LogManager.getLogger(HomeService.class);

    public String index(){
        logger.info("Index page is loaded");
        return "index";
    }
}
