package br.com.gustavohenrique.cleanmediasapi.application.assessment;

import br.com.gustavohenrique.cleanmediasapi.application.assessment.dto.AssessmentResponse;
import br.com.gustavohenrique.cleanmediasapi.domain.assessment.Assessment;
import br.com.gustavohenrique.cleanmediasapi.domain.assessment.AssessmentNotFoundException;
import br.com.gustavohenrique.cleanmediasapi.domain.assessment.AssessmentRepository;
import br.com.gustavohenrique.cleanmediasapi.domain.formula.FormulaParser;
import br.com.gustavohenrique.cleanmediasapi.domain.formula.RpnEvaluator;
import br.com.gustavohenrique.cleanmediasapi.domain.formula.ShuntingYard;
import br.com.gustavohenrique.cleanmediasapi.domain.projection.Projection;
import br.com.gustavohenrique.cleanmediasapi.domain.projection.ProjectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final ProjectionRepository projectionRepository;

    private final FormulaParser formulaParser = new FormulaParser();
    private final ShuntingYard shuntingYard = new ShuntingYard();
    private final RpnEvaluator rpnEvaluator = new RpnEvaluator();

    @Override
    public void initializeForProjection(Projection projection, String averageMethod) {
        Map<String, Double> identifiers = formulaParser.extractIdentifiers(averageMethod);
        List<Assessment> assessments = identifiers.entrySet().stream()
                .map(e -> new Assessment(projection.getId(), e.getKey(), e.getValue()))
                .toList();
        assessmentRepository.saveAll(assessments);
    }

    @Override
    public void deleteAllByProjectionId(Long projectionId) {
        assessmentRepository.deleteAllByProjectionId(projectionId);
    }

    @Override
    public List<AssessmentResponse> findAllByProjectionId(Long projectionId) {
        return assessmentRepository.findAllByProjectionId(projectionId).stream()
                .map(AssessmentResponse::from)
                .toList();
    }

    @Override
    public AssessmentResponse findByIdAndProjectionId(Long id, Long projectionId) {
        return assessmentRepository.findByIdAndProjectionId(id, projectionId)
                .map(AssessmentResponse::from)
                .orElseThrow(() -> new AssessmentNotFoundException(id, projectionId));
    }

    @Override
    public AssessmentResponse applyGrade(Long projectionId, Long assessmentId, double grade, String averageMethod) {
        Assessment assessment = assessmentRepository.findByIdAndProjectionId(assessmentId, projectionId)
                .orElseThrow(() -> new AssessmentNotFoundException(assessmentId, projectionId));

        assessment.applyGrade(grade); // valida e marca fixed=true
        Assessment saved = assessmentRepository.save(assessment);

        recalculateFinalGrade(projectionId, averageMethod);

        return AssessmentResponse.from(saved);
    }

    // Recalcula o finalGrade da projeção usando o pipeline RPN com as notas atuais
    private void recalculateFinalGrade(Long projectionId, String averageMethod) {
        List<Assessment> all = assessmentRepository.findAllByProjectionId(projectionId);
        Map<String, Double> grades = all.stream()
                .collect(Collectors.toMap(Assessment::getIdentifier, Assessment::getGrade));

        var tokens = formulaParser.tokenize(averageMethod);
        var rpn = shuntingYard.toRpn(tokens);
        double finalGrade = rpnEvaluator.evaluate(rpn, grades::get);

        Projection projection = projectionRepository.findById(projectionId)
                .orElseThrow();
        projectionRepository.save(new Projection(
                projection.getId(), projection.getCourseId(),
                projection.getName(), finalGrade
        ));
    }
}
