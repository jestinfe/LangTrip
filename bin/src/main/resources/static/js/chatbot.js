  function toggleChatbot() {
            const modal = document.getElementById('chatbotModal');
            modal.classList.toggle('active');
        }

        function sendChatbotMessage() {
            const input = document.getElementById('chatbotInput');
            const messages = document.getElementById('chatbotMessages');
            const message = input.value.trim();
            
            if (message) {
                // 사용자 메시지 추가
                const userMessage = document.createElement('div');
                userMessage.className = 'message user';
                userMessage.textContent = message;
                messages.appendChild(userMessage);
                
                // 입력 필드 초기화
                input.value = '';
                
                // 챗봇 응답 시뮬레이션
//                 setTimeout(() => {
//                     const botResponse = document.createElement('div');
//                     botResponse.className = 'message bot';
//                     botResponse.textContent = getBotResponse(message);
//                     messages.appendChild(botResponse);
                    
//                     // 스크롤을 맨 아래로
//                     messages.scrollTop = messages.scrollHeight;
//                 }, 1000);
                axios.post("/api/chat", message, {
                	headers:{
                		"Content-Type" : "text/plain"
                	}
                }).
                then(res=>{
				const botResponse = document.createElement('div');
				botResponse.className = 'message bot';
				botResponse.textContent = res.data;
				messages.scrollTop = messages.scrollHeight;
				messages.appendChild(botResponse);
                // 스크롤을 맨 아래로
                messages.scrollTop = messages.scrollHeight;
                	
                })
                .catch(err=>{
                	const errorMsg = document.createElement('div');
                    errorMsg.className = 'message bot';
                    errorMsg.textContent = "오류 발생: " + err;
                    messages.appendChild(errorMsg);
                });
                
            }
        }

//         function getBotResponse(message) {
//             const responses = [
//                 "강의 관련 질문이시군요! 더 자세히 알려주시면 도움을 드릴 수 있어요.",
//                 "좋은 질문이네요. 학습에 도움이 되는 정보를 찾아드릴게요.",
//                 "강의실 사용법에 대해 궁금하신가요? 언제든 물어보세요!",
//                 "학습 진도나 퀴즈에 대한 질문이 있으시면 말씀해주세요.",
//                 "영상 업로드나 강의 등록에 대해 도움이 필요하시면 안내해드릴게요."
//             ];
//             return responses[Math.floor(Math.random() * responses.length)];
//         }

        function handleChatbotEnter(event) {
            if (event.key === 'Enter') {
                sendChatbotMessage();
            }
        }

        // 모달 외부 클릭 시 닫기
        document.getElementById('chatbotModal').addEventListener('click', function(e) {
            if (e.target === this) {
                toggleChatbot();
            }
        });

        // 사이드바 링크 활성화
        document.querySelectorAll('.sidebar a').forEach(link => {
            link.addEventListener('click', function(e) {
                e.preventDefault();
                document.querySelectorAll('.sidebar a').forEach(a => a.classList.remove('active'));
                this.classList.add('active');
                
                // 모바일에서 메뉴 클릭 시 사이드바 닫기
                if (window.innerWidth <= 768) {
                    toggleMobileMenu();
                }
            });
        });

        // 윈도우 리사이즈 시 사이드바 상태 초기화
        window.addEventListener('resize', function() {
            if (window.innerWidth > 768) {
                document.getElementById('sidebar').classList.remove('active');
                document.querySelector('.mobile-overlay').classList.remove('active');
            }
        });