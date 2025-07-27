function uploadProfile() {
    var fileInput = document.getElementById('profileFile');
    var file = fileInput.files[0];

    // 1. 파일 선택 확인
    if (file) {
        var formData = new FormData();
        formData.append('file', file);

        console.log("파일이 선택되었습니다.", file); // 디버깅 로그

        // 2. 프로필 이미지 업로드 요청
        fetch('/mypage/upload_profile', {
            method: 'POST',
            body: formData
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('서버 응답이 실패했습니다.');
            }
            return response.json();
        })
        .then(data => {
            console.log("서버 응답:", data); // 디버깅 로그
            if (data.success) {
                // 3. 서버 응답이 성공적일 경우, 이미지 업데이트
                document.getElementById('profilePreview').src = "http://localhost:8080" + data.newPath + "?t=" + new Date().getTime();
                alert("프로필 이미지가 변경되었습니다.");
            } else {
                // 4. 서버 응답이 실패한 경우
                alert("업로드 실패: " + data.message);
            }
        })
        .catch(error => {
            // 5. 서버 통신 중 오류 발생 시 처리
            console.error("오류 발생:", error); // 디버깅 로그
            alert("오류 발생: " + error.message);
        });
    } else {
        // 6. 파일이 선택되지 않은 경우
        alert("파일을 선택하지 않았습니다.");
    }
}
