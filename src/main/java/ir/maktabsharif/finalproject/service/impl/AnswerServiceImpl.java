package ir.maktabsharif.finalproject.service.impl;

import ir.maktabsharif.finalproject.model.Answer;
import ir.maktabsharif.finalproject.repository.AnswerRepository;
import ir.maktabsharif.finalproject.service.AnswerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerServiceImpl(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    @Transactional
    public void createAnswers(List<Answer> answers) {
        answerRepository.saveAll(answers);
    }

    @Override
    public List<Answer> getAnswersByExamIdAndStudentId(Long examId, Long studentId) {
        return answerRepository.findAnswersByExamIdAndStudentId(examId, studentId);
    }
}
