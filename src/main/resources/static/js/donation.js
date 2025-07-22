// src/main/resources/static/js/donation.js

function filterDonations() {
  const donationType = document.querySelector('.donation-filter').value;

  fetch(`/api/donation/donations?type=${donationType}`)
    .then(response => response.json())
    .then(data => {
      const tbody = document.querySelector('.donation-table tbody');
      const noDonationsMessage = document.getElementById('noDonationsMessage'); // 메시지 요소 가져오기
      tbody.innerHTML = ''; // 기존 데이터 비우기

      // 데이터가 없을 경우 메시지 표시
      if (data.length === 0) {
        noDonationsMessage.style.display = 'block'; // 메시지 보이기
      } else {
        noDonationsMessage.style.display = 'none'; // 메시지 숨기기

        // 데이터가 있을 경우 테이블에 추가
        data.forEach(donation => {
          const tr = document.createElement('tr');
          tr.innerHTML = `
            <td>${donation.courseTitle}</td>
            <td>${donation.donationAmount} M</td>
            <td>${donation.message}</td>
            <td>${donation.createdAt}</td>
          `;
          tbody.appendChild(tr);
        });
      }
    })
    .catch(error => {
      console.error('Error fetching donations:', error);
      const noDonationsMessage = document.getElementById('noDonationsMessage');
      noDonationsMessage.style.display = 'block'; // 오류 발생 시 메시지 표시
    });
}

// 페이지 로드 시 초기 데이터 로드
document.addEventListener('DOMContentLoaded', () => {
  // donation.html이 로드될 때만 filterDonations를 호출하도록 조건 추가
  if (document.querySelector('.donation-filter')) {
    filterDonations();
  }
});
