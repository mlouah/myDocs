package com.perso.mydocs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.perso.mydocs.domain.Doc} entity. This class is used
 * in {@link com.perso.mydocs.web.rest.DocResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /docs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter subTitle;

    private StringFilter publishYear;

    private StringFilter coverImgPath;

    private IntegerFilter editionNumer;

    private LocalDateFilter purchaseDate;

    private LocalDateFilter startReadingDate;

    private LocalDateFilter endReadingDate;

    private FloatFilter price;

    private IntegerFilter copies;

    private IntegerFilter pageNumber;

    private StringFilter numDoc;

    private StringFilter filename;

    private LongFilter publisherId;

    private LongFilter collectionId;

    private LongFilter formatId;

    private LongFilter langueId;

    private LongFilter maintopicId;

    private LongFilter mainAuthorId;

    private LongFilter docCategoryId;

    private Boolean distinct;

    public DocCriteria() {}

    public DocCriteria(DocCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.subTitle = other.subTitle == null ? null : other.subTitle.copy();
        this.publishYear = other.publishYear == null ? null : other.publishYear.copy();
        this.coverImgPath = other.coverImgPath == null ? null : other.coverImgPath.copy();
        this.editionNumer = other.editionNumer == null ? null : other.editionNumer.copy();
        this.purchaseDate = other.purchaseDate == null ? null : other.purchaseDate.copy();
        this.startReadingDate = other.startReadingDate == null ? null : other.startReadingDate.copy();
        this.endReadingDate = other.endReadingDate == null ? null : other.endReadingDate.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.copies = other.copies == null ? null : other.copies.copy();
        this.pageNumber = other.pageNumber == null ? null : other.pageNumber.copy();
        this.numDoc = other.numDoc == null ? null : other.numDoc.copy();
        this.filename = other.filename == null ? null : other.filename.copy();
        this.publisherId = other.publisherId == null ? null : other.publisherId.copy();
        this.collectionId = other.collectionId == null ? null : other.collectionId.copy();
        this.formatId = other.formatId == null ? null : other.formatId.copy();
        this.langueId = other.langueId == null ? null : other.langueId.copy();
        this.maintopicId = other.maintopicId == null ? null : other.maintopicId.copy();
        this.mainAuthorId = other.mainAuthorId == null ? null : other.mainAuthorId.copy();
        this.docCategoryId = other.docCategoryId == null ? null : other.docCategoryId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DocCriteria copy() {
        return new DocCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getSubTitle() {
        return subTitle;
    }

    public StringFilter subTitle() {
        if (subTitle == null) {
            subTitle = new StringFilter();
        }
        return subTitle;
    }

    public void setSubTitle(StringFilter subTitle) {
        this.subTitle = subTitle;
    }

    public StringFilter getPublishYear() {
        return publishYear;
    }

    public StringFilter publishYear() {
        if (publishYear == null) {
            publishYear = new StringFilter();
        }
        return publishYear;
    }

    public void setPublishYear(StringFilter publishYear) {
        this.publishYear = publishYear;
    }

    public StringFilter getCoverImgPath() {
        return coverImgPath;
    }

    public StringFilter coverImgPath() {
        if (coverImgPath == null) {
            coverImgPath = new StringFilter();
        }
        return coverImgPath;
    }

    public void setCoverImgPath(StringFilter coverImgPath) {
        this.coverImgPath = coverImgPath;
    }

    public IntegerFilter getEditionNumer() {
        return editionNumer;
    }

    public IntegerFilter editionNumer() {
        if (editionNumer == null) {
            editionNumer = new IntegerFilter();
        }
        return editionNumer;
    }

    public void setEditionNumer(IntegerFilter editionNumer) {
        this.editionNumer = editionNumer;
    }

    public LocalDateFilter getPurchaseDate() {
        return purchaseDate;
    }

    public LocalDateFilter purchaseDate() {
        if (purchaseDate == null) {
            purchaseDate = new LocalDateFilter();
        }
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateFilter purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDateFilter getStartReadingDate() {
        return startReadingDate;
    }

    public LocalDateFilter startReadingDate() {
        if (startReadingDate == null) {
            startReadingDate = new LocalDateFilter();
        }
        return startReadingDate;
    }

    public void setStartReadingDate(LocalDateFilter startReadingDate) {
        this.startReadingDate = startReadingDate;
    }

    public LocalDateFilter getEndReadingDate() {
        return endReadingDate;
    }

    public LocalDateFilter endReadingDate() {
        if (endReadingDate == null) {
            endReadingDate = new LocalDateFilter();
        }
        return endReadingDate;
    }

    public void setEndReadingDate(LocalDateFilter endReadingDate) {
        this.endReadingDate = endReadingDate;
    }

    public FloatFilter getPrice() {
        return price;
    }

    public FloatFilter price() {
        if (price == null) {
            price = new FloatFilter();
        }
        return price;
    }

    public void setPrice(FloatFilter price) {
        this.price = price;
    }

    public IntegerFilter getCopies() {
        return copies;
    }

    public IntegerFilter copies() {
        if (copies == null) {
            copies = new IntegerFilter();
        }
        return copies;
    }

    public void setCopies(IntegerFilter copies) {
        this.copies = copies;
    }

    public IntegerFilter getPageNumber() {
        return pageNumber;
    }

    public IntegerFilter pageNumber() {
        if (pageNumber == null) {
            pageNumber = new IntegerFilter();
        }
        return pageNumber;
    }

    public void setPageNumber(IntegerFilter pageNumber) {
        this.pageNumber = pageNumber;
    }

    public StringFilter getNumDoc() {
        return numDoc;
    }

    public StringFilter numDoc() {
        if (numDoc == null) {
            numDoc = new StringFilter();
        }
        return numDoc;
    }

    public void setNumDoc(StringFilter numDoc) {
        this.numDoc = numDoc;
    }

    public StringFilter getFilename() {
        return filename;
    }

    public StringFilter filename() {
        if (filename == null) {
            filename = new StringFilter();
        }
        return filename;
    }

    public void setFilename(StringFilter filename) {
        this.filename = filename;
    }

    public LongFilter getPublisherId() {
        return publisherId;
    }

    public LongFilter publisherId() {
        if (publisherId == null) {
            publisherId = new LongFilter();
        }
        return publisherId;
    }

    public void setPublisherId(LongFilter publisherId) {
        this.publisherId = publisherId;
    }

    public LongFilter getCollectionId() {
        return collectionId;
    }

    public LongFilter collectionId() {
        if (collectionId == null) {
            collectionId = new LongFilter();
        }
        return collectionId;
    }

    public void setCollectionId(LongFilter collectionId) {
        this.collectionId = collectionId;
    }

    public LongFilter getFormatId() {
        return formatId;
    }

    public LongFilter formatId() {
        if (formatId == null) {
            formatId = new LongFilter();
        }
        return formatId;
    }

    public void setFormatId(LongFilter formatId) {
        this.formatId = formatId;
    }

    public LongFilter getLangueId() {
        return langueId;
    }

    public LongFilter langueId() {
        if (langueId == null) {
            langueId = new LongFilter();
        }
        return langueId;
    }

    public void setLangueId(LongFilter langueId) {
        this.langueId = langueId;
    }

    public LongFilter getMaintopicId() {
        return maintopicId;
    }

    public LongFilter maintopicId() {
        if (maintopicId == null) {
            maintopicId = new LongFilter();
        }
        return maintopicId;
    }

    public void setMaintopicId(LongFilter maintopicId) {
        this.maintopicId = maintopicId;
    }

    public LongFilter getMainAuthorId() {
        return mainAuthorId;
    }

    public LongFilter mainAuthorId() {
        if (mainAuthorId == null) {
            mainAuthorId = new LongFilter();
        }
        return mainAuthorId;
    }

    public void setMainAuthorId(LongFilter mainAuthorId) {
        this.mainAuthorId = mainAuthorId;
    }

    public LongFilter getDocCategoryId() {
        return docCategoryId;
    }

    public LongFilter docCategoryId() {
        if (docCategoryId == null) {
            docCategoryId = new LongFilter();
        }
        return docCategoryId;
    }

    public void setDocCategoryId(LongFilter docCategoryId) {
        this.docCategoryId = docCategoryId;
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
        final DocCriteria that = (DocCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(subTitle, that.subTitle) &&
            Objects.equals(publishYear, that.publishYear) &&
            Objects.equals(coverImgPath, that.coverImgPath) &&
            Objects.equals(editionNumer, that.editionNumer) &&
            Objects.equals(purchaseDate, that.purchaseDate) &&
            Objects.equals(startReadingDate, that.startReadingDate) &&
            Objects.equals(endReadingDate, that.endReadingDate) &&
            Objects.equals(price, that.price) &&
            Objects.equals(copies, that.copies) &&
            Objects.equals(pageNumber, that.pageNumber) &&
            Objects.equals(numDoc, that.numDoc) &&
            Objects.equals(filename, that.filename) &&
            Objects.equals(publisherId, that.publisherId) &&
            Objects.equals(collectionId, that.collectionId) &&
            Objects.equals(formatId, that.formatId) &&
            Objects.equals(langueId, that.langueId) &&
            Objects.equals(maintopicId, that.maintopicId) &&
            Objects.equals(mainAuthorId, that.mainAuthorId) &&
            Objects.equals(docCategoryId, that.docCategoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            subTitle,
            publishYear,
            coverImgPath,
            editionNumer,
            purchaseDate,
            startReadingDate,
            endReadingDate,
            price,
            copies,
            pageNumber,
            numDoc,
            filename,
            publisherId,
            collectionId,
            formatId,
            langueId,
            maintopicId,
            mainAuthorId,
            docCategoryId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (subTitle != null ? "subTitle=" + subTitle + ", " : "") +
            (publishYear != null ? "publishYear=" + publishYear + ", " : "") +
            (coverImgPath != null ? "coverImgPath=" + coverImgPath + ", " : "") +
            (editionNumer != null ? "editionNumer=" + editionNumer + ", " : "") +
            (purchaseDate != null ? "purchaseDate=" + purchaseDate + ", " : "") +
            (startReadingDate != null ? "startReadingDate=" + startReadingDate + ", " : "") +
            (endReadingDate != null ? "endReadingDate=" + endReadingDate + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (copies != null ? "copies=" + copies + ", " : "") +
            (pageNumber != null ? "pageNumber=" + pageNumber + ", " : "") +
            (numDoc != null ? "numDoc=" + numDoc + ", " : "") +
            (filename != null ? "filename=" + filename + ", " : "") +
            (publisherId != null ? "publisherId=" + publisherId + ", " : "") +
            (collectionId != null ? "collectionId=" + collectionId + ", " : "") +
            (formatId != null ? "formatId=" + formatId + ", " : "") +
            (langueId != null ? "langueId=" + langueId + ", " : "") +
            (maintopicId != null ? "maintopicId=" + maintopicId + ", " : "") +
            (mainAuthorId != null ? "mainAuthorId=" + mainAuthorId + ", " : "") +
            (docCategoryId != null ? "docCategoryId=" + docCategoryId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
