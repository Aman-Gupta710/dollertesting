package com.example.blogapp.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResourceNotMatchedException extends RuntimeException {
    String resource1;
    String resource2;
    public ResourceNotMatchedException(String resource1, String resource2) {
        super(String.format("%s does not match with %s",resource1,resource2));
        this.resource1 = resource1;
        this.resource2 = resource2;
    }
}
