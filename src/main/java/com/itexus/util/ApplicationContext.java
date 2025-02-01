package com.itexus.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationContext {

    private static final ApplicationContext instance = new ApplicationContext();
    private Locale locale;

    public static ApplicationContext getInstance() {
        return instance;
    }
}