function searchSuggest() {
  $('#keywordInput').on('input', function() {
	const query = $(this).val().trim();

	if (query.length === 0) {
	  $('#suggestBox').empty();
	  $('#overlay').hide();  // 입력 없으면 오버레이 숨김
	  return;
	}

	$('#overlay').show(); // 입력 있으면 오버레이 보임

    $.ajax({
      url: '/notices/search-title',
      method: 'GET',
      data: { keyword: query },
      success: function(notices) {
        const box = $('#suggestBox');
        box.empty();

        if (notices.length === 0) {
          box.append('<div style="padding-top:30px; padding-bottom:30px;color: #898989; text-align : center; font-size:36px;">관련 키워드가 없습니다.</div>');
          box.append(`
            <div class="suggestBottom">
              <div style="display: flex;" onclick="location.href='/support/feedback/send'" class="suggestBottomIn">
                <div style="flex: 1;"><div><img src="/support_static/support_images/symbol_feedback.png" style="width:40px; height:40px;" /></div></div>
                <div style="flex: 4; text-align: center;">피드백 작성하기</div>
                <div style="flex: 1;"></div>
              </div>
            </div>
          `);
          return;
        }

        notices.forEach(notice => {
          const item = $('<div></div>')
            .css({
              padding: '10px',
              cursor: 'pointer',
              borderBottom: '1px solid #eee',
              display: 'flex',
              alignItems: 'center',
              gap: '10px',
              transition: 'background-color 0.3s, color 0.3s'
            })
            .on('click', function() {
              $('#keywordInput').val(notice.notice_title);
              location.href = `/support/notice/notice_detail/${notice.notice_id}`;
              box.empty();
            })
            .hover(
              function() { // mouse enter
                $(this).css({
                  backgroundColor: 'rgba(87,1,208, 0.2)',
                  color: 'rgb(87,1,208)'
                });
              },
              function() { // mouse leave
                $(this).css({
                  backgroundColor: '',
                  color: ''
                });
              }
            );

          // 이미지 div
          const imgDiv = $('<div></div>').css({
            flex: '0 0 40px',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center'
          });
          const img = $('<img>')
            .attr('src', '/support_static/support_images/symbol_notice.png')
            .css({
              width: '30px',
              height: '30px',
              objectFit: 'contain'
            });
          imgDiv.append(img);

          // 공지사항 텍스트 div
          const label = $('<div></div>')
            .text('공지사항')
            .css({
              flex: '0 0 80px',
              color: '#5701d0',
              fontWeight: 'bold',
              fontSize: '14px',
              textAlign: 'center',
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              transition: 'color 0.3s'
            });

          // 제목 div
          const title = $('<div></div>')
            .text(notice.notice_title)
            .css({
              flex: '1',
              overflow: 'hidden',
              whiteSpace: 'nowrap',
              textOverflow: 'ellipsis'
            });

          item.append(imgDiv).append(label).append(title);
          box.append(item);
        });

        box.append(`
          <div class="suggestBottom">
            <div style="display: flex;" onclick="location.href='/support/feedback/send'" class="suggestBottomIn">
              <div style="flex: 1;"><div><img src="/support_static/support_images/symbol_feedback.png" style="width:40px; height:40px;"/></div></div>
              <div style="flex: 4; text-align: center;">피드백 작성하기</div>
              <div style="flex: 1;"></div>
            </div>
          </div>
        `);
      },
      error: function() {
        $('#suggestBox').html('<div style="padding:10px;">요청 실패</div>');
      }
    });
  });
}

$(document).ready(function() {
  $('#overlay').on('click', function() {
    $('#suggestBox').empty();      // 제안 박스 비우기
    $('#overlay').hide();          // 오버레이 숨기기
    $('#keywordInput').val('');    // 입력창 초기화 (선택 사항)
  });
});
