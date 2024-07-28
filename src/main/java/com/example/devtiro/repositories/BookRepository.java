package com.example.devtiro.repositories;

import com.example.devtiro.domain.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookRepository extends  CrudRepository<BookEntity, String>,
        PagingAndSortingRepository<BookEntity,String> {
}
