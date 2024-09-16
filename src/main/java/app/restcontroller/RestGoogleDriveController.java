package app.restcontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.service.GoogleDriveService;

@RestController
public class RestGoogleDriveController {

    private final GoogleDriveService googleDriveService;

    public RestGoogleDriveController(GoogleDriveService googleDriveService) {
        this.googleDriveService = googleDriveService;
    }

    @GetMapping("/createFolder")
    public String createFolder(@RequestParam String folderName) {
        try {
            return googleDriveService.createFolder(folderName);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating folder: " + e.getMessage();
        }
    }
}
