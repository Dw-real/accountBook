const calendarDates = document.getElementById("calendarDates");

let page = 0;
let isLoading = false;
let dailySummary = {}; // 날짜별 수입, 지출 계산

// 월별 캘린더 생성
function renderCalendar() {
    const firstDayOfMonth = new Date(currentYear, currentMonth, 1);
    const daysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();
    const startDayOfWeek = firstDayOfMonth.getDay();
    currentMonthElement.textContent = `${currentYear}년 ${currentMonth + 1}월`;

    calendarDates.innerHTML = "";

    for (let i = 0; i < startDayOfWeek; i++) {
        const emptyDate = document.createElement("div");
        emptyDate.classList.add("date", "empty");
        calendarDates.appendChild(emptyDate);
    }

    for (let i = 1; i <= daysInMonth; i++) {
        const dateElement = document.createElement("div");
        dateElement.classList.add("date");

        const dateNum = document.createElement("span");
        dateNum.textContent = i;
        dateElement.appendChild(dateNum);

        // YYYY-MM-DD 형식으로 키를 만들기 위함
        const day = String(i).padStart(2, '0');
        const month = String(currentMonth + 1).padStart(2, '0');
        const dateKey = `${currentYear}-${month}-${day}`;

        if (dailySummary[dateKey]) {
            const incomeEl = document.createElement("p");
            incomeEl.classList.add("income");
            incomeEl.textContent = `+${dailySummary[dateKey].income.toLocaleString('ko-KR', { style: 'currency', currency: 'KRW' })}`;

            const expenseEl = document.createElement("p");
            expenseEl.classList.add("expense");
            expenseEl.textContent = `-${dailySummary[dateKey].expense.toLocaleString('ko-KR', { style: 'currency', currency: 'KRW' })}`;

            if (dailySummary[dateKey].income != 0)
                dateElement.appendChild(incomeEl);

            if (dailySummary[dateKey].expense != 0)
                dateElement.appendChild(expenseEl);
        }

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
    dailySummary = {};
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

function loadMoreData() {
    if (isLoading) return;
    isLoading = true;
    $(".loading").show();

    $.ajax({
      url: `/accounting/getMonthly?year=${currentYear}&month=${currentMonth + 1}&page=${page}&size=20`,
      method: "GET",
      success: function(data) {
          if (data.content.length > 0) {
              data.content.forEach(accounting => {
                  const dateObj = new Date(accounting.date);
                  const year = dateObj.getFullYear();
                  const month = String(dateObj.getMonth() + 1).padStart(2, '0');
                  const day = String(dateObj.getDate()).padStart(2, '0');
                  const dateKey = `${year}-${month}-${day}`; // YYYY-MM-DD 형식

                  if (!dailySummary[dateKey]) {
                      dailySummary[dateKey] = { income: 0, expense: 0 };
                  }

                  if (accounting.type === "INCOME") {
                      dailySummary[dateKey].income += accounting.amount;
                  } else if (accounting.type === "EXPENSE") {
                      dailySummary[dateKey].expense += accounting.amount;
                  }

                  $("#breakDown").append(`
                      <tr>
                          <td>
                              <a href="/accounting/lookUp/${accounting.id}" style="color: inherit; text-decoration: none;">
                                  ${accounting.date}
                              </a>
                          </td>
                          <td>${typeMap[accounting.type] || accounting.type}</td>
                          <td>${categoryMap[accounting.category] || accounting.category}</td>
                          <td>${accounting.amount.toLocaleString('ko-KR', { style: 'currency', currency: 'KRW'})}</td>
                          <td>${accounting.description}</td>
                      </tr>
                  `);
              });

              page++;
              if (data.content.length < 20) {
                  isLoading = false;
                  $(".loading").hide();
              }
              renderCalendar();
          } else {
              $(".loading").hide();
          }
      }
    });
}