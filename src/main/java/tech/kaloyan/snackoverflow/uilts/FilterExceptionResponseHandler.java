/*
 * Copyright (c) 2024. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.uilts;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FilterExceptionResponseHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendErrorResponse(HttpServletResponse response, int statusCode, String errorMessage) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(errorMessage);
        String responseBody = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getWriter().write(responseBody);
        response.getWriter().flush();
    }

    @Getter
    @Setter
    static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}

