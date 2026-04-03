package br.com.gustavohenrique.cleanmediasapi.domain.assessment;

import br.com.gustavohenrique.cleanmediasapi.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Teste puro JUnit — valida invariantes da entidade Assessment.
class AssessmentTest {

    @Test
    void shouldCreateAssessmentWithDefaultValues() {
        Assessment a = new Assessment(1L, "AV1", 10.0);

        assertThat(a.getProjectionId()).isEqualTo(1L);
        assertThat(a.getIdentifier()).isEqualTo("AV1");
        assertThat(a.getMaxValue()).isEqualTo(10.0);
        assertThat(a.getGrade()).isEqualTo(0.0);
        assertThat(a.getRequiredGrade()).isEqualTo(0.0);
        assertThat(a.isFixed()).isFalse();
    }

    @Test
    void shouldApplyGradeAndMarkFixed() {
        Assessment a = new Assessment(1L, "AV1", 10.0);

        a.applyGrade(8.5);

        assertThat(a.getGrade()).isEqualTo(8.5);
        assertThat(a.isFixed()).isTrue();
    }

    @Test
    void shouldThrowWhenGradeExceedsMaxValue() {
        Assessment a = new Assessment(1L, "AV1", 10.0);

        assertThatThrownBy(() -> a.applyGrade(10.1))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Grade 10.1 exceeds max value 10.0 for AV1");
    }

    @Test
    void shouldThrowWhenGradeIsNegative() {
        Assessment a = new Assessment(1L, "AV1", 10.0);

        assertThatThrownBy(() -> a.applyGrade(-1.0))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Grade cannot be negative");
    }

    @Test
    void shouldAllowGradeEqualToMaxValue() {
        Assessment a = new Assessment(1L, "AV1", 10.0);

        a.applyGrade(10.0);

        assertThat(a.getGrade()).isEqualTo(10.0);
        assertThat(a.isFixed()).isTrue();
    }

    @Test
    void shouldUpdateRequiredGrade() {
        Assessment a = new Assessment(1L, "AV1", 10.0);

        a.updateRequiredGrade(7.5);

        assertThat(a.getRequiredGrade()).isEqualTo(7.5);
    }

    @Test
    void shouldCreateAssessmentWithFullConstructor() {
        Assessment a = new Assessment(5L, 1L, "AT1", 8.0, 6.0, 5.0, true);

        assertThat(a.getId()).isEqualTo(5L);
        assertThat(a.getGrade()).isEqualTo(8.0);
        assertThat(a.isFixed()).isTrue();
    }
}
