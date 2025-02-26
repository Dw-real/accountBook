const notice = document.getElementById("notice");

// 회원 탈퇴 처리 함수
function withdrawUser(email, pwd) {
    if (email && pwd) {
        var header = $("meta[name='_csrf_header']").attr('content');
        var token = $("meta[name='_csrf']").attr('content');

        $.ajax({
            type: 'DELETE',
            url: '/user/delete',
            contentType: 'application/json',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            data: JSON.stringify({
                email: email,
                pwd: pwd
            }),
            success: function(response) {
                alert('탈퇴되었습니다.');
                window.location.href = '/';
            },
            error: function(xhr, status, error) {
                try {
                    notice.textContent = "회원 정보가 일치하지 않습니다.";
                } catch (e) {
                    alert("서버 오류가 발생했습니다. 다시 시도해주세요.");
                }
            }
        });
    }
}

// 버튼 클릭 이벤트 핸들러
document.getElementById('confirmDelete').addEventListener('click', function() {
    const email = document.getElementById('emailInput').value;
    const pwd = document.getElementById('pwdInput').value;
    withdrawUser(email, pwd); // 탈퇴 처리 함수 호출
});