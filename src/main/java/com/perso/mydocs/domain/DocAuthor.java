package com.perso.mydocs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocAuthor.
 */
@Entity
@Table(name = "doc_author")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocAuthor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "img_url")
    private String imgUrl;

    @Lob
    @Column(name = "notes")
    private String notes;

    @Column(name = "url")
    private String url;

    @OneToMany(mappedBy = "mainAuthor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "publisher", "format", "langue", "maintopic", "mainAuthor", "collection" }, allowSetters = true)
    private Set<Doc> docs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocAuthor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public DocAuthor name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public DocAuthor imgUrl(String imgUrl) {
        this.setImgUrl(imgUrl);
        return this;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNotes() {
        return this.notes;
    }

    public DocAuthor notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUrl() {
        return this.url;
    }

    public DocAuthor url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Doc> getDocs() {
        return this.docs;
    }

    public void setDocs(Set<Doc> docs) {
        if (this.docs != null) {
            this.docs.forEach(i -> i.setMainAuthor(null));
        }
        if (docs != null) {
            docs.forEach(i -> i.setMainAuthor(this));
        }
        this.docs = docs;
    }

    public DocAuthor docs(Set<Doc> docs) {
        this.setDocs(docs);
        return this;
    }

    public DocAuthor addDoc(Doc doc) {
        this.docs.add(doc);
        doc.setMainAuthor(this);
        return this;
    }

    public DocAuthor removeDoc(Doc doc) {
        this.docs.remove(doc);
        doc.setMainAuthor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocAuthor)) {
            return false;
        }
        return id != null && id.equals(((DocAuthor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocAuthor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", imgUrl='" + getImgUrl() + "'" +
            ", notes='" + getNotes() + "'" +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
