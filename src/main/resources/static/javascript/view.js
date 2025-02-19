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
      url: `/accounting/getMore?page=${page}&size=20`,
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
