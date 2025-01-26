package com.itexus.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@SuperBuilder
@Entity
@Table(name = "genres")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Genre extends BaseEntity {

    @Column(name = "name")
    private String name;

    @ToString.Exclude // для исключения поля из автоматически сгенерированного метода toString()
    @EqualsAndHashCode.Exclude // для исключения поля из автоматически сгенерированных методов equals() и hashCode()
    @OneToMany(mappedBy = "genre")
    private Set<Book> books;
}
