const validList = document.querySelector(".valid-list");

function checkId() {
    const id = document.getElementById("id").value;
    const checkResult = document.getElementById("check-result");

    // 아이디를 입력하지 않은 경우
    if (!id) {
        checkResult.textContent = "아이디를 입력해주세요.";
        checkResult.style.color = "red";
        return;
    }

    // AJAX 요청
    $.ajax({
        type: "GET",
        url: "/user/id-check",  // URL 파라미터로 id 전달
        data: {id: id},  // 데이터를 URL 파라미터로 보냄
        success: function(response) {
            if (response.success) {
                validList.innerHTML = '';
                const checkResult = document.createElement('p');
                checkResult.textContent = response.message;
                checkResult.style.color = "green";
                validList.appendChild(checkResult);
            }
        },
        error: function(xhr, status, error) {
            if (xhr.status == 409) {
                validList.innerHTML = '';
                const checkResult = document.createElement('p');
                checkResult.textContent = xhr.responseJSON.message;
                checkResult.style.color = "red";
                validList.appendChild(checkResult);
            } else {
                alert("서버 오류가 발생했습니다. 다시 시도해주세요.");
            }
        }
    });
}

$(document).ready(function() {
    $('#createAccountForm').on('submit', function(event) {
        event.preventDefault();

        const name = $('#name').val();
        const email = $('#email').val();
        const id = $('#id').val();
        const pwd = $('#pwd').val();
        const pwdCheck = $('#pwdCheck').val();

        if (pwd != pwdCheck) {
            alert("비밀번호가 일치하지 않습니다.");
            return;
        }

        var header = $("meta[name='_csrf_header']").attr('content');
        var token = $("meta[name='_csrf']").attr('content');

        $.ajax({
            type: 'POST',
            url: '/user/create',
            contentType:'application/json',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            data: JSON.stringify({
                name: name,
                email: email,
                id: id,
                pwd: pwd
            }),
            success: function(response) {
                alert("계정이 생성되었습니다!");
                location.href = "/";
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