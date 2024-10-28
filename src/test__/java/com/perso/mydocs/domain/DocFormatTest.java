package com.perso.mydocs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.perso.mydocs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocFormatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocFormat.class);
        DocFormat docFormat1 = new DocFormat();
        docFormat1.setId(1L);
        DocFormat docFormat2 = new DocFormat();
        docFormat2.setId(docFormat1.getId());
        assertThat(docFormat1).isEqualTo(docFormat2);
        docFormat2.setId(2L);
        assertThat(docFormat1).isNotEqualTo(docFormat2);
        docFormat1.setId(null);
        assertThat(docFormat1).isNotEqualTo(docFormat2);
    }
}
