$(document).ready(function () {

    // Initialize Select2
    $('#gradeDropdown, #semesterDropdown, #subjectDropdown').select2({
        placeholder: 'Select an option',
        allowClear: true,
        width: '100%'  
    });

    // Grades
    fetchData('/api/grades', {}).done(function (grades) {
        let gradeDropdown = $('#gradeDropdown');
        gradeDropdown.empty().append('<option></option>');
        grades.forEach(grade => {
            gradeDropdown.append(`<option value="${grade.gradeId}">${grade.gradeLevel}</option>`);
        });
        gradeDropdown.trigger('change.select2');
    });

    // Semesters
    $('#gradeDropdown').on('change', function () {
        let gradeId = $(this).val();
        let semDropdown = $('#semesterDropdown');
        semDropdown.empty().append('<option></option>');
        $('#subjectDropdown').empty().append('<option></option>');

        if (!gradeId)
            return;

        fetchData('/api/semesters/by-grade', {gradeId: gradeId}).done(function (semesters) {
            semesters.forEach(function (sem) {
                semDropdown.append(`<option value="${sem.id}">${sem.name}</option>`);
            });
            semDropdown.trigger('change.select2');
        });
    });

    // Subjects
    $('#semesterDropdown').on('change', function () {
        let semesterId = $(this).val();
        let subjDropdown = $('#subjectDropdown');
        subjDropdown.empty().append('<option></option>');

        if (!semesterId)
            return;

        fetchData('/api/subjects/by-semester', {semesterId: semesterId}).done(function (subjects) {
            subjects.forEach(function (subj) {
                subjDropdown.append(`<option value="${subj.id}">${subj.name}</option>`);
            });
            subjDropdown.trigger('change.select2');
        });
    });

});
