package com.perso.mydocs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocPublisher.
 */
@Entity
@Table(name = "doc_publisher")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocPublisher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "notes")
    private String notes;

    @Column(name = "url")
    private String url;

    @OneToMany(mappedBy = "publisher")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "publisher", "format", "langue", "maintopic", "mainAuthor", "collection" }, allowSetters = true)
    private Set<Doc> docs = new HashSet<>();

    @OneToMany(mappedBy = "docPublisher")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "docs", "docPublisher" }, allowSetters = true)
    private Set<DocCollection> collections = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocPublisher id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public DocPublisher name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return this.notes;
    }

    public DocPublisher notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUrl() {
        return this.url;
    }

    public DocPublisher url(String url) {
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
            this.docs.forEach(i -> i.setPublisher(null));
        }
        if (docs != null) {
            docs.forEach(i -> i.setPublisher(this));
        }
        this.docs = docs;
    }

    public DocPublisher docs(Set<Doc> docs) {
        this.setDocs(docs);
        return this;
    }

    public DocPublisher addDoc(Doc doc) {
        this.docs.add(doc);
        doc.setPublisher(this);
        return this;
    }

    public DocPublisher removeDoc(Doc doc) {
        this.docs.remove(doc);
        doc.setPublisher(null);
        return this;
    }

    public Set<DocCollection> getCollections() {
        return this.collections;
    }

    public void setCollections(Set<DocCollection> docCollections) {
        if (this.collections != null) {
            this.collections.forEach(i -> i.setDocPublisher(null));
        }
        if (docCollections != null) {
            docCollections.forEach(i -> i.setDocPublisher(this));
        }
        this.collections = docCollections;
    }

    public DocPublisher collections(Set<DocCollection> docCollections) {
        this.setCollections(docCollections);
        return this;
    }

    public DocPublisher addCollection(DocCollection docCollection) {
        this.collections.add(docCollection);
        docCollection.setDocPublisher(this);
        return this;
    }

    public DocPublisher removeCollection(DocCollection docCollection) {
        this.collections.remove(docCollection);
        docCollection.setDocPublisher(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocPublisher)) {
            return false;
        }
        return id != null && id.equals(((DocPublisher) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocPublisher{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", notes='" + getNotes() + "'" +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
