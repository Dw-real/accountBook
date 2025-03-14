document.addEventListener("DOMContentLoaded", function () {
    toggleDisplay(loggedIn, userName);
});

document.getElementById("logo").addEventListener('click', function() {
    location.href = "/";
});

// 홈 메뉴
document.getElementById("home").addEventListener('click', function() {
    location.href = "/";
});

// 추가하기 메뉴
document.getElementById("post").addEventListener('click', function(){
    location.href = "/post";
});

document.getElementById("view").addEventListener('click', function(){
    location.href = "/viewAll";
    if (event.target.id === "viewAll") {
        location.href = "/viewAll";
    } else if (event.target.id === "viewMonthly") {
        location.href = "/viewMonthly";
    } else if (event.target.id === "showBudget") {
        location.href = "/budget";
    }
});

// 월별 분석 보기 메뉴
document.getElementById("analysisMonthly").addEventListener('click', function(){
    location.href = "/analysisMonthly";
})

// 회원 가입 버튼
document.getElementById("create").addEventListener('click', function() {
    location.href = "/create";
});

// 로그인 버튼
document.getElementById("logIn").addEventListener('click', function() {
    location.href = "/logIn";
});

// 로그아웃 버튼
document.getElementById("logout").addEventListener('click', function() {
    $.ajax({
        url: "/logout",
        method: "GET",
        success: function(response) {
           // 로그아웃 성공 시 홈 페이지로 이동
           location.href = "/";
        },
        error: function() {
           // 로그아웃 실패 시 에러 처리
           alert("로그아웃 실패");
        }
    });
});

// 아이디 찾기 버튼
document.getElementById("findId").addEventListener('click', function() {
    location.href = "/findId";
});

// 비밀번호 찾기 버튼
document.getElementById("findPwd").addEventListener('click', function() {
    location.href = "/findPwd";
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
    location.href = "/updatePwd";
});

document.getElementById('deleteId').addEventListener('click', function () {
    location.href = "/withDraw";
});

function toggleDisplay(loggedIn, userName) {
    if (loggedIn) { // 로그인 성공
        document.getElementById('create').style.display = 'none';
        document.getElementById('logIn').style.display = 'none';
        document.getElementById('findId').style.display = 'none';
        document.getElementById('findPwd').style.display = 'none';
        document.getElementById('userName').style.display = 'inline';
        document.getElementById('userName').textContent = userName + "님";
        document.getElementById('updatePwd').style.display = 'inline';
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

document.getElementById("year").textContent = new Date().getFullYear();
