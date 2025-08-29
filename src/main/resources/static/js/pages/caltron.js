function getGradeSuggestions(finalGrade) {
    const allSuggestions = {
        below75: [
            "Focus on understanding the basics before attempting harder topics.",
            "Review previous lessons and practice more exercises.",
            "Ask your teacher for clarification on topics you don't understand.",
            "Try breaking topics into smaller parts to understand better.",
            "Use online tutorials or videos for additional explanations.",
            "Create a study schedule and stick to it consistently.",
            "Practice regularly rather than cramming before exams.",
            "Take detailed notes and review them daily.",
            "Identify common mistakes and avoid repeating them.",
            "Set achievable short-term goals for each week.",
            "Use flashcards to remember key terms and formulas."
        ],
        upTo79: [
            "Identify weak areas and work on them consistently.",
            "Participate in class and ask questions when unsure.",
            "Allocate more time to practice assignments and study sessions.",
            "Take short quizzes to check your understanding.",
            "Summarize each lesson in your own words.",
            "Use flashcards to memorize key concepts.",
            "Review graded assignments to learn from mistakes.",
            "Form a study group with peers for collaborative learning.",
            "Use diagrams or mind maps to visualize concepts.",
            "Set up a reward system to stay motivated.",
            "Try teaching a concept to someone else to reinforce learning."
        ],
        upTo84: [
            "Improve time management for studying and assignments.",
            "Practice more advanced exercises to strengthen understanding.",
            "Seek feedback from your teacher on how to improve further.",
            "Teach a topic to someone else to reinforce your knowledge.",
            "Organize notes and highlight important points.",
            "Set specific goals for each study session.",
            "Use past exams to familiarize yourself with question patterns.",
            "Focus on understanding concepts rather than memorization.",
            "Work on problem-solving strategies for complex questions.",
            "Review regularly to prevent forgetting learned material.",
            "Balance study time between weak and strong subjects."
        ],
        upTo89: [
            "Challenge yourself with higher-level exercises.",
            "Review mistakes and ensure they are not repeated.",
            "Form study groups to learn from peers.",
            "Try applying concepts to real-life examples.",
            "Maintain a revision routine before exams.",
            "Use mind maps to connect related topics.",
            "Time yourself while solving practice problems.",
            "Seek mentorship or guidance for advanced topics.",
            "Reflect on past performances and adjust study methods.",
            "Work on multiple subjects in rotation to stay balanced.",
            "Participate in academic competitions to push your limits."
        ],
        upTo94: [
            "Aim for consistency in all subjects.",
            "Assist classmates to reinforce your understanding.",
            "Explore enrichment activities to further excel.",
            "Participate in competitions or extra challenges.",
            "Reflect on your learning strategies and improve them.",
            "Keep track of your progress regularly.",
            "Read beyond the curriculum to deepen understanding.",
            "Take detailed notes on advanced topics.",
            "Set higher goals to push your own limits.",
            "Share knowledge with peers to enhance retention.",
            "Balance academics with extracurricular activities."
        ],
        above94: [
            "Maintain your high performance.",
            "Take on leadership roles in class activities.",
            "Continue seeking challenges to expand your knowledge.",
            "Mentor classmates to deepen your understanding.",
            "Start exploring advanced topics beyond the syllabus.",
            "Document your learning process for future reference.",
            "Participate in research projects or academic clubs.",
            "Teach concepts to peers or younger students.",
            "Pursue competitions or Olympiads for extra recognition.",
            "Reflect regularly on strengths and areas to improve.",
            "Set personal milestones for continuous growth."
        ]
    };

    let category;
    if (finalGrade < 75)
        category = allSuggestions.below75;
    else if (finalGrade <= 79)
        category = allSuggestions.upTo79;
    else if (finalGrade <= 84)
        category = allSuggestions.upTo84;
    else if (finalGrade <= 89)
        category = allSuggestions.upTo89;
    else if (finalGrade <= 94)
        category = allSuggestions.upTo94;
    else
        category = allSuggestions.above94;

    let numSuggestions;
    if (finalGrade < 75)
        numSuggestions = 3;     
    else if (finalGrade <= 84)
        numSuggestions = 4; 
    else if (finalGrade <= 94)
        numSuggestions = 5; 
    else
        numSuggestions = 6;                      

    const shuffled = category.sort(() => 0.5 - Math.random());
    return shuffled.slice(0, numSuggestions);
}


