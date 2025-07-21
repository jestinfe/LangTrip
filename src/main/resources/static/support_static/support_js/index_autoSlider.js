// static/js/autoSlider.js

function moveToFaq(typeId) {
	localStorage.setItem("pendingFaqType", typeId); // ✅ 저장
	// 					    alert("typeId: "+typeId);
	location.href = "/support/faq/faqs";            // ✅ URL 고정
}


function autoSlider() {
	const track = document.querySelector('.slide-track');
	if (!track) return;

	let items = Array.from(track.children);
	const itemWidth = 180;

	function updateFocus() {
		items.forEach((card, i) => {
			card.classList.toggle('active', i === 2); // 가운데 강조
		});
	}

	function slideOnce() {
		track.style.transform = `translateX(-${itemWidth}px)`;

		setTimeout(() => {
			track.appendChild(items[0]);
			track.style.transition = 'none';
			track.style.transform = 'translateX(0)';

			setTimeout(() => {
				track.style.transition = 'transform 0.3s ease-in-out';
			}, 50);

			items = Array.from(track.children);
			updateFocus();
		}, 300);
	}

	updateFocus();
	setInterval(slideOnce, 2000);
}//autoSlider
