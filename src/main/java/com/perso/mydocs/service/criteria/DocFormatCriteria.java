package com.perso.mydocs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.perso.mydocs.domain.DocFormat} entity. This class is used
 * in {@link com.perso.mydocs.web.rest.DocFormatResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /doc-formats?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocFormatCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter format;

    private StringFilter code;

    private StringFilter notes;

    private LongFilter docId;

    private Boolean distinct;

    public DocFormatCriteria() {}

    public DocFormatCriteria(DocFormatCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.format = other.format == null ? null : other.format.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.docId = other.docId == null ? null : other.docId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DocFormatCriteria copy() {
        return new DocFormatCriteria(this);
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

    public StringFilter getFormat() {
        return format;
    }

    public StringFilter format() {
        if (format == null) {
            format = new StringFilter();
        }
        return format;
    }

    public void setFormat(StringFilter format) {
        this.format = format;
    }

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
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
        final DocFormatCriteria that = (DocFormatCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(format, that.format) &&
            Objects.equals(code, that.code) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(docId, that.docId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, format, code, notes, docId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocFormatCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (format != null ? "format=" + format + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (docId != null ? "docId=" + docId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
