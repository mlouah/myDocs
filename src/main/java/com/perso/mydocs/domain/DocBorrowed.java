package com.perso.mydocs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocBorrowed.
 */
@Entity
@Table(name = "doc_borrowed")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocBorrowed implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "borrow_date")
    private LocalDate borrowDate;

    @Column(name = "borrower_name")
    private String borrowerName;

    @Column(name = "notes")
    private String notes;

    @JsonIgnoreProperties(value = { "publisher", "format", "langue", "maintopic", "mainAuthor", "collection" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Doc doc;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocBorrowed id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBorrowDate() {
        return this.borrowDate;
    }

    public DocBorrowed borrowDate(LocalDate borrowDate) {
        this.setBorrowDate(borrowDate);
        return this;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getBorrowerName() {
        return this.borrowerName;
    }

    public DocBorrowed borrowerName(String borrowerName) {
        this.setBorrowerName(borrowerName);
        return this;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getNotes() {
        return this.notes;
    }

    public DocBorrowed notes(String notes) {
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
        this.doc = doc;
    }

    public DocBorrowed doc(Doc doc) {
        this.setDoc(doc);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocBorrowed)) {
            return false;
        }
        return id != null && id.equals(((DocBorrowed) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocBorrowed{" +
            "id=" + getId() +
            ", borrowDate='" + getBorrowDate() + "'" +
            ", borrowerName='" + getBorrowerName() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
