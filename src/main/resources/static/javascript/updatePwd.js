function updatePwd() {
    const currentPwd = document.getElementById('currentPwd').value;
    const newPwd = document.getElementById('newPwd').value;
    const checkNewPwd = document.getElementById('checkNewPwd').value;
    const notice = document.getElementById('notice');

    if (!currentPwd || !newPwd || !checkNewPwd) {
        notice.textContent = "모든 칸을 입력해주세요."
        notice.style.color = "red";
        return;
    } else if (newPwd !== checkNewPwd) {
        notice.textContent = "새 비밀번호를 정확히 입력해주세요.";
        notice.style.color = "red";
        return;
    }

    var header = $("meta[name='_csrf_header']").attr('content');
    var token = $("meta[name='_csrf']").attr('content');

    $.ajax({
        type: 'PATCH',
        url: "/user/updatePwd",
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        data: JSON.stringify({
            currentPwd: currentPwd,
            newPwd: newPwd
        }),
        success: function(response) {
            alert("비밀번호가 변경되었습니다.");
            location.href ="/";
        },
        error: function(xhr, status, error) {
            if (xhr.status == 400) {
                notice.innerHTML = '';
                const errorResponse = JSON.parse(xhr.responseText);
                errorResponse.errors.forEach(error => {
                    const validMessage = document.createElement('p');
                    validMessage.textContent = error;
                    document.getElementById("notice").appendChild(validMessage);
                });
            } else {
                alert("서버 오류가 발생했습니다. 다시 시도해주세요.");
            }
        }
    });
}