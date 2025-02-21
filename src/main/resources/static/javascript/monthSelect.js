const currentMonthElement = document.getElementById("currentMonth");
const today = new Date(); // 현재 날짜
let currentMonth = today.getMonth(); // 0~11
let currentYear = today.getFullYear();

const prevBtn = document.getElementById("prevBtn");
const nextBtn = document.getElementById("nextBtn");

const typeMap = { "INCOME" : "수입", "EXPENSE" : "지출"};

const categoryMap = {
    "SALARY" : "월급",
    "ALLOWANCE" : "용돈",
    "INTEREST" : "이자",
    "SHOPPING" : "쇼핑",
    "FOOD" : "식비",
    "PHONE" : "통신비",
    "TRANSPORT" : "교통비",
    "CAFE" : "카페, 간식",
    "TRAVEL" : "여행",
    "HEALTH" : "건강",
    "EDUCATION" : "교육",
    "INSURANCE" : "보험",
    "OTHER" : "기타"
};

const colorMap = {
    "SALARY" : "#ff0000",
    "ALLOWANCE" : "#ffbb00",
    "INTEREST" : "#ffe400",
    "SHOPPING" : "#ff0000",
    "FOOD" : "#1dd816",
    "PHONE" : "#00d8ff",
    "TRANSPORT" : "#0054ff",
    "CAFE" : "#5f00ff",
    "TRAVEL" : "#ff00dd",
    "HEALTH" : "#ff007f",
    "EDUCATION" : "#bb2929",
    "INSURANCE" : "#abf200",
    "OTHER" : "#747474"
};