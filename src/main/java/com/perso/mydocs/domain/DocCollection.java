package com.perso.mydocs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocCollection.
 */
@Entity
@Table(name = "doc_collection")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "notes")
    private String notes;

    @JsonIgnoreProperties(
        value = { "publisher", "collection", "format", "langue", "maintopic", "mainAuthor", "docCategory" },
        allowSetters = true
    )
    @OneToOne(mappedBy = "collection")
    private Doc doc;

    @ManyToOne
    @JsonIgnoreProperties(value = { "collections" }, allowSetters = true)
    private DocPublisher docPublisher;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocCollection id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public DocCollection name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return this.notes;
    }

    public DocCollection notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Doc getDoc() {
        return this.doc;
    }

    public void setDoc(Doc doc) {
        if (this.doc != null) {
            this.doc.setCollection(null);
        }
        if (doc != null) {
            doc.setCollection(this);
        }
        this.doc = doc;
    }

    public DocCollection doc(Doc doc) {
        this.setDoc(doc);
        return this;
    }

    public DocPublisher getDocPublisher() {
        return this.docPublisher;
    }

    public void setDocPublisher(DocPublisher docPublisher) {
        this.docPublisher = docPublisher;
    }

    public DocCollection docPublisher(DocPublisher docPublisher) {
        this.setDocPublisher(docPublisher);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocCollection)) {
            return false;
        }
        return id != null && id.equals(((DocCollection) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocCollection{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
