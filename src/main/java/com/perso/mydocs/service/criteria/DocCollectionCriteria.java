package com.perso.mydocs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.perso.mydocs.domain.DocCollection} entity. This class is used
 * in {@link com.perso.mydocs.web.rest.DocCollectionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /doc-collections?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocCollectionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter notes;

    private LongFilter docId;

    private LongFilter docPublisherId;

    private Boolean distinct;

    public DocCollectionCriteria() {}

    public DocCollectionCriteria(DocCollectionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.docId = other.docId == null ? null : other.docId.copy();
        this.docPublisherId = other.docPublisherId == null ? null : other.docPublisherId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DocCollectionCriteria copy() {
        return new DocCollectionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public StringFilter notes() {
        if (notes == null) {
            notes = new StringFilter();
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public LongFilter getDocId() {
        return docId;
    }

    public LongFilter docId() {
        if (docId == null) {
            docId = new LongFilter();
        }
        return docId;
    }

    public void setDocId(LongFilter docId) {
        this.docId = docId;
    }

    public LongFilter getDocPublisherId() {
        return docPublisherId;
    }

    public LongFilter docPublisherId() {
        if (docPublisherId == null) {
            docPublisherId = new LongFilter();
        }
        return docPublisherId;
    }

    public void setDocPublisherId(LongFilter docPublisherId) {
        this.docPublisherId = docPublisherId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DocCollectionCriteria that = (DocCollectionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(docId, that.docId) &&
            Objects.equals(docPublisherId, that.docPublisherId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, notes, docId, docPublisherId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocCollectionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (docId != null ? "docId=" + docId + ", " : "") +
            (docPublisherId != null ? "docPublisherId=" + docPublisherId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
