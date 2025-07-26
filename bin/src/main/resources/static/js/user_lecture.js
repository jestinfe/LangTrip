function goToDonate(courseSeq){
			location.href = "/user/donation?lectureNo="+ courseSeq;
		}
		//퀴즈
		const startButtons = document.querySelectorAll(".start-quiz-button");
		  startButtons.forEach(btn => {
		    btn.addEventListener("click", function () {
		      const quizListSeq = this.dataset.quizId;
		      const quizCourseSeq = this.dataset.courseId;
		      startQuiz(quizListSeq, quizCourseSeq);
		    });
		  });
		//퀴즈 시작시 학습 화면으로 이동
		  function startQuiz(quizListSeq, courseSeq) {
		    location.href = "/quiz/playQuiz/" + quizListSeq+"?courseSeq=" + courseSeq;
		  }
		
        function toggleMobileMenu() {
            const sidebar = document.getElementById('sidebar');
            const overlay = document.querySelector('.mobile-overlay');
            
            sidebar.classList.toggle('active');
            overlay.classList.toggle('active');
        }

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
                
                
                axios.post("/api/chat", message, {
                	headers:{
                		"Content-Type": "text/plain"
                	}
                }).
                then(res=>{
  					const botResponse = document.createElement('div');
  					botResponse.className = 'message bot';
  					botResponse.textContent = res.data;
  					messages.scrollTop = messages.scrollHeight;
  					messages.appendChild(botResponse);
  					messages.scrollTop = messages.scrollHeight;
                }).
                catch(err=>{
                	 const errorMsg = document.createElement('div');
                     errorMsg.className = 'message bot';
                     errorMsg.textContent = "오류 발생: " + err;
                     messages.appendChild(errorMsg);
                });
            }
        }
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

        // 페이지 로드 시 애니메이션
        document.addEventListener('DOMContentLoaded', function() {
            const panels = document.querySelectorAll('.side-panel');
            panels.forEach((panel, index) => {
                panel.style.animationDelay = `${index * 0.1}s`;
            });
        });
        