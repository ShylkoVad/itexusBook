package com.itexus.author.domain;

import com.itexus.book.domain.Book;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name = "authors")
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Author {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @ToString.Exclude // для исключения поля из автоматически сгенерированного метода toString()
    @EqualsAndHashCode.Exclude // для исключения поля из автоматически сгенерированных методов equals() и hashCode()
    @ManyToMany(mappedBy = "authors")
    private Set<Book> books;
}
