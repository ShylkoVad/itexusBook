package com.itexus.book.repository;

import com.itexus.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT author_id FROM book_authors WHERE book_id = :bookId", nativeQuery = true)
    List<Long> findAuthorIdsByBookId(@Param("bookId") Long bookId);
}

