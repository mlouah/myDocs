package com.perso.mydocs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.perso.mydocs.domain.DocTopic} entity. This class is used
 * in {@link com.perso.mydocs.web.rest.DocTopicResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /doc-topics?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocTopicCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter code;

    private StringFilter imgUrl;

    private LongFilter docId;

    private LongFilter domaineId;

    private Boolean distinct;

    public DocTopicCriteria() {}

    public DocTopicCriteria(DocTopicCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.imgUrl = other.imgUrl == null ? null : other.imgUrl.copy();
        this.docId = other.docId == null ? null : other.docId.copy();
        this.domaineId = other.domaineId == null ? null : other.domaineId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DocTopicCriteria copy() {
        return new DocTopicCriteria(this);
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

    public LongFilter getDomaineId() {
        return domaineId;
    }

    public LongFilter domaineId() {
        if (domaineId == null) {
            domaineId = new LongFilter();
        }
        return domaineId;
    }

    public void setDomaineId(LongFilter domaineId) {
        this.domaineId = domaineId;
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
        final DocTopicCriteria that = (DocTopicCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(code, that.code) &&
            Objects.equals(imgUrl, that.imgUrl) &&
            Objects.equals(docId, that.docId) &&
            Objects.equals(domaineId, that.domaineId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, imgUrl, docId, domaineId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocTopicCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (imgUrl != null ? "imgUrl=" + imgUrl + ", " : "") +
            (docId != null ? "docId=" + docId + ", " : "") +
            (domaineId != null ? "domaineId=" + domaineId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
