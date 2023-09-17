package com.perso.mydocs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.perso.mydocs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocCategory.class);
        DocCategory docCategory1 = new DocCategory();
        docCategory1.setId(1L);
        DocCategory docCategory2 = new DocCategory();
        docCategory2.setId(docCategory1.getId());
        assertThat(docCategory1).isEqualTo(docCategory2);
        docCategory2.setId(2L);
        assertThat(docCategory1).isNotEqualTo(docCategory2);
        docCategory1.setId(null);
        assertThat(docCategory1).isNotEqualTo(docCategory2);
    }
}
