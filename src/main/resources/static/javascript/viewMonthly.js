const calendarDates = document.getElementById("calendarDates");
const currentMonthElement = document.getElementById("currentMonth");
const prevBtn = document.getElementById("prevBtn");
const nextBtn = document.getElementById("nextBtn");

const today = new Date(); // 현재 날짜
let currentMonth = today.getMonth(); // 0~11
let currentYear = today.getFullYear();

// 월별 캘린더 생성
function renderCalendar() {
    const firstDayOfMonth = new Date(currentYear, currentMonth, 1);
    const daysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate(); // 현재 월의 총 일수
    const startDayOfWeek = firstDayOfMonth.getDay(); // 현재 월의 1일의 요일
    currentMonthElement.textContent = `${currentYear}년 ${currentMonth + 1}월`;

    calendarDates.innerHTML = ""; // 일자를 표시하는 그리드 컨테이너 비우기

    // 빈 날짜(이전 달)
    for (let i = 0; i < startDayOfWeek; i++) {
        const emptyDate = document.createElement("div");
        // 빈 날짜를 나타내는 div 요소를 생성.
        emptyDate.classList.add("date", "empty");
        // 생성한 div 요소에 "date"와 "empty" 클래스를 추가.
        calendarDates.appendChild(emptyDate);
        // 생성한 빈 날짜 요소를 캘린더 그리드에 추가.
      }

      // 현재 달의 날짜
      for (let i = 1; i <= daysInMonth; i++) {
        const dateElement = document.createElement("div");
        dateElement.classList.add("date");

        const dateNum = document.createElement("span");
        dateNum.textContent = i;
        dateElement.appendChild(dateNum);
        calendarDates.appendChild(dateElement);
      }
}

renderCalendar(); // 초기 달력 렌더링

prevBtn.addEventListener("click", () => {
    currentMonth--;
    if (currentMonth < 0) {
        currentMonth = 11;
        currentYear--;
    }
    renderCalendar();
});

nextBtn.addEventListener("click", () => {
    currentMonth++;
    if (currentMonth > 11) {
        currentMonth = 0;
        currentYear++;
    }
    renderCalendar();
});