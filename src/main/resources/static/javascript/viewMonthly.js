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
    resetAndLoadData();
});

nextBtn.addEventListener("click", () => {
    currentMonth++;
    if (currentMonth > 11) {
        currentMonth = 0;
        currentYear++;
    }
    renderCalendar();
    resetAndLoadData();
});

function resetAndLoadData() {
    page = 0;
    isLoading = false;
    $("#breakDown").empty(); // 기존 데이터 초기화
    loadMoreData();
}

$(document).ready(function() {
  loadMoreData();
  $(".content").scroll(function() {
      if ($(".content").scrollTop() + $(".content").height() >= $(".content").height() - 50) {
          loadMoreData();
      }
  });
});

let page = 0;
let isLoading = false;

function loadMoreData() {
  if (isLoading) return;
  isLoading = true;
  $(".loading").show();

  $.ajax({
      url: `/accounting/getMonthly?year=${currentYear}&month=${currentMonth + 1}&page=${page}&size=20`,
      method: "GET",
      success: function(data) {
          if (data.content.length > 0) {
              const typeMap = {
                  "INCOME" : "수입",
                  "EXPENSE" : "지출"
              }

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
              }

              data.content.forEach(accounting => {
                  $("#breakDown").append(`
                      <tr>
                          <td>${accounting.date}</td>
                          <td>${typeMap[accounting.type] || accounting.type}</td>
                          <td>${categoryMap[accounting.category] || accounting.category}</td>
                          <td>${accounting.amount.toLocaleString('ko-KR', { style: 'currency', currency: 'KRW'})}</td>
                          <td>${accounting.description}</td>
                      </tr>
                  `);
              });

              page++;
              // 20개 이하일 때 isLoading을 false로 설정
              if (data.content.length < 20) {
                  isLoading = false;
                  $(".loading").hide();
              }
          } else {
              $(".loading").hide();
          }
      }
  });
}