  // URL에서 keyword 파라미터 추출
  function getKeywordFromURL() {
    const params = new URLSearchParams(window.location.search);
    return params.get('keyword') || '';
  }

  // 정규식 특수문자 이스케이프 처리
  function escapeRegExp(string) {
    return string.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
  }
  
//    document.addEventListener('DOMContentLoaded', () => {
//      const keyword = getKeywordFromURL();
//    });
	
	document.addEventListener('DOMContentLoaded', () => {
	  const keyword = getKeywordFromURL();
	  highlightKeywordInCells(keyword);
	});


//  // 키워드 하이라이트 함수 (notice_title 컬럼만)
//  function highlightKeywordInCells(keyword) {
//    if (!keyword.trim()) return;
//
//    const safeKeyword = escapeRegExp(keyword);
//    const regex = new RegExp(safeKeyword, 'gi');
//
//    const cells = document.querySelectorAll('.notice-row .notice-cell');
//
//    cells.forEach((cell, index) => {
//      // notice_title 컬럼만 (기존 index % 9 === 1)
//      if (index % 9 === 1) {
//        const originalHTML = cell.innerHTML;
//
//        if (originalHTML.includes('highlighted')) return;
//
//        const highlightedHTML = originalHTML.replace(regex, match => {
//          return `<span class="highlighted">${match}</span>`;
//        });
//
//        cell.innerHTML = highlightedHTML;
//      }
//    });
//  }

// 셀 내 키워드 하이라이트
function highlightKeywordInCells(keyword) {
  if (!keyword.trim()) return;

  const escapedKeyword = escapeRegExp(keyword);
  const regex = new RegExp(`(${escapedKeyword})`, 'gi');

  // 제목 셀 선택
  const titleCells = document.querySelectorAll('.notice-row .notice-cell:nth-child(2)');
  titleCells.forEach(cell => {
    const originalText = cell.textContent;
    const highlightedText = originalText.replace(regex, '<span style="background-color: #9a43fe; color: white; font-weight: bold">$1</span>');
    cell.innerHTML = highlightedText;
  });
}

