package com.perso.mydocs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocFormat.
 */
@Entity
@Table(name = "doc_format")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocFormat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "format")
    private String format;

    @Column(name = "code")
    private String code;

    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "format")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "publisher", "format", "langue", "maintopic", "mainAuthor", "collection" }, allowSetters = true)
    private Set<Doc> docs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocFormat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormat() {
        return this.format;
    }

    public DocFormat format(String format) {
        this.setFormat(format);
        return this;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCode() {
        return this.code;
    }

    public DocFormat code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNotes() {
        return this.notes;
    }

    public DocFormat notes(String notes) {
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
            this.docs.forEach(i -> i.setFormat(null));
        }
        if (docs != null) {
            docs.forEach(i -> i.setFormat(this));
        }
        this.docs = docs;
    }

    public DocFormat docs(Set<Doc> docs) {
        this.setDocs(docs);
        return this;
    }

    public DocFormat addDoc(Doc doc) {
        this.docs.add(doc);
        doc.setFormat(this);
        return this;
    }

    public DocFormat removeDoc(Doc doc) {
        this.docs.remove(doc);
        doc.setFormat(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocFormat)) {
            return false;
        }
        return id != null && id.equals(((DocFormat) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocFormat{" +
            "id=" + getId() +
            ", format='" + getFormat() + "'" +
            ", code='" + getCode() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
