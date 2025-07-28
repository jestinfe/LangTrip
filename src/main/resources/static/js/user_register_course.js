//let currentPage = 1;  // 현재 페이지
//let totalPages = 5;   // 전체 페이지 수 (예시로 5페이지)
//let limit = 4;        // 한 페이지에 표시할 항목 수
//
//// 페이지네이션 업데이트 함수
//function updatePagination() {
//    let pagination = document.getElementById("pagination");
//    pagination.innerHTML = "";  // 기존 페이지네이션 초기화
//
//    // 이전 페이지 버튼
//    if (currentPage > 1) {
//        let prevButton = document.createElement("li");
//        prevButton.classList.add("page-item");
//        prevButton.innerHTML = `<a class="page-link" href="#" onclick="goToPage(${currentPage - 1})">이전</a>`;
//        pagination.appendChild(prevButton);
//    }
//
//    // 페이지 번호들
//    for (let i = 1; i <= totalPages; i++) {
//        let pageButton = document.createElement("li");
//        pageButton.classList.add("page-item");
//        pageButton.classList.toggle("active", i === currentPage);
//        pageButton.innerHTML = `<a class="page-link" href="#" onclick="goToPage(${i})">${i}</a>`;
//        pagination.appendChild(pageButton);
//    }
//
//    // 다음 페이지 버튼
//    if (currentPage < totalPages) {
//        let nextButton = document.createElement("li");
//        nextButton.classList.add("page-item");
//        nextButton.innerHTML = `<a class="page-link" href="#" onclick="goToPage(${currentPage + 1})">다음</a>`;
//        pagination.appendChild(nextButton);
//    }
//}
//
//// 해당 페이지로 이동하는 함수
//function goToPage(page) {
//    if (page < 1 || page > totalPages) return;  // 범위를 벗어나지 않도록 체크
//    currentPage = page;
//    updatePagination();  // 페이지네이션 갱신
//    loadPageData();      // 해당 페이지의 데이터를 로드
//}
//
//// 데이터를 로드하는 함수 (예시로 강의 목록 표시)
//function loadPageData() {
//    // 여기에서 서버나 클라이언트에서 해당 페이지의 데이터를 가져오는 로직을 구현합니다.
//    // 예시로 console.log로 현재 페이지를 출력
//    console.log("페이지 데이터 로딩:", currentPage);
//}
//
//// 페이지네이션 초기화 및 첫 페이지 데이터 로딩

function confirmDelete(courseSeq) {
    // 삭제 확인 창 띄우기
    var confirmDelete = window.confirm("강의 수강을 취소하시겠습니까?");
  
    // 확인을 누르면 삭제 요청
    if (confirmDelete) {
        console.log(courseSeq);  // courseSeq 값 확인
        // AJAX 요청 보내기
        $.ajax({
            url: '/ui/user_delete_course',  // 삭제 요청 URL
            type: 'GET',
            data: { seq: courseSeq },  // courseSeq를 쿼리 파라미터로 전달
            success: function(response) {
                console.log("응답:", response);  // 서버에서 받은 응답 출력
                // 성공 시, 성공 메시지를 표시
                alert(response.message);  // JSON 응답에서 메시지를 표시
                location.reload();  // 페이지 새로 고침
            },
            error: function(xhr, status, error) {
                console.log("에러 발생:", error);  // 에러 메시지 출력
                alert("강의 삭제에 실패했습니다.");
            }
        });
    }
}
//updatePagination();
//loadPageData();