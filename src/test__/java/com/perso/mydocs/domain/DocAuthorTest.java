package com.perso.mydocs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.perso.mydocs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocAuthorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocAuthor.class);
        DocAuthor docAuthor1 = new DocAuthor();
        docAuthor1.setId(1L);
        DocAuthor docAuthor2 = new DocAuthor();
        docAuthor2.setId(docAuthor1.getId());
        assertThat(docAuthor1).isEqualTo(docAuthor2);
        docAuthor2.setId(2L);
        assertThat(docAuthor1).isNotEqualTo(docAuthor2);
        docAuthor1.setId(null);
        assertThat(docAuthor1).isNotEqualTo(docAuthor2);
    }
}
