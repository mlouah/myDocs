package com.perso.mydocs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.perso.mydocs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Doc.class);
        Doc doc1 = new Doc();
        doc1.setId(1L);
        Doc doc2 = new Doc();
        doc2.setId(doc1.getId());
        assertThat(doc1).isEqualTo(doc2);
        doc2.setId(2L);
        assertThat(doc1).isNotEqualTo(doc2);
        doc1.setId(null);
        assertThat(doc1).isNotEqualTo(doc2);
    }
}
