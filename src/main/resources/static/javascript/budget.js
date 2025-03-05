const recentAccountingTable = document.getElementById("recent-accounting-table");
const remainBudget = document.getElementById("remaining-budget");
const budgetProgress = document.getElementById("budget-progress");
const percentageLabel = document.getElementById("percentage-label");
const today = new Date();
const year = today.getFullYear();
const month = today.getMonth() + 1;
let currentIncome = 0;
let currentExpense = 0;
let currentAccountingData = [];

document.addEventListener("DOMContentLoaded", function () {
    getMonthlyData();
    document.getElementById("nameTag").textContent = userName;
});

function getMonthlyData() {
    $.ajax({
        url: `/accounting/getMonthly?year=${year}&month=${month}&size=1000`,
        method: "GET",
        success: function(data) {
            if (data.content.length > 0) {
                currentAccountingData = data.content;
                getRecentData(); // 최근 5개 표시
                getMonthlyBudget();
            }
        },
        error: function(xhr, status, error) {

        }
    });
}

function getMonthlyBudget() {
    currentAccountingData.forEach(accounting => {
        if (accounting.type === "INCOME") {
            currentIncome += accounting.amount;
        } else if (accounting.type === "EXPENSE") {
            currentExpense += accounting.amount;
        }
    });

    const remainingBudget = currentIncome - currentExpense;

    if (currentIncome === 0) {
        budgetProgress.value = 0;
        remainBudget.textContent = new Intl.NumberFormat('ko-KR').format(remainingBudget);
    } else {
        const percentage = (currentExpense / currentIncome) * 100;
        remainBudget.textContent = new Intl.NumberFormat('ko-KR').format(remainingBudget); // 예산 형식화
        budgetProgress.value = percentage;
        percentageLabel.textContent = percentage + "%";
    }
}

function getRecentData() {
    const recentData = currentAccountingData.slice(0, 5); // 최근 5개
    $("#recent-accounting").empty();

    recentData.forEach(accounting => {
        const sign = accounting.type === "EXPENSE" ? "-" : "+";
        const formattedAmount = `${sign} ${accounting.amount.toLocaleString('ko-KR', { style: 'currency', currency: 'KRW' })}`;

        $("#recent-accounting").append(`
          <tr>
              <td>${typeMap[accounting.type] || accounting.type}</td>
              <td>${formattedAmount}</td>
              <td>${accounting.date}</td>
          </tr>
        `);
    });
}