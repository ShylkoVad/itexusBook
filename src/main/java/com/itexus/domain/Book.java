package com.itexus.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private Long id;
    private String title;
    private String description;
    private LocalDate publishedDate;
    private List<Long> authorIds; // Для хранения ID авторов
    private List<Long> genreIds;   // Для хранения ID жанров



}
