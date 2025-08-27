package com.caltron.caltron.dao;

import com.caltron.caltron.beans.GradeBean;
import com.caltron.caltron.beans.SemesterBean;
import com.caltron.caltron.beans.SubjectBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

@Repository
public class DropdownDAO {

    JdbcTemplate jdbcTemplate;

    public DropdownDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Get all grades
    public List<GradeBean> getAllGrades() {
        String sql = "SELECT grade_id, grade_level FROM grades";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            GradeBean grade = new GradeBean();
            grade.setGradeId(rs.getInt("grade_id"));
            grade.setGradeLevel(rs.getString("grade_level"));
            return grade;
        });
    }

    // Get semesters by gradeId
    public List<SemesterBean> getSemestersByGrade(Long gradeId) {
        String sql = "SELECT semester_id AS id, "
                + "grade_id AS gradeId, "
                + "semester_name AS name "
                + "FROM semesters "
                + "WHERE grade_id = ?";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(SemesterBean.class),
                gradeId
        );
    }

    // Get subjects by semesterId
    public List<SubjectBean> getSubjectsBySemester(Long semesterId) {
        String sql = "SELECT subject_id AS id, semester_id, subject_name AS name FROM subjects WHERE semester_id = ? ORDER BY subject_name ASC";
        return jdbcTemplate.query(sql, new Object[]{semesterId}, (rs, rowNum) -> {
            SubjectBean bean = new SubjectBean();
            bean.setId(rs.getLong("id"));
            bean.setSemesterId(rs.getLong("semester_id"));
            bean.setName(rs.getString("name"));
            return bean;
        });
    }
}
