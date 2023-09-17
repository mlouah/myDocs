package com.perso.mydocs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.perso.mydocs.domain.DocBorrowed} entity. This class is used
 * in {@link com.perso.mydocs.web.rest.DocBorrowedResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /doc-borroweds?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocBorrowedCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter borrowDate;

    private StringFilter borrowerName;

    private StringFilter notes;

    private LongFilter docId;

    private Boolean distinct;

    public DocBorrowedCriteria() {}

    public DocBorrowedCriteria(DocBorrowedCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.borrowDate = other.borrowDate == null ? null : other.borrowDate.copy();
        this.borrowerName = other.borrowerName == null ? null : other.borrowerName.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.docId = other.docId == null ? null : other.docId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DocBorrowedCriteria copy() {
        return new DocBorrowedCriteria(this);
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

    public LocalDateFilter getBorrowDate() {
        return borrowDate;
    }

    public LocalDateFilter borrowDate() {
        if (borrowDate == null) {
            borrowDate = new LocalDateFilter();
        }
        return borrowDate;
    }

    public void setBorrowDate(LocalDateFilter borrowDate) {
        this.borrowDate = borrowDate;
    }

    public StringFilter getBorrowerName() {
        return borrowerName;
    }

    public StringFilter borrowerName() {
        if (borrowerName == null) {
            borrowerName = new StringFilter();
        }
        return borrowerName;
    }

    public void setBorrowerName(StringFilter borrowerName) {
        this.borrowerName = borrowerName;
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
        final DocBorrowedCriteria that = (DocBorrowedCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(borrowDate, that.borrowDate) &&
            Objects.equals(borrowerName, that.borrowerName) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(docId, that.docId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, borrowDate, borrowerName, notes, docId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocBorrowedCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (borrowDate != null ? "borrowDate=" + borrowDate + ", " : "") +
            (borrowerName != null ? "borrowerName=" + borrowerName + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (docId != null ? "docId=" + docId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
