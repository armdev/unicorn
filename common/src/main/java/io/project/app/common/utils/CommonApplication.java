package io.project.app.common.utils;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommonApplication {

    public static void main(String[] args) {
        final SpringApplication application = new SpringApplication(CommonApplication.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }
    
    

}
