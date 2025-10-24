<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>부서 관리</title>

<!-- ✅ Bootstrap CSS & JS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<style>
    body { background-color: #f8f9fa; }
    table tr { cursor: pointer; }
</style>
</head>
<body class="container mt-5">
    <h2 class="mb-4 text-center">부서 목록</h2>

    <!-- ✅ 부서 등록 버튼 -->
    <div class="mb-3 text-end">
        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#insertModal">부서 등록하기</button>
    </div>

    <!-- ✅ 부서 목록 테이블 -->
    <table class="table table-bordered table-hover text-center" id="deptTable">
        <thead class="table-light">
            <tr>
                <th>부서번호</th>
                <th>부서명</th>
                <th>지역</th>
                <th>삭제</th>
            </tr>
        </thead>
        <tbody></tbody>
    </table>

    <!-- ✅ 등록 모달 -->
    <div class="modal fade" id="insertModal" tabindex="-1" aria-labelledby="insertModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">부서 등록</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="insertForm">
                        <div class="mb-3">
                            <label>부서번호</label>
                            <input type="number" class="form-control" name="deptno" required>
                        </div>
                        <div class="mb-3">
                            <label>부서명</label>
                            <input type="text" class="form-control" name="dname" required>
                        </div>
                        <div class="mb-3">
                            <label>지역</label>
                            <input type="text" class="form-control" name="loc" required>
                        </div>
                        <button type="submit" class="btn btn-success w-100">등록</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- ✅ 수정 모달 -->
    <div class="modal fade" id="updateModal" tabindex="-1" aria-labelledby="updateModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">부서 수정</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="updateForm">
                        <input type="hidden" name="deptno">
                        <div class="mb-3">
                            <label>부서명</label>
                            <input type="text" class="form-control" name="dname" required>
                        </div>
                        <div class="mb-3">
                            <label>지역</label>
                            <input type="text" class="form-control" name="loc" required>
                        </div>
                        <button type="submit" class="btn btn-warning w-100">수정</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

<script>
/* -------------------------------
   ✅ 초기 로드 시 전체 부서 목록 불러오기
-------------------------------- */
document.addEventListener("DOMContentLoaded", () => {
    loadDeptList();
});

/* ✅ 전체 목록 조회 */
function loadDeptList() {
    fetch("/api/dept")
        .then(res => {
            if (!res.ok) throw new Error("목록 조회 실패");
            return res.json();
        })
        .then(list => {
            const tbody = document.querySelector("#deptTable tbody");
            tbody.innerHTML = "";

            list.forEach(dept => {
                const tr = document.createElement("tr");
                tr.dataset.deptno = dept.deptno;
                tr.dataset.dname = dept.dname;
                tr.dataset.loc = dept.loc;
                tr.innerHTML = `
                    <td>\${dept.deptno}</td>
                    <td>\${dept.dname}</td>
                    <td>\${dept.loc}</td>
                    <td><button class="btn btn-danger btn-sm deleteBtn">삭제</button></td>
                `;
                tbody.appendChild(tr);
            });
        })
        .catch(err => {
            alert(err.message);
        });
}

/* -------------------------------
   ✅ 부서 등록
-------------------------------- */
document.querySelector("#insertForm").addEventListener("submit", e => {
    e.preventDefault();

    const data = {
        deptno: e.target.deptno.value,
        dname: e.target.dname.value,
        loc: e.target.loc.value
    };

    fetch("/api/dept", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
        .then(res => {
            if (!res.ok) throw new Error("등록 실패");
            return res.text();
        })
        .then(msg => {
            alert(msg);
            bootstrap.Modal.getInstance(document.getElementById('insertModal')).hide();
            e.target.reset();
            loadDeptList();
        })
        .catch(err => alert(err.message));
});

/* -------------------------------
   ✅ 행 클릭 시 수정 모달 열기
-------------------------------- */
document.querySelector("#deptTable").addEventListener("click", e => {
    const tr = e.target.closest("tr");
    if (!tr || e.target.classList.contains("deleteBtn")) return;

    const deptno = tr.dataset.deptno;
    const dname = tr.dataset.dname;
    const loc = tr.dataset.loc;

    const form = document.querySelector("#updateForm");
    form.deptno.value = deptno;
    form.dname.value = dname;
    form.loc.value = loc;

    new bootstrap.Modal(document.getElementById("updateModal")).show();
});

/* -------------------------------
   ✅ 부서 수정
-------------------------------- */
document.querySelector("#updateForm").addEventListener("submit", e => {
    e.preventDefault();

    const data = {
        deptno: e.target.deptno.value,
        dname: e.target.dname.value,
        loc: e.target.loc.value
    };

    fetch("/api/dept", {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
        .then(res => {
            if (!res.ok) throw new Error("수정 실패");
            return res.text();
        })
        .then(msg => {
            alert(msg);
            bootstrap.Modal.getInstance(document.getElementById('updateModal')).hide();
            loadDeptList();
        })
        .catch(err => alert(err.message));
});

/* -------------------------------
   ✅ 삭제 버튼 클릭 시 삭제 요청
-------------------------------- */
document.querySelector("#deptTable").addEventListener("click", e => {
    if (e.target.classList.contains("deleteBtn")) {
        e.stopPropagation();
        const deptno = e.target.closest("tr").dataset.deptno;
        if (confirm(`부서번호 \${deptno}을 삭제하시겠습니까?`)) {
            fetch(`/api/dept/\${deptno}`, { method: "DELETE" })
                .then(res => {
                    if (!res.ok) throw new Error("삭제 실패");
                    return res.text();
                })
                .then(msg => {
                    alert(msg);
                    loadDeptList();
                })
                .catch(err => alert(err.message));
        }
    }
});
</script>
</body>
</html>
