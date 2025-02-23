const currentMonthElement = document.getElementById("currentMonth");
const today = new Date(); // 현재 날짜
let currentMonth = today.getMonth(); // 0~11
let currentYear = today.getFullYear();

const prevBtn = document.getElementById("prevBtn");
const nextBtn = document.getElementById("nextBtn");

