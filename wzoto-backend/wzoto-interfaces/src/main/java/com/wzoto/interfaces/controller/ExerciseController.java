package com.wzoto.interfaces.controller;

import com.wzoto.application.service.ExerciseBankApplicationService;
import com.wzoto.domain.entity.ExerciseBank;
import com.wzoto.domain.entity.TestPaper;
import com.wzoto.interfaces.common.R;
import com.wzoto.interfaces.dto.SubmitAnswerDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 习题/试卷控制器 */
@Slf4j
@RestController
@RequestMapping("/api/exercise")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseBankApplicationService exerciseBankApplicationService;

    @GetMapping("/list")
    public R<List<ExerciseBank>> listExercises(@RequestParam(required = false) String grade,
                                                @RequestParam(required = false) String subject,
                                                @RequestParam(required = false) String difficulty,
                                                @RequestParam(required = false) String questionType,
                                                @RequestParam(defaultValue = "20") int limit) {
        return R.ok(exerciseBankApplicationService.listExercises(grade, subject, difficulty, questionType, limit));
    }

    @GetMapping("/{id}")
    public R<ExerciseBank> getExercise(@PathVariable Long id) {
        return R.ok(exerciseBankApplicationService.getExercise(id));
    }

    @PostMapping("/submit")
    public R<Boolean> submitAnswer(@Valid @RequestBody SubmitAnswerDTO dto) {
        boolean correct = exerciseBankApplicationService.submitAnswer(dto.getExerciseId(), dto.getStudentAnswer());
        return R.ok(correct);
    }

    @GetMapping("/paper/{id}")
    public R<TestPaper> getTestPaper(@PathVariable Long id) {
        return R.ok(exerciseBankApplicationService.getTestPaper(id));
    }

    @GetMapping("/paper/list")
    public R<List<TestPaper>> listTestPapers(@RequestParam(required = false) String grade,
                                              @RequestParam(required = false) String subject,
                                              @RequestParam(required = false) String paperType) {
        return R.ok(exerciseBankApplicationService.listTestPapers(grade, subject, paperType));
    }
}
