document.addEventListener("DOMContentLoaded", function () {
    $("thead").hide();
    loadMoreData();
    document.getElementById("nameTag").innerText = userName;
    $(".content").scroll(function() {
      if ($(".content").scrollTop() + $(".content").height() >= $(".content").height() - 50) {
          loadMoreData();
      }
    });
});

let page = 0;
let isLoading = false;
const table = document.getElementById("accountingTable");

function loadMoreData() {
  if (isLoading) return;
  isLoading = true;
  $(".loading").show();

  $.ajax({
      url: `/accounting/getMore?page=${page}&size=20`,
      method: "GET",
      success: function(data) {
          if (data.content.length > 0) {
              $("thead").show();
              $("#noDataMessage").hide();
              data.content.forEach(accounting => {
                  $("#breakDown").append(`
                      <tr>
                          <td>
                              <a href="/lookUp/${accounting.id}" style="color: inherit; text-decoration: none;">
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
              if (page === 0 && data.content.length === 0)
                $("#noDataMessage").show();
              $(".loading").hide();
          }
      }
  });
}
