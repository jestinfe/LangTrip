document.addEventListener("DOMContentLoaded", async function () {
    const userNameEl = document.getElementById("user-name");
    const loggedInEl = document.getElementById("user-logged-in");
    const loggedOutEl = document.getElementById("user-logged-out");

    try {
        const res = await fetch("/api/auth/status", {
            method: "GET",
            credentials: "include"
        });

        const result = await res.json();
        console.log("로그인 상태:", result);

        if (result.loggedIn) {
            userNameEl.textContent = result.nickname || "사용자";

            // display 조작
            loggedInEl.style.display = "flex";
            loggedOutEl.style.display = "none";

            // 클래스 조작 (선택적)
            document.body.classList.add("logged-in");
            document.body.classList.remove("logged-out");
        } else {
            loggedInEl.style.display = "none";
            loggedOutEl.style.display = "flex";

            document.body.classList.add("logged-out");
            document.body.classList.remove("logged-in");
        }
    } catch (error) {
        console.error("로그인 상태 확인 실패:", error);
        loggedInEl.style.display = "none";
        loggedOutEl.style.display = "flex";

        document.body.classList.add("logged-out");
        document.body.classList.remove("logged-in");
    }
});
