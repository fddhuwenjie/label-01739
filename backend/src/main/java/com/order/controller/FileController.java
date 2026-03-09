package com.order.controller;

import com.order.entity.Order;
import com.order.service.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {
    private final OrderService orderService;

    @Value("${file.upload-path}")
    private String uploadPath;

    @GetMapping("/download/{orderId}")
    public void downloadFile(@PathVariable Long orderId, HttpServletResponse response) throws IOException {
        Order order = orderService.getById(orderId);
        if (order == null || order.getAttachmentPath() == null) {
            response.setStatus(404);
            return;
        }

        Path filePath = Paths.get(uploadPath).toAbsolutePath().resolve(order.getAttachmentPath());
        File file = filePath.toFile();
        if (!file.exists()) {
            response.setStatus(404);
            return;
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + 
                URLEncoder.encode(order.getAttachmentName(), StandardCharsets.UTF_8));

        try (InputStream is = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int len;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
        }
    }
}
