// 최초 페이지 로딩 시
window.addEventListener("DOMContentLoaded", () => {
  const container = document.getElementById("faq-result-container");
  if (!container) return;

  const pendingType = localStorage.getItem("pendingFaqType");
  loadFaqType(pendingType || "all");
  localStorage.removeItem("pendingFaqType");
});

function scrollToFaq(event, id) {
    event.preventDefault();
    const el = document.getElementById(id);
    if (el) {
      el.scrollIntoView({ behavior: 'smooth' });
      history.pushState(null, null, '#' + id);
    }
  }

  // 아코디언 열고 닫기
  function toggleAccordion(el) {
    const accordion = el.nextElementSibling;

    if (accordion.style.maxHeight) {
      accordion.style.maxHeight = null;
    } else {
      accordion.style.maxHeight = accordion.scrollHeight + "px";

      const faqId = el.getAttribute("data-id");
      if (faqId) {
        fetch(`/faq/hit?id=${faqId}`, { method: 'POST' })
          .catch(err => console.error("조회수 증가 오류:", err));
      }
    }
  }


// 비동기 FAQ 유형 로딩
function loadFaqType(typeId) {
  const container = document.getElementById("faq-result-container");
  if (!container) return;

  fetch(`/support/faq/faqs/${typeId}`)
    .then(response => response.text())
    .then(data => {
      container.innerHTML = data;

      // 아코디언 바인딩 재설정
      container.querySelectorAll('.child-div[onclick="toggleAccordion(this)"]').forEach(el => {
        el.onclick = function () {
          toggleAccordion(this);
        };
      });

      // openId 처리
      const openId = new URLSearchParams(window.location.search).get("openId");
      if (openId) {
        const checkExist = setInterval(() => {
          const target = document.querySelector(`[data-id="${openId}"]`);
          if (target) {
            toggleAccordion(target);
            target.scrollIntoView({ behavior: "smooth" });
            clearInterval(checkExist);
          }
        }, 100);
      }

    })
    .catch(error => {
      console.error("FAQ 데이터 불러오기 실패:", error);
    });
}

window.addEventListener("DOMContentLoaded", () => {
  const container = document.getElementById("faq-result-container");
  if (!container) {
    // 해당 페이지는 FAQ 결과 영역이 없으므로 loadFaqType 호출하지 않음
    return;
  }

  const pendingType = localStorage.getItem("pendingFaqType");
  loadFaqType(pendingType || "all");
  localStorage.removeItem("pendingFaqType");

  // 특정 FAQ 자동 펼치기 및 이동
  const openId = new URLSearchParams(window.location.search).get("openId");
  if (openId) {
    const checkExist = setInterval(() => {
      const target = document.querySelector(`[data-id="${openId}"]`);
      if (target) {
        toggleAccordion(target);
        target.scrollIntoView({ behavior: "smooth" });
        clearInterval(checkExist);
      }
    }, 100);
  }
});







function toggleAccordion(el) {
  const accordion = el.nextElementSibling;

  if (accordion.style.maxHeight) {
    accordion.style.maxHeight = null;
  } else {
    accordion.style.maxHeight = accordion.scrollHeight + "px";

    const faqId = el.getAttribute("data-id");
    if (faqId) {
      fetch(`/faq/hit?id=${faqId}`, {
        method: 'POST'
      }).catch(err => {
        console.error("조회수 증가 오류:", err);
      });
    }
  }
}




document.addEventListener("DOMContentLoaded", () => {
  const openId = new URLSearchParams(window.location.search).get("openId");
  if (openId) {
    const checkExist = setInterval(() => {
      const target = document.querySelector(`[data-id="${openId}"]`);
      if (target) {
        toggleAccordion(target);
        scrollToWithOffset(target, 240); // ← 여기!
        clearInterval(checkExist);
      }
    }, 100);
  }
});







function scrollToWithOffset(element, offset = 240) {
  const rect = element.getBoundingClientRect();
  const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
  window.scrollTo({
    top: rect.top + scrollTop - offset,
    behavior: 'smooth'
  });
}