function computeGrade() {
    let isValid = true;

    // Reset validation
    $('input, select').removeClass('is-invalid');

    // Validate number inputs
    $('input').each(function () {
        let value = $(this).val().trim();
        if (value === '' || isNaN(value)) {
            $(this).addClass('is-invalid');
            isValid = false;
        }
    });

    // Validate scores not exceeding totals
    const scoreTotalPairs = [
        {score: '#wwScore', total: '#wwTotal'},
        {score: '#ptScore', total: '#ptTotal'},
        {score: '#exScore', total: '#exTotal'}
    ];

    scoreTotalPairs.forEach(pair => {
        const scoreVal = parseFloat($(pair.score).val());
        const totalVal = parseFloat($(pair.total).val());
        if (scoreVal > totalVal) {
            $(pair.score).addClass('is-invalid');
            $(pair.total).addClass('is-invalid');
            isValid = false;
        }
    });

    // Validate dropdowns
    const dropdowns = [
        {id: '#gradeDropdown', name: 'Grade'},
        // {id: '#semesterDropdown', name: 'Semester'},
        {id: '#subjectDropdown', name: 'Subject'}
    ];

    dropdowns.forEach(dd => {
        const $dropdown = $(dd.id);
        const val = $dropdown.val();

        if (!val || val.trim() === '') {
            // add is-invalid to <select>
            $dropdown.addClass('is-invalid');

            // 🔹 if Select2 is applied, also add red border to Select2 container
            if ($dropdown.hasClass('select2-hidden-accessible')) {
                $dropdown.next('.select2').find('.select2-selection').addClass('is-invalid');
            }

            isValid = false;
        } else {
            $dropdown.removeClass('is-invalid');

            // 🔹 clean up Select2 UI if valid
            if ($dropdown.hasClass('select2-hidden-accessible')) {
                $dropdown.next('.select2').find('.select2-selection').removeClass('is-invalid');
            }
        }
    });

    if (!isValid) {
        Swal.fire({
            icon: 'error',
            title: 'Invalid Input',
            text: 'Please fill in all fields with valid numbers and selections, and ensure scores do not exceed totals.'
        });
        return;
    }

    Swal.fire({
        title: 'Confirm Inputs?',
        html: `
            <div style="text-align:center; font-size:15px; line-height:1.6;">
                <b>Grade:</b> ${$('#gradeDropdown option:selected').text() || '—'} <br>
                <b>Subject:</b> ${$('#subjectDropdown option:selected').text() || '—'} <br>
                <b>Written Works:</b> ${$('#wwScore').val()} / ${$('#wwTotal').val()} <br>
                <b>Performance Tasks:</b> ${$('#ptScore').val()} / ${$('#ptTotal').val()} <br>
                <b>Exam:</b> ${$('#exScore').val()} / ${$('#exTotal').val()} <br>
                <b>Weights:</b> 
                <span style="color:#007bff;">WW: ${$('#wWW').val()}%</span> | 
                <span style="color:#28a745;">PT: ${$('#wPT').val()}%</span> | 
                <span style="color:#dc3545;">Exam: ${$('#wEX').val()}%</span>
            </div>
        `,
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Yes, Compute',
        cancelButtonText: 'Cancel',
        customClass: {
            popup: 'swal2-border-radius',
            confirmButton: 'btn btn-success mx-1',
            cancelButton: 'btn btn-danger'
        },
        buttonsStyling: false
    }).then((result) => {
        if (!result.isConfirmed)
            return;

        const payload = {
            grade: $('#gradeDropdown').val(),
            semester: $('#semesterDropdown').val(),
            subject: $('#subjectDropdown').val(),
            wwScore: $('#wwScore').val(),
            wwTotal: $('#wwTotal').val(),
            ptScore: $('#ptScore').val(),
            ptTotal: $('#ptTotal').val(),
            examScore: $('#exScore').val(),
            examTotal: $('#exTotal').val(),
            wwWeight: $('#wWW').val(),
            ptWeight: $('#wPT').val(),
            examWeight: $('#wEX').val()
        };

        $.ajax({
            url: '/caltron/compute',
            method: 'POST',
            data: payload
        }).done(function (data) {
//            let normalizedNote = '';
//            if (data.weights && data.weights.normalized) {
//                normalizedNote = `\n(Weights auto-normalized to 100%) -> WW: ${data.weights.ww.toFixed(2)}%, PT: ${data.weights.pt.toFixed(2)}%, Exam: ${data.weights.exam.toFixed(2)}%\n`;
//            }

            const suggestions = getGradeSuggestions(data.finalGrade);
            const suggestionHtml = suggestions.map(s => `<li class="list-group-item">${s}</li>`).join('');

            const output = `
            <div class="card shadow-sm border-0 mx-auto my-3 px-2" style="font-family: 'Segoe UI', sans-serif;">
                <div class="card-header d-flex align-items-center bg-primary text-white">
                    <i class="fa-solid fa-chart-pie me-2 fs-5"></i>
                    <h5 class="mb-0 fw-bold">Grade Summary & Suggestions</h5>
                </div>

                <div class="card-body d-flex flex-column flex-md-row gap-3">
                    <!-- Left Column: Student Info & Scores -->
                    <div class="flex-grow-1">
                        <!-- Student Info -->
                        <div class="mb-3">
                            <!-- Grade -->
                            <div class="d-flex flex-column flex-md-row justify-content-between mb-2">
                                <span class="fw-semibold me-md-2">Grade:</span>
                                <span>${$('#gradeDropdown option:selected').text() || '—'}</span>
                            </div>

                            <!-- Subject -->
                            <div class="d-flex flex-column flex-md-row justify-content-between mb-3">
                                <span class="fw-semibold me-md-2">Subject:</span>
                                <span>${$('#subjectDropdown option:selected').text() || '—'}</span>
                            </div>
                        </div>

                        <!-- Scores Section -->
                        <div class="mb-2">
                            <h6 class="fw-bold mb-1">Scores</h6>

                            <div class="mb-2">
                                <label class="form-label mb-1">Written Works</label>
                                <div class="progress" style="height: 20px;">
                                    <div class="progress-bar bg-primary" role="progressbar" style="width: ${data.writtenWorks.percent.toFixed(2)}%">
                                        ${data.writtenWorks.percent.toFixed(2)}%
                                    </div>
                                </div>
                            </div>

                            <div class="mb-2">
                                <label class="form-label mb-1">Performance Tasks</label>
                                <div class="progress" style="height: 20px;">
                                    <div class="progress-bar bg-success" role="progressbar" style="width: ${data.performanceTasks.percent.toFixed(2)}%">
                                        ${data.performanceTasks.percent.toFixed(2)}%
                                    </div>
                                </div>
                            </div>

                            <div class="mb-2">
                                <label class="form-label mb-1">Quarterly Exam</label>
                                <div class="progress" style="height: 20px;">
                                    <div class="progress-bar bg-warning text-dark" role="progressbar" style="width: ${data.exam.percent.toFixed(2)}%">
                                        ${data.exam.percent.toFixed(2)}%
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Right Column: Final Grade & Remarks -->
                    <div class="flex-shrink-0" style="width: 250px;">
                        <!-- Final Grade -->
                        <div class="d-flex justify-content-between align-items-center p-3 mb-3 bg-light rounded border">
                            <span class="fw-bold d-flex align-items-center gap-2">
                                Final Grade
                                <i id="toggleFinalGrade" class="fa-solid fa-eye" role="button" style="cursor: pointer;"></i>
                            </span>
                            <span id="finalGradeValue" class="badge bg-primary fs-6">${Math.round(data.finalGrade)}</span>
                        </div>

                        <!-- Remarks & Descriptor -->
                        <div class="mb-3">
                            <div class="d-flex justify-content-between py-1 border-bottom">
                                <span class="fw-semibold">Remarks:</span>
                                <span class="fw-bold" style="color: ${data.color};">${data.status}</span>
                            </div>
                            <div class="d-flex justify-content-between py-1 border-bottom">
                                <span class="fw-semibold">Classification :</span>
                                <span class="badge 
                                    ${data.finalGrade < 75 ? 'bg-danger' :
                    data.finalGrade <= 79 ? 'bg-warning text-dark' :
                    data.finalGrade <= 84 ? 'bg-info text-dark' :
                    data.finalGrade <= 89 ? 'bg-primary' :
                    data.finalGrade <= 94 ? 'bg-success' :
                    'bg-success'}">
                                    ${data.desc}
                                </span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Suggestions Section: BELOW the columns -->
                <div class="mt-3 p-3 rounded-3" style="background-color: #fff3cd;">
                    <h6 class="fw-bold mb-2">Suggestions to Improve</h6>
                    <div class="fst-italic text-dark mb-0">
                        ${suggestionHtml || 'No suggestions available.'}
                    </div>
                </div>
            </div>
            `;

            $('#out').html(output);


            const gradeSpan = $("#finalGradeValue");
            gradeSpan.data("realValue", gradeSpan.text());
            gradeSpan.text("*****");
            gradeSpan.data("hidden", true);
            $("#toggleFinalGrade").removeClass("fa-eye").addClass("fa-eye-slash");

            //===== CAN BE USED FOR FUTURE REFERENCES (place this inside the const output) =====//
            //ML Prediction (baseline): ${data.prediction.toFixed(2)}
            //AI Suggestions:
            //${data.advice}

            Swal.fire({
                toast: true,
                position: 'top-end',
                icon: 'success',
                title: 'Calculation Complete',
                text: 'Your grade has been computed successfully!',
                showConfirmButton: false,
                timer: 5000,
                timerProgressBar: true,
                background: '#ffffff',
                color: '#333333'
            });
        }).fail(function (xhr) {
            const msg = (xhr.responseJSON && xhr.responseJSON.message) ? xhr.responseJSON.message : 'Unexpected error.';
            Swal.fire({
                icon: 'error',
                title: 'Compute Failed',
                text: msg
            });
        });
    });
}

