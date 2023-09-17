package com.perso.mydocs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocTopic.
 */
@Entity
@Table(name = "doc_topic")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocTopic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "img_url")
    private String imgUrl;

    @Lob
    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "maintopic")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "publisher", "collection", "format", "langue", "maintopic", "mainAuthor", "docCategory" },
        allowSetters = true
    )
    private Set<Doc> docs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "topics" }, allowSetters = true)
    private Domaine domaine;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocTopic id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public DocTopic name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public DocTopic code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public DocTopic imgUrl(String imgUrl) {
        this.setImgUrl(imgUrl);
        return this;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNotes() {
        return this.notes;
    }

    public DocTopic notes(String notes) {
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
            this.docs.forEach(i -> i.setMaintopic(null));
        }
        if (docs != null) {
            docs.forEach(i -> i.setMaintopic(this));
        }
        this.docs = docs;
    }

    public DocTopic docs(Set<Doc> docs) {
        this.setDocs(docs);
        return this;
    }

    public DocTopic addDoc(Doc doc) {
        this.docs.add(doc);
        doc.setMaintopic(this);
        return this;
    }

    public DocTopic removeDoc(Doc doc) {
        this.docs.remove(doc);
        doc.setMaintopic(null);
        return this;
    }

    public Domaine getDomaine() {
        return this.domaine;
    }

    public void setDomaine(Domaine domaine) {
        this.domaine = domaine;
    }

    public DocTopic domaine(Domaine domaine) {
        this.setDomaine(domaine);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocTopic)) {
            return false;
        }
        return id != null && id.equals(((DocTopic) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocTopic{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", imgUrl='" + getImgUrl() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
