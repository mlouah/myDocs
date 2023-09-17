package com.perso.mydocs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocCategory.
 */
@Entity
@Table(name = "doc_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Lob
    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "docCategory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "publisher", "collection", "format", "langue", "maintopic", "mainAuthor", "docCategory" },
        allowSetters = true
    )
    private Set<Doc> docs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public DocCategory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public DocCategory code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNotes() {
        return this.notes;
    }

    public DocCategory notes(String notes) {
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
            this.docs.forEach(i -> i.setDocCategory(null));
        }
        if (docs != null) {
            docs.forEach(i -> i.setDocCategory(this));
        }
        this.docs = docs;
    }

    public DocCategory docs(Set<Doc> docs) {
        this.setDocs(docs);
        return this;
    }

    public DocCategory addDoc(Doc doc) {
        this.docs.add(doc);
        doc.setDocCategory(this);
        return this;
    }

    public DocCategory removeDoc(Doc doc) {
        this.docs.remove(doc);
        doc.setDocCategory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocCategory)) {
            return false;
        }
        return id != null && id.equals(((DocCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
