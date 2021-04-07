package fr.abes.periscope;

import fr.abes.periscope.service.NoticeStoreService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {

    private final NoticeStoreService noticeStoreService;

    public MainApplication(NoticeStoreService noticeStoreService) {
        this.noticeStoreService = noticeStoreService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
