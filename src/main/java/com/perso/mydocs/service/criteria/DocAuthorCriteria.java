package com.perso.mydocs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.perso.mydocs.domain.DocAuthor} entity. This class is used
 * in {@link com.perso.mydocs.web.rest.DocAuthorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /doc-authors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocAuthorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter imgUrl;

    private StringFilter url;

    private LongFilter docId;

    private Boolean distinct;

    public DocAuthorCriteria() {}

    public DocAuthorCriteria(DocAuthorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.imgUrl = other.imgUrl == null ? null : other.imgUrl.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.docId = other.docId == null ? null : other.docId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DocAuthorCriteria copy() {
        return new DocAuthorCriteria(this);
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

    public StringFilter getImgUrl() {
        return imgUrl;
    }

    public StringFilter imgUrl() {
        if (imgUrl == null) {
            imgUrl = new StringFilter();
        }
        return imgUrl;
    }

    public void setImgUrl(StringFilter imgUrl) {
        this.imgUrl = imgUrl;
    }

    public StringFilter getUrl() {
        return url;
    }

    public StringFilter url() {
        if (url == null) {
            url = new StringFilter();
        }
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
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
        final DocAuthorCriteria that = (DocAuthorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(imgUrl, that.imgUrl) &&
            Objects.equals(url, that.url) &&
            Objects.equals(docId, that.docId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, imgUrl, url, docId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocAuthorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (imgUrl != null ? "imgUrl=" + imgUrl + ", " : "") +
            (url != null ? "url=" + url + ", " : "") +
            (docId != null ? "docId=" + docId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
