package com.perso.mydocs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.perso.mydocs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocBorrowedTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocBorrowed.class);
        DocBorrowed docBorrowed1 = new DocBorrowed();
        docBorrowed1.setId(1L);
        DocBorrowed docBorrowed2 = new DocBorrowed();
        docBorrowed2.setId(docBorrowed1.getId());
        assertThat(docBorrowed1).isEqualTo(docBorrowed2);
        docBorrowed2.setId(2L);
        assertThat(docBorrowed1).isNotEqualTo(docBorrowed2);
        docBorrowed1.setId(null);
        assertThat(docBorrowed1).isNotEqualTo(docBorrowed2);
    }
}
