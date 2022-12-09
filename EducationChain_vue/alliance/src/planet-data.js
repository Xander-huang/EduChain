export const planetChartData = {
    type: "line",
    data: {
        labels: ["水星", "金星", "地球", "火星", "木星", "土星", "天王星", "海王星"],
        datasets: [
            {
                label: "行星卫星数量",
                data: [0, 0, 1, 2, 79, 82, 27, 14],
                backgroundColor: "rgba(54,73,93,.5)",
                borderColor: "#36495d",
                borderWidth: 3
            },
            {
                label: "太阳系行星质量 (相对于太阳 x 10^-6)",
                data: [16.6, 208.1, 300.3, 123, 954.792, 685.886, 243.662, 201.514],
                backgroundColor: "rgba(71, 183,132,.5)",
                borderColor: "#47b784",
                borderWidth: 3
            }
        ]
    },
    options: {
        responsive: true,
        lineTension: 1,
        scales: {
            yAxes: [
                {
                    ticks: {
                        beginAtZero: true,
                        padding: 25
                    }
                }
            ]
        }
    }
};

export default planetChartData;