function resetForm() {
    $('input').val('').removeClass('is-invalid');

    $('select').each(function () {
        $(this).val('');
        $(this).removeClass('is-invalid');
        if ($(this).hasClass('select2-hidden-accessible')) {
            $(this).trigger('change');
        }
    });

    $('#out').text('');

    $('#resetBtn').prop('disabled', true);
}

function toggleFinalGrade() {
    const gradeSpan = $("#finalGradeValue");
    const icon = $("#toggleFinalGrade");

    // check if currently hidden
    const isHidden = gradeSpan.data("hidden") === true;

    if (isHidden) {
        // 👁 Show the real grade
        gradeSpan.text(gradeSpan.data("realValue"));
        gradeSpan.data("hidden", false);
        icon.removeClass("fa-eye-slash").addClass("fa-eye");
    } else {
        // 🙈 Hide the grade
        gradeSpan.data("realValue", gradeSpan.text());
        gradeSpan.text("*****");
        gradeSpan.data("hidden", true);
        icon.removeClass("fa-eye").addClass("fa-eye-slash");
    }
}

$(document).ready(function () {
    function checkFormValues() {
        let hasValue = false;

        $('input').each(function () {
            if ($(this).val().trim() !== '') {
                hasValue = true;
                return false;
            }
        });

        if (!hasValue) {
            $('select').each(function () {
                const val = $(this).val();
                if (val && val.toString().trim() !== '') {
                    hasValue = true;
                    return false;
                }
            });
        }

        $('#resetBtn').prop('disabled', !hasValue);
    }

    $('input').on('input', checkFormValues);
    $('select').on('change', checkFormValues);

    checkFormValues();

    // ✅ Bind toggle to click
    $(document).on("click", "#toggleFinalGrade", function () {
        toggleFinalGrade();
    });

    // ✅ Default to hidden (mask instead of hide)
    const gradeSpan = $("#finalGradeValue");
    gradeSpan.data("value", gradeSpan.text());
    gradeSpan.text("*****");
    gradeSpan.data("hidden", true);
    $("#toggleFinalGrade").removeClass("fa-eye").addClass("fa-eye-slash");
});



