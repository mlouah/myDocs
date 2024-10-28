package com.perso.mydocs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
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

    @OneToMany(mappedBy = "collection")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "publisher", "format", "langue", "maintopic", "mainAuthor", "collection" }, allowSetters = true)
    private Set<Doc> docs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "docs", "collections" }, allowSetters = true)
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

    public Set<Doc> getDocs() {
        return this.docs;
    }

    public void setDocs(Set<Doc> docs) {
        if (this.docs != null) {
            this.docs.forEach(i -> i.setCollection(null));
        }
        if (docs != null) {
            docs.forEach(i -> i.setCollection(this));
        }
        this.docs = docs;
    }

    public DocCollection docs(Set<Doc> docs) {
        this.setDocs(docs);
        return this;
    }

    public DocCollection addDoc(Doc doc) {
        this.docs.add(doc);
        doc.setCollection(this);
        return this;
    }

    public DocCollection removeDoc(Doc doc) {
        this.docs.remove(doc);
        doc.setCollection(null);
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
