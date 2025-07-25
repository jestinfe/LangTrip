window.addEventListener('load', () => {
	fetch("/admin/api/user_stats")
	  .then(res => {
	    if (!res.ok) {
	      throw new Error("Network response was not ok");
	    }
	    return res.json();
	  })
	  .then(data => {
	    console.log("회원 통계 데이터:", data);  // 데이터 확인
	    if (data.hourlyStats) {
	      drawHourlyAccessChart(data.hourlyStats);
	    } else {
	      console.error("시간별 접속 통계 데이터가 없습니다.");
	    }
	    drawSignupChart(data.dailySignup);
	    drawSignupPathChart(data.signupPath);
	    drawUnsignReasonChart(data.unsignReason);
	    drawCourseCategoryChart(data.courseCategoryStats);
	    drawCourseDifficultyChart(data.courseDifficultyStats);
	    drawAdClickChart(data.adClickStats);
	  })
	  .catch(err => console.error("회원 통계 로드 실패", err));
	  });

	function drawHourlyAccessChart(stats) {
	  console.log(stats);  // 데이터가 잘 넘어오는지 확인
	  if (!stats) {
	    console.error('시간별 접속 통계 데이터가 없습니다.');
	    return;
	  }

	  const labels = stats.map(i => i.hour);  // 시간 (00, 01, ..., 23)
	  const counts = stats.map(i => i.accessCount);  // 접속 수

	  new Chart(document.getElementById('hourlyAccessChart'), {
	    type: 'line',
	    data: {
	      labels: labels,
	      datasets: [{
	        label: '접속 수',
	        data: counts,
	        borderColor: '#FF5733',
	        backgroundColor: 'rgba(255, 99, 132, 0.2)',
	        borderWidth: 2,
	        tension: 0.3
	      }]
	    },
	    options: {
	      responsive: true,
          devicePixelRatio: window.devicePixelRatio,
	      plugins: {
	        legend: { display: false }
	      },
	      scales: {
	        y: { 
	          beginAtZero: true,
	          title: {
	            display: true,
	            text: '접속 수'
	          }
	        },
	        x: {
	          title: {
	            display: true,
	            text: '시간'
	          }
	        }
	      }
	    }
	  });
	}

function drawSignupChart(signups) {
  const dates = signups.map(i => i.regDate);
  const counts = signups.map(i => i.dailyCount); // dailyCount로 수정

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
      devicePixelRatio: window.devicePixelRatio,
      plugins: {
        legend: { display: false }
      }
    }
  });
}

function drawSignupPathChart(paths) {
  const labels = paths.map(i => i.signupPath);
  const counts = paths.map(i => i.dailyCount); // dailyCount로 수정

  const colors = [
    '#FF5733', '#33FF57', '#3357FF', '#FF33A1', '#8A2BE2', '#00FFFF', '#FF8C00', '#4B0082', '#FFD700'
  ];

  new Chart(document.getElementById('pathDonutChart'), {
    type: 'polarArea',
    data: {
      labels: labels,
      datasets: [{
        data: counts,
        backgroundColor: colors,
        borderWidth: 1
      }]
    },
    options: {
      responsive: true,
      devicePixelRatio: window.devicePixelRatio,
      plugins: {
        legend: { position: 'bottom' },
        title: { display: true, text: '유입 경로 통계' }
      },
      scales: {
        r: {
          angleLines: { display: true, color: '#ddd' },
          suggestedMin: 0,
          suggestedMax: Math.max(...counts) * 1.2,
          ticks: { display: false }
        }
      }
    }
  });
}

function drawUnsignReasonChart(reasons) {
  const reasonMap = {
    "1": "흥미 감소", "2": "진행 어려움", "3": "콘텐츠 불만족", "4": "기술 문제", "5": "개인 사정", "6": "기타"
  };

  const labels = reasons.map(i => reasonMap[i.unsignPath] || "알 수 없음");
  const counts = reasons.map(i => i.unsignCount);

  new Chart(document.getElementById('reasonBarChart'), {
    type: 'bar',
    data: {
      labels: labels,
      datasets: [{
        label: '탈퇴자 수',
        data: counts,
        backgroundColor: 'rgba(255, 255, 0, 0.5)', // 노란색으로 변경
        borderWidth: 1
      }]
    },
    options: {
      responsive: true,
      devicePixelRatio: window.devicePixelRatio,
      plugins: {
        legend: { display: false }
      },
      scales: {
        y: { beginAtZero: true }
      }
    }
  });
}

function drawCourseCategoryChart(stats) {
  const labels = stats.map(i => i.category);  // 카테고리 (영어, 일본어, 중국어)
  const counts = stats.map(i => i.courseCount);  // 강의 수

  new Chart(document.getElementById('courseCategoryChart'), {
    type: 'bar',
    data: {
      labels: labels,
      datasets: [{
        label: '강의 수',
        data: counts,
        backgroundColor: ['#3357FF', '#FF33A1', '#FF5733'],
        borderWidth: 1
      }]
    },
    options: {
      responsive: true,
      devicePixelRatio: window.devicePixelRatio,
      plugins: {
        legend: { display: false },
        title: { display: true, text: '카테고리별 강의 등록 수' }
      },
      scales: {
        y: { beginAtZero: true, title: { display: true, text: '강의 수' } }
      }
    }
  });
}

function drawCourseDifficultyChart(stats) {
  const labels = stats.map(i => i.difficulty);  // 난이도 (예: 초급, 중급, 고급)
  const counts = stats.map(i => i.difficultyCount);  // 강의 등록 수

  new Chart(document.getElementById('courseDifficultyChart'), {
    type: 'bar',
    data: {
      labels: labels,
      datasets: [{
        label: '강의 수',
        data: counts,
        backgroundColor: ['#FF5733', '#33FF57', '#3357FF'],  // 각 난이도별 색상 설정
        borderWidth: 1
      }]
    },
    options: {
      responsive: true,
      devicePixelRatio: window.devicePixelRatio,
      plugins: {
        legend: { display: false },
        title: {
          display: true,
          text: '난이도별 강의 등록 수'
        }
      },
      scales: {
        y: {
          beginAtZero: true,
          title: {
            display: true,
            text: '강의 수'
          }
        }
      }
    }
  });
}

function drawAdClickChart(stats) {
  console.log(stats);
  const labels = stats.map(i => i.title);  // 배너 제목
  const clicks = stats.map(i => i.clickCount);  // 클릭 수

  new Chart(document.getElementById('adClickChart'), {
    type: 'line',
    data: {
      labels: labels,
      datasets: [{
        label: '클릭 수',
        data: clicks,
        borderWidth: 1
      }]
    },
    options: {
      responsive: true,
      devicePixelRatio: window.devicePixelRatio,
      plugins: {
        legend: { display: false },
        title: {
          display: true,
          text: '광고별 클릭 횟수'
        }
      },
      scales: {
        y: {
          beginAtZero: true,
          title: {
            display: true,
            text: '클릭 수'
          },
          ticks: {
            stepSize: 1, 
            precision: 0 
          }
        },
        x: {
          title: {
            display: true,
            text: '광고 배너'
          }
        }
      }
    }
  });
}

