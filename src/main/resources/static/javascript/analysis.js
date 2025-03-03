Chart.register(ChartDataLabels);

const incomeContainer = document.querySelector(".incomeChart");
const expenseContainer = document.querySelector(".expenseChart");
var dpr = window.devicePixelRatio || 1;

// 수입 차트 데이터
let incomeLabels = [];
let incomeData = [];
let incomeColors = [];

// 지출 차트 데이터
let expenseLabels = [];
let expenseData = [];
let expenseColors = [];

// 차트 생성
const incomeCt = document.getElementById("income-pie-chart");
const expenseCt = document.getElementById("expense-pie-chart");

let incomeChart = null;
let expenseChart = null;

function createPieChart(ctx, labels, data, colors) {
    return new Chart(ctx, {
        type: 'pie',
        data: {
            labels: labels,
            datasets: [{
                backgroundColor: colors,
                data: data
            }]
        },
        options: {
            responsive: false,
            maintainAspectRatio: false,
            plugins: {
                tooltip: {
                    enabled: true,
                    padding: 15
                },
                legend: {
                    display: true,
                    position: 'bottom',
                    labels: {
                        fontSize: 15,
                        boxWidth: 20
                    }
                },
                datalabels: {
                    formatter: (value, ctx) => {
                        let datasets = ctx.chart.data.datasets[0].data;

                        if (value !== 0) {
                            let sum = datasets.reduce((acc, data) => acc + data, 0);
                            let percentage = Math.round((value * 100) / sum) + "%";
                            return percentage;
                        } else {
                            return "";
                        }
                    },
                    color: '#fff'
                }
            }
        }
    });
}

$(document).ready(function() {
    loadData();
});

function loadData() {
    const firstDayOfMonth = new Date(currentYear, currentMonth, 1);
    const daysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();
    const startDayOfWeek = firstDayOfMonth.getDay();
    currentMonthElement.textContent = `${currentYear}년 ${currentMonth + 1}월`;

    $.ajax({
        url: `/accounting/getMonthlyData?year=${currentYear}&month=${currentMonth + 1}`,
        method: "GET",
        success: function(data) {
            // 차트 데이터 초기화
            incomeLabels.length = 0;
            incomeData.length = 0;
            incomeColors.length = 0;
            expenseLabels.length = 0;
            expenseData.length = 0;
            expenseColors.length = 0;

            if (data.length > 0) {
                // 기존 데이터 초기화
                let incomeCategoryMap = {};  // 수입 카테고리별 금액 저장
                let expenseCategoryMap = {}; // 지출 카테고리별 금액 저장

                data.forEach(accounting => {
                    const type = accounting.type;
                    const category = accounting.category;
                    const amount = accounting.amount;

                    if (type === "INCOME") {
                        if (!incomeCategoryMap[category]) {
                            incomeCategoryMap[category] = 0;
                        }
                        incomeCategoryMap[category] += amount;
                    } else if (type === "EXPENSE") {
                        if (!expenseCategoryMap[category]) {
                            expenseCategoryMap[category] = 0;
                        }
                        expenseCategoryMap[category] += amount;
                    }
                });

                // 수입 데이터 변환
                Object.keys(incomeCategoryMap).forEach(category => {
                    incomeLabels.push(categoryMap[category]); // 한글 카테고리명
                    incomeData.push(incomeCategoryMap[category]); // 합산된 금액
                    incomeColors.push(colorMap[category]);
                });

                // 지출 데이터 변환
                Object.keys(expenseCategoryMap).forEach(category => {
                    expenseLabels.push(categoryMap[category]); // 한글 카테고리명
                    expenseData.push(expenseCategoryMap[category]); // 합산된 금액
                    expenseColors.push(colorMap[category]);
                });
            }
            updateCharts();
        }
    });
}

// 차트 업데이트
function updateCharts() {
    if (incomeChart) {
        incomeChart.destroy(); // 기존 차트 제거
    }
    if (expenseChart) {
        expenseChart.destroy();
    }

    // 수입 내역이 없는 경우
    if (incomeData.length == 0) {
        var incomeContext = incomeCt.getContext("2d");
        var msg = "수입 내역이 없습니다.";
        showMessage(incomeCt, incomeContext, msg);
    } else {
        incomeChart = createPieChart(incomeCt, incomeLabels, incomeData, incomeColors);
    }

    // 지출 내역이 없는 경우
    if (expenseData.length == 0) {
        var expenseContext = expenseCt.getContext("2d");
        var msg = "지출 내역이 없습니다."
        showMessage(expenseCt, expenseContext, msg);
    } else {
        expenseChart = createPieChart(expenseCt, expenseLabels, expenseData, expenseColors);
    }
}

function showMessage(ct, context, msg) {
    // 기존 내용 지우기
    context.clearRect(0, 0, ct.width, ct.height);

    context.setTransform(1, 0, 0, 1, 0, 0); // 스케일 초기화
    context.scale(dpr, dpr);

    var centerX = (ct.width) / (2 * dpr);
    var centerY = (ct.height) / (2 * dpr);
    context.fillStyle = "#bdbdbd";
    context.textAlign = "center";
    context.font = "bold 8px Arial";
    context.fillText(msg, centerX, centerY);
}


prevBtn.addEventListener("click", () => {
    currentMonth--;
    if (currentMonth < 0) {
        currentMonth = 11;
        currentYear--;
    }
    loadData();
});

nextBtn.addEventListener("click", () => {
    currentMonth++;
    if (currentMonth > 11) {
        currentMonth = 0;
        currentYear++;
    }
    loadData();
});