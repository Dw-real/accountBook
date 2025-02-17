document.getElementById("logo").addEventListener('click', function() {
    location.href = "/";
});

// 홈 메뉴
document.getElementById("home").addEventListener('click', function() {
    location.href = "/";
});

// 추가하기 메뉴
document.getElementById("post").addEventListener('click', function(){
    location.href = "/accounting/post";
});

// 전체 내역 보기 메뉴
document.getElementById("viewAll").addEventListener('click', function(){
    location.href = "/accounting/viewAll";
});

// 회원 가입 버튼
document.getElementById("create").addEventListener('click', function() {
    location.href = "/user/create"
});

// 로그인 버튼
document.getElementById("logIn").addEventListener('click', function() {
    location.href = "/logIn"
});

// 로그아웃 버튼
document.getElementById("logout").addEventListener('click', function() {
    location.href = "/logout"
});

// 아이디 찾기 버튼
document.getElementById("findId").addEventListener('click', function() {
    location.href = "/user/findId";
});

// 비밀번호 찾기 버튼
document.getElementById("findPwd").addEventListener('click', function() {
    location.href = "/user/findPwd";
});

// 다이얼로그 띄우기
document.getElementById('userName').addEventListener('click', function() {
    document.getElementById('dialog-overlay').style.display = document.getElementById('dialog-overlay').style.display === "block" ? "none" : "block";
    document.getElementById('userIdDialog').style.display = document.getElementById('userIdDialog').style.display === "block" ? "none" : "block";
});

document.getElementById('dialog-overlay').addEventListener('click', function() {
    document.getElementById('dialog-overlay').style.display = 'none';
    document.getElementById('userIdDialog').style.display = 'none';
});

document.getElementById("updatePwd").addEventListener('click', function() {
    location.href = "/user/updatePwd";
});

document.getElementById('deleteId').addEventListener('click', function () {
    document.getElementById('withDrawModal').style.display = 'block';
});

document.querySelector('.close').addEventListener('click', function () {
    document.getElementById('withDrawModal').style.display = 'none';
});

window.addEventListener('click', function (event) {
    const modal = document.getElementById('withDrawModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
});

function toggleDisplay(loggedIn, userName) {
    if (loggedIn) { // 로그인 성공
        document.getElementById('create').style.display = 'none';
        document.getElementById('logIn').style.display = 'none';
        document.getElementById('findId').style.display = 'none';
        document.getElementById('findPwd').style.display = 'none';
        document.getElementById('userName').style.display = 'inline';
        document.getElementById('userName').textContent = userName + "님";
        //document.getElementById('updatePwd').style.display = 'inline';
        document.getElementById('logout').style.display = 'inline';
    } else {
        document.getElementById('create').style.display = 'inline';
        document.getElementById('logIn').style.display = 'inline';
        document.getElementById('findId').style.display = 'inline';
        document.getElementById('findPwd').style.display = 'inline';
        document.getElementById('userName').style.display = 'none';
        document.getElementById('updatePwd').style.display = 'none';
        document.getElementById('logout').style.display = 'none';
    }
}

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
                document.getElementById('passwordModal').style.display = 'none';
            },
            error: function(xhr, status, error) {
                try {
                    // 서버에서 반환된 오류 메시지를 추출하여 alert로 표시
                    const errorResponse = JSON.parse(xhr.responseText);
                    alert(errorResponse.errors.join("\n"));
                } catch (e) {
                    alert("서버 오류가 발생했습니다. 다시 시도해주세요.");
                }
            }
        });
    } else {
        alert('비밀번호를 입력하세요.');
    }
}

// 버튼 클릭 이벤트 핸들러
document.getElementById('confirmDelete').addEventListener('click', function() {
    const email = document.getElementById('emailInput').value;
    const pwd = document.getElementById('pwdInput').value;
    withdrawUser(email, pwd); // 탈퇴 처리 함수 호출
});
