package com.books.chapters.restfulapi.patterns.parttwo.dataaccess;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.BusinessEntity;

public class BusinessEntityTranslator {
	private BusinessEntityTranslator() {
	}

	public static BusinessEntity fromJpa(BusinessEntityJpa jpaEntity) {
		if (jpaEntity == null) {
			return null;
		}

		return new BusinessEntity().withId(jpaEntity.getId()).withName(jpaEntity.getName())
				.withStatus(jpaEntity.getStatus()).withEntitySpecification(jpaEntity.getEntitySpecification())
				.withEntityType(jpaEntity.getEntityType()).withRelatedEntities(
						translateToList(jpaEntity.getRelatedEntities(), BusinessEntityTranslator::fromJpa));
	}

	public static BusinessEntityJpa toJpa(BusinessEntity entity) {
		if (entity == null) {
			return null;
		}

		return new BusinessEntityJpa().withId(entity.getId()).withName(entity.getName()).withStatus(entity.getStatus())
				.withEntitySpecification(entity.getEntitySpecification()).withEntityType(entity.getEntityType())
				.withRelatedEntities(translateToList(entity.getRelatedEntities(), BusinessEntityTranslator::toJpa));
	}

	public static <F, T> List<T> translateToList(Collection<F> froms, Function<F, T> translate) {
		return Optional.ofNullable(froms).orElse(Collections.emptyList()).stream().map(translate)
				.filter(Objects::nonNull).collect(Collectors.toList());
	}

}
