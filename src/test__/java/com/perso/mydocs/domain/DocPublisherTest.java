package com.perso.mydocs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.perso.mydocs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocPublisherTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocPublisher.class);
        DocPublisher docPublisher1 = new DocPublisher();
        docPublisher1.setId(1L);
        DocPublisher docPublisher2 = new DocPublisher();
        docPublisher2.setId(docPublisher1.getId());
        assertThat(docPublisher1).isEqualTo(docPublisher2);
        docPublisher2.setId(2L);
        assertThat(docPublisher1).isNotEqualTo(docPublisher2);
        docPublisher1.setId(null);
        assertThat(docPublisher1).isNotEqualTo(docPublisher2);
    }
}
