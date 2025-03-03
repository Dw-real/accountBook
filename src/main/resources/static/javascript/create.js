const validList = document.querySelector(".valid-list");
let check = false;

function checkId() {
    const id = document.getElementById("id").value;
    const checkResult = document.getElementById("check-result");
    check = true;

    // 아이디를 입력하지 않은 경우
    if (!id) {
        checkResult.textContent = "아이디를 입력해주세요.";
        checkResult.style.color = "red";
        return;
    }

    // AJAX 요청
    $.ajax({
        type: "GET",
        url: "/user/id-check",
        data: {id: id},
        success: function(response) {
            if (response.success) {
                otherMessage(response.message, "green");
            }
        },
        error: function(xhr, status, error) {
            if (xhr.status == 409) {
                otherMessage(xhr.responseJSON.message, "red");
            } else if (xhr.status == 400) {
                badRequestMessage(xhr);
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

        if (!check) {
            alert("아이디 중복확인을 해주세요.");
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
                    badRequestMessage(xhr);
                } else {
                    alert("서버 오류가 발생했습니다. 다시 시도해주세요.");
                }
            }
        });
    });
});

function badRequestMessage(xhr) {
    validList.innerHTML = '';
    const errorResponse = JSON.parse(xhr.responseText);
    errorResponse.errors.forEach(error => {
        const validMessage = document.createElement('p');
        validMessage.textContent = error;
        validMessage.style.color = "red";
        validList.appendChild(validMessage);
    });
}

function otherMessage(message, color) {
    validList.innerHTML = '';
    const checkResult = document.createElement('p');
    checkResult.textContent = message;
    checkResult.style.color = color;
    validList.appendChild(checkResult);
}