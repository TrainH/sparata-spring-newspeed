package spartaspringnewspeed.spartafacespeed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpartaFacespeedApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpartaFacespeedApplication.class, args);
    }}
