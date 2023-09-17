package com.perso.mydocs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.perso.mydocs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DomaineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Domaine.class);
        Domaine domaine1 = new Domaine();
        domaine1.setId(1L);
        Domaine domaine2 = new Domaine();
        domaine2.setId(domaine1.getId());
        assertThat(domaine1).isEqualTo(domaine2);
        domaine2.setId(2L);
        assertThat(domaine1).isNotEqualTo(domaine2);
        domaine1.setId(null);
        assertThat(domaine1).isNotEqualTo(domaine2);
    }
}
