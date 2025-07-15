window.addEventListener('load', () => {
  fetch("/admin/api/user_stats")
    .then(res => res.json())
    .then(data => {
      drawSignupChart(data.dailySignup);
      drawSignupPathChart(data.signupPath);
      drawUnsignReasonChart(data.unsignReason);
    })
    .catch(err => console.error("회원 통계 로드 실패", err));
});

function drawSignupChart(signups) {
  const dates = signups.map(i => i.regDate);
  const counts = signups.map(i => i.count);

  new Chart(document.getElementById('signupLineChart'), {
    type: 'line',
    data: {
      labels: dates,
      datasets: [{
        label: '가입자 수',
        data: counts,
        borderColor: '#3f51b5',
        backgroundColor: 'rgba(63, 81, 181, 0.2)',
        borderWidth: 2,
        tension: 0.3
      }]
    },
    options: {
      responsive: true,
      plugins: {
        legend: { display: false }
      }
    }
  });
}

function drawSignupPathChart(paths) {
  const labels = paths.map(i => i.signupPath);
  const counts = paths.map(i => i.count);

  new Chart(document.getElementById('pathDonutChart'), {
    type: 'doughnut',
    data: {
      labels: labels,
      datasets: [{
        data: counts,
        backgroundColor: ['#4caf50', '#2196f3', '#ff9800', '#9c27b0', '#f44336'],
        borderWidth: 1
      }]
    },
    options: {
      responsive: true,
	  plugins: {
	    legend: {
	      position: 'bottom',
	      labels: {
	        font: {
	          size: 14,
	          weight: 'bold'
	        },
	        color: '#000' // 필요시 폰트색 추가
	      }
	    }
	  }
    }
  });
}

function drawUnsignReasonChart(reasons) {
  const labels = reasons.map(i => i.unsugnPath);
  const counts = reasons.map(i => i.count);

  new Chart(document.getElementById('reasonBarChart'), {
    type: 'bar',
    data: {
      labels: labels,
      datasets: [{
        label: '탈퇴자 수',
        data: counts,
        backgroundColor: 'rgba(255, 99, 132, 0.5)',
        borderWidth: 1
      }]
    },
    options: {
      responsive: true,
      plugins: {
        legend: { display: false }
      },
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });
}
