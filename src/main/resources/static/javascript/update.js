const validList = document.querySelector(".valid-list");

document.addEventListener("DOMContentLoaded", function () {
    // 기존 데이터에 따라 초기 선택값 설정
    if (accountingData.type === "INCOME") {
        incomeRadio.checked = true;
        updateCategories(incomeCategories);
    } else {
        expenseRadio.checked = true;
        updateCategories(expenseCategories);
    }

    // 카테고리 설정
    // 기존 accountingData.category 값을 설정
    if (accountingData.category) {
        categorySelect.value = accountingData.category;
    }

    // 금액 설정
    $("#amount").val(accountingData.amount);

    // 메모 설정
    $("#description").val(accountingData.description);

    // 날짜 설정
    $("#calendar").val(accountingData.date);
});

$('#updateForm').submit(function(e) {
    e.preventDefault();

    const id = accountingData.id;
    const type = document.querySelector('input[name="type"]:checked').value;
    const category = $('#category').val();
    const amount = $('#amount').val();
    const description = $('#description').val();
    const calendar = document.querySelector('#calendar');
    const date = calendar.value;
    const userCode = accountingData.userCode;

    var header = $("meta[name='_csrf_header']").attr('content');
    var token = $("meta[name='_csrf']").attr('content');

    $.ajax({
      type: 'PATCH',
      url: '/accounting/update',
      contentType:'application/json',
      beforeSend: function(xhr) {
        xhr.setRequestHeader(header, token);
      },
      data: JSON.stringify({
        id: id,
        type: type,
        category: category,
        amount: amount,
        description: description,
        date: date,
        userCode: userCode
      }),
      success: function(response) {
          alert('수정되었습니다.');
          window.location.href = "/accounting/lookUp/" + id;
      },
      error: function(xhr, status, error) {
          if (xhr.status == 400) {
              validList.innerHTML = '';
              const errorResponse = JSON.parse(xhr.responseText);
              errorResponse.errors.forEach(error => {
                  const validMessage = document.createElement('p');
                  validMessage.textContent = error;
                  validMessage.style.color = "red";
                  validList.appendChild(validMessage);
              });
          } else {
              alert("서버 오류가 발생했습니다. 다시 시도해주세요.");
          }
      }
    });
});