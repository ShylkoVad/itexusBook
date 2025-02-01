package com.itexus.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

@SuperBuilder
@Entity
@Table(name = "books")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Book extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "published_date")
    private LocalDate publishedDate;

    @ToString.Exclude // для исключения поля из автоматически сгенерированного метода toString()
    @EqualsAndHashCode.Exclude // для исключения поля из автоматически сгенерированных методов equals() и hashCode()
    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors;

    @ToString.Exclude // для исключения поля из автоматически сгенерированного метода toString()
    @EqualsAndHashCode.Exclude // для исключения поля из автоматически сгенерированных методов equals() и hashCode()
    @ManyToOne
    @JoinColumn(name = "genre_id") // Указываем, что это внешний ключ для жанра
    private Genre genre; // теперь жанр является одним из полей книги

    @Column(name = "image_id") // Поле для хранения ID изображения в GridFS
    private String imageId;

}
