// 이전 페이지로 이동
function listReq() {
    window.history.back();
}

// 내역 수정
function updateReq() {
    const id = accountingData.id;
    location.href = "/accounting/update/" + id;
}

// 내역 삭제
function deleteReq() {
    const id = accountingData.id;

    var header = $("meta[name='_csrf_header']").attr('content');
    var token = $("meta[name='_csrf']").attr('content');

    $.ajax({
        type: "DELETE",
        url: "/accounting/delete/" + `${id}`,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(response) {
            window.history.back();
        },
        error: function(xhr, status, error) {

        }
    });
}