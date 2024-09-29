package com.example.demo;


import org.springframework.core.io.InputStreamResource;

import org.springframework.core.io.Resource;

import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



@Controller
public class VideoStreamController {

    private final ResourceLoader resourceLoader;

    @Value("${video.path}")
    private String videoPath;

    public VideoStreamController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/videos/{fileName:.+}")
    public ResponseEntity<Resource> streamVideo(@PathVariable String fileName,
            @RequestHeader(value = "Range", required = false) String rangeHeader)
            throws IOException {
        System.out.println("Requested video: " + fileName);
        
        if (!fileName.toLowerCase().endsWith(".mp4")) {
            System.out.println("Invalid file type requested: " + fileName);
            return ResponseEntity.badRequest().build();
        }

        Path filePath = Paths.get(videoPath, fileName);
        Resource videoResource = resourceLoader.getResource("file:" + filePath.toString());

        if (!videoResource.exists()) {
            System.out.println("Video resource not found: " + filePath);
            return ResponseEntity.notFound().build();
        }

        System.out.println("Video resource found, content length: " + videoResource.contentLength());

        long contentLength = videoResource.contentLength();
        String contentType = "video/mp4";
        System.out.println("Content-Type: " + contentType);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "video/mp4");

        if (StringUtils.hasText(rangeHeader)) {
            // Handle range requests for seeking
            String[] ranges = rangeHeader.substring("bytes=".length()).split("-");
            long rangeStart = Long.parseLong(ranges[0]);
            long rangeEnd = ranges.length > 1 ? Long.parseLong(ranges[1]) : contentLength - 1;
            
            //Ensure rangeEnd is within bounds
            rangeEnd = Math.min(rangeEnd,  contentLength - 1);

            if (rangeStart > rangeEnd || rangeStart < 0 || rangeEnd >= contentLength) {
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
            }

            long contentRangeLength = rangeEnd - rangeStart + 1;

            headers.add(HttpHeaders.CONTENT_RANGE, "bytes " + rangeStart + "-" + rangeEnd + "/"
                    + contentLength);
            headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentRangeLength));

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .headers(headers)
                    .body(new InputStreamResource(new ByteArrayInputStream
                    		(Files.readAllBytes(filePath), (int) rangeStart, (int) contentRangeLength)));
        } else {
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(videoResource);
        }
    }
}