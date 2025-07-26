function applyDonationStyle() {
  const el = document.getElementById('donationLevelDisplay');
  if (!el) return;
  const level = el.innerText;
  switch (level) {
    case '브론즈': el.style.color = '#cd7f32'; break;
    case '실버': el.style.color = '#c0c0c0'; break;
    case '골드': el.style.color = '#ffd700'; break;
    case '플래티넘': el.style.color = '#e5e4e2'; break;
    case '에메랄드': el.style.color = '#50c878'; break;
    case '다이아': el.style.color = '#b9f2ff'; break;
    case '도네이셔너':
      el.style.color = 'crimson';
      el.style.fontWeight = 'bold';
      el.style.textShadow = '0 0 5px gold';
      break;
  }
}
window.applyDonationStyle = applyDonationStyle;
