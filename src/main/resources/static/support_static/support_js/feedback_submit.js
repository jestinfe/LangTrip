
document.addEventListener("DOMContentLoaded", function () {
  const realnameRadio = document.getElementById("realnameRadio");
  const anonymousRadio = document.getElementById("anonymousRadio");
  const userIdSection = document.getElementById("userIdSection");

  function toggleUserIdSection() {
    if (anonymousRadio.checked) {
      userIdSection.style.display = "none";
    } else {
      userIdSection.style.display = "flex"; // 또는 원래 스타일대로 "block"
    }
  }

  // 처음 로드 시 체크된 상태 반영
  toggleUserIdSection();

  // 라디오 변경 시 감지해서 토글
  realnameRadio.addEventListener("change", toggleUserIdSection);
  anonymousRadio.addEventListener("change", toggleUserIdSection);
});

document.addEventListener('DOMContentLoaded', () => {
	const form = document.querySelector('form[th\\:action="@{/support/feedback/submit}"]') || document.querySelector('form');

	form.addEventListener('submit', function(event) {

		const email = form.email.value.trim();
		const title = form.feedback_title.value.trim();
		const content = form.feedback_content.value.trim();

		if (!email) {
			alert('이메일을 입력해주세요.');
			form.email.focus();
			event.preventDefault();
			return false;
		}
		if (!title) {
			alert('제목을 입력해주세요.');
			form.feedback_title.focus();
			event.preventDefault();
			return false;
		}
		if (!content) {
			alert('내용을 입력해주세요.');
			form.feedback_content.focus();
			event.preventDefault();
			return false;
		}
	});
});