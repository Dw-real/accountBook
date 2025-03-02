const validList = document.querySelector(".valid-list");

// 페이지 로드 시 기본 값 (지출 카테고리)
updateCategories(expenseCategories);

$(document).ready(function() {
    $('#postForm').submit(function(event) {
        event.preventDefault();

        const type = document.querySelector('input[name="type"]:checked').value;
        const category = $('#category').val();
        const amount = $('#amount').val();
        const description = $('#description').val();
        const calendar = document.querySelector('#calendar');
        const date = calendar.value;

        var header = $("meta[name='_csrf_header']").attr('content');
        var token = $("meta[name='_csrf']").attr('content');

        $.ajax({
            type: 'POST',
            url: '/accounting/post',
            contentType:'application/json',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            data: JSON.stringify({
                type: type,
                category: category,
                amount: amount,
                description: description,
                date: date,
                userCode: userCode
            }),
            success: function(response) {
                window.location.href = "/";
            },
            error: function(xhr, status, error) {
                if (xhr.status == 400) {
                    validList.innerHTML = '';
                    const errorResponse = JSON.parse(xhr.responseText);
                    errorResponse.errors.forEach(error => {
                        const validMessage = document.createElement('p');
                        validMessage.textContent = error;
                        validMessage.style.color = "red";
                        validList.appendChild(validMessage);
                    });
                } else {
                    alert("서버 오류가 발생했습니다. 다시 시도해주세요.");
                }
            }
        });
    });
});