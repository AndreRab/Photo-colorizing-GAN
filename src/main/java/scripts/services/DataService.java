package scripts.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import scripts.models.MyJsonData;

@Service
public class DataService {
    private static final Logger logger = LogManager.getLogger(HomeService.class);
    private static final String pythonServerUrl = "http://python-app:5000/process";
    private final RestTemplate restTemplate = new RestTemplate();

    public String sendStringToPython(){
        MyJsonData data = new MyJsonData("Test");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        logger.info("Headers were set");

        HttpEntity<MyJsonData> request = new HttpEntity<>(data, headers);
        logger.info("Request was created");

        ResponseEntity<String> response = restTemplate.exchange(
                pythonServerUrl,
                HttpMethod.POST,
                request,
                String.class
        );

        return response.getBody();
    }
}
