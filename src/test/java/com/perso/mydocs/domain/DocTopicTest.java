package com.perso.mydocs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.perso.mydocs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocTopicTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocTopic.class);
        DocTopic docTopic1 = new DocTopic();
        docTopic1.setId(1L);
        DocTopic docTopic2 = new DocTopic();
        docTopic2.setId(docTopic1.getId());
        assertThat(docTopic1).isEqualTo(docTopic2);
        docTopic2.setId(2L);
        assertThat(docTopic1).isNotEqualTo(docTopic2);
        docTopic1.setId(null);
        assertThat(docTopic1).isNotEqualTo(docTopic2);
    }
}
