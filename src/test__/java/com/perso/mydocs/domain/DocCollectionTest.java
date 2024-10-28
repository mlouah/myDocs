package com.perso.mydocs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.perso.mydocs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocCollectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocCollection.class);
        DocCollection docCollection1 = new DocCollection();
        docCollection1.setId(1L);
        DocCollection docCollection2 = new DocCollection();
        docCollection2.setId(docCollection1.getId());
        assertThat(docCollection1).isEqualTo(docCollection2);
        docCollection2.setId(2L);
        assertThat(docCollection1).isNotEqualTo(docCollection2);
        docCollection1.setId(null);
        assertThat(docCollection1).isNotEqualTo(docCollection2);
    }
}
