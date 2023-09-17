package com.perso.mydocs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Doc.
 */
@Entity
@Table(name = "doc")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Doc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "sub_title")
    private String subTitle;

    @Size(min = 4, max = 4)
    @Column(name = "publish_year", length = 4)
    private String publishYear;

    @Column(name = "cover_img_path")
    private String coverImgPath;

    @Column(name = "edition_numer")
    private Integer editionNumer;

    @Lob
    @Column(name = "summary")
    private String summary;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "start_reading_date")
    private LocalDate startReadingDate;

    @Column(name = "end_reading_date")
    private LocalDate endReadingDate;

    @Column(name = "price")
    private Float price;

    @Column(name = "copies")
    private Integer copies;

    @Column(name = "page_number")
    private Integer pageNumber;

    @Column(name = "num_doc")
    private String numDoc;

    @Lob
    @Column(name = "my_notes")
    private String myNotes;

    @Lob
    @Column(name = "keywords")
    private String keywords;

    @Lob
    @Column(name = "toc")
    private String toc;

    @Column(name = "filename")
    private String filename;

    @JsonIgnoreProperties(value = { "collections" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private DocPublisher publisher;

    @JsonIgnoreProperties(value = { "doc", "docPublisher" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private DocCollection collection;

    @ManyToOne
    @JsonIgnoreProperties(value = { "docs" }, allowSetters = true)
    private DocFormat format;

    @ManyToOne
    @JsonIgnoreProperties(value = { "docs" }, allowSetters = true)
    private Language langue;

    @ManyToOne
    @JsonIgnoreProperties(value = { "docs", "domaine" }, allowSetters = true)
    private DocTopic maintopic;

    @ManyToOne
    @JsonIgnoreProperties(value = { "docs" }, allowSetters = true)
    private DocAuthor mainAuthor;

    @ManyToOne
    @JsonIgnoreProperties(value = { "docs" }, allowSetters = true)
    private DocCategory docCategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Doc id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Doc title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public Doc subTitle(String subTitle) {
        this.setSubTitle(subTitle);
        return this;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getPublishYear() {
        return this.publishYear;
    }

    public Doc publishYear(String publishYear) {
        this.setPublishYear(publishYear);
        return this;
    }

    public void setPublishYear(String publishYear) {
        this.publishYear = publishYear;
    }

    public String getCoverImgPath() {
        return this.coverImgPath;
    }

    public Doc coverImgPath(String coverImgPath) {
        this.setCoverImgPath(coverImgPath);
        return this;
    }

    public void setCoverImgPath(String coverImgPath) {
        this.coverImgPath = coverImgPath;
    }

    public Integer getEditionNumer() {
        return this.editionNumer;
    }

    public Doc editionNumer(Integer editionNumer) {
        this.setEditionNumer(editionNumer);
        return this;
    }

    public void setEditionNumer(Integer editionNumer) {
        this.editionNumer = editionNumer;
    }

    public String getSummary() {
        return this.summary;
    }

    public Doc summary(String summary) {
        this.setSummary(summary);
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public LocalDate getPurchaseDate() {
        return this.purchaseDate;
    }

    public Doc purchaseDate(LocalDate purchaseDate) {
        this.setPurchaseDate(purchaseDate);
        return this;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDate getStartReadingDate() {
        return this.startReadingDate;
    }

    public Doc startReadingDate(LocalDate startReadingDate) {
        this.setStartReadingDate(startReadingDate);
        return this;
    }

    public void setStartReadingDate(LocalDate startReadingDate) {
        this.startReadingDate = startReadingDate;
    }

    public LocalDate getEndReadingDate() {
        return this.endReadingDate;
    }

    public Doc endReadingDate(LocalDate endReadingDate) {
        this.setEndReadingDate(endReadingDate);
        return this;
    }

    public void setEndReadingDate(LocalDate endReadingDate) {
        this.endReadingDate = endReadingDate;
    }

    public Float getPrice() {
        return this.price;
    }

    public Doc price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getCopies() {
        return this.copies;
    }

    public Doc copies(Integer copies) {
        this.setCopies(copies);
        return this;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    public Integer getPageNumber() {
        return this.pageNumber;
    }

    public Doc pageNumber(Integer pageNumber) {
        this.setPageNumber(pageNumber);
        return this;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getNumDoc() {
        return this.numDoc;
    }

    public Doc numDoc(String numDoc) {
        this.setNumDoc(numDoc);
        return this;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getMyNotes() {
        return this.myNotes;
    }

    public Doc myNotes(String myNotes) {
        this.setMyNotes(myNotes);
        return this;
    }

    public void setMyNotes(String myNotes) {
        this.myNotes = myNotes;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public Doc keywords(String keywords) {
        this.setKeywords(keywords);
        return this;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getToc() {
        return this.toc;
    }

    public Doc toc(String toc) {
        this.setToc(toc);
        return this;
    }

    public void setToc(String toc) {
        this.toc = toc;
    }

    public String getFilename() {
        return this.filename;
    }

    public Doc filename(String filename) {
        this.setFilename(filename);
        return this;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public DocPublisher getPublisher() {
        return this.publisher;
    }

    public void setPublisher(DocPublisher docPublisher) {
        this.publisher = docPublisher;
    }

    public Doc publisher(DocPublisher docPublisher) {
        this.setPublisher(docPublisher);
        return this;
    }

    public DocCollection getCollection() {
        return this.collection;
    }

    public void setCollection(DocCollection docCollection) {
        this.collection = docCollection;
    }

    public Doc collection(DocCollection docCollection) {
        this.setCollection(docCollection);
        return this;
    }

    public DocFormat getFormat() {
        return this.format;
    }

    public void setFormat(DocFormat docFormat) {
        this.format = docFormat;
    }

    public Doc format(DocFormat docFormat) {
        this.setFormat(docFormat);
        return this;
    }

    public Language getLangue() {
        return this.langue;
    }

    public void setLangue(Language language) {
        this.langue = language;
    }

    public Doc langue(Language language) {
        this.setLangue(language);
        return this;
    }

    public DocTopic getMaintopic() {
        return this.maintopic;
    }

    public void setMaintopic(DocTopic docTopic) {
        this.maintopic = docTopic;
    }

    public Doc maintopic(DocTopic docTopic) {
        this.setMaintopic(docTopic);
        return this;
    }

    public DocAuthor getMainAuthor() {
        return this.mainAuthor;
    }

    public void setMainAuthor(DocAuthor docAuthor) {
        this.mainAuthor = docAuthor;
    }

    public Doc mainAuthor(DocAuthor docAuthor) {
        this.setMainAuthor(docAuthor);
        return this;
    }

    public DocCategory getDocCategory() {
        return this.docCategory;
    }

    public void setDocCategory(DocCategory docCategory) {
        this.docCategory = docCategory;
    }

    public Doc docCategory(DocCategory docCategory) {
        this.setDocCategory(docCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Doc)) {
            return false;
        }
        return id != null && id.equals(((Doc) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Doc{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", subTitle='" + getSubTitle() + "'" +
            ", publishYear='" + getPublishYear() + "'" +
            ", coverImgPath='" + getCoverImgPath() + "'" +
            ", editionNumer=" + getEditionNumer() +
            ", summary='" + getSummary() + "'" +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            ", startReadingDate='" + getStartReadingDate() + "'" +
            ", endReadingDate='" + getEndReadingDate() + "'" +
            ", price=" + getPrice() +
            ", copies=" + getCopies() +
            ", pageNumber=" + getPageNumber() +
            ", numDoc='" + getNumDoc() + "'" +
            ", myNotes='" + getMyNotes() + "'" +
            ", keywords='" + getKeywords() + "'" +
            ", toc='" + getToc() + "'" +
            ", filename='" + getFilename() + "'" +
            "}";
    }
}
