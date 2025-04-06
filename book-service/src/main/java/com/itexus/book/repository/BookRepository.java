package com.itexus.book.repository;

import com.itexus.author.dto.AuthorDTO;
import com.itexus.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT author_id FROM book_authors WHERE book_id = :bookId", nativeQuery = true)
//    List<AuthorDTO> findAuthorIdsByBookId(@Param("bookId") Long bookId);
    List<Long> findAuthorIdsByBookId(@Param("bookId") Long bookId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO book_authors (book_id, author_id) VALUES (:bookId, :authorId)", nativeQuery = true)
    void addBookAuthor(@Param("bookId") Long bookId, @Param("authorId") Long authorId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM book_authors WHERE book_id = :bookId", nativeQuery = true)
    void removeBookAuthors(@Param("bookId") Long bookId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE book_authors SET role = :role WHERE book_id = :bookId AND author_id = :authorId", nativeQuery = true)
    void updateBookAuthor(@Param("bookId") Long bookId, @Param("authorId") Long authorId, @Param("role") String role);

    @Query(value = "SELECT COUNT(*) > 0 FROM book_authors WHERE book_id = :bookId AND author_id = :authorId", nativeQuery = true)
    boolean existsByBookIdAndAuthorId(@Param("bookId") Long bookId, @Param("authorId") Long authorId);
}

