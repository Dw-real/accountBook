var displayInput = document.getElementById("display");
var resultInput = document.getElementById("result");
var numberClicked = false;

function showCalculator() {
    var calculatorModal = document.getElementById("calculatorModal");
    if (calculatorModal.style.display === "none" || calculatorModal.style.display === "") {
        calculatorModal.style.display = "block";
    } else {
        calculatorModal.style.display = "none";
    }
}

function addChar(char) {
    if (numberClicked == false) { // 연산자를 클릭한 상태
        if (isNaN(char) == true) { // 입력 받은 값이 연산자
            // 기존 연산자를 지우고 새로 입력 받은 연산자 입력
            displayInput.value = displayInput.value.slice(0, -1);
            displayInput.value += char;
        } else {
            displayInput.value += char;
        }
    } else { // 숫자를 클릭한 상태
        displayInput.value += char;
    }

    if (isNaN(char) == true) {
        numberClicked = false;
    } else {
        numberClicked = true;
    }
}

function calculate() {
    var result = eval(display.value);
    resultInput.value = result;
}

function reset() {
    displayInput.value = "";
    resultInput.value = "";
}

document.getElementById("inputResult").addEventListener('click', function() {
    const amountInput = document.getElementById("amount")
    amountInput.value = resultInput.value;
});
