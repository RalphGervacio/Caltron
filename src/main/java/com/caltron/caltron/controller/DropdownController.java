package com.caltron.caltron.controller;

import com.caltron.caltron.beans.GradeBean;
import com.caltron.caltron.beans.SemesterBean;
import com.caltron.caltron.beans.SubjectBean;
import com.caltron.caltron.dao.DropdownDAO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DropdownController {

    DropdownDAO dropdownDAO;

    public DropdownController(DropdownDAO dropdownDAO) {
        this.dropdownDAO = dropdownDAO;
    }

    // Get all grades
    @GetMapping("/grades")
    public List<GradeBean> getGrades() {
        return dropdownDAO.getAllGrades();
    }

    // Get semesters by grade
    @GetMapping("/semesters/by-grade")
    public List<SemesterBean> getSemesters(@RequestParam Long gradeId) {
        return dropdownDAO.getSemestersByGrade(gradeId);
    }

    // Get subjects by semester
    @GetMapping("/subjects/by-semester")
    public List<SubjectBean> getSubjects(@RequestParam Long semesterId) {
        return dropdownDAO.getSubjectsBySemester(semesterId);
    }
}
