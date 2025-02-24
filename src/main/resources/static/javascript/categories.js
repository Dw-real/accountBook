const categorySelect = document.getElementById("category");
const incomeRadio = document.getElementById("income");
const expenseRadio = document.getElementById("expense");

// categories.js
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
    {value: "LEISURE", text: "여가"},
    {value: "TRAVEL", text: "여행"},
    {value: "HEALTH", text: "건강"},
    {value: "EDUCATION", text: "교육"},
    {value: "INSURANCE", text: "보험"},
    {value: "OTHER", text: "기타"}
];

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
