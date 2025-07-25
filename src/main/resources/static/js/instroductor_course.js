let currentPage = 1;
const limit = 4;

window.initInstroductorCourse = function () {
  console.log("📌 instroductor_course fragment loaded");

  const list = document.getElementById("lectureList");
  if (list) {
    loadMyCourse(1);
  } else {
    console.warn("❌ lectureList not found in DOM");
  }
};

async function loadMyCourse(page = 1) {
  const empty = document.getElementById("lectureEmpty");
  const list = document.getElementById("lectureList");
  currentPage = page;

  list.innerHTML = "";
  if (empty) empty.style.display = "none";

  try {
    const res = await axios.get(`/ui/my_lecture?page=${page}&limit=${limit}`);

    // 로그인 상태 확인 (비로그인 시 로그인 페이지로 리디렉션되는 경우 차단)
    if (res.request.responseURL.includes("/login")) {
      alert("로그인이 필요합니다. 로그인 후 다시 시도해주세요.");
      location.href = "/login";
      return;
    }
	
	// 리다이렉트 감지
	if (res.request.responseURL && res.request.responseURL.includes("/login")) {
	  alert("로그인 세션이 만료되었습니다. 다시 로그인 해주세요.");
	  location.href = "/login";
	  return;
	}

    const courseListData = res.data.courseList;
    const totalCount = res.data.totalCount;

    if (!courseListData || courseListData.length === 0) {
      if (empty) empty.style.display = "block";
      return;
    }

    for (const course of courseListData) {
      appendLectureForm(course);
    }

    getPagination(totalCount, page, limit);
  } catch (err) {
    console.error(err);
    alert("강의 불러오기 실패");
  }
}

async function registerLecture() {
  const form = document.getElementById("lecture-register");
  const formData = new FormData(form);
  const empty = document.getElementById("lectureEmpty");
  if (empty) empty.style.display = "none";

  try {
    const res = await axios.post('/ui/register_course', formData);
    alert("강의 등록 완료 : " + res.data.msg);

    const countRes = await axios.get('/ui/my_lecture?page=1&limit=4');
    const totalCount = countRes.data.totalCount;
    const lastPage = Math.ceil(totalCount / limit);

    const currentCardCount = document.querySelectorAll(".lecture-card").length;

    if (currentCardCount < limit) {
      appendLectureForm(res.data.courseData);
    } else {
      loadMyCourse(lastPage);
    }

    form.reset();
  } catch (err) {
    console.error(err);
    alert("강의 등록 실패");
  }
}

function appendLectureForm(course) {
  const list = document.getElementById("lectureList");
  const card = document.createElement("div");

  let safePath = "/images/fallback.png"; // 로그인 없이 접근 가능한 경로

  if (course.thumbnailPath && course.thumbnailName) {
    const lastIndex = course.thumbnailPath.lastIndexOf("/");
    const base = course.thumbnailPath.substring(0, lastIndex + 1);
    safePath = base + encodeURIComponent(course.thumbnailName);
  }

  card.className = "lecture-card";
  card.id = `course-${course.courseSeq}`;
  card.innerHTML = `
  <img src="${safePath}" alt="썸네일" class="lecture-img"
       onerror="if (!this.dataset.fallback) { this.src='/images/default.png'; this.dataset.fallback='true'; }">
    <div class="lecture-content">
      <div class="lecture-info">
        <div class="lecture-title">
          <span class="difficulty-badge difficulty-${course.difficulty}">${course.difficulty}</span>
          <span class="language-badge language-${course.category}">${course.category}</span>
          ${course.courseTitle}
        </div>
        <div class="lecture-meta">
          <div class="lecture-count">컨텐츠 개수 5/7</div>
          <div class="lecture-stats">
            <div class="stat-item">
              <span class="stat-icon">👥</span><span>1,234명</span>
            </div>
            <div class="stat-item">
              <span class="stat-icon">⭐</span><span>4.8</span>
            </div>
          </div>
        </div>
        <div class="lecture-description">${course.introduction || '강의 설명이 없습니다.'}</div>
      </div>
      <div class="button-group">
        <button class="btn btn-primary" onclick="goToCourse('${course.courseSeq}')">강의 이동</button>
        <button class="btn btn-secondary" onclick='goToCourseEdit(${JSON.stringify(course)})'>강의 수정</button>
        <button type="button" class="btn btn-danger" onclick="deleteCourse('${course.courseSeq}')">강의 삭제</button>
      </div>
    </div>
  `;
  list.appendChild(card);
}

function getPagination(totalCount, currentPage, limit) {
  const pagination = document.getElementById("pagination");
  pagination.innerHTML = "";

  const totalPages = Math.ceil(totalCount / limit);
  const maxButtons = 5;
  const currentBlock = Math.floor((currentPage - 1) / maxButtons);
  const startPage = currentBlock * maxButtons + 1;
  const endPage = Math.min(startPage + maxButtons - 1, totalPages);

  if (startPage > 1) {
    const prevBtn = document.createElement("button");
    prevBtn.textContent = "<";
    prevBtn.className = "btn btn-sm btn-outline-secondary m-1";
    prevBtn.onclick = () => loadMyCourse(startPage - 1);
    pagination.appendChild(prevBtn);
  }

  for (let i = startPage; i <= endPage; i++) {
    const btn = document.createElement("button");
    btn.className = "btn btn-sm btn-outline-primary m-1";
    btn.textContent = i;
    if (i === currentPage) btn.classList.add("active");
    btn.onclick = () => loadMyCourse(i);
    pagination.appendChild(btn);
  }

  if (endPage < totalPages) {
    const nextBtn = document.createElement("button");
    nextBtn.textContent = ">";
    nextBtn.className = "btn btn-sm btn-outline-secondary m-1";
    nextBtn.onclick = () => loadMyCourse(endPage + 1);
    pagination.appendChild(nextBtn);
  }
}

function deleteCourse(courseSeq) {
  if (!confirm("정말 삭제하시겠습니까?")) return;

  axios.post("/upload/delete_course?seq=" + courseSeq)
    .then(res => {
      if (res.data === "success") {
        alert("삭제 성공");
        const card = document.getElementById(`course-${courseSeq}`);
        if (card) card.remove();
        loadMyCourse(currentPage);
      } else {
        alert(res.msg || "삭제 실패");
      }
    })
    .catch(err => {
      alert("삭제 실패");
      console.error(err);
    });
}

function goToCourse(courseSeq) {
  location.href = "/upload/upload_course?seq=" + courseSeq;
}

function goToCourseEdit(course) {
  axios.post("/upload/storeCourseSession", course, {
    headers: { "Content-Type": "application/json" }
  })
    .then(() => {
      location.href = "/upload/upload_update";
    })
    .catch(err => {
      alert("세션 저장 실패");
      console.error(err);
    });
}
