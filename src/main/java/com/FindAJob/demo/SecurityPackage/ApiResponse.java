package com.FindAJob.demo.SecurityPackage;

public record ApiResponse(
        int statusCode,
        String status,
        String reason) {
}
