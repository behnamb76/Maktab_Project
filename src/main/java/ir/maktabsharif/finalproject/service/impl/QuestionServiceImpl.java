package ir.maktabsharif.finalproject.service.impl;

import ir.maktabsharif.finalproject.exception.ResourceNotFoundException;
import ir.maktabsharif.finalproject.model.*;
import ir.maktabsharif.finalproject.model.dto.request.OptionReqDTO;
import ir.maktabsharif.finalproject.model.dto.request.QuestionReqDTO;
import ir.maktabsharif.finalproject.repository.ExamRepository;
import ir.maktabsharif.finalproject.repository.OptionRepository;
import ir.maktabsharif.finalproject.repository.QuestionBankRepository;
import ir.maktabsharif.finalproject.repository.QuestionRepository;
import ir.maktabsharif.finalproject.service.CourseService;
import ir.maktabsharif.finalproject.service.QuestionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionBankRepository questionBankRepository;
    private final OptionRepository optionRepository;
    private final CourseService courseService;
    private final ExamRepository examRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository, QuestionBankRepository questionBankRepository, OptionRepository optionRepository, CourseService courseService, ExamRepository examRepository) {
        this.questionRepository = questionRepository;
        this.questionBankRepository = questionBankRepository;
        this.optionRepository = optionRepository;
        this.courseService = courseService;
        this.examRepository = examRepository;
    }

    @Override
    @Transactional
    public Question createQuestion(QuestionReqDTO questionReqDTO, Long courseId) {

        Question question;
        if (questionReqDTO.getType().equals("MULTIPLE_CHOICE")) {

            MultipleChoiceQuestion mcq = MultipleChoiceQuestion.builder()
                    .title(questionReqDTO.getTitle())
                    .content(questionReqDTO.getContent())
                    .defaultScore(questionReqDTO.getDefaultScore())
                    .build();
            mcq.setOptions(createOption(questionReqDTO, mcq));

            question = questionRepository.save(mcq);
        } else {
            EssayQuestion eq = EssayQuestion.builder()
                    .title(questionReqDTO.getTitle())
                    .content(questionReqDTO.getContent())
                    .defaultScore(questionReqDTO.getDefaultScore())
                    .build();

            question = questionRepository.save(eq);
        }

        Course course = courseService.getCourseById(courseId);
        QuestionBank questionBank = questionBankRepository.findQuestionBankByCourseId(courseId)
                .orElseGet(() -> {
                    QuestionBank newBank = QuestionBank.builder()
                            .course(course).build();
                    return questionBankRepository.save(newBank);
                });

        questionBank.getQuestions().add(question);
        questionBankRepository.save(questionBank);
        return question;
    }

    @Override
    public void updateQuestion(Long id, QuestionReqDTO questionReqDTO) {
        if (questionReqDTO.getType().equals("MULTIPLE_CHOICE")) {
            MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) getQuestionById(id);
            mcq.setTitle(questionReqDTO.getTitle());
            mcq.setContent(questionReqDTO.getContent());
            mcq.setDefaultScore(questionReqDTO.getDefaultScore());
            if (!questionReqDTO.getOptions().isEmpty()) {
                Set<Option> options = new HashSet<>();
                for (OptionReqDTO optionReqDTO : questionReqDTO.getOptions()) {
                    Option option = Option.builder()
                            .text(optionReqDTO.getText())
                            .correct(optionReqDTO.isCorrect())
                            .question(mcq)
                            .build();
                    options.add(option);
                }
                mcq.setOptions(options);
            }
        }
    }

    @Override
    public Question getQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
    }

    @Override
    public Set<Option> createOption(QuestionReqDTO questionReqDTO, MultipleChoiceQuestion mcq) {
        if (!questionReqDTO.getOptions().isEmpty()) {
            Set<Option> options = new HashSet<>();
            for (OptionReqDTO optionReqDTO : questionReqDTO.getOptions()) {
                Option option = Option.builder()
                        .text(optionReqDTO.getText())
                        .correct(optionReqDTO.isCorrect())
                        .question(mcq)
                        .build();
                options.add(option);
            }
            return options;
        }
        return Set.of();
    }

    @Override
    public List<Option> getOptionsByQuestionId(Long questionId) {
        return optionRepository.findOptionsByQuestionId(questionId);
    }

    @Override
    public List<Option> getCorrectOptions(Long questionId) {
        return optionRepository.findOptionsByQuestionIdAndCorrect(questionId, true);
    }

    @Override
    @Transactional
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    @Override
    public void deleteOption(Long optionId) {
        optionRepository.deleteById(optionId);
    }

    @Override
    @Transactional
    public void reuseQuestionInExam(Long examId, Long questionId, Double score) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        Question question = getQuestionById(questionId);

        ExamQuestion examQuestion = ExamQuestion.builder()
                .exam(exam)
                .question(question)
                .score(score)
                .build();

        exam.getExamQuestions().add(examQuestion);

        examRepository.save(exam);
    }

    @Override
    public List<Question> getQuestionsByCourseId(Long courseId) {
        QuestionBank questionBank = questionBankRepository.findQuestionBankByCourseId(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Question bank not found for course"));

        return questionBank.getQuestions();
    }
}
