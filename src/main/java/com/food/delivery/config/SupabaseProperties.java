package com.food.delivery.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SupabaseProperties {
    private String url;
    private String key;
}