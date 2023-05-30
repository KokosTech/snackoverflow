/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import tech.kaloyan.snackoverflow.config.OpenAPIConfiguration;

@SpringBootApplication
@Import(OpenAPIConfiguration.class)
public class SnackoverflowApplication {
    public static void main(String[] args) {
        SpringApplication.run(SnackoverflowApplication.class, args);
    }

}
