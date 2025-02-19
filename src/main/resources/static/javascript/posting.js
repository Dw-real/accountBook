// 수입 및 지출 카테고리
const incomeCategories = [
    {value: "SALARY", text: "급여"},
    {value: "ALLOWANCE", text: "용돈"},
    {value: "INTEREST", text: "이자"},
    {value: "OTHER", text: "기타"}
];

const expenseCategories = [
    {value: "SHOPPING", text: "쇼핑"},
    {value: "FOOD", text: "식비"},
    {value: "PHONE", text: "통신비"},
    {value: "TRANSPORT", text: "교통비"},
    {value: "CAFE", text: "카페, 간식"},
    {value: "TRAVEL", text: "여행"},
    {value: "HEALTH", text: "건강"},
    {value: "EDUCATION", text: "교육"},
    {value: "INSURANCE", text: "보험"},
    {value: "OTHER", text: "기타"}
];

const categorySelect = document.getElementById("category");
const incomeRadio = document.getElementById("income");
const expenseRadio = document.getElementById("expense");

// 카테고리 옵션 업데이트
function updateCategories(categories) {
    categorySelect.innerHTML = "";
    categories.forEach(category => {
        const option = document.createElement("option");
        option.value = category.value;
        option.textContent = category.text;
        categorySelect.appendChild(option);
    });
}

// 페이지 로드 시 기본 값 (지출 카테고리)
updateCategories(expenseCategories);

// 버튼 클릭 시 해당 카테고리로 업데이트
incomeRadio.addEventListener("change", () => {
    if (incomeRadio.checked) {
        updateCategories(incomeCategories);
    }
});

expenseRadio.addEventListener("change", () => {
    if (expenseRadio.checked) {
        updateCategories(expenseCategories);
    }
});

$(document).ready(function() {
    $('#postForm').submit(function(event) {
        event.preventDefault();

        const type = document.querySelector('input[name="type"]:checked').value;
        const category = $('#category').val();
        const amount = $('#amount').val();
        const description = $('#description').val();
        const calendar = document.querySelector('#calendar');
        const date = calendar.value;

        if (!amount) {
            alert("금액을 입력해주세요.");
            return;
        }
        if (!date) {
            alert("날짜를 선택해주세요.");
            return;
        }

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
                // 오류가 발생하면 처리
            }
        });
    });
});