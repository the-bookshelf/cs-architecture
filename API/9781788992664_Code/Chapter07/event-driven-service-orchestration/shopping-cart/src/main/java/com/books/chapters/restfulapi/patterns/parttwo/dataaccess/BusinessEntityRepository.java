package com.books.chapters.restfulapi.patterns.parttwo.dataaccess;

import org.springframework.data.repository.CrudRepository;

public interface BusinessEntityRepository extends CrudRepository<BusinessEntityJpa, String> {
}
