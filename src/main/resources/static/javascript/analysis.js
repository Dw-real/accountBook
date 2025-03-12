Chart.register(ChartDataLabels);

var dpr = window.devicePixelRatio || 1;

// 도넛 차트 데이터 저장 객체
let doughnutChartData = {
    INCOME: { labels: [], data: [], colors: [], chart: null, ctx: "income-doughnut-chart", text: "수입" },
    EXPENSE: { labels: [], data: [], colors: [], chart: null, ctx: "expense-doughnut-chart", text: "지출" }
};

// 바 차트 데이터 저장 객체
let barChartData = {
    labels: [], data: [], colors: ["#0100FF", "#FF0000"], chart: null, ctx: "bar-chart"
};

// 도넛 차트 생성 함수
function createDoughnutChart(ctx, labels, data, colors, text) {
    return new Chart(ctx, {
        type: 'doughnut',
        data: { labels, datasets: [{ backgroundColor: colors, data }]},
        options: {
            responsive: false,
            maintainAspectRatio: false,
            plugins: {
                tooltip: { enabled: true, padding: 15, intersect: false },
                legend: { display: true, position: 'bottom', labels: { fontSize: 15, boxWidth: 20 } },
                datalabels: {
                    formatter: (value, ctx) => {
                        let datasets = ctx.chart.data.datasets[0].data;
                        let sum = datasets.reduce((acc, data) => acc + data, 0);
                        return value ? Math.round((value * 100) / sum) + "%" : "";
                    },
                    color: '#fff'
                }
            }
        },
        plugins: [{
            beforeDraw: function(chart) {
                let { width, height, ctx } = chart;
                ctx.save();
                ctx.font = "bold 16px Arial";
                ctx.fillStyle = "#333";
                ctx.textAlign = "center";
                ctx.textBaseline = "middle";
                ctx.fillText(text, width / 2, height / 2.2);
                ctx.restore();
            }
        }]
    });
}

// 바 차트 생성 함수
function createBarChart() {
    return new Chart(barChartData.ctx, {
        type: 'bar',
        data: {
            labels: barChartData.labels,
            datasets: [{
                data: barChartData.data,
                backgroundColor: barChartData.colors,
                borderColor: barChartData.colors,
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                x: {
                    grid: {display: false},
                    ticks: {font: {size: 12, weight: "bold"}, color: "#555"}
                },
                y: {
                    beginAtZero: true,
                    grid: { color: "rgba(200, 200, 200, 0.3)" },
                    ticks: {font: {size: 12, weight: "bold"}, color: "#555"}
                }
            },
            plugins: {
                tooltip: { padding: 10},
                legend: {display: false},
                datalabels: {display: false}
            }
        }
    });
}

// 차트 데이터 초기화
function resetDoughnutChartData(type) {
    doughnutChartData[type].labels.length = 0;
    doughnutChartData[type].data.length = 0;
    doughnutChartData[type].colors.length = 0;
}

function resetBarChartData() {
    barChartData.labels.length = 0;
    barChartData.data.length = 0;
}

// 데이터 로드
function loadData() {
    currentMonthElement.textContent = `${currentYear}년 ${currentMonth + 1}월`;

    $.ajax({
        url: `/accounting/getMonthlyData?year=${currentYear}&month=${currentMonth + 1}`,
        method: "GET",
        success: function(data) {
            ["INCOME", "EXPENSE"].forEach(resetDoughnutChartData);
            resetBarChartData();
            // 도넛 차트 데이터
            let categoryMapData = { INCOME: {}, EXPENSE: {} };
            // 바 차트 데이터
            let typeMapData = {INCOME: 0, EXPENSE: 0 };

            data.forEach(({ type, category, amount }) => {
                categoryMapData[type][category] = (categoryMapData[type][category] || 0) + amount;
                typeMapData[type] += amount;
            });

            // 수입, 지출별 데이터
            Object.keys(typeMapData).forEach(type => {
                barChartData.labels.push(typeMap[type]);
                barChartData.data.push(typeMapData[type]);
            });

            // 카테고리별 데이터
            Object.keys(categoryMapData).forEach(type => {
                Object.entries(categoryMapData[type]).forEach(([category, amount]) => {
                    doughnutChartData[type].labels.push(categoryMap[category]);
                    doughnutChartData[type].data.push(amount);
                    doughnutChartData[type].colors.push(colorMap[category]);
                });
            });

            updateCharts();
        }
    });
}

// 차트 업데이트
function updateCharts() {
    ["INCOME", "EXPENSE"].forEach(type => {
        if (doughnutChartData[type].chart) doughnutChartData[type].chart.destroy();

        let doughnutCtx = document.getElementById(doughnutChartData[type].ctx).getContext("2d");

        if (doughnutChartData[type].data.length === 0) {
            showNoDataImage(doughnutCtx);
        } else {
            doughnutChartData[type].chart = createDoughnutChart(doughnutCtx.canvas, doughnutChartData[type].labels, doughnutChartData[type].data, doughnutChartData[type].colors, doughnutChartData[type].text);
        }
    });
    if (barChartData.chart) {
        barChartData.chart.destroy();
    }

    const isBarChartDataEmpty = barChartData.data.every(item => item === 0);
    if (barChartData.data.length === 0 || isBarChartDataEmpty) {
        let barCtx = document.getElementById(barChartData.ctx).getContext("2d");
        showNoDataImage(barCtx);
    } else {
        barChartData.chart = createBarChart();
    }
}

function showNoDataImage(ctx) {
    const imgElement = document.createElement('img');
    imgElement.src = "/image/no_data.jpg";

    imgElement.addEventListener('load', () => {
        const chartWidth = ctx.canvas.width;
        const chartHeight = ctx.canvas.height;
        const imgWidth = chartWidth;
        const imgHeight = chartHeight;
        ctx.drawImage(imgElement, (chartWidth - imgWidth) / 2, (chartHeight - imgHeight) / 2, imgWidth, imgHeight);
    });
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

// 초기 데이터 로드
$(document).ready(function() {
    loadData();
});
