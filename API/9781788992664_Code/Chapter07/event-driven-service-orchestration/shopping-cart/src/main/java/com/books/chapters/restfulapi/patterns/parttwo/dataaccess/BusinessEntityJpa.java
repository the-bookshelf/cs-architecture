package com.books.chapters.restfulapi.patterns.parttwo.dataaccess;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "BusinessEntity")
public class BusinessEntityJpa {

	@Id
	private String id;

	@Column(nullable = false)
	private String entityType;

	@Column(nullable = false)
	private String entitySpecification;

	private String name;

	private String status;

	@JoinTable(name = "entityRelation", joinColumns = {
			@JoinColumn(name = "referencingEntity", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "referencedEntity", referencedColumnName = "id", nullable = false) })
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<BusinessEntityJpa> relatedEntities = new ArrayList<BusinessEntityJpa>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BusinessEntityJpa withId(String id) {
		this.id = id;
		return this;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public BusinessEntityJpa withEntityType(String entityType) {
		this.entityType = entityType;
		return this;
	}

	public String getEntitySpecification() {
		return entitySpecification;
	}

	public void setEntitySpecification(String entitySpecification) {
		this.entitySpecification = entitySpecification;
	}

	public BusinessEntityJpa withEntitySpecification(String entitySpecification) {
		this.entitySpecification = entitySpecification;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BusinessEntityJpa withName(String name) {
		this.name = name;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BusinessEntityJpa withStatus(String status) {
		this.status = status;
		return this;
	}

	public List<BusinessEntityJpa> getRelatedEntities() {
		return relatedEntities;
	}

	public void setRelatedEntities(List<BusinessEntityJpa> relatedEntities) {
		this.relatedEntities = relatedEntities;
	}

	public BusinessEntityJpa withRelatedEntities(List<BusinessEntityJpa> relatedEntities) {
		this.relatedEntities = relatedEntities;
		return this;
	}
}
